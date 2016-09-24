package com.clarity.rest.core.component;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A formatted comment with code related syntax removed, suitable for Front-end
 * display.
 */
public class FormattedComment {

    private final String comment;

    public FormattedComment(String comment) {
        this.comment = comment;
    }

    public String value() {

        String description = comment.replaceAll("\\/", "");
        description = description.replaceAll(("\\\\"), (""));
        description = description.replaceAll(("\\*"), (""));
        description = description.replaceAll("\\s+", " ");
        final Document doc = Jsoup.parse(description);
        description = doc.body().text();
        return description;
    }
}
