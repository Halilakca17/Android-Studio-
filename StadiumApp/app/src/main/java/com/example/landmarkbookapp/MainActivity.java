package com.example.landmarkbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.landmarkbookapp.databinding.ActivityDetailsBinding;
import com.example.landmarkbookapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    ArrayList<Stadium> stadiumArrayList;

    /* kod açıklaması notlarda */
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        stadiumArrayList = new ArrayList<>();

        Stadium gs = new Stadium("Nef Stadyumu","Türkiye",R.drawable.nef);
        Stadium schalke = new Stadium("Veltins Arena","Almanya",R.drawable.veltins);
        Stadium Benfica = new Stadium("Estádio da Luz","Portekiz",R.drawable.estadio);
        Stadium Chelsea = new Stadium("Stamford Bridge","Ingiltere",R.drawable.chelsea);

        stadiumArrayList.add(gs);
        stadiumArrayList.add(schalke);
        stadiumArrayList.add(Benfica);
        stadiumArrayList.add(Chelsea);

        /*
        adapter layout ile kodları birbirine bağlayıp kullanıcıya göstermek için yazılır.
        ArrayAdapter sınıfı, bir veri kaynağından (genellikle bir liste) verileri alarak bunları ListView veya Spinner gibi bir görüntü bileşeniyle eşleştirmek için kullanılan bir adaptördür.
        Bu adaptör, verileri görüntü bileşeninde düzenli bir şekilde göstermek için kullanılır.

        Bu satır, arrayAdapter adında yeni bir ArrayAdapter nesnesi oluşturur. ArrayAdapter'ın yapıcı metodu üç argüman alır:
        this, adaptörün kullanıldığı geçerli bağlamı veya etkinliği temsil eder.
        android.R.layout.simple_list_item_1, Android tarafından sağlanan önceden tanımlanmış bir düzen kaynağını temsil eder.
        Tek bir öğeyi liste içinde görüntülemek için kullanılan basit bir düzeni temsil eder.

        stadiumArrayList.stream().map(stadium -> stadium.name).collect(Collectors.toList()),
        her bir stadyum nesnesini stadiumArrayList içindeki adlarına eşler ve adları yeni bir liste halinde toplar.
        Bu liste, adaptörün veri kaynağı olacak.

        binding.listView.setAdapter(arrayAdapter); Bu satır, arrayAdapter'ı binding.listView'ın adaptörü olarak ayarlar.*/
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                stadiumArrayList.stream().map(stadium -> stadium.name).collect(Collectors.toList())
        );
        /* Bu kodların açıklaması not2'de*/
        binding.listView.setAdapter(arrayAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,stadiumArrayList.get(position).name,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("stadium",stadiumArrayList.get(position));
                startActivity(intent);
            }
        });
    }
}