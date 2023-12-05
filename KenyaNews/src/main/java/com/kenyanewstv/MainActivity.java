package com.kenyanewstv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<TVContainer> tvContainers;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started.");

        tvContainers = TVAdapter.initializeTVContainers();
        addListenerOnCard();
    }

    public void addListenerOnCard() {
        for (final TVContainer tvContainer : tvContainers) {
            switch (tvContainer.getName()) {
                case "NTV News":
                    cardView = findViewById(R.id.ntv);
                    break;
                case "KTN News":
                    cardView = findViewById(R.id.ktn_news);
                    break;
                case "K24 News":
                    cardView = findViewById(R.id.k24);
                    break;
                case "Citizen TV":
                    cardView = findViewById(R.id.citizen);
                    break;
                case "KTN TV":
                    cardView = findViewById(R.id.ktn);
                    break;
                case "KBC News":
                    cardView = findViewById(R.id.kbc);
                    break;
                case "Spice FM":
                    cardView = findViewById(R.id.spicefm);
                    break;
                case "Kenya Digital News":
                    cardView = findViewById(R.id.kenya_digital);
                    break;
                default:
                    break;
            }

            cardView.setOnClickListener(v -> {
                Toast.makeText(this, tvContainer.getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, VideoListActivity.class);
                intent.putExtra("name", tvContainer.getName());
                intent.putExtra("url", tvContainer.getUrl());
                intent.putExtra("colors", tvContainer.getColors());
                this.startActivity(intent);
            });
        }
    }
}
