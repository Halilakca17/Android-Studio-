package com.example.landmarkbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.landmarkbookapp.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        // Casting
        /*Bu kod parçası, bir "intent" üzerinden geçirilen ve "stadium" adında bir seri hale getirilebilir nesneyi alır ve "Stadium" sınıfına dönüştürür.
        Ardından, seçilen stadyumun özelliklerini kullanarak bir kullanıcı arayüzüne bağlı olan "nameText" ve "countryText" isimli metin görüntüleme alanlarına stadyumun adını ve ülkesini yerleştirir.

        Son olarak, "imageView" adında bir görüntü görüntüleme bileşenine, seçilen stadyumun "id" özelliğine sahip olan bir görüntü kaynağı atanır.
        Bu, stadyumun bir görselini ekranda göstermek için kullanılır. */
        Stadium selectedStadium = (Stadium) intent.getSerializableExtra("stadium");
        binding.nameText.setText(selectedStadium.name);
        binding.countryText.setText(selectedStadium.Country);
        binding.imageView.setImageResource(selectedStadium.id);

    }
}