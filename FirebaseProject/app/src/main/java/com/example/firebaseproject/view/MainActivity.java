package com.example.firebaseproject.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebaseproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // bununla firebasedeki bütün giriş çıkış işlemleri yapılır.
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) {
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void  signInClicked (View view) {
        String email = binding.emailText.getText().toString();
        String password = binding.keyText.getText().toString();
        if(email.equals("") || password.equals("")) {
            Toast.makeText(this,"Please enter e-mail and password",Toast.LENGTH_LONG).show();
        }else {
            // bu işlem gerçekleştiğinde google'ın bir sunucusuna istek olarak atılacak bir ıp adresine yani.
            // bu istek bir sunucuda işlenecek ve bize geri dönecek.
            // bu geri dönüş başarılı mı oldu başarısız mı oldu bunları aşağıdaki listenerlar ile kontrol edebiliriz ve ne olacağını seçeriz.

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    public void signUpClicked (View view) {
        String email = binding.emailText.getText().toString();
        String password = binding.keyText.getText().toString();
        if(email.equals("") || password.equals("")) {
            Toast.makeText(this,"Please enter e-mail and password",Toast.LENGTH_LONG).show();
        }else {
            // bu işlem gerçekleştiğinde google'ın bir sunucusuna istek olarak atılacak bir ıp adresine yani.
            // bu istek bir sunucuda işlenecek ve bize geri dönecek.
            // bu geri dönüş başarılı mı oldu başarısız mı oldu bunları aşağıdaki listenerlar ile kontrol edebiliriz ve ne olacağını seçeriz.

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}