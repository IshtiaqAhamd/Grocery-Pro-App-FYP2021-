package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.R;
import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.models.ModelShop;

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.HolderShope>{
    Context context;
    ArrayList<ModelShop> shopsList;

    public AdapterShop(Context context, ArrayList<ModelShop> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }

    @NonNull
    @Override
    public HolderShope onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate Layout row_shop.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_shop, parent, false);
        return new HolderShope(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderShope holder, int position) {
        // Get Data
        ModelShop modelShop = shopsList.get(position);
        String accountType = modelShop.getAccount_Type();
        String address = modelShop.getAddress();
        String cityName= modelShop.getCity_Name();
        String countryName = modelShop.getCountry_Name();
        String deliverFee = modelShop.getDeliver_Fee();
        String emailAddress = modelShop.getEmail_Address();
        String latitude = modelShop.getLatitude();
        String longitude = modelShop.getLongitude();
        String online = modelShop.getOnline();
        String fullName = modelShop.getFull_Name();
        String phoneNumber = modelShop.getPhone_Number();
        String uid = modelShop.getUid();
        String timeStamp = modelShop.getTime_Stamp();
        String shopOpen = modelShop.getShop_Open();
        String stateName = modelShop.getState_Name();
        String profileImage = modelShop.getProfile_Image();
        String shopName = modelShop.getShop_Name();

        // Set Data
        holder.shopNameTV.setText(shopName);
        holder.phoneTV.setText(phoneNumber);
        holder.addressTV.setText(address);
        // Check if online
        if (online.equals("true")){
            //Shop Owner is Online
            holder.onlineIV.setVisibility(View.VISIBLE);
        }
        else {
            //Shop Owner is Offline
            holder.onlineIV.setVisibility(View.GONE);
        }
        //Check if Shop Open
        if (shopOpen.equals("true")){
            // Shop Open
            holder.shopClosedTV.setVisibility(View.GONE);
        }
        else {
            // Shope Closed
            holder.shopClosedTV.setVisibility(View.VISIBLE);
        }
        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_gray).into(holder.shopIV);
        }
        catch (Exception exp)
        {
            holder.shopIV.setImageResource(R.drawable.ic_store_gray);
        }
    }

    @Override
    public int getItemCount() {
        return shopsList.size(); // Return Number of Records
    }

    // View Holder
    class HolderShope extends RecyclerView.ViewHolder{
        ImageView shopIV, onlineIV;
        TextView shopClosedTV, shopNameTV, phoneTV, addressTV;
        RatingBar ratingBar;
        // UI Views of row_shop.xml
        public HolderShope(@NonNull View itemView) {
            super(itemView);
            shopIV = itemView.findViewById(R.id.shopIV);
            onlineIV = itemView.findViewById(R.id.onlineIV);
            shopClosedTV = itemView.findViewById(R.id.shopClosedTV);
            shopNameTV = itemView.findViewById(R.id.shopNameTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }

    }
}
