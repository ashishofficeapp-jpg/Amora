package com.amora.app.shapeDesign;

public class IncompatibleRatioException extends RuntimeException {

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}