package com.stage.plugin;

public class Panic extends Exception {
    public Panic(String message) {
        super(message);
    }

    public Panic(Integer status) {
        super(status.toString());
    }
}
