package com.example.drivercar.adapter_customer;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.FilterProductUser.FilterProductUser;
import com.example.drivercar.R;
import com.example.drivercar.model.ModelDriver;
import com.example.drivercar.model.ModelInfomation;

import org.w3c.dom.Text;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterInfomation extends RecyclerView.Adapter<AdapterInfomation.HolderInfomation>  {

        private Context context;
        public ArrayList<ModelInfomation> infomationsList, filterList;


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

            holder.locationGo.setText(nameLoInfo);
            holder.locationG.setText(nameLoGoInfo);
            holder.cargoTV.setText(productInfo);
            holder.TimeTV.setText(rankingTimeInfo);
            holder.weightTV.setText(cargoInfo+"tấn");
            holder.car2TV.setText(nameCarInfo);



        }

        @Override
        public int getItemCount() {
            return infomationsList.size();
        }



        class HolderInfomation extends RecyclerView.ViewHolder{

            //ui views
            private ImageView nextIv,TimeIV,weightIV,carIV;
            private TextView negotiate_number,locationGo,locationG,TimeTV,weightTV,cargoTV, carTV,car2TV;

            public HolderInfomation(@NonNull View item){
                super(item);

                //init ui views

                nextIv = item.findViewById(R.id.nextIv);
                TimeIV = item.findViewById(R.id.TimeIV);
                weightIV = item.findViewById(R.id.weightIV);
                carIV = item.findViewById(R.id.carIV);
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

