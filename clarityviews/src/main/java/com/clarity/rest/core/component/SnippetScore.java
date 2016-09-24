package com.clarity.rest.core.component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SnippetScore {

    private final ArrayList<String> keyWords;
    private final String code;

    public SnippetScore(ArrayList<String> keyWords, String code) {
        this.keyWords = keyWords;
        this.code = code;
    }

    public int score() {
        int snippetScore = 1;
        String regx = "[\\W]";
        regx += "(";
        for (final String snippetKeyWord : keyWords) {
            regx += snippetKeyWord + "|";
        }
        regx = regx.substring(0, regx.length() - 1);
        regx += ")";
        regx += "[\\W]";
        final Pattern variablesMatches = Pattern.compile(regx);
        final Matcher m = variablesMatches.matcher(code);
        while (m.find()) {
            snippetScore++;
        }
        return snippetScore;
    }
}
