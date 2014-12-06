package edu.oakland.cse523.phonerunner;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Belkalon on 12/5/2014.
 */
public class SocketService extends IntentService {

    public SocketService(){
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String json = intent.getStringExtra(Main.CODE);
        final  String host = "71.238.27.69";
        final int port = 3420;

        Runnable run = new Runnable() {
            @Override
            public void run() {
                JSONObject output;

                //Connect to server and post json request
                try {
                    Socket sock = new Socket(InetAddress.getByName(host), port);
                    OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(sock.getOutputStream()));
                    out.write(json);
                    out.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    String response = reader.readLine();
                    output = new JSONObject(response);

                    if (output.has("error")) {

                        String error = output.getString("message");
                        String message = "Error: " + error;


                        Main.showDialog(getApplicationContext(), "Error", message);
                    } else {
                        String compiledCode = output.getString("output");
                        String message = "Response: " + compiledCode;


                        Main.showDialog(getApplicationContext(), "Result", message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Main.showDialog(getApplicationContext(), "Error", "There was an error processing your request");
                }
            }
        };
        run.run();

    }
}
