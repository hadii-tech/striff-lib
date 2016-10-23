package com.clarity.binary;

public class JavaDocSymbolStrippedText implements Text {

    private Text text;

    public JavaDocSymbolStrippedText(Text text) {
        this.text = text;
    }

    @Override
    public String value() {
        return this.text.value().replace("/*", "").replace("*/", "").replace("*", "");
    }
}
