package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import android.widget.Filter;

import java.util.ArrayList;

import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.adapters.AdapterProductSeller;
import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.models.ModelProduct;

public class FilterProduct extends Filter {

    private AdapterProductSeller adapter;
    private ArrayList<ModelProduct> filterList;

    public FilterProduct(AdapterProductSeller adapter, ArrayList<ModelProduct> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        // Validate Data for Search Query
        if (constraint !=null && constraint.length()>0)
        {
            //Search filed Not Empty, Search Something, Perform Search
            // Change to Upper Case, to Make Insensitive
            constraint = constraint.toString().toUpperCase();
            // Stored Our Filtered List
            ArrayList<ModelProduct> filteredModels = new ArrayList<>();
            for (int i=0; i<filterList.size();i++){
                // Check, Search by Title and Category
                if (filterList.get(i).getProduct_Title().toUpperCase().contains(constraint) ||
                filterList.get(i).getProduct_Category().toUpperCase().contains(constraint)){
                    // Add Filtered Data to List
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else
        {
            //Search filed Empty, Not Searching, Return Original/All/ Complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.productsList = (ArrayList<ModelProduct>) results.values;
        // Refresh Adapter
        adapter.notifyDataSetChanged();
    }
}