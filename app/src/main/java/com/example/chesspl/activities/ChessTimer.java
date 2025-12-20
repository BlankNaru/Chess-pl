package com.example.chesspl.activities;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;


public class ChessTimer {

    public interface OnTimeOutListener {
        void onTimeOut();
    }

    private int totalSeconds;
    private boolean isRunning = false;
    private final TextView textView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private OnTimeOutListener timeOutListener;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;

            if (totalSeconds <= 0) {
                isRunning = false;
                if (timeOutListener != null) {
                    timeOutListener.onTimeOut(); // call the listener
                }
                return;
            }

            totalSeconds--;
            updateTextView();
            handler.postDelayed(this, 1000);
        }
    };

    public ChessTimer(TextView textView, int minutes) {
        this.textView = textView;
        this.totalSeconds = minutes * 60;
        updateTextView();
    }

    private void updateTextView() {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        textView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void setOnTimeOutListener(OnTimeOutListener listener) {
        this.timeOutListener = listener;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            handler.post(runnable);
        }
    }

    public void pause() {
        isRunning = false;
    }

    public void reset(int minutes) {
        totalSeconds = minutes * 60;
        updateTextView();
    }

    public int getRemainingSeconds() {
        return totalSeconds;
    }
}
