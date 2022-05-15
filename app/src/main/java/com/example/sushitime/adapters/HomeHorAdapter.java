package com.example.sushitime.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sushitime.R;
import com.example.sushitime.models.HomeHorModel;
import com.example.sushitime.models.HomeVerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class HomeHorAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db;

    private static final String TAG = "DocSnippets";
    List<Map<String, Object>> questionsList = new ArrayList<java.util.Map<String, Object>>();
    List<String> keys = new ArrayList<>();
    Map<String, Object> data = new HashMap<>();

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    List<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, List<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }

    public HomeHorAdapter() {
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
        // firebaseDatabase = FirebaseDatabase.getInstance();


        // below line is used to get
        // reference for our database.
        if (check) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            homeVerModels.add(new HomeVerModel(R.drawable.sushi1, "SushiRolls", "45DT", "11:00-23:00", "4.9"));
            homeVerModels.add(new HomeVerModel(R.drawable.sushi3, "SignatureRolls", "45DT", "11:00-23:00", "4.9"));
            homeVerModels.add(new HomeVerModel(R.drawable.sushi2, "Lava", "45DT", "11:00-23:00", "4.9"));
            homeVerModels.add(new HomeVerModel(R.drawable.sushi4, "California", "45DT", "11:00-23:00", "4.9"));

            updateVerticalRec.callBack(position, homeVerModels);
            check = false;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //this.getAllDocsPlat();
                // [START get_multiple_all]

                row_index = position;
                notifyDataSetChanged();

                db = FirebaseFirestore.getInstance();
                db.collection("plat")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                /***find sushi***/
                                if (position == 0) {
                                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if (document.getData().get("categorie").toString().equals("sushi")) {
                                            System.out.println("eat" + document.getData().get("categorie").toString());

                                            homeVerModels.add(new HomeVerModel(R.drawable.sushi1, document.getData().get("Nom").toString(), document.getData().get("prix").toString(), "11:00-23:00", "4.9"));
                                        }
                                        // Log.d(TAG, document.getId() + " => " + document.getData().get("Nom").toString());
                                    }
                                    updateVerticalRec.callBack(position, homeVerModels);
                                }

                                /****find yakitori******/
                                if (position == 1) {
                                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if (document.getData().get("categorie").toString().equals("Nos_Yakitoris")) {
                                            System.out.println("eat" + document.getData().get("categorie").toString());

                                            homeVerModels.add(new HomeVerModel(R.drawable.yakitori, document.getData().get("Nom").toString(), document.getData().get("prix").toString(), "11:00-23:00", "4.9"));
                                        }
                                        // Log.d(TAG, document.getId() + " => " + document.getData().get("Nom").toString());
                                    }
                                    updateVerticalRec.callBack(position, homeVerModels);
                                }

                                /****find thai******/
                                if (position == 2) {
                                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if (document.getData().get("categorie").toString().equals("thailandais")) {
                                            System.out.println("eat" + document.getData().get("categorie").toString());

                                            homeVerModels.add(new HomeVerModel(R.drawable.thai, document.getData().get("Nom").toString(), document.getData().get("prix").toString(), "11:00-23:00", "4.9"));
                                        }
                                        // Log.d(TAG, document.getId() + " => " + document.getData().get("Nom").toString());
                                    }
                                    updateVerticalRec.callBack(position, homeVerModels);
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });

                if (select) {
                    if (position == 0) {
                        holder.cardView.setCardBackgroundColor(R.drawable.change_bg);
                        select = false;

                    }
                } else {
                    if (row_index == position) {
                        holder.cardView.setCardBackgroundColor(R.drawable.change_bg);
                    } else {
                        holder.cardView.setCardBackgroundColor(R.drawable.default_bg);
                    }
                }
            }


            public int getImage() {
                return 0;
            }

            public String getName() {
                return this.getName();
            }


            public int getItemCount() {
                return list.size();
            }
        });
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name;
            CardView cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.hor_img);
                name = itemView.findViewById(R.id.hor_text);
                cardView = itemView.findViewById(R.id.cardView);

            }
        }}}