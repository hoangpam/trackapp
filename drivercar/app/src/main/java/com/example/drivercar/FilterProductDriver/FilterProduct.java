package com.example.drivercar.FilterProductDriver;

import android.widget.Filter;

import com.example.drivercar.adapter_Driver.AdapterProductDriver;
import com.example.drivercar.adapter_Driver.AdapterProductDriver;
import com.example.drivercar.model.ModelProduct;

import java.util.ArrayList;

public class FilterProduct extends Filter {

    private AdapterProductDriver adapter;
    private ArrayList<ModelProduct> filterList,productsList;


    public FilterProduct(AdapterProductDriver adapter, ArrayList<ModelProduct> productsList){
        this.adapter = adapter;
        this.productsList = productsList;
        this.filterList = productsList;
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
            ArrayList<ModelProduct> filterModels  =new ArrayList<>();
            for (int i =0; i <filterModels.size();i++){
                //check, search by title and category
                if(filterModels.get(i).getProductTitle().toUpperCase().contains(constraint) ||
                        filterModels.get(i).getProductCategory().toUpperCase().contains(constraint)){
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
        adapter.productsList = (ArrayList<ModelProduct>) results.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
