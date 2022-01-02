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

public class AdapterProductDriver extends RecyclerView.Adapter<AdapterProductDriver.HolderProductDriver> implements Filterable {

    private Context context;
    public ArrayList<ModelDriver> driverList, filterList;
    public  ArrayList<ModelCars> carsList, filtercarList;
    private FilterDriver filter;
    final int VIEW_TYPE_MESSAGE = 0;
    final int VIEW_TYPE_IMAGE = 1;

    public AdapterProductDriver(Context context, ArrayList<ModelDriver> driverList, ArrayList<ModelCars> carsList){
        this.context = context;
        this.driverList = driverList;
        this.filterList = driverList;
        this.carsList = carsList;
        this.filtercarList = carsList;
    }


    class HolderProductDriver extends RecyclerView.ViewHolder{
        private Button dropBtn,updayeBtn;
        private TextView namecarTv,license_plateTv,weightTv,stateTv;

        /*holds views of recyclerview*/
        public HolderProductDriver(@NonNull View itemView) {
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
    public HolderProductDriver onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_driver,parent,false);

        return new HolderProductDriver(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductDriver holder, int position) {
        //get data
        ModelDriver modelDriver = driverList.get(position);

        String timestamp = modelDriver.getTimestamp();

        ModelCars modelCars = carsList.get(position);
        String vehicletypename = modelCars.getVehicleTypeName(); //loại xe
        String licenseplate = modelCars.getLicense_Plates(); //biển só xe
        String vehicletonnage = modelCars.getVehicleTonnage();//trọng tải xe
        String sizecar = modelCars.getSizeCar();
        String status = modelCars.getStatus();
        String id = modelCars.getTimestamp();

        //set data
        holder.namecarTv.setText(vehicletypename);
        holder.license_plateTv.setText(licenseplate);
        holder.weightTv.setText(sizecar);

        //change status text color
        if(status.equals("Đang thực thi")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.stateTv.setText("Đang thực thi");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
        }
        else if(status.equals("Đã phê duyệt")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.green));
            holder.stateTv.setText("Đã phê duyệt");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect03));
        }
        else if(status.equals("Đang chờ phê duyệt")){
            holder.stateTv.setTextColor(context.getResources().getColor(R.color.Red));
            holder.stateTv.setText("Đang chờ phê duyệt");
            holder.stateTv.setBackground(context.getResources().getDrawable(R.drawable.shape_rect06));
        }

        //xoá phần đang thông tin
        holder.dropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(context, "Bạn đang xoá thông tin"+vehicletypename+" hay không ???", Toast.LENGTH_SHORT, true).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo xoá")
                        .setMessage("Bạn chắc có muốn xoá thông tin "+vehicletypename+" hay không ???")
                        .setPositiveButton("Chắc xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteDriver(id);

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
        });

        //cập nhật lại thông tin
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
//                Toasty.warning(context, "Bạn đang xoá thông tin"+title+" hay không ???", Toast.LENGTH_SHORT, true).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Thông báo xoá")
//                        .setMessage("Bạn chắc có muốn xoá sản phẩm "+title+" hay không ???")
//                        .setPositiveButton("Chắc xoá", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //delete
//                                deleteProduct(id);//id is the product id
//                                Intent intent = new Intent(context, DriverPanel_BottomNavigation.class);
//                                context.startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Không xoá", new DialogInterface.OnClickListener() {
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver");
        reference.child(firebaseAuth.getUid()).child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //product deleted
                        Toasty.success(context, "Thông tin đã được xoá...", Toast.LENGTH_SHORT, true).show();
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
        return driverList.size() + carsList.size();
    }
    @Override
    public int getItemViewType(int position){
        if(position < carsList.size()){
            return VIEW_TYPE_MESSAGE;
        }

        if(position - carsList.size() < carsList.size()){
            return VIEW_TYPE_IMAGE;
        }

        return -1;
    }
    public void filterList(ArrayList<ModelDriver> filteredList) {
        filterList = filteredList;
        notifyDataSetChanged();
    }

    public void setFiltercarList(ArrayList<ModelCars> filteredList) {
        filtercarList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new FilterDriver(this, filterList);
        }
//        }else{
//
//            notifyDataSetChanged();
//            Toasty.error(context, "Không có trong hệ thống sản phẩm này", Toast.LENGTH_SHORT).show();
////            Toast.makeText(context, "Không có trong hệ thống sản phẩm này", Toast.LENGTH_SHORT).show();
//        }

        return filter;
    }


}
