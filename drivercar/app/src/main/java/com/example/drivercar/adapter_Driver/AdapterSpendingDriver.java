package com.example.drivercar.adapter_Driver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.drivercar.R;
import com.example.drivercar.activity.GoogleMapActivity;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelInfomation;
import com.example.drivercar.model.SpendingDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AdapterSpendingDriver extends RecyclerView.Adapter<AdapterSpendingDriver.HolderSpendingDrivers>{

    private Context context;
    public ArrayList<SpendingDriver> spendingDriverArrayList;

    public AdapterSpendingDriver(Context context, ArrayList<SpendingDriver> spendingDriverArrayList) {
        this.context = context;
        this.spendingDriverArrayList = spendingDriverArrayList;
    }
    public class HolderSpendingDrivers extends RecyclerView.ViewHolder {
        private TextView negotiate_number,locationGo,locationG,TimeTV,weightTV,cargoTV, carTV,car2TV,negotiate;

        public HolderSpendingDrivers(@NonNull View itemView) {
            super(itemView);
            //init ui views

            negotiate = itemView.findViewById(R.id.negotiate);
            negotiate_number = itemView.findViewById(R.id.negotiate_number);
            locationGo = itemView.findViewById(R.id.locationGo);
            locationG = itemView.findViewById(R.id.locationG);
            TimeTV = itemView.findViewById(R.id.TimeTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            cargoTV = itemView.findViewById(R.id.cargoTV);
            carTV = itemView.findViewById(R.id.carTV);
            car2TV = itemView.findViewById(R.id.car2TV);
        }
    }

    @NonNull
    @Override
    public HolderSpendingDrivers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_booking,parent,false);
        return new HolderSpendingDrivers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSpendingDriver.HolderSpendingDrivers holder, int position) {
        SpendingDriver spendingDriver = spendingDriverArrayList.get(position);
        String nameCarInfo = spendingDriver.getNameCarInfo();
        String nameLoGoInfo = spendingDriver.getNameLoGoInfo();
        String nameLoInfo = spendingDriver.getNameLoInfo();
        String productInfo = spendingDriver.getProductInfo();
        String rankingTimeInfo = spendingDriver.getRankingTimeInfo();
        String cargoInfo = spendingDriver.getCargoInfo();
        String status = spendingDriver.getStatus();

        holder.locationGo.setText(nameLoInfo);
        holder.locationG.setText(nameLoGoInfo);
        holder.cargoTV.setText(productInfo);
        holder.TimeTV.setText(rankingTimeInfo);
        holder.weightTV.setText(cargoInfo+"tấn");
        holder.car2TV.setText(nameCarInfo);

        if(status.equals("Đang thương lượng"))
        {
            holder.negotiate.setTextColor(context.getResources().getColor(R.color.white));
            holder.negotiate.setText("Đang thương lượng");
            holder.negotiate.setBackground(context.getResources().getDrawable(R.drawable.shape_rect07));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show items details (in bottom sheet)
                detailsBottomSheet(spendingDriver);
            }
        });
    }
    private void detailsBottomSheet(SpendingDriver spendingDriver)
    {
        //bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_detail_comple_order, null);
        //set view to bottomsheet
        bottomSheetDialog.setContentView(view);

        //innit views of bottomsheet
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton drop_order_Btn = view.findViewById(R.id.drop_order_Btn);
        ImageButton callBtn = view.findViewById(R.id.callBtn);
        TextView numbetTv = view.findViewById(R.id.numbetTv);
        TextView nameorderTv = view.findViewById(R.id.nameorderTv);
        TextView priceTv = view.findViewById(R.id.priceTv);
        TextView priceTTv = view.findViewById(R.id.priceTTv);
        Button oderdeliBtn = view.findViewById(R.id.oderdeliBtn);


        FirebaseAuth firebaseAuth ;
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        spendingDriver = spendingDriverArrayList.get(0);

        String PriceDriverOrder = spendingDriver.getPriceDriverOrder();
        String AreaLocation = spendingDriver.getAreaLocation();
        String Services = spendingDriver.getServices();
        String Services1 = spendingDriver.getServices1();
        String Services2 = spendingDriver.getServices2();
        String Services3 = spendingDriver.getServices3();
        String Services4 = spendingDriver.getServices4();

        String ID = spendingDriver.getID();
        String Latitude = spendingDriver.getLatitude();
        String Longitude = spendingDriver.getLongitude();
        String NameCarInfo = spendingDriver.getNameCarInfo();
        String NameLoGoInfo = spendingDriver.getNameLoGoInfo();
        String NameLoInfo = spendingDriver.getNameLoInfo();
        String PhoneCus = spendingDriver.getPhoneCus();
        String PhoneDriver = spendingDriver.getPhoneDriver();
        String ProductInfo = spendingDriver.getProductInfo();
        String RankingTimeInfo = spendingDriver.getRankingTimeInfo();
        String UID_Customer = spendingDriver.getUID_Customer();
        String UID_Driver = spendingDriver.getUID_Driver();
        String cargoInfo = spendingDriver.getCargoInfo();



        ref.child("Role").child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    String UID = ""+ds.child("UID").getValue();
                    String MobileNo = ""+ds.child("MobileNo").getValue();
                    String FirstName = ""+ds.child("FirstName").getValue();
                    String LastName = ""+ds.child("LastName").getValue();
                    numbetTv.setText(MobileNo);
                    nameorderTv.setText(FirstName+LastName);
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

        drop_order_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.child("Role").child("Customer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren())
                        {
                            String UID = ""+ds.child("UID").getValue();
                            String MobileNo = ""+ds.child("MobileNo").getValue();
                            String FirstName = ""+ds.child("FirstName").getValue();
                            String LastName = ""+ds.child("LastName").getValue();
                            ref.child("SpendingDriver")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds:snapshot.getChildren())
                                            {
                                                String IDD = ""+ds.child("ID").getValue();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Thông báo xoá")
                                                        .setMessage("Bạn chắc có muốn xoá thông tin giao hàng của anh/chị "+ FirstName + LastName +" hay không ???")
                                                        .setPositiveButton("Chắc xoá", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //delete
                                                                ref.child("SpendingDriver").child(IDD).removeValue()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                //product deleted
                                                                                Toasty.success(context, "Thông tin đã được xoá thành công...", Toast.LENGTH_SHORT, true).show();
                                                                                spendingDriverArrayList.remove(0);
                                                                                notifyDataSetChanged();
                                                                                notifyItemChanged(0);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                            }
                                                                        });
                                                                Intent intent = new Intent(context, DriverPanel_BottomNavigation.class);
                                                                context.startActivity(intent);
                                                            }
                                                        })
                                                        .setNegativeButton("Không xoá", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //cancel, dismiss dialog
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .show();
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


        int dva = Integer.parseInt(AreaLocation);
        int dvb = Integer.parseInt(Services);
        int dvc = Integer.parseInt(Services1);
        int dvd = Integer.parseInt(Services2);
        int dve = Integer.parseInt(Services3);
        int dvf = Integer.parseInt(Services4);
        int tong = dva + dvb + dvc + dvd + dve + dvf;


        priceTv.setText(PriceDriverOrder+" "+"VND");
        priceTTv.setText(""+tong+" "+"VND");

        oderdeliBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(oderdeliBtn.getContext());
                progressDialog.setMessage("Đang cập nhật báo giá cho bạn...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                String timetamps = "" + System.currentTimeMillis();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("ID",""+timetamps);
                hashMap.put("PriceDriverOrder",""+PriceDriverOrder);
                hashMap.put("AreaLocation",""+AreaLocation);
                hashMap.put("Services",""+Services);
                hashMap.put("Services1",""+Services1);
                hashMap.put("Services2",""+Services2);
                hashMap.put("Services3",""+Services3);
                hashMap.put("Services4",""+Services4);
                hashMap.put("DeliveryDate",""+ID);
                hashMap.put("Latitude",""+Latitude);
                hashMap.put("Longitude",""+Longitude);
                hashMap.put("NameCarInfo",""+NameCarInfo);
                hashMap.put("NameLoGoInfo",""+NameLoGoInfo);
                hashMap.put("NameLoInfo",""+NameLoInfo);
                hashMap.put("PhoneCus",""+PhoneCus);
                hashMap.put("PhoneDriver",""+PhoneDriver);
                hashMap.put("ProductInfo",""+ProductInfo);
                hashMap.put("UID_Customer",""+UID_Customer);
                hashMap.put("UID_Driver",""+UID_Driver);
                hashMap.put("CargoInfo",""+cargoInfo);
                hashMap.put("TotalPrice",""+tong);
                hashMap.put("Status","Đang giao hàng");
                hashMap.put("RankingTimeInfo",""+RankingTimeInfo);

                ref.child("OrderDelivery").child(timetamps)
                        .setValue(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toasty.success(context, "Đang thực thi giao đơn hàng" ,Toast.LENGTH_LONG, true).show();
                                    Intent intent = new Intent(context, GoogleMapActivity.class);
                                    context.startActivity(intent);
                                }else{
                                    progressDialog.dismiss();
                                    Toasty.error(context, "Đang lỗi" ,Toast.LENGTH_LONG, true).show();
                                }
                            }
                        });
                ref.child("SpendingDriver").child(ID)
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        spendingDriverArrayList.remove(0);
                        notifyDataSetChanged();
                        notifyItemChanged(0);
                    }
                });



            }
        });
        //show dialog
        bottomSheetDialog.show();

    }

    private void deleteOrder(String id) {
        //delete product using its id
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child("SpendingDriver").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //product deleted
                        Toasty.success(context, "Thông tin đã được xoá thành công...", Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return spendingDriverArrayList.size();
    }


}
