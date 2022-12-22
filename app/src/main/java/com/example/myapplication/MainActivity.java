package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import org.java_websocket.WebSocket;
//import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    static final int NORMAL_CLOSURE_STATUS = 1;
    protected static final int REQUEST_ENABLE  = 0;
    private static final int SET_PASSWORD = 1 ;

    //public variables
    ImageView shield;
    String uri = "wss//localhost:";
    int message = 1;
    int port ;

    //privated variables
    private static final String CHANNEL_ID = "devpay" ;
    private static final int CHANNEL_ID_iNT = 4 ;
    private WebSocket client;

    //DEVICE_ADMIN:
    public static final String ACTION_ADD_DEVICE_ADMIN= "android.app.action.ADD_DEVICE_ADMIN";
    public static final String EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN";
    public static final String EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION";
    public static final String ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final String NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final int USES_POLICY_DISABLE_KEYGUARD_FEATURES = 9;

    private byte[] generateRandomPasswordToken() {
        try {
            return SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //final
    final OkHttpClient clien = new OkHttpClient();

    //calendar variables an important things
    Calendar mcurrentDate = Calendar.getInstance();
    int year = mcurrentDate.get(Calendar.YEAR);
    int month = mcurrentDate.get(Calendar.MONTH);
    int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

    int selected_date;

    DevicePolicyManager mDPM;
    static ComponentName mDeviceAdminSample;


    //db
    DBmanager db;

    // main function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity activity = new MainActivity();

        //dandole el valor al puerto y al paydate

        // port =  datos extraido de la db
        // paydate = datos extraido de la db

        //llamando las funciones
        notification_date();
        provisionManagedProfile();
        conection();
        //isActiveAdmin();

        shield = findViewById(R.id.shield);

        shield.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    set_paydates();
                } catch (Exception e) {
                    Log.e(TAG, "" + e);
                    Toast.makeText(MainActivity.this, "error de conexion al cliente" + e, Toast.LENGTH_LONG).show();

                }
            }
        });

       /* block =findViewById(R.id.block);
        block.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
            }

        });*/
    }

    private void set_port() {
        try {
            if (port == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Define el numero de puerto ");
                builder.setMessage("Recuerda que el numero de puerto debe ser unico ");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        port = Integer.parseInt(input.getText().toString());
                        Connection.conection(port);
                        Toast.makeText(MainActivity.this, "estamos esperando un mensaje del cliente" + port, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
            if (port > 0 ){
                new AlertDialog.Builder(shield.getContext())
                        .setTitle("conectate al dispositivo")
                        .setMessage("uri: " + uri + " puerto: " + port)
                        .setPositiveButton("test", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialog, int id) {
                                Connection.conection(port);
                                Toast.makeText(MainActivity.this, "estamos esperando un mensaje del cliente" + port, Toast.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();
            }
            else{
                //conection( port);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //conection functions
    private void conection(){

        Log.d("webSocket", "Connecting");
        String apiKey = "VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV";
        Request request = (new Request.Builder())
                .url("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self: " +
                        + port)
                .build();
        Server server = new Server();
        WebSocket ws = clien.newWebSocket(request, (WebSocketListener) server);
        //db.insert(port, paydates());
    }

    // device  administrator functions
    private boolean isActiveAdmin() {
        return mDPM.isAdminActive(mDeviceAdminSample);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Administration_rigths(){
        try {

            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getString(R.string.device_admin_explanation));
            startActivity(intent);

            if (mDPM.isAdminActive(mDeviceAdminSample)) {
                Toast.makeText(this, "Administrativo", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is an admin!");
                //mDPM.setResetPasswordToken(mDeviceAdminSample, generateRandomPasswordToken());
                mDPM.resetPasswordWithToken(mDeviceAdminSample, "aeiou", generateRandomPasswordToken() , 0);
                //mDPM.resetPassword("aeoiu",0);
                mDPM.lockNow();

            }else {
                Toast.makeText(this, "no Administrativo", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is not an admin!");
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void provisionManagedProfile() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE);

        // Use a different intent extra below M to configure the admin component.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //noinspection deprecation
            intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME,
                    AdminReceiver.getComponentName(this));
        } else {
            final ComponentName component = new ComponentName(this,
                    AdminReceiver.class.getName());
            intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
                    component);
        }

    }

    //making the notification
    public void notification_date(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification",
                "Main Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("");
            mNotificationManager.createNotificationChannel(channel);
        }
        try {
            if (day == selected_date + 1) {
                String message = " Recuerda que debe pagar el telfono para que no te cobren atraso si ya ha pagado obviar este mensaje";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");
                builder.setContentTitle("Recordatorio de pago");
                builder.setContentText(message);
                builder.setSmallIcon(R.mipmap.shield);
                builder.setAutoCancel(true);

                Intent intent = new Intent(this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(1, builder.build());
            }
            if (day == selected_date - 1) {
                String message = " Recuerda que debe pagar el telfono para que no te cobren atraso si ya ha pagado obviar este mensaje";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");
                builder.setContentTitle("Recordatorio de pago");
                builder.setContentText(message);
                builder.setSmallIcon(R.mipmap.shield);
                builder.setAutoCancel(true);

                Intent intent = new Intent(this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(1, builder.build());
            }
            if (day == selected_date) {
                String message = " Recuerda que debe pagar el telfono para que no te cobren atraso si ya ha pagado obviar este mensaje";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");
                builder.setContentTitle("Recordatorio de pago");
                builder.setContentText(message);
                builder.setSmallIcon(R.mipmap.shield);
                builder.setAutoCancel(true);

                Intent intent = new Intent(this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(1, builder.build());
            }
            else {
                Log.e(TAG, "hoy no es el, dia de pago ");
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

/*    public int paydates(){
        return selected_date;
    }
*/
    public void set_paydates(){
        if (selected_date == 0){
            DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    message = 0;
                    selected_date = day;
                    Log.e(TAG, "el dia de pago es el "+ selected_date);
                    Toast.makeText(MainActivity.this, "" + selected_date, Toast.LENGTH_SHORT).show();
                    notification_date();
                    set_port();

                }
            }, year, month, day);
            dpd.setTitle("selecciona la fecha");
            dpd.show();
        }
        else {
            Toast.makeText(MainActivity.this, "el dia de paga es el:  " + selected_date, Toast.LENGTH_SHORT).show();
            set_port();
        }
    }
}