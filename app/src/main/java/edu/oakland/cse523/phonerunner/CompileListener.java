package edu.oakland.cse523.phonerunner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Belkalon on 12/4/2014.
 */
public class CompileListener extends BroadcastReceiver {


    Main m = null;
    public CompileListener(Main main) {
        m = main;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra(Main.MESSAGE);
        String code = intent.getStringExtra(Main.OUTPUT);
        m.Output(msg, code);

    }
}
