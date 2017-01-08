package com.clarity.binary.diagram.display;

public class DiagramClassDisplayName implements DiagramDisplayName {

    private String displayName;

    public DiagramClassDisplayName(String classComponentUniqueName) {

        if (classComponentUniqueName.contains(".")) {
            this.displayName = classComponentUniqueName.substring(classComponentUniqueName.lastIndexOf(".") + 1);
        } else {
            this.displayName = classComponentUniqueName;
        }
    }

    @Override
    public String value() {
        return this.displayName;
    }
}
