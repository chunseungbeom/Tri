package com.example.yunan.tripscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yunan on 2017-05-20.
 */
public class CommunicationManager {

    public String DELETE(String url){

        String result = "";

        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();


            // 요청 방식 선택
            httpCon.setRequestMethod("DELETE");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            // Set some headers to inform server about the type of the content
            // 서버 Response 데이터를 json 타입으로 요청
            httpCon.setRequestProperty("Accept", "application/json");
            // 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.
            httpCon.setRequestProperty("Content-type", "application/json");

            //TODO: Test Login Session
            String email = ProfileManager.getInstance().getUserEmail();
            String token = ProfileManager.getInstance().getUserToken();
            httpCon.setRequestProperty("X-User-Email",email);
            httpCon.setRequestProperty("X-User-Token",token);


            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    public String GET(String url){
        String json = "";
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();


            // ** The Way to convert Object to JSON string using Jackson Lib
            ObjectMapper mapper = new ObjectMapper();
            //json = mapper.writeValueAsString(obj);

            // 요청 방식 선택 (GET, POST)
            httpCon.setRequestMethod("GET");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            // Set some headers to inform server about the type of the content
            // 서버 Response 데이터를 json 타입으로 요청
            httpCon.setRequestProperty("Accept", "application/json");
            // 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.
            httpCon.setRequestProperty("Content-type", "application/json");

            //TODO: Test Login Session
            String email = ProfileManager.getInstance().getUserEmail();
            String token = ProfileManager.getInstance().getUserToken();
            httpCon.setRequestProperty("X-User-Email",email);
            httpCon.setRequestProperty("X-User-Token",token);


            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public String POST(String url, Object obj){
        String json = "";
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();


            // ** The Way to convert Object to JSON string using Jackson Lib
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(obj);

            // 요청 방식 선택 (GET, POST)
            httpCon.setRequestMethod("POST");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            // Set some headers to inform server about the type of the content
            // 서버 Response 데이터를 json 타입으로 요청
            httpCon.setRequestProperty("Accept", "application/json");
            // 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.
            httpCon.setRequestProperty("Content-type", "application/json");

            if(!(obj instanceof User)){
                //TODO: Test Login Session
                String email = ProfileManager.getInstance().getUserEmail();
                String token = ProfileManager.getInstance().getUserToken();
                httpCon.setRequestProperty("X-User-Email",email);
                httpCon.setRequestProperty("X-User-Token",token);
            }

            //send http message
            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();
            os.close();

            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
    private String convertInputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
