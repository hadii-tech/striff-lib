package com.clarity.rest.core.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;

public class CodeSnippet implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code = null;
    private int snippetScore = 0;
    private List<String> highlightedVars = new ArrayList<String>();
    private int snippetBeginLineNo = 0;
    private int snippetEndLineNo = 0;
    private String snippetSourceFilePath = null;
    private List<String> referencedComponents = new ArrayList<String>();
    private boolean testRelated = false;
    private String componentName = null;

    public CodeSnippet(final Component snippetComponent, final int snippetScore,
            final List<String> keyWords,
            List<String> referencedComponents) {

        this.referencedComponents = referencedComponents;
        code = snippetComponent.code().replace("\t", "   ");
        // see GH Issue #61
        code = snippetComponent.code().replaceAll("<[^>]*>", "");
        this.snippetScore = snippetScore;
        highlightedVars = keyWords;
        snippetBeginLineNo = Integer.parseInt(snippetComponent.startLine())
                - org.apache.commons.lang.StringUtils.countMatches(snippetComponent.comment(), "\n");
        snippetEndLineNo = Integer.parseInt(snippetComponent.endLine());
        snippetSourceFilePath = snippetComponent.sourceFile();
        this.referencedComponents = referencedComponents;
        testRelated = testRelated(snippetComponent);
        componentName = snippetComponent.uniqueName();
    }

    public CodeSnippet(final Component snippetComponent, List<String> referencedComponents) {

        this(snippetComponent, 0, new ArrayList<String>(), referencedComponents);
    }

    public CodeSnippet(String code) {

        this.code = code;
    }

    public
    String getCode() {
        return code;
    }

    public
    void setCode(final String code) {
        this.code = code;
    }

    public void insertMainVar(String var) {
        highlightedVars.add(var);
    }

    public
    int getSnippetScore() {
        return snippetScore;
    }

    public
    List<String> getSnippetMainVars() {
        return highlightedVars;
    }

    public int getSnippetBeginLineNo() {
        return snippetBeginLineNo;
    }

    public int getSnippetEndLineNo() {
        return snippetEndLineNo;
    }

    public String getSnippetClassFilePath() {
        return snippetSourceFilePath;
    }

    public List<String> getReferencedComponents() {
        return referencedComponents;
    }

    private boolean testRelated(Component snippetComponent) {
        if (snippetComponent == null) {
            return false;
        } else {
            for (final ComponentInvocation invocation :snippetComponent.componentInvocations(ComponentInvocations.ANNOTATION)) {
                if (invocation.invokedComponent().contains("Test")) {
                    return true;
                }
            }
            return false;
        }
    }

    public String getComponentName() {
        return componentName;
    }

    public boolean isTestRelated() {
        return testRelated;
    }
}
