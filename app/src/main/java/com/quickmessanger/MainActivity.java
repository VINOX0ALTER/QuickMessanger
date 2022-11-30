package com.quickmessanger;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.quickmessanger.Utils.Constances;
import com.quickmessanger.Utils.Prefs;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText et_msg_cc, et_msg_mobileno, et_msg_msg;
    Button btn_msg_sendmsg;
    AlertDialog.Builder builder;
    Boolean msgSend = false;
    private AdView mAdView;
    RelativeLayout rl_msg_howtouseapp;
    Timer timer;
    TimerTask hourlyTask;
    Context context;
    ImageView iv_main_information, iv_setting;
    public InterstitialAd mInterstitialAd;
    AlertDialog alertDialog;

    public static MainActivity instance = null;

    public MainActivity() {
        instance = MainActivity.this;
    }

    public static synchronized MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_msg_cc = (EditText) findViewById(R.id.et_msg_cc);
        et_msg_mobileno = (EditText) findViewById(R.id.et_msg_mobileno);
        et_msg_msg = (EditText) findViewById(R.id.et_msg_msg);
        btn_msg_sendmsg = (Button) findViewById(R.id.btn_msg_sendmsg);
        builder = new AlertDialog.Builder(this);
        rl_msg_howtouseapp = (RelativeLayout) findViewById(R.id.rl_msg_howtouseapp);
        mAdView = (AdView) findViewById(R.id.adView);
        context = MainActivity.this;
        iv_setting = (ImageView) findViewById(R.id.iv_setting);

        // Country code............
       /* TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID = manager.getSimCountryIso().toUpperCase();*/
        // Locale.getDefault().getCountry();
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //Toast.makeText(context, " Co" +Locale.getDefault().getCountry() , Toast.LENGTH_LONG).show();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("C04B1BFFB0774708339BC273F8A43708")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("6A2D2B68A7166B0DE00868C6F74E8DB9")
                .addTestDevice("88045C0A4BBC3C24FABBF3D543FC7C8C")
                .addTestDevice("3BCC9944F0D7A19C3D3BEFCD7D8B3EDE")
                .addTestDevice("D3662558A58B055494404223B20E0CA8")
                .build());

        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }*/
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

                Constances.AllowToOpenAdvertise = false;
                MainActivity.instance.stopTask();
                MainActivity.instance.StartTimer();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Constances.AllowToOpenAdvertise = false;
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Constances.AllowToOpenAdvertise = false;
                MainActivity.instance.stopTask();
                MainActivity.instance.StartTimer();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Constances.AllowToOpenAdvertise = false;
                MainActivity.instance.stopTask();
                MainActivity.instance.StartTimer();
                Constances.msgSend = true;
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        .addTestDevice("6A2D2B68A7166B0DE00868C6F74E8DB9")
                        .addTestDevice("88045C0A4BBC3C24FABBF3D543FC7C8C")
                        .addTestDevice("3BCC9944F0D7A19C3D3BEFCD7D8B3EDE")
                        .addTestDevice("D3662558A58B055494404223B20E0CA8")
                        .build());
            }
        });

       /* TelephonyManager teleM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = teleM.getNetworkCountryIso();*/

        et_msg_cc.setText(Prefs.getPreferanceString(context,Constances.countryCode,"212"));
        et_msg_cc.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0)
                {
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

                Prefs.setPreferanceString(context,Constances.countryCode,et_msg_cc.getText().toString());

            }
        });
        et_msg_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_msg_cc.requestFocus();
                et_msg_cc.setCursorVisible(true);

            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                // LayoutInflater inflater = (getA).getLayoutInflater();
                View dialogView = LayoutInflater.from(context).inflate(R.layout.alertbox_design, null);
                dialogBuilder.setView(dialogView);

                Button btn_information = (Button) dialogView.findViewById(R.id.btn_cancle);
                ImageView iv_alert_rate = (ImageView) dialogView.findViewById(R.id.iv_alert_rate);
                ImageView iv_alert_share = (ImageView) dialogView.findViewById(R.id.iv_alert_share);
                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                iv_alert_rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }

                    }
                });
                iv_alert_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* String AppLink = "";
                        Intent i = new Intent(Intent.ACTION_SEND);
                        try {
                            AppLink = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
                        } catch (ActivityNotFoundException e) {
                            AppLink = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
                        }

                        i.setType("text/link");
                        String shareBody = "hello" + "\n" + AppLink;
                        String shareSub = "whats Quick Message";
                        i.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                        i.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(i, "share using"));*/

                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.


                        share.putExtra(Intent.EXTRA_TEXT, "you wants to send WhatsApp message without save phone number then use Whats Quick Message " + "\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName());

                        startActivity(Intent.createChooser(share, "Share link!"));

                    }
                });
                btn_information.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });
        rl_msg_howtouseapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, AppInformation.class);
                startActivity(i);
            }
        });
        btn_msg_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_msg_mobileno.getText().toString().equals("") || et_msg_mobileno == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please enter Mobile Number");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);
                    pbutton.setBackgroundColor(Color.WHITE);

                }
                else{
                    if (!MainActivity.getInstance().mInterstitialAd.isLoading()) {
                        openWhatsApp();
                    }
                    displayAdMob();
                }



            }
        });
    }

  /*  public void validation() {

        if (et_msg_mobileno.getText().toString().equals("") || et_msg_mobileno == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("Please enter Mobile number");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        }
    }*/

    public void StartTimer() {
        timer = new Timer();
        hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (!Constances.isFirstTimeOpen) {
                    Constances.AllowToOpenAdvertise = true;
                    stopTask();
                } else {
                    Constances.isFirstTimeOpen = false;
                }
            }
        };

        Constances.isFirstTimeOpen = true;
        if (timer != null) {
            timer.schedule(hourlyTask, 0, 1000 * 60);
        }
    }

    public void stopTask() {
        if (hourlyTask != null) {
            Log.d("TIMER", "timer canceled");
            hourlyTask.cancel();
        }
    }

   /* @Override
    protected void onResume() {

        if (Constances.msgSend) {
            openWhatsApp();
        }
        super.onResume();
    }*/


    public void openWhatsApp() {

        Constances.msgSend = false;
        try {
            String text = et_msg_msg.getText().toString();// Replace with your message.

            String toNumber = et_msg_cc.getText().toString() + et_msg_mobileno.getText().toString(); // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            // String countrycode = et_msg_cc.getText().toString();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void displayAdMob() {


        if (MainActivity.getInstance().mInterstitialAd != null) {
            if (MainActivity.getInstance().mInterstitialAd.isLoaded()) {
                MainActivity.getInstance().mInterstitialAd.show();
            } else {
                MainActivity.instance.stopTask();
                MainActivity.instance.StartTimer();
            }
        } else {
            MainActivity.instance.stopTask();
            MainActivity.instance.StartTimer();
        }


    }
}
