package com.example.drivercar.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.drivercar.R;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.example.drivercar.driverCarPanel.ProfileEditDriver;
import com.example.drivercar.object.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class DriverInfomationActivity extends AppCompatActivity {

    //permission constants
    //hằng số quyền

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    //hằng số chọn hình ảnh
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //khởi tạo dành cho GPS
    //định vị
    //chọn khoang vùng
    private final static int ALL_PERMISSIONS_RESULT = 101;

    //chọn hình ảnh

    private Uri image_uri,image_uri1,image_uri2;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    String[] cameraPermission;
    String[] storagePermission;
    String UID1;
    ImageView cmndmtIv,cmndmsIv,gplxIv;
    TextInputLayout NameCar,TonnageCar, SizeCar, license_plate, completeCityAddress,completeAddress;
    TextInputEditText TonnageCarTET,NameCarTET,edcompleteAddressTET,edcompAddress;
    Button button;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    FirebaseAuth FAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");
    ImageButton btnBN;
    String ImageURL1,ImageURL2,ImageURL3,nameCar,tonnageCar,sizeCar,license_Plate,CompleteCityAddress;
    String phoneno,fname,lname,password,confpassword,house,statee,cityy,Area,cpAddress,ImageURL,emailid,longitude,latitude;
    String plate;
    //check no internet
    //kiểm tra có mạng hay không
    private Boolean wifiConnected = false;
    private Boolean mobileConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_infomation2);
        cmndmtIv = (ImageView) findViewById(R.id.cmndmtIv);
        cmndmsIv = (ImageView) findViewById(R.id.cmndmtIv);
        gplxIv = (ImageView) findViewById(R.id.gplxIv);
        NameCar = (TextInputLayout) findViewById(R.id.NameCar);
        TonnageCar = (TextInputLayout) findViewById(R.id.TonnageCar);
        SizeCar = (TextInputLayout) findViewById(R.id.SizeCar);
        license_plate = (TextInputLayout) findViewById(R.id.license_plate);
        completeCityAddress = (TextInputLayout) findViewById(R.id.completeCityAddress);
        TonnageCarTET = (TextInputEditText) findViewById(R.id.TonnageCarTET);
        NameCarTET = (TextInputEditText) findViewById(R.id.NameCarTET);
        edcompleteAddressTET = (TextInputEditText) findViewById(R.id.edcompleteAddress);
        completeAddress = (TextInputLayout) findViewById(R.id.completeAddress);
        edcompAddress = (TextInputEditText) findViewById(R.id.edcompAddress);
        button = (Button) findViewById(R.id.button);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnBN = (ImageButton) findViewById(R.id.backBN);
        //không cho tự nhập
//        disableEditText(NameCarTET);
//        disableEditText(TonnageCarTET);
        NameCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameCarDialog();
            }
        });

        NameCarTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameCarDialog();
            }
        });
        TonnageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TonnageCarDialog();
            }
        });
        TonnageCarTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TonnageCarDialog();
            }
        });
        edcompleteAddressTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeCityAddressDialog();
            }
        });
        btnBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trả về phía trước đó
                onBackPressed();
            }
        });
        cmndmtIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                //chọn hình ảnh
                showImagePickDialog();

            }
        });

        cmndmsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                //chọn hình ảnh
                showImagePickDialog();

            }
        });

        gplxIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                //chọn hình ảnh
                showImagePickDialog();

            }
        });


        isOnline();
        FAuth = FirebaseAuth.getInstance();

        phoneno = getIntent().getStringExtra("phonenumber").trim();
        fname = getIntent().getStringExtra("fname").trim();
        lname = getIntent().getStringExtra("lname").trim();
        password = getIntent().getStringExtra("password").trim();
        house = getIntent().getStringExtra("house").trim();
        confpassword = getIntent().getStringExtra("confpassword").trim();
        statee = getIntent().getStringExtra("statee").trim();
        cityy = getIntent().getStringExtra("cityy").trim();
        Area = getIntent().getStringExtra("Area").trim();
        cpAddress = getIntent().getStringExtra("cpAddress").trim();
        ImageURL = getIntent().getStringExtra("ImageURL").trim();
        emailid = getIntent().getStringExtra("emailid").trim();
        latitude = getIntent().getStringExtra("latitude").trim();
        longitude = getIntent().getStringExtra("longitude").trim();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImageURL1 = String.valueOf(image_uri).trim();
                    ImageURL2 = String.valueOf(image_uri1).trim();
                    ImageURL3 = String.valueOf(image_uri2).trim();
                    tonnageCar = TonnageCar.getEditText().getText().toString().trim();
                    sizeCar = SizeCar.getEditText().getText().toString().trim();
                    license_Plate = license_plate.getEditText().getText().toString().trim();
                    nameCar = NameCar.getEditText().getText().toString().trim();
                    CompleteCityAddress = completeCityAddress.getEditText().getText().toString().trim();
                    plate = completeAddress.getEditText().getText().toString().trim();
                    FirebaseUser firebaseUser = FAuth.getCurrentUser();
                    String emailid1 = firebaseUser.getEmail();
                    if (isValid() && image_uri == null && image_uri1 == null && image_uri2 == null && gplxIv == null && cmndmsIv == null && cmndmtIv == null)
                    {
                        Toasty.error(DriverInfomationActivity.this, "Hình ảnh là bắt buộc", Toast.LENGTH_LONG, true).show();

                    }
                    else{

                        String filePathAndName = "profile_image_driver/" + "" + FAuth.getUid();
                        final ProgressDialog progressDialog = new ProgressDialog(DriverInfomationActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setIndeterminate(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgress(0);
                        progressDialog.setTitle("\uD83E\uDD84 Tình hình mạng yếu");
                        progressDialog.setMessage("Đang đăng ký và tải hình ảnh đại diện lên, vui lòng đợi tí....");
                        progressDialog.show();

                        ref = FirebaseStorage.getInstance().getReference(filePathAndName);
                        //cho ảnh 1
                        ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                            @Override
                            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask1 = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask1.isSuccessful()) ;
                                Uri downloadImageUri1 = uriTask1.getResult();
                                if (uriTask1.isSuccessful()) {
                                    HashMap<String ,Object> hashMap = new HashMap<>();
                                    hashMap.put("ImageURL1","");
                                    databaseReference.child(FAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(@NonNull Void unused) {
                                            //cho ảnh 2
                                            ref.putFile(image_uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                                                @Override
                                                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> uriTask2 = taskSnapshot.getStorage().getDownloadUrl();
                                                    while (!uriTask2.isSuccessful()) ;
                                                    Uri downloadImageUri2 = uriTask2.getResult();
                                                    if (uriTask2.isSuccessful()) {

                                                        databaseReference.child(FAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                            @Override
                                                            public void onSuccess(@NonNull Void unused) {
                                                                //cho ảnh 3
                                                                ref.putFile(image_uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                                                                    @Override
                                                                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                                        Task<Uri> uriTask3 = taskSnapshot.getStorage().getDownloadUrl();
                                                                        while (!uriTask3.isSuccessful()) ;
                                                                        Uri downloadImageUri3 = uriTask3.getResult();

                                                                        String uid = firebaseUser.getUid();
//                                                                        String fname = firebaseUser.getDisplayName();
//                                                                        String emailid = firebaseUser.getEmail();
//                                                                        String phone =firebaseUser.getPhoneNumber();
                                                                        String timestamp = ""+System.currentTimeMillis();

                                                                        if (uriTask3.isSuccessful()) {
                                                                            HashMap<String ,Object> hashMap = new HashMap<>();
                                                                            hashMap.put("UID", "" + uid);
                                                                            hashMap.put("MobileNo", ""+phoneno);
                                                                            hashMap.put("LastName", ""+lname);
                                                                            hashMap.put("City", ""+cityy);
                                                                            hashMap.put("Area", ""+Area);//phường xã
                                                                            hashMap.put("Password", ""+password);
                                                                            hashMap.put("CompleteAddress", ""+cpAddress);
                                                                            hashMap.put("State", ""+statee);//Quận huyện
                                                                            hashMap.put("ConfirmPassword", ""+confpassword);
                                                                            hashMap.put("House", ""+house);//Số đường
                                                                            hashMap.put("Latitude", ""+latitude);
                                                                            hashMap.put("Longitude", ""+longitude );
                                                                            hashMap.put("Online", "true");
                                                                            hashMap.put("AccountType","Driver");
                                                                            hashMap.put("FirstName", fname);
                                                                            hashMap.put("EmailId", emailid);
                                                                            hashMap.put("ImageURL", ""+ImageURL);
                                                                            hashMap.put("Timestamp", "" + timestamp);
                                                                            hashMap.put("ImageURL1",""+downloadImageUri1);
                                                                            hashMap.put("ImageURL2",""+downloadImageUri2);
                                                                            hashMap.put("ImageURL3",""+downloadImageUri3);


                                                                            databaseReference.child(FirebaseAuth.getInstance()
                                                                                    .getCurrentUser().getUid())
                                                                                    .setValue(hashMap)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>(){

                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        progressDialog.dismiss();
                                                                                        HashMap<String ,Object> hashMap1 = new HashMap<>();
                                                                                        hashMap1.put("carId",""+timestamp);
                                                                                        hashMap1.put("City", ""+CompleteCityAddress);
                                                                                        hashMap1.put("CompleteAddress", ""+cpAddress);
                                                                                        hashMap1.put("Latitude", ""+latitude);
                                                                                        hashMap1.put("Longitude", ""+longitude );
                                                                                        hashMap1.put("Timestamp",""+timestamp);
                                                                                        hashMap1.put("VehicleTypeName",""+nameCar);
                                                                                        hashMap1.put("VehicleTonnage",""+tonnageCar);
                                                                                        hashMap1.put("SizeCar",""+sizeCar);
                                                                                        hashMap1.put("License_Plates",""+license_Plate);
                                                                                        hashMap1.put("Status","Đang chờ phê duyệt");
                                                                                        hashMap1.put("Plate",""+plate);
                                                                                        hashMap1.put("uid",""+FAuth.getUid());
                                                                                        databaseReference.child("Cars")
                                                                                                .child(timestamp)
                                                                                                .setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(@NonNull Void unused) {
                                                                                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                                                                                while (!uriTask.isSuccessful()) ;
                                                                                                Uri downloadImageUri = uriTask.getResult();
                                                                                                String image = ""+downloadImageUri;
                                                                                                progressDialog.dismiss();
                                                                                                Toasty.success(DriverInfomationActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT,true).show();
                                                                                                Intent Z = new Intent(DriverInfomationActivity.this, MainMenu.class);
                                                                                                startActivity(Z);
                                                                                                finish();
                                                                                            }
                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                //failed adding to db
                                                                                                progressDialog.dismiss();
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                    else{
                                                                                        progressDialog.dismiss();

                                                                                        ReusableCodeForAll.ShowAlert(DriverInfomationActivity.this,"Kết nối của bạn đang bị lỗi",task.getException().getMessage());
                                                                                    }
                                                                                }
                                                                            });


                                                                        }else{
                                                                            Toasty.error(DriverInfomationActivity.this, "Bị lỗi", Toast.LENGTH_SHORT, true).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressDialog.dismiss();
                                                                        Toasty.error(DriverInfomationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                                                    }
                                                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                                        progressDialog.setMessage("Đang tải " + (int) progress + "%");
                                                                        progressDialog.setCanceledOnTouchOutside(false);
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //failed adding to db
                                                                progressDialog.dismiss();
                                                            }
                                                        });


                                                    }else{
                                                        Toasty.error(DriverInfomationActivity.this, "Bị lỗi", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toasty.error(DriverInfomationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();
//                                    Toast.makeText(ChefRegistration.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                    progressDialog.setMessage("Đang tải " + (int) progress + "%");
                                                    progressDialog.setCanceledOnTouchOutside(false);
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //failed adding to db
                                            progressDialog.dismiss();
                                        }
                                    });


                                }else{
                                    Toasty.error(DriverInfomationActivity.this, "Bị lỗi", Toast.LENGTH_SHORT, true).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Đang tải " + (int) progress + "%");
                                progressDialog.setCanceledOnTouchOutside(false);
                            }
                        });



                    }

                }catch (Exception e)
                {

                }

            }
        });
    }



    private void isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            wifiConnected = ni.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = ni.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
//                Toasty.success(DriverInfomationActivity.this, "Bạn đã kết nối thành công đến wifi", Toast.LENGTH_SHORT, true).show();
            } else if (mobileConnected) {
//                Toasty.success(DriverInfomationActivity.this, "Bạn đã kết nối thành công đến điện thoại", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.error(DriverInfomationActivity.this, "Hiện tại bạn không có kết nối", Toast.LENGTH_SHORT, true).show();
            showSettingsWifis();
        }
    }

    //mở cài đặt đến phần wifi
    public void showSettingsWifis() {
//        Context context = new ContextThemeWrapper(ProfileEditChef.this, R.style.AppTheme2);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                context,R.style.MaterialAlertDialog_rounded);
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DriverInfomationActivity.this);
        alertDialog.setTitle("Cài đặt");
        alertDialog.setMessage("Bật wifi để dùng app! Chuyển đến menu cài đặt?");//Enable Location Provider! Go to settings menu?
        alertDialog.setPositiveButton("Đi tới cài đặt",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        DriverInfomationActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Huỷ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    //chọn tên thể loại
    private void NameCarDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Tên loại xe")
                .setItems(Constants.vehicleTypeName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked vehicleTypeName
                        String vehicleTypeName = Constants.vehicleTypeName[which];
                        //set picked vehicleTypeName
                        NameCarTET.setText(vehicleTypeName);
                    }
                })
                .show();
    }

    //chọn số tấn
    private void TonnageCarDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Số tấn")
                .setItems(Constants.tonnage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked tonnage
                        String tonnage = Constants.tonnage[which];
                        //set picked tonnage
                        TonnageCarTET.setText(tonnage);
                    }
                })
                .show();
    }

    //chọn các tỉnh hay chạy
    private void completeCityAddressDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Các tỉnh hay chạy")
                .setItems(Constants.province, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked province
                        String province = Constants.province[which];
                        //set picked province
                        edcompleteAddressTET.setText(province);
                    }
                })
                .show();
    }



    public boolean isValid() {
        TonnageCar.setErrorEnabled(false);
        TonnageCar.setError("");
        SizeCar.setErrorEnabled(false);
        SizeCar.setError("");
        license_plate.setErrorEnabled(false);
        license_plate.setError("");
        completeCityAddress.setErrorEnabled(false);
        completeCityAddress.setError("");
        boolean isValid = false, isValidTonnage = false, isValidSizeCar = false, isValidlicense_plate = false, isValidcompleteCityAddress = false;
        if (TextUtils.isEmpty(tonnageCar)) {
            TonnageCar.setErrorEnabled(true);
            TonnageCar.setError("Bạn chưa nhập tên loại xe");
        } else {
            isValidTonnage = true;
        }
        if (TextUtils.isEmpty(sizeCar)) {
            SizeCar.setErrorEnabled(true);
            SizeCar.setError("Bạn chưa nhập tên loại xe");
        } else {
            isValidSizeCar = true;
        }
        if (TextUtils.isEmpty(license_Plate)) {
            license_plate.setErrorEnabled(true);
            license_plate.setError("Bạn chưa nhập tên loại xe");
        } else {
            isValidSizeCar = true;
        }
        if (TextUtils.isEmpty(CompleteCityAddress)) {
            completeCityAddress.setErrorEnabled(true);
            completeCityAddress.setError("Bạn chưa nhập các tỉnh hay chạy");
        } else {
            isValidcompleteCityAddress = true;
        }


        isValid = (isValidTonnage && isValidSizeCar && isValidlicense_plate && isValidcompleteCityAddress ) ? true : false;
        return isValid;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DriverInfomationActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void showImagePickDialog() {
        //options to display in dialog
        //các tùy chọn để hiển thị trong hộp thoại
        String[] options = {"Máy ảnh", "Bộ sưu tập hoặc Kho ảnh"};//camara, gallery
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vui lòng chọn hình ảnh ở dạng")//pick image
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // máy ảnh được chọn //camara clicked
                            if (checkCameraPermission()) {
                                //camera permissin allowed
                                pickFromCamera();
                            } else {
                                //not allowed, request
                                requestCameraPermission();
                            }
                        } else {
                            //bộ sưu tập được chọn //galley clicked
                            if (checkStoragePermission()) {
                                //storage permissin allowed
                                pickFromGallery();
                            } else {
                                //not allowed, request
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");//Temp_Image Title
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");//Temp_Image Description

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        image_uri1 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        image_uri2 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri2);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//image
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }
    @NotNull
    private ArrayList findUnAskedPermissions(@NotNull ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case CAMERA_REQUEST_CODE:{
                if(grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(image_uri!=null&& image_uri1!=null && image_uri2!=null && cameraAccepted && storageAccepted){
                        //permission allowed
                        //sự cho phép được phép cấp quyền
                        pickFromCamera();

                    }
                    else {
                        //permission denied
                        //sự cho phép không được phép cấp quyền
                        //location permission is necessary
                        Toasty.success(this, "Sự cho phép máy ảnh là cần thiết.....!", Toast.LENGTH_SHORT, true).show();

                    }
                }
            }break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length > 0)
                {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted && image_uri!=null && image_uri1!=null && image_uri2!=null ){
                        //permission allowed
                        //sự cho phép được phép cấp quyền
                        pickFromGallery();
                    }
                    else {
                        //permission denied
                        //sự cho phép không được phép cấp quyền
                        //location permission is necessary
                        Toasty.success(this, "Sự cho phép bộ nhớ là cần thiết.....!", Toast.LENGTH_SHORT, true).show();

//                        Toast.makeText(this,"Sự cho phép bộ nhớ là cần thiết.....",Toast.LENGTH_SHORT).show();
                    }
                }
            }break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //get picked image
                image_uri = data.getData();
                image_uri1 = data.getData();
                image_uri2 = data.getData();
                ((ImageView) findViewById(R.id.cmndmtIv)).setImageURI(image_uri);
//                Toasty.success(this, "Đã chọn hình thành công!!", Toast.LENGTH_SHORT, true).show();
                ((ImageView) findViewById(R.id.cmndmsIv)).setImageURI(image_uri1);
//                Toasty.success(this, "Đã chọn hình thành công!!", Toast.LENGTH_SHORT, true).show();
                ((ImageView) findViewById(R.id.gplxIv)).setImageURI(image_uri2);
//                Toasty.success(this, "Đã chọn hình thành công!!", Toast.LENGTH_SHORT, true).show();
//                Toast.makeText(this, "Đã chọn hình thành công!", Toast.LENGTH_SHORT).show();
                //set to imageview
                cmndmtIv.setImageURI(image_uri);
                cmndmsIv.setImageURI(image_uri1);
                gplxIv.setImageURI(image_uri2);


            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //set to imageview

                cmndmtIv.setImageURI(image_uri);
                cmndmsIv.setImageURI(image_uri1);
                gplxIv.setImageURI(image_uri2);

                ((ImageView) findViewById(R.id.cmndmtIv)).setImageURI(image_uri);
//                Toasty.success(this, "Đã chụp hình thành công!!", Toast.LENGTH_SHORT, true).show();
                ((ImageView) findViewById(R.id.cmndmsIv)).setImageURI(image_uri1);
//                Toasty.success(this, "Đã chụp hình thành công!!", Toast.LENGTH_SHORT, true).show();
                ((ImageView) findViewById(R.id.gplxIv)).setImageURI(image_uri2);
//                Toasty.success(this, "Đã chụp hình thành công!!", Toast.LENGTH_SHORT, true).show();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}