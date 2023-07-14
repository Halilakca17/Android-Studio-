package com.example.firebaseproject.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.firebaseproject.R;
import com.example.firebaseproject.adapter.FeedRecyclerAdapter;
import com.example.firebaseproject.databinding.ActivityFeedBinding;
import com.example.firebaseproject.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    FeedRecyclerAdapter feedRecyclerAdapter;
    private ActivityFeedBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<Post>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        //RecyclerView

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(postArrayList);
        binding.recyclerView.setAdapter(feedRecyclerAdapter);
    }

    public void getData() {
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(FeedActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting
                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");

                        Post post = new Post(userEmail,comment,downloadUrl);

                        postArrayList.add(post);

                    }
                    feedRecyclerAdapter.notifyDataSetChanged();

                }

            }
        });
    }

    // menu bağlama işlemi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // xml ile burayı bağlar
        // Bir XML dosyasını şişirmek,
        // XML dosyasında tanımlanan kullanıcı arayüzü öğelerini (UI öğeleri)
        // gerçek Android nesnelerine dönüştürmek anlamına gelir.
        // "Inflater" sınıfı, XML dosyalarındaki UI öğelerini gerçek Android görünümlerine (nesnelere) dönüştürerek,
        // kodu görsel bir şekilde temsil eder. Yani, XML dosyasında tanımlanan kullanıcı arayüzü öğeleri,
        // "Inflater" kullanılarak kod içerisinde görsel nesneler haline getirilir.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    // bağlanılan menüye tıklanınca ne olacağı
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentToUpload);
        } else if (item.getItemId() == R.id.signout) {
            auth.signOut();

            Intent intentToSignUp = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intentToSignUp);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}