package com.meetlive.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEdittext extends EditText {

    public CustomEdittext(Context context) {
        super(context);
        setFont();

    }

    public CustomEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomEdittext(Context context, AttributeSet attrs, int defStyle) {
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
