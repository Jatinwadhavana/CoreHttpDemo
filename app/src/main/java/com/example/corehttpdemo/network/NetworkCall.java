package com.example.corehttpdemo.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkCall {

    public String makeServiceCall(String urlParam,String userName,String pass){
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlParam);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", userName);
            jsonParam.put("password", pass);
            jsonParam.put("gmtoffset", "5.5");
            jsonParam.put("deviceinfo", "{\"deviceid\" : \"android-test\", \"devicetype\" : \"GCM\", \"deviceimieuuid\" : \"android-test\"}");

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            Log.i("Status=", String.valueOf(conn.getResponseCode()));
            Log.i("Res=" , conn.getResponseMessage());
            is = conn.getInputStream();
            return readResponse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) {
                    conn.disconnect();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String readResponse(InputStream iStream) throws Exception {
        String singleLine = "";
        StringBuilder totalLines = new StringBuilder(iStream.available());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(iStream));
            while ((singleLine = reader.readLine()) != null) {
                totalLines.append(singleLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalLines.toString();
    }
}