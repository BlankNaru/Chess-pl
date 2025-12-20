package com.example.chesspl.activities;

import com.example.chesspl.chessClasses.onlineGameClasses.OnlineGameHelper;

public class WebSocketManager {
    private static WebSocketManager instance;
    private OnlineGameHelper onlineGameHelper;

    private WebSocketManager() {}

    public static WebSocketManager getInstance() {
        if (instance == null) instance = new WebSocketManager();
        return instance;
    }

    public void setHelper(OnlineGameHelper onlineGameHelper) {
        this.onlineGameHelper = onlineGameHelper;
    }

    public OnlineGameHelper getHelper() {
        return onlineGameHelper;
    }
}