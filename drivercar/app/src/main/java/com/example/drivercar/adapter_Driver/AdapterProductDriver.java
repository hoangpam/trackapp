package com.example.drivercar.adapter_Driver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.R;
import com.example.drivercar.FilterProductDriver.FilterProduct;
import com.example.drivercar.bottomnavigation.DriverPanel_BottomNavigation;
import com.example.drivercar.model.ModelProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.app.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AdapterProductDriver extends RecyclerView.Adapter<AdapterProductDriver.HolderProductChef> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productsList, filterList;
    private FilterProduct filter;

    public AdapterProductDriver(Context context, ArrayList<ModelProduct> productsList){
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;
    }




    class HolderProductChef extends RecyclerView.ViewHolder{
        private ImageView productIconIv;
        private TextView discountNoteTv,titleTv,quantilyTv,discountPriceTv,originalPriceTv;

        /*holds views of recyclerview*/
        public HolderProductChef(@NonNull View itemView) {
            super(itemView);

            productIconIv = (ImageView) itemView.findViewById(R.id.productIconIv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            quantilyTv = (TextView) itemView.findViewById(R.id.quantilyTv);
            discountPriceTv = (TextView) itemView.findViewById(R.id.discountPriceTv);
            discountNoteTv = (TextView) itemView.findViewById(R.id.discountNoteTv);
            originalPriceTv = (TextView) itemView.findViewById(R.id.originalPriceTv);

        }
    }
    @NonNull
    @Override
    public HolderProductChef onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_driver,parent,false);

        return new HolderProductChef(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductChef holder, int position) {
        //get data
        ModelProduct modelProduct = productsList.get(position);
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String originalPrice = modelProduct.getOriginalPrice();
        String icon = modelProduct.getProductIcon();
        String quantity = modelProduct.getProductQuanlity();
        String title = modelProduct.getProductTitle();
        String timestamp = modelProduct.getTimestamp();

        //set data
        holder.titleTv.setText(title);
        holder.quantilyTv.setText(quantity);
        holder.discountNoteTv.setText(discountNote);
        holder.discountPriceTv.setText(" VND"+discountPrice+"");
        holder.originalPriceTv.setText("VND"+originalPrice+"");
        if(discountAvailable.equals("true"))
        {
            //product is on discount
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.discountNoteTv.setVisibility(View.VISIBLE);
            holder.originalPriceTv.setPaintFlags(holder.originalPriceTv.getPaintFlags()  | Paint.STRIKE_THRU_TEXT_FLAG); // add strike through on original price
        }
        else {
            //product is on not discount
            holder.discountPriceTv.setVisibility(View.GONE);
            holder.discountNoteTv.setVisibility(View.GONE);
        }
        try{
            Picasso.get().load(icon).placeholder(R.drawable.ic_shop).into(holder.productIconIv);
        }catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.ic_shop);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show items details (in bottom sheet)
                detailsBottomSheet(modelProduct);//here modelProduct contrains detailt of click product
            }
        });

    }

    private void detailsBottomSheet(ModelProduct modelProduct) {
        //bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottomsheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_details_driver, null);
        //set view to bottomsheet
        bottomSheetDialog.setContentView(view);



        //init views of bottomsheet
        ImageButton backBtn = view.findViewById(R.id.backBtn);
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
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountNote();
        String discountPrice = modelProduct.getDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDesciptions();
        String originalPrice = modelProduct.getOriginalPrice();
        String icon = modelProduct.getProductIcon();
        String quantity = modelProduct.getProductQuanlity();
        String title = modelProduct.getProductTitle();
        String timestamp = modelProduct.getTimestamp();

        //set data
        titleTv.setText(title);
        descriptionTv.setText(productDescription);
        categoryTv.setText(productCategory);
        quantilyTv.setText(quantity);
        discountNoteTv.setText(discountNote);
        discountPriceTv.setText("VND"+discountPrice+"");
        originalPriceTv.setText("VND"+originalPrice+"");

        if(discountAvailable.equals("true"))
        {
            //product is on discount
            discountPriceTv.setVisibility(View.VISIBLE);
            discountNoteTv.setVisibility(View.VISIBLE);
            originalPriceTv.setPaintFlags(originalPriceTv.getPaintFlags()  | Paint.STRIKE_THRU_TEXT_FLAG); // add strike through on original price
        }
        else {
            //product is on not discount
            discountPriceTv.setVisibility(View.GONE);
            discountNoteTv.setVisibility(View.GONE);
        }
        try{
            Picasso.get().load(icon).placeholder(R.drawable.ic_shop).into(productIconIv);
        }catch (Exception e){
            productIconIv.setImageResource(R.drawable.ic_shop);
        }


        //delete click
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete confirm dialog
                Toasty.warning(context, "Bạn đang xoá sản phẩm"+title+" hay không ???", Toast.LENGTH_SHORT, true).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo xoá")
                        .setMessage("Bạn chắc có muốn xoá sản phẩm "+title+" hay không ???")
                        .setPositiveButton("Chắc xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                                deleteProduct(id);//id is the product id
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
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss bottom sheet
                bottomSheetDialog.dismiss();
            }
        });
        //show dialog
        bottomSheetDialog.show();
    }

    private void deleteProduct(String id) {
        //delete product using its id

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chef");
        reference.child(firebaseAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //product deleted
                        Toasty.success(context, "Sản phẩm đã được xoá...", Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed deleting product
                        Toasty.error(context, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void filterList(ArrayList<ModelProduct> filteredList) {
        filterList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new FilterProduct(this, filterList);
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
