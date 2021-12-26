package com.example.bookingcar.adapter_Customer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.bookingcar.R;
import com.example.bookingcar.FilterProductUser.FilterProductUser;
import com.example.bookingcar.activity.DriverDetalsActivity;
import com.example.bookingcar.model.ModeDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AdapterDriver extends RecyclerView.Adapter<AdapterDriver.HolderDriver> implements Filterable {

    private Context context;
    public ArrayList<ModeDriver> driverList,filterListDriver;
    private FilterProductUser filter;

    public AdapterDriver(Context context, ArrayList<ModeDriver> driverList) {
        this.context = context;
        this.driverList = driverList;
        this.filterListDriver = driverList;
    }

    @NonNull
    @Override
    public HolderDriver onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate layout row_shop.xml

        View view = LayoutInflater.from(context).inflate(R.layout.row_driver,parent,false);
        return new HolderDriver(view);
    }



    @Override
    public void onBindViewHolder(@NonNull HolderDriver holder, int position) {

        //get data
        ModeDriver modelDriver = driverList.get(position);

        String accountType =  modelDriver.getAccountType();
        String address = modelDriver.getCompleteAddress();
        String area = modelDriver.getArea();
        String city = modelDriver.getCity();
        String state = modelDriver.getState();
        String house = modelDriver.getHouse();

        String email = modelDriver.getEmailId();
        String latitude = modelDriver.getLatitude();
        String longitude = modelDriver.getLongitude();
        String online = modelDriver.getOnline();
        String fname = modelDriver.getFirstName();
        String lname = modelDriver.getLastName();

        String phone = modelDriver.getMobileNo();
        String uid = modelDriver.getUID();
        String timestamp = modelDriver.getTimestamp();

        String profileImage = modelDriver.getImageURL();

        loadReviews(modelDriver, holder);//load avg rating, set to rattingbar


        //set data


        holder.phoneTv.setText(phone);
        holder.addressTv.setText(address);
        //check if online
        if(online.equals("true")){
            //shop ower is online
            holder.onlineIv.setVisibility(View.VISIBLE);
        }else{
            //shop ower is offline
            holder.onlineIv.setVisibility(View.GONE);
        }


        try{
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_shop).into(holder.shopIv);
        }catch (Exception e){
            holder.shopIv.setImageResource(R.drawable.ic_shop);
            Toasty.error(context, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();
        }

        //handle click listener, show shop details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DriverDetalsActivity.class);
                intent.putExtra("shopUid", uid);
                context.startActivity(intent);
            }
        });
    }

    private float ratingSum = 0;
    private void loadReviews(ModeDriver modelDriver, HolderDriver holder) {

        String shopUid = modelDriver.getUID();


        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before adding data into it
                        try{


                            ratingSum=0;
                            for (DataSnapshot ds: snapshot.getChildren()){
                                float rating = Float.parseFloat(""+ds.child("rattings").getValue()); // e.g 4.3
                                ratingSum = ratingSum + rating; //for avg rating , add(addition of) all ratings, later will divide it by number of reviews
                            }

                            long numberOfReviews = snapshot.getChildrenCount();
                            float avgRating = ratingSum/numberOfReviews;

                            holder.ratingBar.setRating(avgRating);

                        }catch (Exception e){
                            Log.e(""+e.getMessage(),"");
//                            Toasty.error(context, "Đang lỗi\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return driverList.size() ; //return number of records
    }

    public void filterList(ArrayList<ModeDriver> filteredList) {
        filterListDriver = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
//            filter = new FilterProductUser(this,filterListShop);
        }else{

            notifyDataSetChanged();
            Toasty.error(context, "Không có trong hệ thống cửa hàng này", Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "Không có trong hệ thống sản phẩm này", Toast.LENGTH_SHORT).show();
        }

        return filter;
    }



    //view holder
    class HolderDriver extends RecyclerView.ViewHolder{

        //ui views of row_shop.xml

        private CircularImageView shopIv;
        private ImageView onlineIv;
        private TextView shopClosedTv,shopNameTv,phoneTv,addressTv;
        private RatingBar ratingBar;

        public HolderDriver(@NonNull View view)
        {
            super(view);

            //init uid views
            shopIv =(CircularImageView) view.findViewById(R.id.shopIv);
            onlineIv =(ImageView) view.findViewById(R.id.onlineIv);
            shopClosedTv =(TextView) view.findViewById(R.id.shopClosedTv);
            shopNameTv =(TextView) view.findViewById(R.id.shopNameTv);
            phoneTv =(TextView)  view.findViewById(R.id.phoneTv);
            addressTv =(TextView)  view.findViewById(R.id.addressTv);
            ratingBar =(RatingBar) view.findViewById(R.id.ratingBar);


        }
    }


}
