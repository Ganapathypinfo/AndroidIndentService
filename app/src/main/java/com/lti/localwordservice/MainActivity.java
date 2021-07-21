package com.lti.localwordservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView ivBasicImage;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String stringPath = bundle.getString(DownloadService.FILEPATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete. Download URI: " + stringPath,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                    showImage(stringPath);

                } else {
                    Toast.makeText(MainActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                }
            }
        }
    };

    private void showImage(String path) {
        File f = new File(path);

        if (f.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

            ivBasicImage.setImageBitmap(myBitmap);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.status);
        ivBasicImage = findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, DownloadService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(DownloadService.FILENAME, "tGbaZCY.jpg");
//        intent.putExtra(DownloadService.URL,
//                "https://www.vogella.com/index.html");
        intent.putExtra(DownloadService.URL,
                "https://i.imgur.com/tGbaZCY.jpg");
        startService(intent);
        textView.setText("Service started");
    }

}