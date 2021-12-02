package com.example.idletexter;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import java.text.SimpleDateFormat;
import java.util.Date;


public class CallReceiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    private String txtToSend;
    private SimpleDateFormat dateTimeFormatter1;

    private int smsNumber;
    private String phoneName;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.w("intent " , intent.getAction().toString());

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        } else {

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }


    }


    public void onCallStateChanged(final Context context, int state, final String number) {

        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;

                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
//                    Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss

                    Log.e("*-*-*-* ", "lastCallnumber");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            StringBuffer stringBuffer = new StringBuffer();

                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)
                                    == PackageManager.PERMISSION_GRANTED) {

                                String[] projection = new String[]{CallLog.Calls.NUMBER};
                                String[] projection1 = new String[]{CallLog.Calls.CACHED_NAME};

                                Cursor cur = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                        projection, null, null, CallLog.Calls.DATE +" desc");
                                Cursor cur1 = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                        projection1, null, null, CallLog.Calls.DATE +" desc");

                                assert cur != null;
                                assert cur1 != null;

                                cur.moveToFirst();
                                cur1.moveToFirst();

                                String lastCallnumber = cur.getString(0);
                                String lastCall = "";

                                if (cur1.getString(0) != null){
                                    lastCall = cur1.getString(0);
                                }else {
                                    lastCall = "";
                                }


                                if (lastCallnumber != null){

                                    try{
                                        /**
                                         *
                                         TODO: On logging in or on syncing, get saved IdleTexter message
                                         TODO: from the portal save it in the shared preference and fetch the
                                         TODO: SMS from the shared preference when it is to be sent.
                                         TODO: From the Dashboard add the phone numbers of the particular phones you want to use the IdleTexter
                                         TODO: On Fetching the user details, Request a phone number when logging in.
                                         TODO: The phone number will link the phone with a particular phone and the provided SMS.
                                         TODO: Send SMS using SenderId when the network is available and send normal sms when no sms is available
                                         *
                                         */


//                                        dateTimeFormatter1 = new SimpleDateFormat("dd/MM/yyyy");
//
//                                        Date date = new Date();
//                                        databaseHelper = new DatabaseHelper(context);
//                                        fetchDatabase = new FetchDatabase();
//
//                                        String tag = context.getResources().getString(R.string.hyperlink);
//                                        String currentTime = dateTimeFormatter1.format(date);
//
//                                        boolean isThere = fetchDatabase.CheckPhoneNumber(lastCallnumber,currentTime, context).getSecond();
//                                        String txtId = fetchDatabase.CheckPhoneNumber(lastCallnumber,currentTime, context).getFirst();
//
//                                        String txtMessage = fetchDatabase.getMessage(context);
//                                        if (txtMessage.equals("false")){
//
//                                            txtToSend = context.getString(R.string.send_text_default);
////                                            txtToSend = context.getString(R.string.send_text_default) + "\n \nIdleTexter Info: "+tag;
//
//                                        }else {
//
//                                            txtToSend = txtMessage;
////                                            txtToSend = txtMessage +  "\n \nIdleTexter Info: "+tag;
//
//                                        }
//
//                                        if (isThere){
//                                            databaseHelper.updateMissedCall(currentTime, txtId);
//                                        }else {
//
//                                            SharedPreferences.Editor editor = preferences.edit();
//
//                                            editor.putString("timeDetails",currentTime);
//                                            editor.putString("contactDetails",lastCallnumber + "\n" + lastCall);
//                                            editor.apply();
//
//                                            SMSUtils.sendSMS(context, lastCallnumber,lastCall, txtToSend, currentTime);
//                                        }


                                    }
                                    catch (Exception e){

                                        Toast.makeText(context, "SMS Failed to Send to " + lastCallnumber, Toast.LENGTH_SHORT).show();
                                    }

                                }


                            }else {
                                Toast.makeText(context, "Please allow permissions ", Toast.LENGTH_LONG).show();

                            }


                        }
                    }, 500);


                } else if(isIncoming){

//                    Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();

                } else{

//                    Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();

                }

                break;
        }
        lastState = state;
    }


}