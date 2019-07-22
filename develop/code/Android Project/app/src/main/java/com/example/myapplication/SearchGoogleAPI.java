package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;


import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;


public class SearchGoogleAPI {

    public static String getSnippet(String result) {
        String snippet = null;

//         Intent intent=getIntent();


        System.out.println("mm");
        try{
        JSONObject jsonObject = new JSONObject(result);
        System.out.println(jsonObject);


        JSONArray jsonArray = jsonObject.getJSONArray("newslist");

//            for (int i=0;i<jsonArray.length();i++){
//            if (jsonArray.getJSONObject(0).get("name")==)
//        }
        System.out.println(jsonArray.getJSONObject(0).get("name"));
        System.out.println(jsonArray.getJSONObject(0).get("type"));
        System.out.println(jsonArray.getJSONObject(0).get("explain"));
        System.out.println(jsonArray.getJSONObject(0).get("contain"));

//        if(jsonArray != null && jsonArray.length() > 0) {
//            snippet =jsonArray.getJSONObject(0).getString("snippet");         }

    }catch (Exception e){
        e.printStackTrace();
        snippet = "NO INFO FOUND";     }
        return result; }


    public static String search(String keyword, String[] params, String[] values) {

        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter="";

        Intent intent = new Intent();
        intent.putExtra("keyword",keyword);



        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];                     }         }
        try {
            url = new URL("https://laji.lr3800.com/api.php?name="+keyword);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                System.out.println((textResult)+"fff");
                textResult += scanner.nextLine();             }         }
        catch (Exception e){             e.printStackTrace();         }
        finally{
            connection.disconnect();         }
        return textResult;     }}
