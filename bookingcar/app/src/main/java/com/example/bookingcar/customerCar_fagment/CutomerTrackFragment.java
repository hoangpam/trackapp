package com.example.bookingcar.customerCar_fagment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookingcar.R;
import com.example.bookingcar.bottomnavigation.BottomNavigationBehavior;
import com.example.bookingcar.object.Constants;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class CutomerTrackFragment extends Fragment implements LocationListener {

    final String TAG = "GPS";
    //permission constants
    //hằng số quyền
    private static final int LOCATION_REQUEST_CODE = 100;

    //khởi tạo dành cho GPS
    //định vị
    //chọn khoang vùng
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    // location last updated time
    private String mLastUpdateTime;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 600;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    //permission arrays
    //mảng quyền
    String[] locationPermission;
    double latitude = 0.0, longitude = 0.0;

    LocationManager locationManager;
    //check no internet
    //kiểm tra có mạng hay không
    private Boolean wifiConnected = false;
    private Boolean mobileConnected = false;

    ImageButton gpsBTN;

    private ImageButton backBN;
    private TextInputLayout NameLocation,NameLoGo,RankingTime,Product,Cargo,NameCar;
    private TextInputEditText NamelocationTET,NameLoGoTET,ProductTET,CargoTET,NameCarTET,RankingTimeTET;
    private String namelocationTET,nameLoGoTET,productTET,cargoTET,nameCarTET,rankingTimeTET,Textchip,ServiceTv,Service1Tv,Service2Tv,Service3Tv,Service4Tv;
    private Button postUpBTN;
    private Chip chipNoiThanh,chipNgoaiThanh;
    private TextView textchip,serviceTv,service1Tv,service2Tv,service3Tv,service4Tv;
    private SwitchCompat serviceSwitch,serviceSwitch1,serviceSwitch2,serviceSwitch3,serviceSwitch4;
    Fragment fragmenthientai;
    FirebaseAuth Fauth;
    DatabaseReference data;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customertrack, null);


        NameLocation =(TextInputLayout)  v.findViewById(R.id.NameLocation);
        NameLoGo = (TextInputLayout)  v.findViewById(R.id.NameLoGo);
        RankingTime = (TextInputLayout)  v.findViewById(R.id.RankingTime);
        Product = (TextInputLayout)  v.findViewById(R.id.Product);
        Cargo = (TextInputLayout)  v.findViewById(R.id.Cargo);
        NameCar = (TextInputLayout)  v.findViewById(R.id.NameCar);

        NamelocationTET =(TextInputEditText)  v.findViewById(R.id.NamelocationTET);
        NameLoGoTET = (TextInputEditText)  v.findViewById(R.id.NameLoGoTET);
        ProductTET = (TextInputEditText)  v.findViewById(R.id.ProductTET);
        CargoTET = (TextInputEditText)  v.findViewById(R.id.CargoTET);
        NameCarTET = (TextInputEditText)  v.findViewById(R.id.NameCarTET);
        RankingTimeTET = (TextInputEditText)  v.findViewById(R.id.RankingTimeTET);
        locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        gpsBTN = (ImageButton) v.findViewById(R.id.gpsBtn);
        chipNoiThanh = (Chip) v.findViewById(R.id.chipNoiThanh);
        chipNgoaiThanh = (Chip) v.findViewById(R.id.chipNgoaiThanh);
        textchip = (TextView) v.findViewById(R.id.textchip);
        serviceSwitch = (SwitchCompat) v.findViewById(R.id.serviceSwitch);
        serviceSwitch1 = (SwitchCompat) v.findViewById(R.id.serviceSwitch1);
        serviceSwitch2 = (SwitchCompat) v.findViewById(R.id.serviceSwitch2);
        serviceSwitch3 = (SwitchCompat) v.findViewById(R.id.serviceSwitch3);
        serviceSwitch4 = (SwitchCompat) v.findViewById(R.id.serviceSwitch4);
        serviceTv = (TextView) v.findViewById(R.id.serviceTv);
        service1Tv = (TextView) v.findViewById(R.id.service1Tv);
        service2Tv = (TextView) v.findViewById(R.id.service2Tv);
        service3Tv = (TextView) v.findViewById(R.id.service3Tv);
        service4Tv = (TextView) v.findViewById(R.id.service4Tv);
        backBN = v.findViewById(R.id.backBN);
        ButterKnife.bind(getActivity());
        initLocation();
        restoreValuesFromBundle(savedInstanceState);

        Fauth = FirebaseAuth.getInstance();

        postUpBTN = (Button)  v.findViewById(R.id.postUpBTN);



        backBN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        NameCarTET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameCarDialog();
            }
        });

        postUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        chipNgoaiThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textchip.setText("2000000");
            }
        });

        chipNoiThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textchip.setText("1000000");
            }
        });

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    serviceTv.setText("100000");
                }else{
                    serviceTv.setText("0");
                }
            }
        });
        serviceSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    service1Tv.setText("300000");
                }else{
                    service1Tv.setText("0");
                }
            }
        });

        serviceSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    service2Tv.setText("400000");
                }else{
                    service2Tv.setText("0");
                }
            }
        });
        serviceSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    service3Tv.setText("600000");
                }else{
                    service3Tv.setText("0");
                }
            }
        });
        serviceSwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    service4Tv.setText("100000");
                }else{
                    service4Tv.setText("0");
                }
            }
        });

        return v;
    }



    private void inputData() {

        namelocationTET = NamelocationTET.getText().toString().trim();
        nameLoGoTET = NameLoGoTET.getText().toString().trim();
        productTET = ProductTET.getText().toString().trim();
        cargoTET = CargoTET.getText().toString().trim();
        nameCarTET = NameCarTET.getText().toString().trim();
        rankingTimeTET = RankingTimeTET.getText().toString().trim();
        Textchip = textchip.getText().toString().trim();
        ServiceTv = serviceTv.getText().toString().trim();
        Service1Tv = service1Tv.getText().toString().trim();
        Service2Tv = service2Tv.getText().toString().trim();
        Service3Tv = service3Tv.getText().toString().trim();
        Service4Tv = service4Tv.getText().toString().trim();

        serviceTv.setError("");
        textchip.setError("");
        NamelocationTET.setError("");
        NameLoGoTET.setError("");
        ProductTET.setError("");
        CargoTET.setError("");
        NameCarTET.setError("'");
        RankingTimeTET.setError("");

        if(TextUtils.isEmpty(namelocationTET)){
            Toasty.error(getActivity(), "Điểm lấy hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NamelocationTET.setError("Điểm lấy hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(nameLoGoTET)){
            Toasty.error(getActivity(), "Điểm giao hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NameLoGoTET.setError("Điểm giao hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(rankingTimeTET)){
            Toasty.error(getActivity(), "Thời gian bốc hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            RankingTimeTET.setError("Thời gian bốc hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(productTET)){
            Toasty.error(getActivity(), "Tên hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            ProductTET.setError("Tên hàng  là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(cargoTET)){
            Toasty.error(getActivity(), "Khối lượng hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            CargoTET.setError("Khối lượng hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(nameCarTET)){
            Toasty.error(getActivity(), "Loại xe muốn đặt là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NameCarTET.setError("Loại xe muốn đặt là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Textchip)){
            Toasty.error(getActivity(), "Chọn khu vực vận chuyển ..", Toast.LENGTH_SHORT, true).show();
            textchip.setError("Khu vực vận chuyển là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(ServiceTv)){
            Toasty.error(getActivity(), "Chọn dịch vụ đóng goi..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service1Tv)){
            Toasty.error(getActivity(), "Chọn dịch vụ khiêng hàng..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service2Tv)){
            Toasty.error(getActivity(), "Chọn dịch vụ vệ sinh..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service3Tv)){
            Toasty.error(getActivity(), "Chọn dịch vụ bảo hiểm hàng hoá..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service4Tv)){
            Toasty.error(getActivity(), "Chọn dịch vụ trọn gói..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        addProduct();
    }

    private void addProduct() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang load Vui lòng chờ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Tiến hành đăng thông tin ...");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("UID").equalTo(Fauth.getUid())
                .addValueEventListener( new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String phone =""+ds.child("MobileNo").getValue();

                            String timestamp = "" + System.currentTimeMillis();
                            HashMap<String ,Object> hashMap = new HashMap<>();
                            hashMap.put("infomationId",""+timestamp);
                            hashMap.put("timestamp",""+timestamp);
                            hashMap.put("uid",""+Fauth.getUid());
                            hashMap.put("nameLoInfo",""+namelocationTET);
                            hashMap.put("nameLoGoInfo",""+nameLoGoTET);
                            hashMap.put("productInfo",""+productTET);
                            hashMap.put("cargoInfo",""+cargoTET);
                            hashMap.put("nameCarInfo",""+nameCarTET);
                            hashMap.put("rankingTimeInfo",""+rankingTimeTET);
                            hashMap.put("Latitude","0.0");
                            hashMap.put("Longitude","0.0");
                            hashMap.put("AreaLocation",""+Textchip);
                            hashMap.put("Services",""+ServiceTv);
                            hashMap.put("Services1",""+Service1Tv);
                            hashMap.put("Services2",""+Service2Tv);
                            hashMap.put("Services3",""+Service3Tv);
                            hashMap.put("Services4",""+Service4Tv);
                            hashMap.put("Status","Chưa nhân đơn");
                            hashMap.put("PhoneCus",""+phone);
                            hashMap.put("PhoneDriver","");


                            data = FirebaseDatabase.getInstance().getReference("Users");
                            data.child("Infomations").child(timestamp).setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void unused) {
                                            progressDialog.dismiss();
                                            Toasty.success(getActivity(), "Đã thêm thông tin thành công...!", Toast.LENGTH_SHORT, true).show();
                                            clearData();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding to db
                                    progressDialog.dismiss();
                                }
                            });



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }
    //chọn tên thể loại
    private void NameCarDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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
    private void clearData(){

        NamelocationTET.setText("");
        NameLoGoTET.setText("");
        ProductTET.setText("");
        CargoTET.setText("");
        NameCarTET.setText("'");
        RankingTimeTET.setText("");
        textchip.setText("");
        serviceTv.setText("");
        service1Tv.setText("");
        service2Tv.setText("");
        service3Tv.setText("");
        service4Tv.setText("");

    }

    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };
        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {

        Log.d(TAG, "updateUI");

        if (mCurrentLocation != null) {
            Log.d("latitude", "latitude--" + mCurrentLocation.getLatitude());
            Log.d("longitude", "longitude--" + mCurrentLocation.getLongitude());
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);

                    String city = addresses.get(0).getAdminArea();
                    String knownName = addresses.get(0).getFeatureName();
                    String state = addresses.get(0).getSubAdminArea();
                    Log.d(TAG, "getAddress:  thành phố" + " " + city);
                    Log.d(TAG, "getAddress:  toàn địa chỉ" + " " + address);

                    Log.d(TAG, "getAddress:  quận" + " " + state);

                    Log.d(TAG, "getAddress:  số nhà" + " " + knownName);

                    //set addresses
                    //đặt dịa chỉ
                    NamelocationTET.setText(address);
                }
            } catch (Exception e) {
                Toasty.error(getActivity(), "Hiện tại không truy cập được vị trí của bạn"+"\n" + e.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }
    @OnClick(R.id.gpsBtn)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
//                            openSettings();
                            showSettingsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("GPS không được kích hoạt!");
        alertDialog.setMessage("Bạn có muốn bật GPS không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //All location settings are satisfied.
                        Log.i(TAG, "Tất cả các cài đặt vị trí đều hài lòng.");
                        //Started location updates!
                        Toasty.warning(getApplicationContext(), "Đã bắt đầu cập nhật vị trí hiện tại của bạn!\nVui lòng tự điền tên đường\n Kiểm tra lại xem đã đúng đại chỉ chưa", Toast.LENGTH_LONG, true).show();

//                        Toast.makeText(getApplicationContext(), "Đã bắt đầu cập nhật vị trí hiện tại của bạn!\nVui lòng tự điền tên đường\n Kiểm tra lại xem đã đúng đại chỉ chưa", Toast.LENGTH_LONG).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                //Location settings are not satisfied. Attempting to upgrade
                                Log.i(TAG, "Cài đặt vị trí không hài lòng. Đang cố gắng nâng cấp " +
                                        "Cài đặt vị trí\n ");//location settings
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    //PendingIntent unable to execute request.
                                    Log.i(TAG, "PendingIntent không thể thực hiện yêu cầu.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toasty.error(getActivity(), ""+errorMessage+"\n"+e.getMessage(), Toast.LENGTH_SHORT, true).show();

//                                Toast.makeText(ChefRegistration.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Có thể nhận được vị trí");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS đã được bật");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null){
                            updateUI(loc);
                        }
                    }
                    else{
                        Toasty.warning(getActivity(), "Vui lòng nhấn nút GPS của bên phải chữ ĐĂNG KÝ .... ", Toast.LENGTH_LONG, true).show();
                        Erro();
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER đã được bật");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            updateUI(loc);
                        }
                    }else {
                        Toasty.warning(getActivity(), "Vui lòng nhấn nút GPS của bên phải chữ ĐĂNG KÝ .... ", Toast.LENGTH_LONG, true).show();
                        Erro();
                    }
                } else {
                    Toasty.warning(getActivity(), "Vui lòng nhấn nút GPS của bên phải chữ ĐĂNG KÝ .... ", Toast.LENGTH_LONG, true).show();
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);

                }
            } else {
                Log.d(TAG, "Không thể nhận được vị trí");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
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
                return (ContextCompat.checkSelfPermission(getActivity(),permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }
    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void Erro() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
        alerta.setTitle("Thông báo lỗi");
        alerta.setMessage("Vui lòng nhấn nút màu đỏ GPS bên phải chữ Đăng Ký");
        alerta.setPositiveButton("OK", null);
        alerta.show();
    }

    private void updateUI(@NotNull Location location) {
        if(location == null)
        {
            Erro();
        }
        else {
            Log.d(TAG, "updateUI");

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.d("latitude", "latitude--" + latitude);
            Log.d("longitude", "longitude--" + longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                if (latitude == 0 && longitude == 0) {
                    Toasty.warning(getActivity(), "Vui lòng nhấn nút GPS của bên phải chữ ĐĂNG KÝ .... ", Toast.LENGTH_LONG, true).show();
                } else {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        Log.d(TAG, "getAddress:  address" + address);
                        Log.d(TAG, "getAddress:  city" + city);
                        Log.d(TAG, "getAddress:  state" + state);
                        Log.d(TAG, "getAddress:  postalCode" + postalCode);
                        Log.d(TAG, "getAddress:  knownName" + knownName);

                        //set addresses
                        //đặt dịa chỉ

                    }
                }
            } catch (Exception e) {
                Toasty.error(getActivity(), ""+ e.getMessage(), Toast.LENGTH_SHORT, true).show();
            }

        }

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("TAG", "onStatusChanged: " + provider);

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
//gps/location disabled
        //gps / vị trí bị vô hiệu hóa
        //please turn on location
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        Toasty.warning(getActivity(), "Vui lòng mở vị trí....", Toast.LENGTH_SHORT, true).show();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:{
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("Các quyền này là bắt buộc đối với ứng dụng. Vui lòng cho phép truy cập.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    //No rejected permissions.
                    Log.d(TAG, "Không có quyền bị từ chối.");
                    canGetLocation = true;
                    getLocation();
                }
            }break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates

        }
    }
}
