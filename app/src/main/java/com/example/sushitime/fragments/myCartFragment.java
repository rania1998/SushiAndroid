package com.example.sushitime.fragments;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sushitime.R;
import com.example.sushitime.adapters.CartAdapter;
import com.example.sushitime.models.CartModel;
import com.example.sushitime.ui.Commandes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class myCartFragment extends Fragment {
    List<CartModel> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    String name;
    String rating;
    String prix;
    int image;
    int i=0;
    FirebaseFirestore db;









    public myCartFragment() {


        // Required empty public constructor
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);
        recyclerView=view.findViewById(R.id.cart_rec);
        Button btnaddcom=(Button) view.findViewById(R.id.ButtonaddToCom);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshOrder();

        btnaddcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking weather the user
                // has entered his mobile number or not.


                db = FirebaseFirestore.getInstance();
                db.collection("order")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // list=new ArrayList<>();

                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        addDataToFirestore(document.getData().get("name").toString(),document.getData().get("prix").toString(),document.getData().get("rating").toString());

                                    }
                                    Toast.makeText(getActivity(), "Your commades has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                                    //deleteOrder();
                                    deleteCollection("order");
                                    Reload();
                                    //refreshOrder(true);

                                }
                            }
                        });

                System.out.println("add tto commade pret");
            }
        });





        return view;

    }

    public void Reload(){

        getActivity().getSupportFragmentManager().beginTransaction().replace(myCartFragment.this.getId(), new myCartFragment()).commit();
    }

    private void refreshOrder() {



        System.out.println("oncreateveiw start");
        db = FirebaseFirestore.getInstance();
        db.collection("order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list=new ArrayList<>();
                            list.clear();


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                list.add(new CartModel(R.drawable.sushi1,document.getData().get("name").toString(),document.getData().get("prix").toString(),"4.0"));

                                cartAdapter=new CartAdapter(list);
                                recyclerView.setAdapter(cartAdapter);


                                //Log.d(TAG, document.getId() + " => " + document.getData().get("name").toString());
                            }
                        }
                    }
                });



    }





    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private void deleteCollection(final String path) {
        deleteCollection(db.collection(path), 50, EXECUTOR);
    }

    private Task<Void> deleteCollection(final CollectionReference collection,
                                        final int batchSize,
                                        Executor executor) {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Get the first batch of documents in the collection
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    // Move the query cursor to start after the last doc in the batch
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });

    }

    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final DownloadManager.Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (QueryDocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }

    private void addDataToFirestore(String name, String prix, String rating) {

        // creating a collection reference
        // for our Firebase Firetore database.
        db = FirebaseFirestore.getInstance();

        // CollectionReference dbOrders = db.collection("order");
        Commandes commandes =new Commandes();
        commandes.setName(name);
        commandes.setPrix(prix);
        commandes.setRating(rating);



        // below method is use to add data to Firebase Firestore.
        db.collection("commande").add(commandes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getActivity(), "Fail to add order \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
