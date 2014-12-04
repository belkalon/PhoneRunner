package edu.oakland.cse523.phonerunner;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;


public class CompileOutput extends Activity {

    Button btnReturn = null;
    TextView textOutput = null;
    TextView textDescription = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_output);

        Intent displayIntent = getIntent();
        DisplayOutput(displayIntent.getStringExtra(Main.MESSAGE), Main.OUTPUT);

        btnReturn = (Button)findViewById(R.id.btnReturn);
        textOutput = (TextView)findViewById(R.id.textOutput);
        textDescription = (TextView)findViewById(R.id.textDescription);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Main.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                v.getContext().startActivity(i);
            }
        });



    }

    public void DisplayOutput(String msg, String outputCode){
        textDescription.setText(msg);
        textOutput.setText(outputCode);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compile_output, menu);
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
