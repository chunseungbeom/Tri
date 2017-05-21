package com.example.yunan.tripscanner;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Context context;
    private Context context2;
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        // ** Alternative way to convert Person object to JSON string using Jackson Lib
        User user = new User();

        user.getUser().put("email","njh@naver.com");
        user.getUser().put("password","123456");

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);



        User user2;
        user2 = mapper.readValue(userString, User.class);



        /*String email = (String)user2.getUser().get("email");
        //String token = (String)user2.getUser().get("authentication_token");

        SharedPreferences pref = context.getSharedPreferences("Prefer",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        //editor.putString("authentication_token", token);
        editor.commit();*/

        //SharedPreferences pref2 = context2.getSharedPreferences("Prefer",0);
        assertEquals("njh@naver.com", user2.getUser().get("email"));
        /*pref2.getString("email","")*/
        //assertEquals(user.getUser().get("password"), user2.getUser().get("password"));
    }

}