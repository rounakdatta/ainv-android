package com.asslpl.ainv;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequest extends AsyncTask <String,String,String> {

    @Override
    protected String doInBackground(String... params) {

        BufferedReader reader = null;
        try {

            URL url = new URL(params[0]);
            String urlParameters = params[1];
            byte[] postDataBytes = urlParameters.getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.getOutputStream().write(postDataBytes);
            //conn.connect();

            int status =conn.getResponseCode();
            if(status<400){
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //read response
            }else{
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                //read response
            }

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String resultText = sb.toString();
            return resultText;

        } catch (Exception ex) {
            Log.e("ATTEST", "Error is :" + ex.toString());
        } finally {
            try {
                if(reader!=null)
                    reader.close();
            } catch (Exception ex1) {

            }
        }

        return null;

    }


    @Override
    protected void onPostExecute(String s) {
        System.out.println(s);
    }

}
