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

    public String testPOST(String url){
        String json = "";
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();


            // ** The Way to convert Object to JSON string using Jackson Lib
            //ObjectMapper mapper = new ObjectMapper();
            //json = mapper.writeValueAsString(obj);
            json = "{\n" +
                    "\t\"trip\": {\n" +
                    "\t\t\"check_in\": \"2017-05-01\",\n" +
                    "\t\t\"check_out\": \"2017-05-05\",\n" +
                    "\t\t\"address\": \"강남구 역삼동\"\n" +
                    "\t}\n" +
                    "}";
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

            //if(!(obj instanceof User)){
                //Login Session
                String email = ProfileManager.getInstance().getUserEmail();
                String token = ProfileManager.getInstance().getUserToken();
                httpCon.setRequestProperty("X-User-Email",email);
                httpCon.setRequestProperty("X-User-Token",token);
            //}

            //send http message
            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
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


    public String PUT(String url, Object obj){
        //TODO: coding PUT
        String result = "";
        return result;
    }


    public String DELETE(String url, Object obj){
        //TODO: check delete error
        String json = "";
        String result = "";

        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            // 요청 방식 선택
            httpCon.setRequestMethod("DELETE");

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            //httpCon.setDoInput(true);

            // Set some headers to inform server about the type of the content
            // 서버 Response 데이터를 json 타입으로 요청
            //httpCon.setRequestProperty("Accept", "application/json");
            // 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.
            httpCon.setRequestProperty("Content-Type", "application/json");

            /*//Login Session
            if(!(obj instanceof User)){
                String email = ProfileManager.getInstance().getUserEmail();
                String token = ProfileManager.getInstance().getUserToken();
                httpCon.setRequestProperty("X-User-Email",email);
                httpCon.setRequestProperty("X-User-Token",token);
            }*/


            //if(obj != null){
                // ** The Way to convert Object to JSON string using Jackson Lib
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(obj);


                httpCon.setDoOutput(true);

                //send http message
                OutputStream os = httpCon.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.flush();
                os.close();
            //}


            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
            }
            catch (IOException e) {
                is = httpCon.getErrorStream();
                e.printStackTrace();
            }
            finally {
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "Did not work!";

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


            // 요청 방식 선택 (GET, POST)
            httpCon.setRequestMethod("GET");

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            // Set some headers to inform server about the type of the content
            // 서버 Response 데이터를 json 타입으로 요청
            httpCon.setRequestProperty("Accept", "application/json");
            // 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.
            httpCon.setRequestProperty("Content-Type", "application/json");

            //Login Session
            String email = ProfileManager.getInstance().getUserEmail();
            String token = ProfileManager.getInstance().getUserToken();
            httpCon.setRequestProperty("X-User-Email",email);
            httpCon.setRequestProperty("X-User-Token",token);


            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
            }
            catch (IOException e) {
                is = httpCon.getErrorStream();
                e.printStackTrace();
            }
            finally {
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "error: Did not work!";

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
            httpCon.setRequestProperty("Content-Type", "application/json");

            if(!(obj instanceof User)){
                //Login Session
                String email = ProfileManager.getInstance().getUserEmail();
                String token = ProfileManager.getInstance().getUserToken();
                httpCon.setRequestProperty("X-User-Email",email);
                httpCon.setRequestProperty("X-User-Token",token);
            }

            //send http message
            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            // receive response as inputStream
            InputStream is = null;
            try {
                is = httpCon.getInputStream();
            }
            catch (IOException e) {
                is = httpCon.getErrorStream();
                e.printStackTrace();
            }
            finally {
                // convert inputstream to string
                if(is != null)
                    result = convertInputStreamToString(is);
                else
                    result = "error : Did not work!";

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

    public String QUERY(String url, String address, String city){
        String queryString = url + "?" + "address=" + address + "&" + "city=" + city;
        //ex : http://localhost:3000/api/v1/trips   ? address=수지구 &    city=수지구
        return GET(queryString);
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
