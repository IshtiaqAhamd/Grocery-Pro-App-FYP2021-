package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.HolderProductSeller> implements Filterable {
    private Context context;
    public ArrayList<ModelProduct> productsList, filterList;
    private  FilterProduct filter;
    public AdapterProductSeller(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;
    }

    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller, parent, false);

        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {
        // Get Data
        final ModelProduct modelProduct = productsList.get(position);
        String ID = modelProduct.getProduct_ID();
        String User_ID = modelProduct.getUser_ID();
        String Discount_Available = modelProduct.getDiscount_Available();
        String Discount_Note = modelProduct.getDiscount_Note();
        String Discount_Price = modelProduct.getDiscount_Price();
        String Product_Category = modelProduct.getProduct_Category();
        String Product_Description = modelProduct.getProduct_Description();
        String Product_Icon = modelProduct.getProduct_Icon();
        String Product_Quantity = modelProduct.getProduct_Quantity();
        String Product_Title = modelProduct.getProduct_Title();
        String Time_Stamp = modelProduct.getTime_Stamp();
        String Original_Price = modelProduct.getOriginal_Price();
        // Set Data
        holder.title.setText(Product_Title);
        holder.quantity.setText(Product_Quantity);
        holder.discountedNote.setText(Discount_Note);
        holder.discountedPrice.setText("$"+Discount_Price);
        holder.originalPrice.setText("$"+Original_Price);

        if (Discount_Available.equals("true"))
        {
            // Product Is On Discount
            holder.discountedPrice.setVisibility(View.VISIBLE);
            holder.discountedNote.setVisibility(View.VISIBLE);
            //Add Strike Through On Original Price
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            // Product Is Not On Discount
            holder.discountedPrice.setVisibility(View.GONE);
            holder.discountedNote.setVisibility(View.GONE);
        }
        try {

        }
        catch (Exception exp)
        {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hold Item Clicks, Show Item Details (In Bottom Sheet)
                detailsBottomSheet(modelProduct); // Here, modelProduct Contains Details of clicked Product
            }
        });
    }

    private void detailsBottomSheet(ModelProduct modelProduct) {
        // Bottom Sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        // Inflate View for Bottom Sheet
        View view = LayoutInflater.from(context).inflate(R.layout.bs_product_details_seller, null);
        // Set View to Bottom Sheet
        bottomSheetDialog.setContentView(view);
        // Initialization Views of Bottom Sheet
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView productIconIV = view.findViewById(R.id.productIconIV);
        TextView discountNoteTV = view.findViewById(R.id.discountNoteTV);
        TextView titleTV = view.findViewById(R.id.titleTV);
        TextView descriptionTV = view.findViewById(R.id.descriptionTV);
        TextView categoryTV = view.findViewById(R.id.categoryTV);
        TextView quantityTV = view.findViewById(R.id.quantityTV);
        TextView discountedPriceTV = view.findViewById(R.id.discountedPriceTV);
        TextView originalPriceTV = view.findViewById(R.id.originalPriceTV);

        //Get Data
        String ID = modelProduct.getProduct_ID();
        String User_ID = modelProduct.getUser_ID();
        String Discount_Available = modelProduct.getDiscount_Available();
        String Discount_Note = modelProduct.getDiscount_Note();
        String Discount_Price = modelProduct.getDiscount_Price();
        String Product_Category = modelProduct.getProduct_Category();
        String Product_Description = modelProduct.getProduct_Description();
        String Product_Icon = modelProduct.getProduct_Icon();
        String Product_Quantity = modelProduct.getProduct_Quantity();
        String Product_Title = modelProduct.getProduct_Title();
        String Time_Stamp = modelProduct.getTime_Stamp();
        String Original_Price = modelProduct.getOriginal_Price();

        //Set Data
        titleTV.setText(Product_Title);
        descriptionTV.setText(Product_Description);
        categoryTV.setText(Product_Category);
        quantityTV.setText(Product_Quantity);
        discountNoteTV.setText(Discount_Note);
        discountedPriceTV.setText("$"+Discount_Price);
        originalPriceTV.setText("$"+Original_Price);

        if (Discount_Available.equals("true"))
        {
            // Product Is On Discount
            discountedPriceTV.setVisibility(View.VISIBLE);
            discountNoteTV.setVisibility(View.VISIBLE);
            //Add Strike Through On Original Price
            originalPriceTV.setPaintFlags(originalPriceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            // Product Is Not On Discount
            discountedPriceTV.setVisibility(View.GONE);
            discountNoteTV.setVisibility(View.GONE);
        }

        try {

        }
        catch (Exception exp)
        {

        }
        // Show Dialog
        bottomSheetDialog.show();
        // Edit Click
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                // Open Edit Product Activity, Pass id of Product
                Intent intent = new Intent(context,EditProductActivity.class);
                intent.putExtra("productId",ID);
                context.startActivity(intent);
            }
        });

        // Delete Click
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                // Show Delete Confirm Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete product" +Product_Title+ "?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                deleteProduct(ID); // id is the product id
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel, Dismiss
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        // Back Press
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss Bottom Sheet
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void deleteProduct(String id) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Product Deleted
                        Toast.makeText(context, "Product Deleted...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed deleting Product
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null)
        {
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder{
        /* Holds View of RecyclerView */
        ImageView productIcon;
        TextView discountedNote, title, quantity, discountedPrice, originalPrice;
        public HolderProductSeller(@NonNull View itemView) {
            super(itemView);
            productIcon = itemView.findViewById(R.id.productIconIV);
            discountedNote = itemView.findViewById(R.id.discountedNoteTV);
            title = itemView.findViewById(R.id.titleTV);
            quantity = itemView.findViewById(R.id.quantityTV);
            discountedPrice = itemView.findViewById(R.id.discountedPriceTV);
            originalPrice = itemView.findViewById(R.id.originalPriceTV);
        }
    }
}
