package com.example.drivercar.activity;


import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drivercar.R;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.security.Permission;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener,
        PermissionsListener, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "GoogleMapActivity";
    private ImageButton compleorderBtn;
    private DatabaseReference ref;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_google_map);
        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startButton);
        compleorderBtn = findViewById(R.id.compleorderBtn);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch navigation UI
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .shouldSimulateRoute(true)
                        .build();
                NavigationLauncher.startNavigation(GoogleMapActivity.this,options);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        compleorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectcomple();
            }
        });
    }

    private void selectcomple(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo hoàn thành giao hàng")
                        .setMessage("Bạn chắc có muốn chọn hoàn thành đơn hàng hay không hay không ???")
                        .setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child("OrderDelivery")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists())
                                                {
                                                    for (DataSnapshot ds:snapshot.getChildren())
                                                    {
                                                        String AreaLocation = ""+ds.child("AreaLocation").getValue();
                                                        String CargoInfo = ""+ds.child("CargoInfo").getValue();
                                                        String DeliveryDate = ""+ds.child("DeliveryDate").getValue();
                                                        String ID = ""+ds.child("ID").getValue();
                                                        String Latitude = ""+ds.child("Latitude").getValue();
                                                        String Longitude = ""+ds.child("Longitude").getValue();
                                                        String NameCarInfo = ""+ds.child("NameCarInfo").getValue();
                                                        String NameLoGoInfo = ""+ds.child("NameLoGoInfo").getValue();
                                                        String NameLoInfo = ""+ds.child("NameLoInfo").getValue();
                                                        String PhoneCus = ""+ds.child("PhoneCus").getValue();
                                                        String PhoneDriver = ""+ds.child("PhoneDriver").getValue();
                                                        String PriceDriverOrder = ""+ds.child("PriceDriverOrder").getValue();
                                                        String ProductInfo = ""+ds.child("ProductInfo").getValue();
                                                        String RankingTimeInfo = ""+ds.child("RankingTimeInfo").getValue();
                                                        String Services = ""+ds.child("Services").getValue();
                                                        String Services1 = ""+ds.child("Services1").getValue();
                                                        String Services2 = ""+ds.child("Services2").getValue();
                                                        String Services3 = ""+ds.child("Services3").getValue();
                                                        String Services4 = ""+ds.child("Services4").getValue();
                                                        String Status = ""+ds.child("Status").getValue();
                                                        String TotalPrice = ""+ds.child("TotalPrice").getValue();
                                                        String UID_Customer = ""+ds.child("ProductInfo").getValue();
                                                        String UID_Driver = ""+ds.child("ProductInfo").getValue();

                                                        String timestamp = "" + System.currentTimeMillis();
                                                        HashMap<String ,Object> hashMap = new HashMap<>();
                                                        hashMap.put("AreaLocation",""+AreaLocation);
                                                        hashMap.put("CargoInfo",""+CargoInfo);
                                                        hashMap.put("DeliveryDate",""+DeliveryDate);
                                                        hashMap.put("DeliveryDateGo",ID);
                                                        hashMap.put("ID",timestamp);
                                                        hashMap.put("Latitude",Latitude);
                                                        hashMap.put("Longitude",Longitude);
                                                        hashMap.put("NameCarInfo",NameCarInfo);
                                                        hashMap.put("NameLoGoInfo",NameLoGoInfo);
                                                        hashMap.put("NameLoInfo",NameLoInfo);
                                                        hashMap.put("PhoneCus",PhoneCus);
                                                        hashMap.put("PhoneDriver",PhoneDriver);
                                                        hashMap.put("PriceDriverOrder",PriceDriverOrder);
                                                        hashMap.put("ProductInfo",ProductInfo);
                                                        hashMap.put("RankingTimeInfo",RankingTimeInfo);
                                                        hashMap.put("Services",Services);
                                                        hashMap.put("Services1",Services1);
                                                        hashMap.put("Services2",Services2);
                                                        hashMap.put("Services3",Services3);
                                                        hashMap.put("Services4",Services4);
                                                        hashMap.put("Status","Đã hoàn thành");
                                                        hashMap.put("TotalPrice",TotalPrice);
                                                        hashMap.put("UID_Customer",UID_Customer);
                                                        hashMap.put("UID_Driver",UID_Driver);

                                                        ref.child("OrderDeliveryGo")
                                                                .child(timestamp)
                                                                .setValue(hashMap)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(@NonNull Void unused) {

                                                                        Toasty.success(GoogleMapActivity.this, "Đã hoàn thành đơn hàng...!", Toast.LENGTH_SHORT, true).show();
                                                                        Intent intent = new Intent(GoogleMapActivity.this, DriverPanel_BottomNavigation.class);
                                                                        startActivity(intent);
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });
                                                        ref.child("OrderDelivery").child(ID).removeValue()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(@NonNull Void unused) {

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });

                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Không chắc", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel, dismiss dialog
                                dialog.dismiss();
                            }
                        })
                        .show();

    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
    }

    private void enableLocation() {
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            //do some stuff
            initializeLocationEngine();
            initializeLocationLayer();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

         Location lastLocation = locationEngine.getLastLocation();
         if(lastLocation != null){
             originLocation  = lastLocation;
             setCameraPosition(lastLocation);
         }else{
             locationEngine.addLocationEngineListener(this);
         }

    }

    @SuppressLint("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location)
    {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());


        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng),13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

        if(destinationMarker != null)
        {
            map.removeMarker(destinationMarker);
        }

        destinationMarker = map.addMarker(new MarkerOptions().position(point));

        destinationPosition = Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition = Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());
        getRoute(originPosition,destinationPosition);
        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapboxBlue);
    }

    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() == null)
                        {
                            Log.e(TAG, "No routes found, check right user and access token");
                            return;
                        }else  if(response.body().routes().size() == 0){
                            Log.e(TAG,"No routes found");
                            return;
                        }

                        DirectionsRoute currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute != null)
                        {
                            navigationMapRoute.removeRoute();
                        }else{
                            navigationMapRoute = new NavigationMapRoute(null,mapView,map);
                        }

                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error:"+t.getMessage());
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // Present toast or dialog
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
           enableLocation();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if(locationEngine != null)
        {
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin != null)
        {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null)
        {
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null)
        {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null)
        {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }


}