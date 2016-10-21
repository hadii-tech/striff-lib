package com.clarity.binary.parse;

import com.clarity.parser.ClarpseProject;
import com.clarity.parser.ParseRequestContent;
import com.clarity.sourcemodel.OOPSourceCodeModel;

public class ParsedProject {

    protected static final String CLARPSE_REST_API_BASE_URL = "http://clarpse.clarityviews.com";
    protected static final String CLARPSE_REST_API_V1_PARSE_URI = "/api/v1/parse";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String DATE_HEADER = "Date";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String PRETTY_PRINT_PARAM = "pretty";
    private ParseRequestContent codebase;

    public ParsedProject(ParseRequestContent codebase) {
        this.codebase = codebase;
    }

    public OOPSourceCodeModel model() throws Exception {
        return new ClarpseProject(codebase).result();
    }
}
