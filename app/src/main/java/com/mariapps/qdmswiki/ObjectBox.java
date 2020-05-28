package com.mariapps.qdmswiki;

import android.content.Context;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore boxStore=null;

    public static void init(Context context) {
        if(boxStore==null){
            boxStore = MyObjectBox.builder().maxReaders(300)
                    .androidContext(context.getApplicationContext())
                    .build();
        }
    }

    public static BoxStore get() {
        return boxStore;
    }
}