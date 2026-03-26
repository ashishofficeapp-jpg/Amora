package com.meetlive.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

    public CustomButton(Context context) {
        super(context);
        setFont();

    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/Dosis_Bold.ttf");
        setTypeface(normal);

        /*Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/steelfish_bd.otf");
        setTypeface(normal, Typeface.BOLD);*/
    }


}
