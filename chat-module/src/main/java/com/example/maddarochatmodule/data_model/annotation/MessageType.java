package com.example.maddarochatmodule.data_model.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({MessageType.TEXT, MessageType.IMAGE, MessageType.VOICE, MessageType.DOCUMENT})
public @interface MessageType {
    int TEXT = 1;
    int IMAGE = 2;
    int VOICE = 3;
    int DOCUMENT = 4;
}
