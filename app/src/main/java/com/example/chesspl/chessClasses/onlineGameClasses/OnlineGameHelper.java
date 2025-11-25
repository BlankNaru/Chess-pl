package com.example.chesspl.chessClasses.onlineGameClasses;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class OnlineGameHelper {

    private WebSocket webSocket;
    private OkHttpClient client;
    public String myColor = "";
    public String receivedMove;
    private OnMoveReceivedListener moveListener;
    private OnColorDefinedListener colorListener;

    public void setOnMoveReceivedListener(OnMoveReceivedListener listener) {
        this.moveListener = listener;
    }

    public void setOnColorDefinedListener(OnColorDefinedListener listener) {
        this.colorListener = listener;
    }

    private void defineColor(String color) {
        this.myColor = color;
        if (colorListener != null) {
            colorListener.onColorDefined(color);
        }
    }

    private void receiveMove(String receivedMove)
    {
        this.receivedMove = receivedMove;
        if(moveListener != null)
            moveListener.onMoveReceived(receivedMove);
    }

    public void connect() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("ws://10.0.2.2:9000/game")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                System.out.println("WebSocket connected!");
                // Optional: send a join message
                send("{\"action\":\"join\",\"gameId\":\"123\"}");
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        JSONObject json = new JSONObject(text);
                        String event = json.getString("event");

                        if (event.equals("colorAssigned")) {
                            defineColor(json.getString("color"));
                        }

                        if (event.equals("move")) {
                            final String move = json.getString("move");

                            new Handler(Looper.getMainLooper()).post(() -> {
                                if (moveListener != null) {
                                    moveListener.onMoveReceived(move);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("Closing: " + reason);
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("Closed: " + reason);
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
                t.printStackTrace();
            }
        });

        client.dispatcher().executorService();
    }

    public void send(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }


    public void sendMove(String message) {
        if (webSocket != null) {
            webSocket.send("{\"gameId\":\"123\",\"action\":\"move\",\"move\":\"" + message + "\"}");
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "App closed");
        }
    }
}
