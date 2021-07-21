package com.lti.localwordservice;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.lti.localwordservice";


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        File output = new File(Environment.getExternalStorageDirectory(),
                fileName);
        if (output.exists()) {
            output.delete();
        }

        InputStream stream = null;
        FileOutputStream fos = null;
        Bitmap bitmap = null;
        try {

            java.net.URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// 2. Open InputStream to connection
            conn.connect();
            stream = conn.getInputStream();

            // 3. Download and decode the bitmap using BitmapFactory
            bitmap = BitmapFactory.decodeStream(stream);

            /*InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }*/

            // successfully finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        writtingFile(bitmap);
        publishResults(output.getAbsolutePath(), result);
    }
    private void writtingFile(Bitmap bitmap){
        if(bitmap!= null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100  , bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos  = null;

//write the bytes in file
            File output = new File(Environment.getExternalStorageDirectory(),
                    "tGbaZCY.jpg");
            if (output.exists()) {
                output.delete();
            }


            try {
                fos = new FileOutputStream(output.getPath());
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
//                Bitmap myBitmap = BitmapFactory.decodeFile(output.getAbsolutePath());
//                ivBasicImage.setImageBitmap(myBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }else{
            Log.d("", "bitmap == null");
        }

    }
    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }


}