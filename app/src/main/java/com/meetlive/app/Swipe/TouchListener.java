package com.meetlive.app.Swipe;

import android.util.Log;

public interface TouchListener {
    void onSingleTap();

    default void onDoubleTap() {
        Log.e("TAG", "Double tap");
    }

    default void onLongPress() {
        Log.e("TAG", "Long press");
    }

    default void onSwipeLeft() {
        Log.e("TAG", "Swipe left");
    }

    default void onSwipeRight() {
        Log.e("TAG", "Swipe right");
    }

    default void onSwipeUp() {
        Log.e("TAG", "Swipe up");
    }

    default void onSwipeDown() {
        Log.e("TAG", "Swipe down");
    }
}
