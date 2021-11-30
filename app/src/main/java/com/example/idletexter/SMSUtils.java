package com.example.idletexter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SMSUtils extends BroadcastReceiver {

    public static final String SENT_SMS_ACTION_NAME = "SMS_SENT";
    public static final String DELIVERED_SMS_ACTION_NAME = "SMS_DELIVERED";

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        preferences = context.getSharedPreferences("Texts", MODE_PRIVATE);
        String message1 = "";

        //Detect l'envoie de sms
        if (intent.getAction().equals(SENT_SMS_ACTION_NAME)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK: // Sms sent
                    message1 = "SMS sent";
                    Toast.makeText(context, message1, Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE: // generic failure
                case SmsManager.RESULT_ERROR_NULL_PDU: // null pdu
                    message1 = "Generic failure";
                    Toast.makeText(context, message1, Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE: // No service
                    message1 = "No service";
                    Toast.makeText(context, message1, Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF: //Radio off
                    message1 = "Null PDU";
                    Toast.makeText(context, message1, Toast.LENGTH_LONG).show();
                    break;
            }

            String contactDetails = preferences.getString("contactDetails", null);
            String timeDetails = preferences.getString("timeDetails", null);

//            Errors errors = new Errors(timeDetails,message1,contactDetails,"",false);
//            IdleTexterViewModel idleTexterViewModel = new IdleTexterViewModel((Application) context.getApplicationContext());
//            idleTexterViewModel.insertError(errors);

        }
        //detect la reception d'un sms
        else if (intent.getAction().equals(DELIVERED_SMS_ACTION_NAME)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS delivered", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public static boolean canSendSMS(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    public static void sendSMS(final Context context, String phoneNumber,
                               String phoneName, String message, String currentTime) {

        if (!canSendSMS(context)) {
            Toast.makeText(context, "Cant send sms", Toast.LENGTH_LONG).show();
            return;
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT_SMS_ACTION_NAME), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED_SMS_ACTION_NAME), 0);

        final SMSUtils smsUtils = new SMSUtils();
        //register for sending and delivery
        context.getApplicationContext().registerReceiver(smsUtils, new IntentFilter(SMSUtils.SENT_SMS_ACTION_NAME));
        context.getApplicationContext().registerReceiver(smsUtils, new IntentFilter(DELIVERED_SMS_ACTION_NAME));

        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);

        ArrayList<PendingIntent> sendList = new ArrayList<>();
        sendList.add(sentPI);

        ArrayList<PendingIntent> deliverList = new ArrayList<>();
        deliverList.add(deliveredPI);

//            databaseHelper.addMissedCall(phoneNumber, currentTime, phoneName);

        sms.sendMultipartTextMessage(phoneNumber, null, parts, sendList, deliverList);


        //we unsubscribed in 10 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.unregisterReceiver(smsUtils);
            }
        }, 10000);



    }


}