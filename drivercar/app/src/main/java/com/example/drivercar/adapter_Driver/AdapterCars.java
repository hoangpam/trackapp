package com.example.drivercar.adapter_Driver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.FilterProductDriver.FilterDriver;
import com.example.drivercar.R;
import com.example.drivercar.activity.EditInfoDriver;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelDriver;
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

import es.dmoral.toasty.Toasty;

public class AdapterCars extends RecyclerView.Adapter<AdapterCars.HolderCars> implements Filterable {

    private Context context;

    public  ArrayList<ModelCars> carsList, filtercarList;
    private FilterDriver filter;
    final int VIEW_TYPE_MESSAGE = 0;
    final int VIEW_TYPE_IMAGE = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public AdapterCars(Context context, ArrayList<ModelCars> carsList){
        this.context = context;
        this.carsList = carsList;
        this.filtercarList = carsList;
    }


    class HolderCars extends RecyclerView.ViewHolder{
        private Button dropBtn,updayeBtn;
        private TextView namecarTv,license_plateTv,weightTv,stateTv;

        /*holds views of recyclerview*/
        public HolderCars(@NonNull View itemView) {
            super(itemView);

            namecarTv = (TextView) itemView.findViewById(R.id.namecarTv);
            license_plateTv = (TextView) itemView.findViewById(R.id.license_plateTv);
            weightTv = (TextView) itemView.findViewById(R.id.weightTv);
            stateTv = (TextView) itemView.findViewById(R.id.stateTv);

            dropBtn = (Button) itemView.findViewById(R.id.dropBtn);
            updayeBtn = (Button) itemView.findViewById(R.id.updayeBtn);


        }
    }
    @NonNull
    @Override
    public HolderCars onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_driver,parent,false);

        return new HolderCars(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCars holder, int position) {
        //get data


        ModelCars modelCars = carsList.get(position);
        String vehicletypename = modelCars.getVehicleTypeName(); //lo???i xe
        String licenseplate = modelCars.getLicense_Plates(); //bi???n s?? xe
        String vehicletonnage = modelCars.getVehicleTonnage();//tr???ng t???i xe
        String sizecar = modelCars.getSizeCar();
        String status = modelCars.getStatus();
        String id = modelCars.getCarId();
        String tam = modelCars.getTimestamp();

        //set data
        holder.namecarTv.setText(vehicletypename);
        holder.license_plateTv.setText(licenseplate);
        holder.weightTv.setText(sizecar);

        //change status text color
        if(status.equals("??ang th???c thi")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.stateTv.setText("??ang th???c thi");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
        }
        else if(status.equals("???? ph?? duy???t")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.green));
            holder.stateTv.setText("???? ph?? duy???t");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect03));
        }
        else if(status.equals("??ang ch??? ph?? duy???t")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.Red));
            holder.stateTv.setText("??ang ch??? ph?? duy???t");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect06));
        }



        //xo?? ph???n ??ang th??ng tin
        holder.dropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(context, "B???n ??ang xo?? th??ng tin"+vehicletypename+" hay kh??ng ???", Toast.LENGTH_SHORT, true).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Th??ng b??o xo??")
                        .setMessage("B???n ch???c c?? mu???n xo?? th??ng tin "+vehicletypename+" hay kh??ng ???")
                        .setPositiveButton("Ch???c xo??", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteDriver(tam);
                            }
                        })
                        .setNegativeButton("Kh??ng xo??", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel, dismiss dialog
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //c???p nh???t l???i th??ng tin
        holder.updayeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditInfoDriver.class);

                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show items details (in bottom sheet)
//                detailsBottomSheet(modelDriver);//here modelProduct contrains detailt of click product
            }
        });
        holder.itemView.setVisibility(View.GONE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    private void detailsBottomSheet(ModelDriver modelDriver) {
        //bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_details_driver, null);
        //set view to bottomsheet
        bottomSheetDialog.setContentView(view);


        //init views of bottomsheet

        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageView productIconIv = view.findViewById(R.id.productIconIv);
        TextView discountNoteTv = view.findViewById(R.id.discountNoteTv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView quantilyTv = view.findViewById(R.id.quantilyTv);
        TextView discountPriceTv = view.findViewById(R.id.discountPriceTv);
        TextView originalPriceTv = view.findViewById(R.id.originalPriceTv);

        //get data

        String timestamp = modelDriver.getTimestamp();

        //set data





        //delete click
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show delete confirm dialog
//                Toasty.warning(context, "B???n ??ang xo?? th??ng tin"+title+" hay kh??ng ???", Toast.LENGTH_SHORT, true).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Th??ng b??o xo??")
//                        .setMessage("B???n ch???c c?? mu???n xo?? s???n ph???m "+title+" hay kh??ng ???")
//                        .setPositiveButton("Ch???c xo??", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //delete
//                                deleteProduct(id);//id is the product id
//                                Intent intent = new Intent(context, DriverPanel_BottomNavigation.class);
//                                context.startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Kh??ng xo??", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //cancel, dismiss dialog
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        });
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //dismiss bottom sheet
//                bottomSheetDialog.dismiss();
//            }
//        });
        //show dialog
        bottomSheetDialog.show();
    }


    private void deleteDriver(String id) {
        //delete product using its id

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Cars").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //product deleted

                        Toasty.success(context, "Th??ng tin ???? ???????c xo??...", Toast.LENGTH_SHORT, true).show();
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
        return  carsList == null ? 0 : carsList.size();
    }



    public void setFiltercarList(ArrayList<ModelCars> filteredList) {
        filtercarList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {



        return filter;
    }


}
