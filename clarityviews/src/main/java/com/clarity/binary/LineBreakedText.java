package com.clarity.binary;

import org.apache.commons.lang.WordUtils;

/**
 * A piece of text with line breaks in it. Assumes input Text object does not
 * have any existing line breaks.
 *
 */
public class LineBreakedText implements Text {

    private Text text;
    private int maxCharsPerLine;

    public LineBreakedText(Text text, int maxCharsPerLine) {
        this.text = text;
        this.maxCharsPerLine = maxCharsPerLine;
    }

    public LineBreakedText(Text text) {
        this(text, 75);
    }

    @Override
    public String value() {
        return WordUtils.wrap(this.text.value(), this.maxCharsPerLine).trim();
    }
}
