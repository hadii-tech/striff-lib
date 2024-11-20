package com.hadii.striff.diagram;

import com.hadii.clarpse.reference.ComponentReference;
import com.hadii.clarpse.sourcemodel.Component;
import com.hadii.clarpse.sourcemodel.OOPSourceCodeModel;
import com.hadii.clarpse.sourcemodel.OOPSourceModelConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the building blocks of a Striff diagram.
 */
public class DiagramComponent {

    private final Component cmp;
    private final List<String> children = new ArrayList<>();

    public DiagramComponent(Component cmp, OOPSourceCodeModel srcModel) {
        this.cmp = cmp;
        if (srcModel != null) {
            for (String child : cmp.children()) {
                Optional<Component> childCmp = srcModel.getComponent(child);
                if (childCmp.isPresent()) {
                    if (childCmp.get().componentType() == OOPSourceModelConstants.ComponentType.FIELD
                            || childCmp.get()
                                    .componentType() == OOPSourceModelConstants.ComponentType.INTERFACE_CONSTANT) {
                        String childName = new DiagramComponent(childCmp.get(), srcModel).uniqueName();
                        children.add(childName);
                        if (!childName.equals(child)) {
                            System.out.println("gotcha");
                        }
                    } else {
                        children.add(child);
                    }
                }
            }
        }
    }

    public DiagramComponent(String componentName) {
        this(new Component(), new OOPSourceCodeModel());
        this.cmp.setComponentName(componentName);
    }

    public List<String> children() {
        return this.children;
    }

    public String uniqueName() {
        return cmp.uniqueName();
    }

    public List<ComponentReference> references(OOPSourceModelConstants.TypeReferences implementation) {
        return cmp.references(implementation);
    }

    public Set<String> modifiers() {
        return this.cmp.modifiers();
    }

    public OOPSourceModelConstants.ComponentType componentType() {
        return this.cmp.componentType();
    }

    public String parentUniqueName() {
        return this.cmp.parentUniqueName();
    }

    public Set<ComponentReference> references() {
        return this.cmp.references();
    }

    public String name() {
        return this.cmp.name();
    }

    public String codeFragment() {
        return this.cmp.codeFragment();
    }

    public int componentHashCode() {
        return this.cmp.codeHash();
    }

    public String comment() {
        return this.cmp.comment();
    }

    public String sourceFile() {
        return this.cmp.sourceFile();
    }

    public void setName(String name) {
        this.cmp.setName(name);
    }

    public String packagePath() {
        if (this.cmp.pkg() != null) {
            if (!this.cmp.pkg().ellipsisSeparatedPkgPath().isEmpty()) {
                return this.cmp.pkg().ellipsisSeparatedPkgPath();
            } else {
                return this.cmp.pkg().name();
            }
        } else {
            return "";
        }
    }

    public String componentName() {
        return this.cmp.componentName();
    }

    /**
     * Fetches the current component's parent base component given the set of
     * components in the code base.
     */
    public DiagramComponent parentBaseCmp(Map<String, DiagramComponent> codeBase) {
        String currParentClassName = this.cmp.parentUniqueName();
        DiagramComponent parent;
        for (parent = codeBase.get(currParentClassName); parent != null
                && !parent.componentType().isBaseComponent(); parent = codeBase.get(currParentClassName)) {
            currParentClassName = parent.parentUniqueName();
        }
        return parent;
    }

    @Override
    public int hashCode() {
        return this.uniqueName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DiagramComponent)) {
            return false;
        }
        DiagramComponent other = (DiagramComponent) obj;
        return other.uniqueName().equals(this.uniqueName());
    }

    @Override
    public String toString() {
        return this.uniqueName();
    }
}
