package com.example.drivercar.adapter_customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.FilterProductUser.FilterProductUser;
import com.example.drivercar.R;
import com.example.drivercar.activity.DriverPhoneActivity;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.example.drivercar.model.ModelDriver;
import com.example.drivercar.model.ModelInfomation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.app.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterInfomation extends RecyclerView.Adapter<AdapterInfomation.HolderInfomation>  {

        private Context context;
        public ArrayList<ModelInfomation> infomationsList;

        public AdapterInfomation(Context context, ArrayList<ModelInfomation> infomationsList) {
            this.context = context;
            this.infomationsList = infomationsList;
        }

        @NonNull
        @Override
        public AdapterInfomation.HolderInfomation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //inflare layout
            View view = LayoutInflater.from(context).inflate(R.layout.row_product_booking,parent,false);
            return new AdapterInfomation.HolderInfomation(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterInfomation.HolderInfomation holder, int position) {
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


//            holder.negotiate_number.setText(getItemViewType(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //handle item clicks, show items details (in bottom sheet)
                   detailsBottomSheet(modelInfomation);
                }
            });
        }
        private void detailsBottomSheet(ModelInfomation modelInfomation)
        {
            //bottom sheet
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            //inflate view for bottomsheet
            View view = LayoutInflater.from(context).inflate(R.layout.activity_driver_phone, null);
            //set view to bottomsheet
            bottomSheetDialog.setContentView(view);

            //innit views of bottomsheet
            ImageButton backBtn = view.findViewById(R.id.backBtn);
            TextView numbetTv = view.findViewById(R.id.numbetTv);
            TextView AreaTv = view.findViewById(R.id.AreaTv);
            ImageButton call_urgent_Btn = view.findViewById(R.id.call_urgent_Btn);
            ImageButton callBtn = view.findViewById(R.id.callBtn);
            MaterialEditText edtNumberPrice =  view.findViewById(R.id.edtNumberPrice);
            Button ordersBtn = view.findViewById(R.id.ordersBtn);


            call_urgent_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialPhone();
                }
            });

            FirebaseAuth firebaseAuth ;
            firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child("Role").child("Customer").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds:snapshot.getChildren())
                    {
                        String UID = ""+ds.child("UID").getValue();
                        String MobileNo = ""+ds.child("MobileNo").getValue();
                        numbetTv.setText(MobileNo);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(numbetTv.getText().toString()))));
                }
            });


            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dismiss bottom sheet
                    bottomSheetDialog.dismiss();
                }
            });

            modelInfomation = infomationsList.get(0);
            String nameCarInfo = modelInfomation.getNameCarInfo();
            String nameLoGoInfo = modelInfomation.getNameLoGoInfo();
            String nameLoInfo = modelInfomation.getNameLoInfo();
            String productInfo = modelInfomation.getProductInfo();
            String rankingTimeInfo = modelInfomation.getRankingTimeInfo();
            String cargoInfo = modelInfomation.getCargoInfo();
            String AreaLocation = modelInfomation.getAreaLocation();
            String Latitude = modelInfomation.getLatitude();
            String Longitude = modelInfomation.getLongitude();
            String PhoneCus = modelInfomation.getPhoneCus();
            String PhoneDriver = modelInfomation.getPhoneDriver();
            String Services = modelInfomation.getServices();
            String Services1 = modelInfomation.getServices1();
            String Services2 = modelInfomation.getServices2();
            String Services3 = modelInfomation.getServices3();
            String Services4 = modelInfomation.getServices4();
            String infomationId = modelInfomation.getInfomationId();
            String uid = modelInfomation.getUid();

            String status = modelInfomation.getStatus();

            ordersBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String Numberprice = edtNumberPrice.getText().toString().trim();
                    final ProgressDialog progressDialog = new ProgressDialog(ordersBtn.getContext());
                    progressDialog.setMessage("Đang cập nhật báo giá cho bạn...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    ref.orderByChild("UID").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds: snapshot.getChildren())
                                    {
                                        String phone =""+ds.child("MobileNo").getValue();
                                        String timetamps = "" + System.currentTimeMillis();
                                        HashMap<String,Object> hashMap = new HashMap<>();
                                        hashMap.put("ID",""+timetamps);
                                        hashMap.put("PriceDriverOrder",""+Numberprice);
                                        hashMap.put("UID_Driver",firebaseAuth.getUid());
                                        hashMap.put("UID_Customer",uid);
                                        hashMap.put("NameLoGoInfo",nameLoGoInfo);
                                        hashMap.put("NameCarInfo",nameCarInfo);
                                        hashMap.put("NameLoInfo",nameLoInfo);
                                        hashMap.put("ProductInfo",productInfo);
                                        hashMap.put("RankingTimeInfo",rankingTimeInfo);
                                        hashMap.put("CargoInfo",cargoInfo);
                                        hashMap.put("Status","Đang thương lượng");
                                        hashMap.put("AreaLocation",AreaLocation);
                                        hashMap.put("Latitude",Latitude);
                                        hashMap.put("Longitude",Longitude);
                                        hashMap.put("PhoneCus",PhoneCus);
                                        hashMap.put("PhoneDriver",phone);
                                        hashMap.put("Services",Services);
                                        hashMap.put("Services1",Services1);
                                        hashMap.put("Services2",Services2);
                                        hashMap.put("Services3",Services3);
                                        hashMap.put("Services4",Services4);


                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                        ref.child("SpendingDriver").child(timetamps)
                                                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toasty.success(context, "Báo giá thành công" ,Toast.LENGTH_LONG, true).show();
                                                    Intent intent = new Intent(context, DriverPanel_BottomNavigation.class);
                                                    context.startActivity(intent);
                                                }else{
                                                    progressDialog.dismiss();
                                                    Toasty.error(context, "Đang lỗi" ,Toast.LENGTH_LONG, true).show();
                                                }

                                            }
                                        });

                                        ref.child("Infomations").child(infomationId)
                                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void unused) {
                                                infomationsList.remove(0);
                                                notifyDataSetChanged();
                                                notifyItemChanged(0);

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

            if(Integer.parseInt(AreaLocation) == 2000000)
            {
                AreaTv.setText("Ngoài tỉnh");
            }else if(Integer.parseInt(AreaLocation) == 1000000)
            {
                AreaTv.setText("Trong tỉnh");
            }
            else {
                AreaTv.setText("Không xác định");
            }
            //show dialog
            bottomSheetDialog.show();
        }
        String phone1 = "1800199000";
        private void dialPhone() {
            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(phone1))));

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

