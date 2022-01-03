package com.example.drivercar.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterCars;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.object.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class EditInfoDriver extends AppCompatActivity {

    private ImageButton backBN;
    private TextInputLayout NameCar,TonnageCar,SizeCar,license_plate,completeAddress,cityAddress;
    private TextInputEditText NameCarTET,TonnageCarTET,edcityAddress;
    private String nameCar,tonnageCar,sizeCar,License_plate,CompleteAddress,CityAddress;
    private Button button;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diolog_edit_car);
        firebaseAuth = FirebaseAuth.getInstance();

        backBN = (ImageButton) findViewById(R.id.backBN);

        NameCar = (TextInputLayout) findViewById(R.id.NameCar);
        NameCarTET = (TextInputEditText) findViewById(R.id.NameCarTET);

        TonnageCar = (TextInputLayout) findViewById(R.id.TonnageCar);
        TonnageCarTET = (TextInputEditText) findViewById(R.id.TonnageCarTET);

        SizeCar = (TextInputLayout) findViewById(R.id.SizeCar);
        license_plate = (TextInputLayout) findViewById(R.id.license_plate);
        completeAddress = (TextInputLayout) findViewById(R.id.completeAddress);

        cityAddress = (TextInputLayout) findViewById(R.id.cityAddress);
        edcityAddress = (TextInputEditText) findViewById(R.id.edcityAddress);
        button = (Button) findViewById(R.id.button);

        backBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        NameCarTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameCarDialog();
            }
        });
        TonnageCarTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TonnageCarDialog();
            }
        });
        edcityAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeCityAddressDialog();
            }
        });
        loadAllCars();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("Users");
                    reference.child("Cars").orderByChild("uid").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //before getting reset list
                                    for(DataSnapshot ds: snapshot.getChildren()){

                                        String CompleteAddress = ""+ds.child("CompleteAddress").getValue();
                                        String carId = ""+ds.child("carId").getValue();
                                        inputData();

                                        HashMap<String , String> hashMap = new HashMap<>();
                                        hashMap.put("carId",""+carId);
                                        hashMap.put("City", ""+CityAddress);
                                        hashMap.put("CompleteAddress", ""+CompleteAddress);
                                        hashMap.put("Latitude", "0.0");
                                        hashMap.put("Longitude", "0.0" );
                                        hashMap.put("Timestamp",""+carId);
                                        hashMap.put("VehicleTypeName",""+nameCar);
                                        hashMap.put("VehicleTonnage",""+tonnageCar);
                                        hashMap.put("SizeCar",""+sizeCar);
                                        hashMap.put("License_Plates",""+License_plate);
                                        hashMap.put("Status","Đang chờ phê duyệt");
                                        hashMap.put("Plate",""+CityAddress);
                                        hashMap.put("uid",""+firebaseAuth.getUid());
                                        databaseReference.child("Cars")
                                                .child(carId)
                                                .setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(@NonNull Void unused) {
                                                        Toasty.success(EditInfoDriver.this, "Đã sửa thành công", Toast.LENGTH_SHORT, true).show();
                                                        clearData();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding to db
                                                Toasty.error(EditInfoDriver.this, ""+"\n"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }catch (Exception e)
                {
                    Toasty.error(EditInfoDriver.this, ""+"\n"+e.getMessage(), Toast.LENGTH_SHORT, true).show();

                }
            }
        });
    }
    private void loadAllCars() {

        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");
        reference.child("Cars").orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String City =""+ds.child("City").getValue();
                            String CompleteAddress = ""+ds.child("CompleteAddress").getValue();
                            String License_Plates = ""+ds.child("License_Plates").getValue();
                            String Plate = ""+ds.child("Plate").getValue();
                            String SizeCar1 = ""+ds.child("SizeCar").getValue();
                            String Status = ""+ds.child("Status").getValue();
                            String VehicleTonnage = ""+ds.child("VehicleTonnage").getValue();
                            String VehicleTypeName = ""+ds.child("VehicleTypeName").getValue();

                            NameCar.getEditText().setText(VehicleTypeName);
                            TonnageCar.getEditText().setText(VehicleTonnage);
                            SizeCar.getEditText().setText(SizeCar1);
                            license_plate.getEditText().setText(License_Plates);
                            completeAddress.getEditText().setText(Plate);
                            cityAddress.getEditText().setText(City);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                        edcityAddress.setText(province);
                    }
                })
                .show();
    }
    private void inputData(){

        nameCar = NameCar.getEditText().getText().toString().trim();
        tonnageCar = TonnageCar.getEditText().getText().toString().trim();
        sizeCar = SizeCar.getEditText().getText().toString().trim();
        License_plate = license_plate.getEditText().getText().toString().trim();
        CompleteAddress = completeAddress.getEditText().getText().toString().trim();
        CityAddress = cityAddress.getEditText().getText().toString().trim();

        NameCar.setError("");
        TonnageCar.setError("");
        SizeCar.setError("");
        license_plate.setError("");
        completeAddress.setError("");
        cityAddress.setError("");
        NameCarTET.setError("'");
        TonnageCarTET.setError("");
        edcityAddress.setError("");
        if(TextUtils.isEmpty(nameCar)){
            Toasty.error(this, "Tên loại xe là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NameCar.setError("Tên loại xe là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(tonnageCar)){
            Toasty.error(this, "Trọng tải xe là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            TonnageCar.setError("Trọng tải xe là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(sizeCar)){
            Toasty.error(this, "Kích thước lòng xe là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            SizeCar.setError("Kích thước lòng xe là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(License_plate)){
            Toasty.error(this, "Biển số xe là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            completeAddress.setError("Biển số xe là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(CompleteAddress)){
            Toasty.error(this, "Địa chỉ bãi đậu xe là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            completeAddress.setError("Địa chỉ bãi đậu xe là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(CityAddress)){
            Toasty.error(this, "Các tỉnh hay chạy là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            completeAddress.setError("Các tỉnh hay chạy là cần thiết ..");
            return;//don't proceed further
        }
    }
    private void clearData(){

        NameCar.getEditText().setText("");
        TonnageCar.getEditText().setText("");
        SizeCar.getEditText().setText("");
        license_plate.getEditText().setText("");
        completeAddress.getEditText().setText("");
        cityAddress.getEditText().setText("");
        NameCarTET.setText("");
        TonnageCarTET.setText("");
        edcityAddress.setText("");
    }
}
