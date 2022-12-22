package com.example.myapplication;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Connection extends MainActivity{

    static final OkHttpClient clien = new OkHttpClient();

    public static void conection(int port){

        Log.d("webSocket", "Connecting");
        String apiKey = "VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV";
        Request request = (new Request.Builder())
                .url("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self: "
                        + port)
                .build();
        Server server = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            server = new Server();
        }
        WebSocket ws = clien.newWebSocket(request, (WebSocketListener) server);
    }
}
