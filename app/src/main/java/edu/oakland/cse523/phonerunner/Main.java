package edu.oakland.cse523.phonerunner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;



public class Main extends Activity {

    EditText textCode = null;
    Button btnCompile = null;
    TextView textView = null;
    public final static String LANGUAGE = "edu.oakland.cse523,phonerunner.LANGUAGE";
    public final static String CODE = "edu.oakland.cse523.phonerunner.CODE";


	public static void showDialog(Context context, String title, String message)
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

        final Intent intent = getIntent();
        final String language = intent.getStringExtra(LANGUAGE);

        textView = (TextView)findViewById(R.id.textView2);
        textCode = (EditText)findViewById(R.id.textCode);
        btnCompile = (Button)findViewById(R.id.btnCompile);

        textView.setText("Type Your " + language + " Code Below:");

        btnCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                String jsonSend = jsonObject.toString();
                Intent intentSend = new Intent(v.getContext(), SocketService.class);
                intentSend.putExtra(CODE, jsonSend);
                startService(intentSend);


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
