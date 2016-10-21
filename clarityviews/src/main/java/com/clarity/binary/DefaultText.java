package com.clarity.binary;

public class DefaultText implements Text {

    private String text;

    public DefaultText(String text) {
        this.text = text;
    }

    @Override
    public String value() {
        return this.text;
    }
}
