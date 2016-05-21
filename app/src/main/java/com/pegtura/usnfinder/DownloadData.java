package com.pegtura.usnfinder;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadData extends AsyncTask<String, Void, String> {

    HttpURLConnection httpURLConnection;
    URL url;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    int data;
    StringBuilder stringBuilder = new StringBuilder();
    char current;

    @Override
    public String doInBackground(String... params) {

        try {
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            data = inputStreamReader.read();
            while (data != -1) {
                current = (char) data;
                stringBuilder.append(current);
                data = inputStreamReader.read();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }

}
