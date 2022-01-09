package com.example.bookingcar.adapter_Customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingcar.R;
import com.example.bookingcar.activity.CusInfoDetailActivity;
import com.example.bookingcar.bottomnavigation.CustomerPanel_BottomNavigation;
import com.example.bookingcar.model.ModelInfomation;
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
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class AdapterInfomation extends RecyclerView.Adapter<AdapterInfomation.HolderInfomation>  {

        private Context context;
        public ArrayList<ModelInfomation> infomationsList;

        public AdapterInfomation(Context context, ArrayList<ModelInfomation> infomationsList) {
            this.context = context;
            this.infomationsList = infomationsList;
        }

        @NonNull
        @Override
        public HolderInfomation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //inflare layout
            View view = LayoutInflater.from(context).inflate(R.layout.row_product_booking,parent,false);
            return new HolderInfomation(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HolderInfomation holder, int position) {
            //get data
            ModelInfomation modelInfomation = infomationsList.get(position);
            String nameCarInfo = modelInfomation.getNameCarInfo();
            String nameLoGoInfo = modelInfomation.getNameLoGoInfo();
            String nameLoInfo = modelInfomation.getNameLoInfo();
            String productInfo = modelInfomation.getProductInfo();
            String rankingTimeInfo = modelInfomation.getRankingTimeInfo();
            String cargoInfo = modelInfomation.getCargoInfo();
            String Satus = modelInfomation.getStatus();
            String status = modelInfomation.getStatus();
            holder.locationGo.setText(nameLoInfo);
            holder.locationG.setText(nameLoGoInfo);
            holder.cargoTV.setText(productInfo);
            holder.TimeTV.setText(rankingTimeInfo);
            holder.weightTV.setText(cargoInfo+"tấn");
            holder.car2TV.setText(nameCarInfo);
            if(status.equals("Chưa nhân đơn"))
            {
                holder.negotiate.setTextColor(context.getResources().getColor(R.color.Red));
                holder.negotiate.setText("Chưa nhân đơn");
                holder.negotiate.setBackground(context.getResources().getDrawable(R.drawable.shape_rect06));
            }
            if(status.equals("Đã nhân đơn"))
            {
                holder.negotiate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.negotiate.setText("Đã nhân đơn");
                holder.negotiate.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
            }
//            holder.negotiate_number.setText(getItemViewType(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //handle item clicks, show items details (in bottom sheet)
//                detailsBottomSheet(modelDriver);//here modelProduct contrains detailt of click product
                    detailsBottomSheet(modelInfomation);
                }
            });

        }

        private void detailsBottomSheet(ModelInfomation modelInfomation)
        {

            //bottom sheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            //inflate view for bottomsheet
            View view = LayoutInflater.from(context).inflate(R.layout.activity_cus_info_detail, null);
            //set view to bottomsheet
            bottomSheetDialog.setContentView(view);

            TextInputLayout NameLocation =(TextInputLayout)  view.findViewById(R.id.NameLocation);
            TextInputLayout NameLoGo = (TextInputLayout)  view.findViewById(R.id.NameLoGo);
            TextInputLayout RankingTime = (TextInputLayout)  view.findViewById(R.id.RankingTime);
            TextInputLayout Product = (TextInputLayout)  view.findViewById(R.id.Product);
            TextInputLayout Cargo = (TextInputLayout)  view.findViewById(R.id.Cargo);
            TextInputLayout NameCar = (TextInputLayout)  view.findViewById(R.id.NameCar);

            TextInputEditText NamelocationTET =(TextInputEditText)  view.findViewById(R.id.NamelocationTET);
            TextInputEditText NameLoGoTET = (TextInputEditText)  view.findViewById(R.id.NameLoGoTET);
            TextInputEditText ProductTET = (TextInputEditText)  view.findViewById(R.id.ProductTET);
            TextInputEditText CargoTET = (TextInputEditText)  view.findViewById(R.id.CargoTET);
            TextInputEditText NameCarTET = (TextInputEditText)  view.findViewById(R.id.NameCarTET);
            TextInputEditText RankingTimeTET = (TextInputEditText)  view.findViewById(R.id.RankingTimeTET);

            ImageButton gpsBTN = (ImageButton) view.findViewById(R.id.gpsBtn);
            Chip chipNoiThanh = (Chip) view.findViewById(R.id.chipNoiThanh);
            Chip chipNgoaiThanh = (Chip) view.findViewById(R.id.chipNgoaiThanh);
            TextView textchip = (TextView) view.findViewById(R.id.textchip);
            SwitchCompat serviceSwitch = (SwitchCompat) view.findViewById(R.id.serviceSwitch);
            SwitchCompat serviceSwitch1 = (SwitchCompat) view.findViewById(R.id.serviceSwitch1);
            SwitchCompat serviceSwitch2 = (SwitchCompat) view.findViewById(R.id.serviceSwitch2);
            SwitchCompat serviceSwitch3 = (SwitchCompat) view.findViewById(R.id.serviceSwitch3);
            SwitchCompat serviceSwitch4 = (SwitchCompat) view.findViewById(R.id.serviceSwitch4);
            TextView serviceTv = (TextView) view.findViewById(R.id.serviceTv);
            TextView service1Tv = (TextView) view.findViewById(R.id.service1Tv);
            TextView service2Tv = (TextView) view.findViewById(R.id.service2Tv);
            TextView service3Tv = (TextView) view.findViewById(R.id.service3Tv);
            TextView service4Tv = (TextView) view.findViewById(R.id.service4Tv);
            ImageButton backBN = view.findViewById(R.id.backBN);
            FirebaseAuth Fauth = FirebaseAuth.getInstance();

            Button postUpBTN = (Button)  view.findViewById(R.id.postUpBTN);

            backBN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CustomerPanel_BottomNavigation.class);
                    context.startActivity(intent);
                }
            });
            NameCarTET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
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
            });

            postUpBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String namelocationTET = NamelocationTET.getText().toString().trim();
                    String nameLoGoTET = NameLoGoTET.getText().toString().trim();
                    String productTET = ProductTET.getText().toString().trim();
                    String cargoTET = CargoTET.getText().toString().trim();
                    String nameCarTET = NameCarTET.getText().toString().trim();
                    String rankingTimeTET = RankingTimeTET.getText().toString().trim();
                    String Textchip = textchip.getText().toString().trim();
                    String ServiceTv = serviceTv.getText().toString().trim();
                    String Service1Tv = service1Tv.getText().toString().trim();
                    String Service2Tv = service2Tv.getText().toString().trim();
                    String Service3Tv = service3Tv.getText().toString().trim();
                    String Service4Tv = service4Tv.getText().toString().trim();

                    serviceTv.setError("");
                    textchip.setError("");
                    NamelocationTET.setError("");
                    NameLoGoTET.setError("");
                    ProductTET.setError("");
                    CargoTET.setError("");
                    NameCarTET.setError("'");
                    RankingTimeTET.setError("");

                    if(TextUtils.isEmpty(namelocationTET)){
                        Toasty.error(context, "Điểm lấy hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        NamelocationTET.setError("Điểm lấy hàng là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(nameLoGoTET)){
                        Toasty.error(context, "Điểm giao hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        NameLoGoTET.setError("Điểm giao hàng là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(rankingTimeTET)){
                        Toasty.error(context, "Thời gian bốc hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        RankingTimeTET.setError("Thời gian bốc hàng là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(productTET)){
                        Toasty.error(context, "Tên hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        ProductTET.setError("Tên hàng  là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(cargoTET)){
                        Toasty.error(context, "Khối lượng hàng là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        CargoTET.setError("Khối lượng hàng là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(nameCarTET)){
                        Toasty.error(context, "Loại xe muốn đặt là cần thiết ..", Toast.LENGTH_SHORT, true).show();
                        NameCarTET.setError("Loại xe muốn đặt là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(Textchip)){
                        Toasty.error(context, "Chọn khu vực vận chuyển ..", Toast.LENGTH_SHORT, true).show();
                        textchip.setError("Khu vực vận chuyển là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(ServiceTv)){
                        Toasty.error(context, "Chọn dịch vụ đóng goi..", Toast.LENGTH_SHORT, true).show();
                        serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(Service1Tv)){
                        Toasty.error(context, "Chọn dịch vụ khiêng hàng..", Toast.LENGTH_SHORT, true).show();
                        serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(Service2Tv)){
                        Toasty.error(context, "Chọn dịch vụ vệ sinh..", Toast.LENGTH_SHORT, true).show();
                        serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(Service3Tv)){
                        Toasty.error(context, "Chọn dịch vụ bảo hiểm hàng hoá..", Toast.LENGTH_SHORT, true).show();
                        serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
                        return;//don't proceed further
                    }
                    if(TextUtils.isEmpty(Service4Tv)){
                        Toasty.error(context, "Chọn dịch vụ trọn gói..", Toast.LENGTH_SHORT, true).show();
                        serviceTv.setError("Dịch vụ đóng goi là cần thiết ..");
                        return;//don't proceed further
                    }
                    ProgressDialog progressDialog = new ProgressDialog(context);
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
                                                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users");
                                                            data.child("Infomations").child(timestamp1).setValue(hashMap)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(@NonNull Void unused) {
                                                                            progressDialog.dismiss();
                                                                            Toasty.success(context, "Đã sửa thông tin thành công...!", Toast.LENGTH_SHORT, true).show();

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
            });
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
                                    chipNoiThanh.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                    chipNoiThanh.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
                                }
                                if(textchip.getText().equals("2000000"))
                                {
                                    chipNgoaiThanh.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                    chipNgoaiThanh.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
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
            chipNgoaiThanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    chipNgoaiThanh.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    chipNgoaiThanh.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
                    textchip.setText("2000000");
                    chipNoiThanh.setTextColor(context.getResources().getColor(R.color.black));
                    chipNoiThanh.setBackground(context.getResources().getDrawable(R.color.orange1));
                }
            });

            chipNoiThanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipNoiThanh.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    chipNoiThanh.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
                    chipNgoaiThanh.setTextColor(context.getResources().getColor(R.color.black));
                    chipNgoaiThanh.setBackground(context.getResources().getDrawable(R.color.orange1));
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

            //show dialog
            bottomSheetDialog.show();
        }


        @Override
        public int getItemCount() {
            return infomationsList.size();
        }



        class HolderInfomation extends RecyclerView.ViewHolder{

            //ui views

            private TextView negotiate_number,locationGo,locationG,TimeTV,weightTV,cargoTV, carTV,car2TV,negotiate;

            public HolderInfomation(@NonNull View item){
                super(item);

                //init ui views
                negotiate = item.findViewById(R.id.negotiate);
                negotiate_number = item.findViewById(R.id.negotiate_number);
                locationGo = item.findViewById(R.id.locationGo);
                locationG = item.findViewById(R.id.locationG);
                TimeTV = item.findViewById(R.id.TimeTV);
                weightTV = item.findViewById(R.id.weightTV);
                cargoTV = item.findViewById(R.id.cargoTV);
                carTV = item.findViewById(R.id.carTV);
                car2TV = item.findViewById(R.id.car2TV);

            }
        }

}

