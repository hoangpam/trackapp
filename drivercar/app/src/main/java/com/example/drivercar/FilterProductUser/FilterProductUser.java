package com.example.drivercar.FilterProductUser;

import android.widget.Filter;

import com.example.drivercar.adapter_customer.AdapterProductUser;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelDriver;

import java.util.ArrayList;

public class FilterProductUser extends Filter {

    private AdapterProductUser adapter;
    private ArrayList<ModelDriver> filterList;

    public FilterProductUser(AdapterProductUser adapter, ArrayList<ModelDriver> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
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
        }



        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
//        modelProductList.clear();
//        modelProductList.addAll((List) results.values);
        adapter.driverList = (ArrayList<ModelDriver>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
