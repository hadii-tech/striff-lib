package com.clarity.rest.core.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;

public class UsageSnippets {
    private final String componentUniqueName;
    private final Map<String, Component> componentList;

    public static final int NUM_SNIPPETS_RETURN = 20;
    private static final int MAX_SNIPPET_COUNT = 50;

    /**
     * @param componentUniqueName
     *            Component for which to find usage snippets
     * @param componentList
     *            List of all the components in the code base
     */
    public UsageSnippets(String componentUniqueName, Map<String, Component> componentList) {

        this.componentList = componentList;
        this.componentUniqueName = componentUniqueName;
    }

    public List<CodeSnippet> snippets() {

        final String componentShortName = componentUniqueName.substring(componentUniqueName.lastIndexOf(".") + 1);
        // for storing non-test related code snippets
        final ArrayList<CodeSnippet> regularSnippets = new ArrayList<CodeSnippet>();
        // for storing test related code snippets
        final ArrayList<CodeSnippet> testSnippets = new ArrayList<CodeSnippet>();
        int snippetCount = 0;

        for (final Map.Entry<String, Component> entry : componentList.entrySet()) {
            final Component tempCmp = entry.getValue();
            if (snippetCount > MAX_SNIPPET_COUNT) {
                break;
            }
            if (tempCmp.componentType().isBaseComponent()) {
                boolean possibleSnippet = false;
                // if the class references the component we are looking for only
                // then will we analyze the methods within for code snippets
                for (final ComponentInvocation invocation : tempCmp.componentInvocations()) {
                    if (invocation.invokedComponent().equals(componentUniqueName)
                            && !tempCmp.uniqueName().equals(componentUniqueName)) {
                        possibleSnippet = true;
                        break;
                    }
                }

                if (possibleSnippet) {
                    final ArrayList<String> snippetKeyWords = new ArrayList<String>();
                    final ArrayList<String> methodNames = new ArrayList<String>();
                    snippetKeyWords.add(componentShortName);
                    snippetKeyWords.add(componentUniqueName);
                    // analyze all the components within the current class for
                    // snippets..
                    for (final String childComponent : tempCmp.children()) {
                        final Component childCmp = componentList.get(childComponent);
                        if (childCmp != null) {
                            // collect the name's of class fields that reference the
                            // desired component
                            if (childCmp.componentType().isVariableComponent()) {
                                for (final ComponentInvocation invocation : childCmp.componentInvocations()) {
                                    if (invocation.invokedComponent().equals(componentUniqueName)) {
                                        snippetKeyWords.add(childCmp.name());
                                    }
                                }
                            } else if (childCmp.componentType().isMethodComponent()) {
                                // collect the names of the method's parameter/local
                                // vars that reference the desired component
                                boolean relevantSnippet = false;
                                for (final String methodChildComponent : childCmp.children()) {
                                    if (componentList.containsKey(methodChildComponent)) {
                                        final Component methodChildCmp = componentList.get(methodChildComponent);
                                        if (methodChildCmp.componentType().isVariableComponent()) {
                                            for (final ComponentInvocation invocation : methodChildCmp.componentInvocations()) {
                                                if (invocation.invokedComponent().equals(componentUniqueName)) {
                                                    snippetKeyWords.add(methodChildCmp.name());
                                                    relevantSnippet = true;
                                                    if (!methodNames.contains(childCmp.uniqueName())) {
                                                        methodNames.add(childCmp.uniqueName());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                // in the case were the method references the given
                                // component through a thrown exception
                                // or return value rather than a local/field
                                // variable, we still want to show it as a sample
                                // usage snippet for the given type
                                if (!relevantSnippet) {
                                    for (final ComponentInvocation invocation : childCmp.componentInvocations()) {
                                        if (invocation.invokedComponent().equals(componentUniqueName)) {
                                            methodNames.add(childCmp.uniqueName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (final String possibleMethodSnippetName : methodNames) {
                        // search all the methods for the list of key words we
                        // have collected consisting of
                        // variables that reference the desired component
                        final Component possibleMethodSnippetCmp = componentList.get(possibleMethodSnippetName);
                        final int snippetScore = new SnippetScore(snippetKeyWords, possibleMethodSnippetCmp.code())
                        .score();
                        if (snippetScore > 0) {
                            final CodeSnippet snippet = new CodeSnippet(possibleMethodSnippetCmp, snippetScore,
                                    snippetKeyWords, new RelevantComponentGrouping(possibleMethodSnippetCmp,
                                            componentList).componentGroup());
                            // so we can prioritize test related snippets..
                            if (snippet.isTestRelated()) {
                                testSnippets.add(snippet);
                            } else {
                                regularSnippets.add(snippet);
                            }
                            snippetCount++;
                        }
                    }
                }
            }
        }
        final Comparator<CodeSnippet> snippetComparator = new Comparator<CodeSnippet>() {
            @Override
            public int compare(final CodeSnippet p1, final CodeSnippet p2) {
                return p2.getSnippetScore() - p1.getSnippetScore(); // Descending
            }
        };
        // sort the list by activity score
        Collections.sort(testSnippets, snippetComparator);
        Collections.sort(regularSnippets, snippetComparator);

        if (testSnippets.size() >= NUM_SNIPPETS_RETURN) {
            return (testSnippets.subList(0, NUM_SNIPPETS_RETURN));
        } else {
            if (regularSnippets.size() + testSnippets.size() >= NUM_SNIPPETS_RETURN) {
                testSnippets.addAll(regularSnippets.subList(0, NUM_SNIPPETS_RETURN - testSnippets.size()));
                return testSnippets;
            } else {
                testSnippets.addAll(regularSnippets);
                return testSnippets;
            }
        }
    }
}
