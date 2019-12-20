package com.josedlpozo.galileo.colorpicker.mapper;

import android.graphics.Color;
import java.nio.ByteBuffer;

public class ColorMapper {
    public static int color(ByteBuffer buffer){
        return Color.argb(255, buffer.get() & 0xff, buffer.get() & 0xff, buffer.get() & 0xff);
    }
}
