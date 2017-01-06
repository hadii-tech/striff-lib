package com.clarity.binary;

import java.util.HashSet;
import java.util.Set;

import com.clarity.sourcemodel.Component;

/**
 * Represents a Set of Components, ensures no duplicates.
 */
public class ComponentSet {

	private Set<Component> componentSet = new HashSet<Component>();

	public ComponentSet(Set<Component>... componentSets) {
		// inefficient way to merge the given sets of components..
		for (Set<Component> set : componentSets) {
			for (Component cmp : set) {
				boolean alreadyExists = false;
				for (Component tmpCmp : componentSet) {
					if (tmpCmp.uniqueName().equals(cmp.uniqueName())) {
						alreadyExists = true;
					}
				}
				if (!alreadyExists) {
					componentSet.add(cmp);
				}
			}
		}
	}

	public Set<Component> set() {
		return this.componentSet;
	}
}
