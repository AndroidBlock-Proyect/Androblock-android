package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

//import org.java_websocket.handshake.ClientHandshake;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.net.InetSocketAddress;

//import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@RequiresApi(api = Build.VERSION_CODES.M)
public final class Server extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    MainActivity main = new MainActivity();

    @NotNull

    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        Intrinsics.checkNotNullParameter(webSocket, "webSocket");
        Intrinsics.checkNotNullParameter(response, "response");
        webSocket.send("zaWardo!");
        Log.d("Server", "onOpen" + response);
        Log.e("ercls", "unknownerror");
    }

    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        Intrinsics.checkNotNullParameter(webSocket, "webSocket");
        Intrinsics.checkNotNullParameter(text, "text");
        Void applicationcontext = null;
        Log.e(TAG,"Received : " + text);

        //admin
        DevicePolicyManager mDPM = (DevicePolicyManager) main.getSystemService(Context.DEVICE_POLICY_SERVICE);

        switch (text) {
            case "hello":
                webSocket.send("zaWardo!");
                break;
            case "close":
                webSocket.close(NORMAL_CLOSURE_STATUS, "");
                break;
            case "block":
                // block the device with a custom password
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mDPM.resetPassword("klk",0);
                }
                break;
            case "unlock":
                // unlock the device
                break;
            case "update":
                break;
            default:
                break;


        }

    }

    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        Intrinsics.checkNotNullParameter(webSocket, "webSocket");
        Intrinsics.checkNotNullParameter(reason, "reason");
        webSocket.close(1000, (String)null);
        this.output("Closing : " + code + " / " + reason);
    }

    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        Intrinsics.checkNotNullParameter(webSocket, "webSocket");
        Intrinsics.checkNotNullParameter(t, "t");
        this.output("Error : " + t.getMessage() + "fsda");
    }

    public final void output(@Nullable String text) {
        Intrinsics.checkNotNull(text);
        Log.d("PieSocket", text);
    }

    public void send_message( WebSocket socket){
        Log.d("PieSocket", "send_message");
        socket.send("hola buenas");
    }

}

class admin extends AppCompatActivity{


}
