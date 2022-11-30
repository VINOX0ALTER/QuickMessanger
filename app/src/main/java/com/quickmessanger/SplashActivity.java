package com.quickmessanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


public class SplashActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lo l=new lo();
        l.start();
        context= SplashActivity.this;

    }
    private class lo extends Thread
    {
        public void run()
        {
            try{
                sleep(4000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }


            // if(hasConnection()) {

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            //}
            /*else
            {
              //  AlertDialogBox.AlertMessage(context,"check your internet connection");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
