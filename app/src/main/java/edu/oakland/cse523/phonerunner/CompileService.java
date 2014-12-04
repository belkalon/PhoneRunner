package edu.oakland.cse523.phonerunner;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Belkalon on 12/3/2014.
 */
public class CompileService extends IntentService {

    public CompileService(){
        super("CompileService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String code = intent.getStringExtra(Main.CODE);
        String language = intent.getStringExtra(Main.LANGUAGE);

        //Check if code is either in C or Python
        JSONObject jsonObject = new JSONObject();
        if(language.equalsIgnoreCase("C")) {
            try {
                jsonObject.put("language", "C");
                jsonObject.put("code", code);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(language.equalsIgnoreCase("Python"))
        {
            try {
                jsonObject.put("language", "Python");
                jsonObject.put("code", code);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String uri = "";
        String jsonResponse;
        StringBuilder builder = new StringBuilder();
        Intent compileIntent = new Intent(Main.OUTPUT);

        //Connect to server and post json request
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        try {
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == 200)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));

                while ((jsonResponse = bf.readLine()) !=  null)
                {
                    builder.append(jsonResponse);
                }
                jsonResponse = builder.toString();
                Log.i("PhoneRunner", jsonResponse);

                //Check for an error message
                JSONObject jsonOutput = new JSONObject(jsonResponse);

                if(jsonOutput.getString("error") != null)
                {

                    String error = jsonOutput.getString("message");

                    String message = "There was an error compiling your code!";
                    compileIntent.putExtra(Main.MESSAGE, message);
                    compileIntent.putExtra(Main.OUTPUT, error);
                }
                //If no error message display output
                else
                {
                    String compiledCode = jsonOutput.getString("output");
                    String message = "Your Compiled Code is Below:";
                    compileIntent.putExtra(Main.MESSAGE, message);
                    compileIntent.putExtra(Main.OUTPUT, compiledCode);
                }

            }
            else
                Log.e("PhoneRunner", "Failed to get a response from server!");
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
