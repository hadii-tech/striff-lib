package com.clarity.binary.diagram;

import com.clarity.invocation.ComponentInvocation;
import com.clarity.sourcemodel.Component;
import com.clarity.sourcemodel.OOPSourceModelConstants.ComponentInvocations;

/**
 * Represents a class component on a UML class diagram.
 */
public class DiagramComponent {

	private Component component;

	public DiagramComponent(Component originalComponent) {
		this.component = originalComponent;
	}

	/**
	 * Returns true if the containing component should be included in a diagram or not.
	 */
	public boolean diagramaticallyRelevantComponent() {

		if (this.component.componentType().isMethodComponent()) {
			// no getters or setters
			if (component.name().startsWith("get") || component.name().startsWith("set")) {
				return false;
			}
			// no overridden methods
			for (final ComponentInvocation invocation : component
					.componentInvocations(ComponentInvocations.ANNOTATION)) {
				if (invocation.invokedComponent().equals("Override")) {
					return false;
				}
			}
		}
		return true;
	}
}
