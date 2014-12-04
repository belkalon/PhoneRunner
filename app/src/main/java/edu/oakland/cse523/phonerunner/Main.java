package edu.oakland.cse523.phonerunner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CompletionService;


public class Main extends Activity {

	final Context context = this;
    EditText textCode = null;
    Button btnCompile = null;
    TextView textView = null;
    public final static String LANGUAGE = "edu.oakland.cse523,phonerunner.LANGUAGE";
    public final static String CODE = "edu.oakland.cse523,phonerunner.CODE";
    public final static String MESSAGE = "edu.oakland.cse523.phonerunner.MESSAGE";
    public final static String OUTPUT = "edu.oakland.cse523.phonerunner.OUTPUT";


	private static void showDialog(Context context, String title, String message)
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(message);
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.create().show();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final String language = intent.getStringExtra(LANGUAGE);

        textView = (TextView)findViewById(R.id.textView2);
        textCode = (EditText)findViewById(R.id.textCode);
        btnCompile = (Button)findViewById(R.id.btnCompile);

        textView.setText("Type Your " + language + " Code Below:");

        btnCompile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Runnable run = new Runnable() {
					@Override
					public void run() {
						String code = textCode.getText().toString();

						//Check if code is either in C or Python
						JSONObject jsonObject = new JSONObject();
						if (language.equalsIgnoreCase("C")) {
							try {
								jsonObject.put("language", "C");
								jsonObject.put("code", code);

							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else if (language.equalsIgnoreCase("Python")) {
							try {
								jsonObject.put("language", "Python");
								jsonObject.put("code", code);

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}


						int port = 3420;

						Intent compileIntent = new Intent(Main.OUTPUT);

						JSONObject output;

						String host = "71.238.27.69";

						StringBuilder builder = new StringBuilder();

						//Connect to server and post json request
						try {
							Socket sock = new Socket(InetAddress.getByName(host), port);
							OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(sock.getOutputStream()));
							out.write(jsonObject.toString());
							out.flush();

							BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
							String response = reader.readLine();
							Log.v("asdf", response);
							output = new JSONObject(response);
							if (output.has("error")) {

						  		String error = output.getString("message");
								String message = "Error: " + error;


								Main.showDialog(context, "Error", message);
							} else {
								String compiledCode = output.getString("output");
								String message = "Response: " + compiledCode;


								Main.showDialog(context, "Result", message);
							}
						} catch (Exception e) {
							e.printStackTrace();

							Main.showDialog(context, "Error", "There was an error processing your request");
						}
					}
				};
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				run.run();

			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
