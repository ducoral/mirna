package com.github.ducoral.mirna.sample;

import com.github.ducoral.mirna.Converter;

import java.awt.*;

import static java.lang.Integer.*;

public class ColorConverter implements Converter {

    @Override
    public String toText(Object value) {
        Color color = (Color) value;
        return color.getRed() + ":" + color.getGreen() + ":" + color.getBlue();
    }

    @Override
    public Object fromText(String text) {
        String[] rgb = text.split(":");
        return new Color(parseInt(rgb[0]), parseInt(rgb[1]), parseInt(rgb[2]));
    }
}
