package com.hadii.striff.text;

import org.apache.commons.lang.WordUtils;

/**
 * A piece of text with line breaks in it. Assumes input Text object does not
 * have any existing line breaks.
 *
 */
public class LineBreakedText implements Text {

    private final Text text;
    private final int maxCharsPerLine;

    public LineBreakedText(Text text, int maxCharsPerLine) {
        this.text = text;
        this.maxCharsPerLine = maxCharsPerLine;
    }

    @Override
    public String value() {
        return WordUtils.wrap(this.text.value(), this.maxCharsPerLine).trim();
    }
}
