package com.meetlive.app.shapeDesign;

import android.graphics.RectF;

public interface TransitionGenerator {

    public Transition generateNextTransition(RectF drawableBounds, RectF viewport);
}
