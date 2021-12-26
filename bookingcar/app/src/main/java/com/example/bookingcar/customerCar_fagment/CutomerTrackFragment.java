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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bookingcar.R;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private TextInputLayout NameLocation,NameLoGo,RankingTime,Product,Cargo,NameCar;
    private TextInputEditText NamelocationTET,NameLoGoTET,ProductTET,CargoTET,NameCarTET,RankingTimeTET;
    private String namelocationTET,nameLoGoTET,productTET,cargoTET,nameCarTET,rankingTimeTET;
    private Button postUpBTN;

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
        ButterKnife.bind(getActivity());
        initLocation();
        restoreValuesFromBundle(savedInstanceState);

        Fauth = FirebaseAuth.getInstance();

        postUpBTN = (Button)  v.findViewById(R.id.postUpBTN);


        postUpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
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
        addProduct();
    }

    private void addProduct() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang load Vui lòng chờ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Tiến hành đăng thông tin ...");
        progressDialog.show();

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

        data = FirebaseDatabase.getInstance().getReference("Users");
        data.child(Fauth.getUid()).child("Infomations").child(timestamp).setValue(hashMap)
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
    private void clearData(){

        NamelocationTET.setText("");
        NameLoGoTET.setText("");
        ProductTET.setText("");
        CargoTET.setText("");
        NameCarTET.setText("'");
        RankingTimeTET.setText("");
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
