package com.pegtura.usnfinder;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostData extends AsyncTask<ArrayList<String>, Void, String> {

    String text11;
    @Override
    public String doInBackground(ArrayList<String>... params) {
        try {
            /*String data = URLEncoder.encode("usn", "UTF-8")
                    + "=" + URLEncoder.encode("aksjdhk", "UTF-8");

            data += "&" + URLEncoder.encode("roomno", "UTF-8") + "="
                    + URLEncoder.encode("jsadhgasd", "UTF-8");*/

            for(int i = 0; i < params[0].size(); i++) {

                URL obj = new URL(params[0].get(i));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(obj.toString());
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line);
                    sb.append("\n");
                }

                text11 = sb.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text11;
    }

}
