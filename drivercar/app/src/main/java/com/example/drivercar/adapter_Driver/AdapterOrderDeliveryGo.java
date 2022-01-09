package com.example.drivercar.adapter_Driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.R;
import com.example.drivercar.model.OrderDelivery;
import com.example.drivercar.model.OrderDeliveryGo;

import java.util.ArrayList;

public class AdapterOrderDeliveryGo extends RecyclerView.Adapter<AdapterOrderDeliveryGo.HolderOrderDeliveryGo> {
    private Context context;
    public ArrayList<OrderDeliveryGo> orderDeliveryGoArrayList;

    public AdapterOrderDeliveryGo(Context context, ArrayList<OrderDeliveryGo> orderDeliveryGoArrayList) {
        this.context = context;
        this.orderDeliveryGoArrayList = orderDeliveryGoArrayList;
    }

    @NonNull
    @Override
    public HolderOrderDeliveryGo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflare layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_booking,parent,false);
        return new HolderOrderDeliveryGo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderDeliveryGo holder, int position) {
        //get data
        OrderDeliveryGo orderDeliveryGo = orderDeliveryGoArrayList.get(position);
        String nameCarInfo = orderDeliveryGo.getNameCarInfo();
        String nameLoGoInfo = orderDeliveryGo.getNameLoGoInfo();
        String nameLoInfo = orderDeliveryGo.getNameLoInfo();
        String productInfo = orderDeliveryGo.getProductInfo();
        String rankingTimeInfo = orderDeliveryGo.getRankingTimeInfo();
        String cargoInfo = orderDeliveryGo.getCargoInfo();

        String status = orderDeliveryGo.getStatus();
        holder.locationGo.setText(nameLoInfo);
        holder.locationG.setText(nameLoGoInfo);
        holder.cargoTV.setText(productInfo);
        holder.TimeTV.setText(rankingTimeInfo);
        holder.weightTV.setText(cargoInfo+"tấn");
        holder.car2TV.setText(nameCarInfo);

        if(status.equals("Đã hoàn thành"))
        {
            holder.negotiate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.negotiate.setText("Đã hoàn thành");
            holder.negotiate.setBackground(context.getResources().getDrawable(R.drawable.shape_rect04));
        }
//            holder.negotiate_number.setText(getItemViewType(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show items details (in bottom sheet)

            }
        });
    }



    @Override
    public int getItemCount() {
        return orderDeliveryGoArrayList.size();
    }



    class HolderOrderDeliveryGo extends RecyclerView.ViewHolder{

        //ui views

        private TextView negotiate_number,locationGo,locationG,TimeTV,weightTV,cargoTV, carTV,car2TV,negotiate;

        public HolderOrderDeliveryGo(@NonNull View item){
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
