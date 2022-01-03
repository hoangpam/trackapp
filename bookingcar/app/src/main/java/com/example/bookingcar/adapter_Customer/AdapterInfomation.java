package com.example.bookingcar.adapter_Customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingcar.R;
import com.example.bookingcar.activity.CusInfoDetailActivity;
import com.example.bookingcar.model.ModelInfomation;

import java.util.ArrayList;

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
                    Intent intent = new Intent(context, CusInfoDetailActivity.class);

                    context.startActivity(intent);
                }
            });

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

