package com.example.myapplication;

import android.net.Uri;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.handshake.Handshakedata;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;

import okhttp3.Request;
import okio.ByteString;

public class Server extends WebSocketAdapter {

    @Override
    public void onWebsocketMessage(WebSocket conn, String message) {
        if( message == "block"){
            //code to block the device in case of steal
        }
        if (message == "unblock"){

        }
        if (message == "shutdown"){
            conn.close(25, "Device has pay at 100%");
        }
        else{
            conn.send("saludo te habla el telefono:" +  "Brand: ${Build.BRAND}" + " Model: ${Build.MODEL}" );
        }
    }

    @Override
    public void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
    }

    public void onWebsocketMessage(WebSocket conn, ByteString blob) {
        Log.e("geting blobs","recibiendo bytes" + blob.hex());
    }

    @Override
    public void onWebsocketOpen(WebSocket conn, Handshakedata d) {
        conn.send("Coneccion establecida");
    }

    @Override
    public void onWebsocketClose(WebSocket ws, int code, String reason, boolean remote) {
    }

    @Override
    public void onWebsocketClosing(WebSocket ws, int code, String reason, boolean remote) {

    }

    @Override
    public void onWebsocketCloseInitiated(WebSocket ws, int code, String reason) {

    }

    @Override
    public void onWebsocketError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onWriteDemand(WebSocket conn) {

    }

    @Override
    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return null;
    }
}
