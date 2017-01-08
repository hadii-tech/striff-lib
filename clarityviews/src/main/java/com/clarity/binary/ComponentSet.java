package com.clarity.binary;

import java.util.HashSet;
import java.util.Set;

import com.clarity.sourcemodel.Component;

/**
 * A set of components.
 */
public class ComponentSet {

    private Set<Component> componentSet = new HashSet<Component>();

    /**
     * Merges two sets of components, gives preference to the newer set of
     * Components.
     */
    public ComponentSet(Set<Component> olderSet, Set<Component> newerSet) {
        // inefficient way to merge the given sets of components..
        for (Component oldCmp : olderSet) {
            boolean existsInNewerSet = false;
            for (Component newCmp : newerSet) {
                if (oldCmp.uniqueName().equals(newCmp.uniqueName())) {
                    existsInNewerSet = true;
                    // merge the old components children into the newer set
                    for (String olderCmpChild : oldCmp.children()) {
                        if (!newCmp.children().contains(olderCmpChild)) {
                            newCmp.children().add(olderCmpChild);
                        }
                    }
                }
            }
            if (!existsInNewerSet) {
                newerSet.add(oldCmp);
            }
        }
        this.componentSet = newerSet;
    }

    public Set<Component> set() {
        return this.componentSet;
    }
}
