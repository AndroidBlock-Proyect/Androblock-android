package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Random;

import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {

    private static final int NORMAL_CLOSURE_STATUS = 1;
    protected static final int REQUEST_ENABLE  = 0;

    //public variables
    ImageView shield;
    String uri = "wss//localhost:";
    int message = 1;
    String new_password = "123456786";
    static final  Random rand = new Random();

    //privated variables
    private static final String CHANNEL_ID = "devpay" ;
    private static final int CHANNEL_ID_iNT = 4 ;
    private static int rand_number = rand.nextInt(4000) + 1000;
    private static final int port =  rand_number;
    private WebSocket client;



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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity activity = new MainActivity();

        //llamando las funciones
        get_paydates();
        notification_date();

        Request.Builder request = new Request.Builder().url("ws://191.168.232.2:");
        WebSocketServer listener = new websocketserver(request, port);
        Toast.makeText(this, "trying to connect", Toast.LENGTH_SHORT).show();


        shield =  findViewById(R.id.shield);
        // on clicks listeners
        //WebSocket finalWs = ws;
        shield.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(shield.getContext())
                        .setTitle("conectate al dispositivo")
                        .setMessage("uri: "+ uri + " puerto: "+ port)
                        .setPositiveButton("test", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialog, int id) {
                                //aqui se pondra el codigo para abrir la conexion del dispositivo y hacer la primera prueba

                                try {
                                    //websockeeet();
                                    Toast.makeText(MainActivity.this, "estamos esperando un mensaje del cliente", Toast.LENGTH_LONG).show();

                                    //codigo para saber que hacer si el cliente esta conectado
                                    if (message ==1 ) {
                                        Toast.makeText(MainActivity.this, "El cliente dice : " + uri , Toast.LENGTH_LONG).show();
                                    }
                                    if (message == 2 ) {
                                        Toast.makeText(MainActivity.this, "error de conexion al cliente" , Toast.LENGTH_LONG).show();
                                        Toast.makeText(MainActivity.this, "Ups no pudimos obtener ningun mensaje del cliente, revise que todo los datos esten correcto ", Toast.LENGTH_LONG).show();
                                    }
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

        shield.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //set_paydates();
                    Administration_rigths();
                }catch (Exception e) {
                    Log.e(TAG, ""+e);
                    Toast.makeText(MainActivity.this, "error de conexion al cliente" + e, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void conection(){
        Request.Builder req = new Request.Builder().url("wss//:192.168.1.1" + port);
        //EchoWebsocketListener listener = new EchoWebSocket();
        //Server ws = new Ser
    }

    // device  administrator
    private boolean isActiveAdmin() {
        return mDPM.isAdminActive(mDeviceAdminSample);
    }

    // terminar ma;ana

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


    //device Admin

    public void Administration_rigths(){
        try {
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDeviceAdminSample = new ComponentName(this, DevicePolicyManager.class);

            if (mDPM.isAdminActive(mDeviceAdminSample)) {
                Toast.makeText(this, "Administrativo", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is an admin!");
                mDPM.lockNow();

            }else {
                Toast.makeText(this, "no Administrativo", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User is not an admin!");
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                startActivity(intent);
                mDPM.lockNow();
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



}