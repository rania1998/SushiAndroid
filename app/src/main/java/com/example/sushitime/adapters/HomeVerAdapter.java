package com.example.sushitime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sushitime.LoginActivity;
import com.example.sushitime.R;
import com.example.sushitime.models.HomeVerModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder>{

        private BottomSheetDialog bottomSheetDialog;
        Context context;
        ArrayList<HomeVerModel> list;
        FirebaseFirestore db;
        String tel;




    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list) {
            this.context = context;
            this.list = list;

        }



    public HomeVerAdapter(LoginActivity context, String toString) {
            this.tel=toString;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item,parent,false));


        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            final String mName=list.get(position).getName();
            final String mTiming=list.get(position).getTiming();
            final String mPrice=list.get(position).getprix();
            final String mRating=list.get(position).getRating();


            final int mImage=list.get(position).getImage();



            holder.imageView.setImageResource(list.get(position).getImage());
            holder.name.setText(list.get(position).getName());
            holder.prix.setText(list.get(position).getprix());
            holder.rating.setText(list.get(position).getRating());
            holder.timing.setText(list.get(position).getTiming());



            holder.itemView.setOnClickListener(view -> {

                bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetTheme);
                View sheetView =LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,null);
                sheetView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("userphone"+tel);
                        addDataToFirestore(mName,mPrice,mRating);

                        Toast.makeText(context,"Added to a cart", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                ImageView bottomImg=sheetView.findViewById(R.id.bottom_img);
                TextView bottomName=sheetView.findViewById(R.id.bottom_name);
                TextView bottomPrice=sheetView.findViewById(R.id.bottom_price);
                TextView bottomRating=sheetView.findViewById(R.id.bottom_rating);

                bottomName.setText(mName);
                bottomRating.setText(mRating);
                bottomPrice.setText(mPrice);
                bottomName.setText(mName);
                bottomImg.setImageResource(mImage);

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();

            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name, timing,rating,prix;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.ver_img);
                name=itemView.findViewById(R.id.name);
                timing=itemView.findViewById(R.id.timing);
                rating=itemView.findViewById(R.id.rating);
                prix=itemView.findViewById(R.id.prix);
            }
        }

        private void addDataToFirestore(String name, String prix, String rating) {

            // creating a collection reference
            // for our Firebase Firetore database.
            db = FirebaseFirestore.getInstance();

            // CollectionReference dbOrders = db.collection("order");
            Orders orders =new Orders();
            orders.setName(name);
            orders.setPrix(prix);
            orders.setRating(rating);



            // below method is use to add data to Firebase Firestore.
            db.collection("order").add(orders).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // after the data addition is successful
                    // we are displaying a success toast message.
                    Toast.makeText(context, "Your order has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    Toast.makeText(context, "Fail to add order \n" + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
}
