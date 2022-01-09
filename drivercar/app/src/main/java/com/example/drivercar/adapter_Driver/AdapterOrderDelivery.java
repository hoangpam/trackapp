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

import java.util.ArrayList;

public class AdapterOrderDelivery extends RecyclerView.Adapter<AdapterOrderDelivery.HolderOrderDelivery> {
    private Context context;
    public ArrayList<OrderDelivery> orderDeliveryArrayList;

    public AdapterOrderDelivery(Context context, ArrayList<OrderDelivery> orderDeliveryArrayList) {
        this.context = context;
        this.orderDeliveryArrayList = orderDeliveryArrayList;
    }

    @NonNull
    @Override
    public HolderOrderDelivery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflare layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_booking,parent,false);
        return new HolderOrderDelivery(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderDelivery holder, int position) {
        //get data
        OrderDelivery orderDelivery = orderDeliveryArrayList.get(position);
        String nameCarInfo = orderDelivery.getNameCarInfo();
        String nameLoGoInfo = orderDelivery.getNameLoGoInfo();
        String nameLoInfo = orderDelivery.getNameLoInfo();
        String productInfo = orderDelivery.getProductInfo();
        String rankingTimeInfo = orderDelivery.getRankingTimeInfo();
        String cargoInfo = orderDelivery.getCargoInfo();

        String status = orderDelivery.getStatus();
        holder.locationGo.setText(nameLoInfo);
        holder.locationG.setText(nameLoGoInfo);
        holder.cargoTV.setText(productInfo);
        holder.TimeTV.setText(rankingTimeInfo);
        holder.weightTV.setText(cargoInfo+"tấn");
        holder.car2TV.setText(nameCarInfo);

        if(status.equals("Đang giao hàng"))
        {
            holder.negotiate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.negotiate.setText("Đang giao hàng");
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
        return orderDeliveryArrayList.size();
    }



    class HolderOrderDelivery extends RecyclerView.ViewHolder{

        //ui views

        private TextView negotiate_number,locationGo,locationG,TimeTV,weightTV,cargoTV, carTV,car2TV,negotiate;

        public HolderOrderDelivery(@NonNull View item){
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
