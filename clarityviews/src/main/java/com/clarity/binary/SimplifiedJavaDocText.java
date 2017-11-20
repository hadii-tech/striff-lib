package com.clarity.binary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplifiedJavaDocText implements Text {

    private Text text;

    public SimplifiedJavaDocText(Text text) {
        this.text = text;
    }

    @Override
    public String value() {
        String textValue = this.text.value();
        List<String> docLines = new ArrayList(Arrays.asList(textValue.split("\\r?\\n")));
        for (int i = 0; i < docLines.size(); i++) {
            if (docLines.get(i).contains("@author")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@version")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@since")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@return")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@throws")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@see")) {
                docLines.set(i, docLines.get(i).replaceAll("@see ", "See "));
            } else if (docLines.get(i).contains("{@link ")) {
                docLines.set(i, docLines.get(i).replaceAll("\\{\\@link ", ""));
                while (i < docLines.size() && !docLines.get(i).contains("}")) {
                    i++;
                }
                docLines.set(i, docLines.get(i).replaceAll("}", ""));
            } else if (docLines.get(i).contains("{@linkplain")) {
                docLines.set(i, docLines.get(i).replaceAll("\\{\\@linkplain ", ""));
                while (i < docLines.size() && !docLines.get(i).contains("}")) {
                    i++;
                }
                docLines.set(i, docLines.get(i).replaceAll("}", ""));
            } else if (docLines.get(i).contains("@code")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@abstract")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@access")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@alias")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@author")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@async")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@augments")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@borrows")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@callback")) {
                docLines.remove(i);
                i--;
            } else if (docLines.get(i).contains("@class")) {
                docLines.remove(i);
                i--;
            }
        }
        return String.join("\n", docLines);
    }
}