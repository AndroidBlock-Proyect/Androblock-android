package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Message;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.URI;

import okhttp3.Request;

public  class websocketserver extends WebSocketServer {

    public websocketserver(Request.Builder request, int port) {
        onStart();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Connection opened: " + conn.getRemoteSocketAddress());
        System.out.println("Server handshake: " + handshake.toString());
        System.out.println();
        conn.send("Hello from WebSocketServer");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
        System.out.println("Reason: " + reason);
        System.out.println("Code: " + code);
        System.out.println();
        conn.close();

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message received: " + message);
        System.out.println();

        if (message == "block"){
            try {
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                conn.send("hubo un problema al realizar la solicitud"+ ie);
            }
        }
        if (message == "unlock"){
            try {
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                conn.send("hubo un problema al realizar la solicitud"+ ie);
            }
        }
        if (message == "delete"){
            try {
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                conn.send("hubo un problema al realizar la solicitud"+ ie);
            }
        }
        else{
            conn.send("hooola te habla el dispositivo \n" + " Brand: ${Build.Brand} \n"+ "Model: ${Build.Model}");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        conn.send("hubo un problema al conectarse");
        ex.printStackTrace();
        Log.e(TAG, "Error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
    }
}
