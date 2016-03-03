package com.example.matheus.phonebuk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        installMinimumRequiredDatabase();

        String p = getBaseContext().getFilesDir().getAbsolutePath()+"/default_user.png";

        if(new File(p).exists()){
            // Do nothing.
        }else{
            try {
                InputStream input = getResources().openRawResource(+R.drawable.default_user);
                OutputStream out  = new FileOutputStream(p);

                int length;
                byte[] buffer = new byte[1024];

                while((length = input.read(buffer)) > 0){
                    out.write(buffer,0,length);
                }

                out.flush();
                out.close();
                input.close();

                if(new File(p).exists()){
                    // Do nothing. Default User Image, successfuly copied to files directory.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dismissSplashActivity();

    }

    /**
     * Install minimum required database using the DataBridge singleton and its DataBaseHelper
     * instance.
     */
    private void installMinimumRequiredDatabase(){
        DataBridge.setDatabaseHelper(new DatabaseHelper(getBaseContext()));
        try{
            DataBridge.getDatabaseHelper().createDatabase();
        }catch(IOException  e){
            throw new Error("Unable to install database.");
        }
    }

    /**
     * Dismisses the Splash Activity after creating or checking if the database either exists or not.
     */
    private void dismissSplashActivity(){
        final Activity activity = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
                startActivity(intent);
            }
        },1500);
    }
}
