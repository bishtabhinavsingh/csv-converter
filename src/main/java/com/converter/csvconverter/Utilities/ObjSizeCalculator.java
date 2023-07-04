package com.converter.csvconverter.Utilities;
import java.lang.instrument.Instrumentation;

public class ObjSizeCalculator {

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation is not initialized");
        }
        return instrumentation.getObjectSize(object);
    }

}

