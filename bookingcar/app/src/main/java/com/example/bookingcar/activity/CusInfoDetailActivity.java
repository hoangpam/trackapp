package com.example.bookingcar.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingcar.R;
import com.example.bookingcar.object.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class CusInfoDetailActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_info_detail);

        NameLocation =(TextInputLayout)  findViewById(R.id.NameLocation);
        NameLoGo = (TextInputLayout)  findViewById(R.id.NameLoGo);
        RankingTime = (TextInputLayout)  findViewById(R.id.RankingTime);
        Product = (TextInputLayout)  findViewById(R.id.Product);
        Cargo = (TextInputLayout)  findViewById(R.id.Cargo);
        NameCar = (TextInputLayout)  findViewById(R.id.NameCar);

        NamelocationTET =(TextInputEditText)  findViewById(R.id.NamelocationTET);
        NameLoGoTET = (TextInputEditText)  findViewById(R.id.NameLoGoTET);
        ProductTET = (TextInputEditText)  findViewById(R.id.ProductTET);
        CargoTET = (TextInputEditText)  findViewById(R.id.CargoTET);
        NameCarTET = (TextInputEditText)  findViewById(R.id.NameCarTET);
        RankingTimeTET = (TextInputEditText)  findViewById(R.id.RankingTimeTET);

        gpsBTN = (ImageButton) findViewById(R.id.gpsBtn);
        chipNoiThanh = (Chip) findViewById(R.id.chipNoiThanh);
        chipNgoaiThanh = (Chip) findViewById(R.id.chipNgoaiThanh);
        textchip = (TextView) findViewById(R.id.textchip);
        serviceSwitch = (SwitchCompat) findViewById(R.id.serviceSwitch);
        serviceSwitch1 = (SwitchCompat) findViewById(R.id.serviceSwitch1);
        serviceSwitch2 = (SwitchCompat) findViewById(R.id.serviceSwitch2);
        serviceSwitch3 = (SwitchCompat) findViewById(R.id.serviceSwitch3);
        serviceSwitch4 = (SwitchCompat) findViewById(R.id.serviceSwitch4);
        serviceTv = (TextView) findViewById(R.id.serviceTv);
        service1Tv = (TextView) findViewById(R.id.service1Tv);
        service2Tv = (TextView) findViewById(R.id.service2Tv);
        service3Tv = (TextView) findViewById(R.id.service3Tv);
        service4Tv = (TextView) findViewById(R.id.service4Tv);
        backBN = findViewById(R.id.backBN);
        Fauth = FirebaseAuth.getInstance();

        postUpBTN = (Button)  findViewById(R.id.postUpBTN);

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
        loadAllProduct();
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
            Toasty.error(CusInfoDetailActivity.this, "Điểm lấy hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NamelocationTET.setError("Điểm lấy hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(nameLoGoTET)){
            Toasty.error(CusInfoDetailActivity.this, "Điểm giao hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NameLoGoTET.setError("Điểm giao hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(rankingTimeTET)){
            Toasty.error(CusInfoDetailActivity.this, "Thời gian bốc hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            RankingTimeTET.setError("Thời gian bốc hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(productTET)){
            Toasty.error(CusInfoDetailActivity.this, "Tên hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            ProductTET.setError("Tên hàng  là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(cargoTET)){
            Toasty.error(CusInfoDetailActivity.this, "Khối lượng hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            CargoTET.setError("Khối lượng hàng là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(nameCarTET)){
            Toasty.error(CusInfoDetailActivity.this, "Loại xe muốn đặt là cần thiết ..", Toast.LENGTH_SHORT, true).show();
            NameCarTET.setError("Loại xe muốn đặt là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Textchip)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn khu vực vận chuyển ..", Toast.LENGTH_SHORT, true).show();
            textchip.setError("Khu vực vận chuyển là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(ServiceTv)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn dịch vụ đóng goi..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service1Tv)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn dịch vụ khiêng hàng..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service2Tv)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn dịch vụ vệ sinh..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service3Tv)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn dịch vụ bảo hiểm hàng hoá..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        if(TextUtils.isEmpty(Service4Tv)){
            Toasty.error(CusInfoDetailActivity.this, "Chọn dịch vụ trọn gói..", Toast.LENGTH_SHORT, true).show();
            serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
            return;//don't proceed further
        }
        addProduct();
    }
    private void loadAllProduct()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");
        reference.child("Infomations").orderByChild("uid").equalTo(Fauth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            String AreaLocation = ""+ds.child("AreaLocation").getValue();
                            String PhoneCus = ""+ds.child("PhoneCus").getValue();
                            String Services = ""+ds.child("Services").getValue();
                            String Services1 = ""+ds.child("Services1").getValue();
                            String Services2 = ""+ds.child("Services2").getValue();
                            String Services3 = ""+ds.child("Services3").getValue();
                            String Services4 = ""+ds.child("Services4").getValue();
                            String Status = ""+ds.child("Status").getValue();
                            String cargoInfo = ""+ds.child("cargoInfo").getValue();
                            String nameCarInfo = ""+ds.child("nameCarInfo").getValue();
                            String nameLoGoInfo = ""+ds.child("nameLoGoInfo").getValue();
                            String nameLoInfo = ""+ds.child("nameLoInfo").getValue();
                            String productInfo = ""+ds.child("productInfo").getValue();
                            String rankingTimeInfo = ""+ds.child("rankingTimeInfo").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();

                            NamelocationTET.setText(nameLoInfo);
                            NameLoGoTET.setText(nameLoGoInfo);
                            ProductTET.setText(productInfo);
                            CargoTET.setText(cargoInfo);
                            NameCarTET.setText(nameCarInfo);
                            RankingTimeTET.setText(rankingTimeInfo);
                            textchip.setText(AreaLocation);
                            if(textchip.getText().equals("1000000")){
                                chipNoiThanh.setTextColor(getResources().getColor(R.color.colorPrimary));
                                chipNoiThanh.setBackground(getResources().getDrawable(R.drawable.shape_rect04));
                            }
                            if(textchip.getText().equals("2000000"))
                            {
                                chipNgoaiThanh.setTextColor(getResources().getColor(R.color.colorPrimary));
                                chipNgoaiThanh.setBackground(getResources().getDrawable(R.drawable.shape_rect04));
                            }
                            serviceTv.setText(Services);
                            if(serviceTv.getText().equals("100000"))
                            {
                                serviceSwitch.setChecked(true);
                            }else{
                                serviceSwitch4.setChecked(false);
                            }
                            service1Tv.setText(Services1);
                            if(service1Tv.getText().equals("300000"))
                            {
                                serviceSwitch1.setChecked(true);
                            }else{
                                serviceSwitch4.setChecked(false);
                            }
                            service2Tv.setText(Services2);
                            if(service2Tv.getText().equals("400000"))
                            {
                                serviceSwitch2.setChecked(true);
                            }else{
                                serviceSwitch4.setChecked(false);
                            }
                            service3Tv.setText(Services3);
                            if(service3Tv.getText().equals("600000"))
                            {
                                serviceSwitch3.setChecked(true);
                            }else{
                                serviceSwitch4.setChecked(false);
                            }
                            service4Tv.setText(Services4);
                            if(service4Tv.getText().equals("100000"))
                            {
                                serviceSwitch4.setChecked(true);
                            }else{
                                serviceSwitch4.setChecked(false);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void addProduct() {
        progressDialog = new ProgressDialog(this);
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
                            
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("Users");
                            reference.child("Infomations").orderByChild("uid").equalTo(Fauth.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot ds: snapshot.getChildren()) {
                                                String timestamp1 = ""+ds.child("timestamp").getValue();
                                                HashMap<String ,Object> hashMap = new HashMap<>();
                                                hashMap.put("infomationId",""+timestamp1);
                                                hashMap.put("timestamp",""+timestamp1);
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
                                                data.child("Infomations").child(timestamp1).setValue(hashMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(@NonNull Void unused) {
                                                                progressDialog.dismiss();
                                                                Toasty.success(CusInfoDetailActivity.this, "Đã thêm thông tin thành công...!", Toast.LENGTH_SHORT, true).show();
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
        textchip.setText("");
        serviceTv.setText("");
        service1Tv.setText("");
        service2Tv.setText("");
        service3Tv.setText("");
        service4Tv.setText("");

    }
}