package com.example.drivercar.FilterProductDriver;

import android.widget.Filter;

import com.example.drivercar.adapter_Driver.AdapterProductDriver;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelDriver;
import com.example.drivercar.adapter_Driver.AdapterProductDriver;


import java.util.ArrayList;

public class FilterCars extends Filter {

    private AdapterProductDriver adapter;
    private ArrayList<ModelCars> filterList,carsList;


    public FilterCars(AdapterProductDriver adapter, ArrayList<ModelCars> carsList){
        this.adapter = adapter;
        this.carsList = carsList;
        this.filterList = carsList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
//        List<ModelProduct> filteredList = new ArrayList<>();
        //validate data for search query
        if(constraint != null && constraint.length() >0) {
            //search filed not empty, searching something, perform search

            //change to upper case, to make insensitive
            constraint = constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<ModelCars> filterModels  =new ArrayList<>();
            for (int i =0; i <filterModels.size();i++){
                //check, search by title and category
                if(filterModels.get(i).getVehicleTypeName().toUpperCase().contains(constraint) ){
                    //add filtered data to list
                    filterModels.add(filterModels.get(i));

                }
            }

            results.count = filterModels.size();
            results.values= filterModels;
        }
        else{
            //search filed empty,not searching, return original/all/complete list
            results.count = filterList.size();
            results.values= filterList;
//            results.count = productsList.size();
//            results.values = productsList;
        }



        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
//        modelProductList.clear();
//        modelProductList.addAll((List) results.values);

        adapter.carsList= (ArrayList<ModelCars>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
