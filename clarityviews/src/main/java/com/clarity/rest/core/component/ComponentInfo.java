package com.clarity.rest.core.component;

import java.io.Serializable;
import java.util.ArrayList;

import com.clarity.sourcemodel.OOPSourceCodeModel;

/**
 * Contains key information about the given component.
 *
 * @author Muntazir Fadhel
 *
 */
public class ComponentInfo implements Serializable {

    private static final long serialVersionUID = -5519767136682363823L;
    private ArrayList<CodeSnippet> snippets;

    public ComponentInfo(String componentUniqueName, OOPSourceCodeModel model) throws Exception {
        // find code snippets for given component in the code base...
        setSnippets(
                new ArrayList<CodeSnippet>(new UsageSnippets(componentUniqueName, model.getComponents()).snippets()));
    }

    public ArrayList<CodeSnippet> getSnippets() {
        return snippets;
    }

    public void setSnippets(final ArrayList<CodeSnippet> links2) {
        snippets = links2;
    }
}
