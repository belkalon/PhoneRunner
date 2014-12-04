package edu.oakland.cse523.phonerunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.CompletionService;


public class Main extends Activity {

    EditText textCode = null;
    Button btnCompile = null;
    TextView textView = null;
    public final static String LANGUAGE = "edu.oakland.cse523,phonerunner.LANGUAGE";
    public final static String CODE = "edu.oakland.cse523,phonerunner.CODE";
    public final static String MESSAGE = "edu.oakland.cse523.phonerunner.MESSAGE";
    public final static String OUTPUT = "edu.oakland.cse523.phonerunner.OUTPUT";


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
                String code = textCode.getText().toString();
                Intent intent = new Intent(v.getContext(), CompileService.class);
                intent.putExtra(CODE, code);
                intent.putExtra(LANGUAGE, language);
                startService(intent);
            }
        });
        CompileListener cl = new CompileListener(this);
        IntentFilter intentFilter = new IntentFilter(OUTPUT);
        registerReceiver(cl, intentFilter);


    }

    public void Output(String msg, String codeOutput)
    {
        Intent i = new Intent(getApplicationContext(), CompileOutput.class);
        i.putExtra(MESSAGE, msg);
        i.putExtra(CODE, codeOutput);
        getApplicationContext().startActivity(i);

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
