package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
    //String new_password = "aaaa";
    static final  Random rand = new Random();
    byte[] bits = new byte[3];
    Button block;

    //privated variables
    private static final String CHANNEL_ID = "devpay" ;
    private static final int CHANNEL_ID_iNT = 4 ;
    private static int rand_number = rand.nextInt(4000) + 1000;
    private static final int port =  rand_number;
    private WebSocket client;

    //DEVICE_ADMIN:
    public static final String ACTION_ADD_DEVICE_ADMIN= "android.app.action.ADD_DEVICE_ADMIN";
    public static final String EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN";
    public static final String EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION";
    public static final String ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final String NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final int USES_POLICY_DISABLE_KEYGUARD_FEATURES = 9;

    //connection


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

    //int year ;
    //int month ;
    int selected_date;
    //final int payday = paydates();\

    DevicePolicyManager mDPM;
    ComponentName mDeviceAdminSample;
/*
    public void websockeeet() throws IOException, NoSuchAlgorithmException {
        ServerSocket server = new ServerSocket(80);
        try {
            Socket cliente = server.accept();
            Log.e(TAG, "info del puerto" + server);
            System.out.println("a client connected");
            InputStream input = cliente.getInputStream();
            OutputStream output = cliente.getOutputStream();
            Scanner s = new Scanner(input, "UTF-8");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    // main function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity activity = new MainActivity();


        //llamando las funciones
        get_paydates();
        notification_date();
        provisionManagedProfile();
        conection();
        //isActiveAdmin();

       // Request.Builder request = new Request.Builder().url("ws://191.168.232.2:");
       // WebSocketServer listener = new websocketserver(request, port);
        //Toast.makeText(this, "trying to connect", Toast.LENGTH_SHORT).show();


        shield = findViewById(R.id.shield);
        // on clicks listeners
        //WebSocket finalWs = ws;
        shield.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(shield.getContext())
                        .setTitle("conectate al dispositivo")
                        .setMessage("uri: " + uri + " puerto: " + port)
                        .setPositiveButton("test", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialog, int id) {
                                //aqui se pondra el codigo para abrir la conexion del dispositivo y hacer la primera prueba

                                try {
                                    //websockeeet();
                                    Toast.makeText(MainActivity.this, "estamos esperando un mensaje del cliente", Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });

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

        block =findViewById(R.id.block);
        block.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Administration_rigths();
            }

        });
    }

    //conection functions
    private void conection(){

        Log.d("webSocket", "Connecting");
        String apiKey = "VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV";
        int port = 0026;
        Request request = (new Request.Builder())
                .url("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self: " +
                        + port)
                .build();
        Server server = new Server();
        WebSocket ws = clien.newWebSocket(request, (WebSocketListener) server);


        /*String ipAddress = "10.0.0.154";
        InetSocketAddress inetSockAddress = new InetSocketAddress(ipAddress, 38301);
        Server wsServer = new Server(inetSockAddress);
        wsServer.run();*/
    }

    // terminar mas tarde ok
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
                //mDPM.resetPasswordWithToken(mDeviceAdminSample, "aeiou", generateRandomPasswordToken() , 0);
                mDPM.resetPassword("aeoiu",0);
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

        if (day == paydates()) {
            Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("notificacion del dispositivo")
                    .setContentText(" hoy es el dia del pago del dispositivo, no esperes a que te cobren mora")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
        }
        else if (day == paydates() -3){
            Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("notificacion del dispositivo")
                    .setContentText(" Recuerda que el pago del dispositivo es pronto, no esperes a que te cobren mora")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // making paydate tool
    public void get_paydates(){
        if (day == paydates()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Dia de pago")
                    .setMessage(" recuerda que hoy es el dia de pago del telefono");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }

    }
    public void set_paydates(){

        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                message = 1;
                selected_date = i2;
                Log.e(TAG, "el dia de pago es el "+ paydates());
                Toast.makeText(MainActivity.this, "" + paydates(), Toast.LENGTH_SHORT).show();
                notification_date();
            }
        }, year, month, day);
        dpd.setTitle("selecciona la fecha");
        dpd.show();
    }
    public int paydates(){
        return selected_date;
    }


}