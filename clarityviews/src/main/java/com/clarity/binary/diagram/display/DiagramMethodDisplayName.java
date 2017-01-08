package com.clarity.binary.diagram.display;

import org.apache.commons.lang.StringUtils;

public class DiagramMethodDisplayName implements DiagramDisplayName {

    private String displayName;


    public DiagramMethodDisplayName(String methodComponentUniqueName) {
        String longMethodName = methodComponentUniqueName.substring(0, methodComponentUniqueName.indexOf("("));
        String shortMethodName;
        if (longMethodName.contains(".")) {
            shortMethodName = longMethodName.substring(longMethodName.lastIndexOf(".") + 1);
        } else {
            shortMethodName = longMethodName;
        }
        String methodParamList = methodComponentUniqueName.substring(longMethodName.length() + 1,
                methodComponentUniqueName.length() - 1);
        String[] longMethodParams = methodParamList.split(",");
        String[] shortMethodParams = new String[longMethodParams.length];
        int i = 0;
        for (String longMethodParam : longMethodParams) {
            if (longMethodParam.contains(".")) {
                shortMethodParams[i++] = longMethodParam.substring(longMethodParam.lastIndexOf(".") + 1);
            } else {
                shortMethodParams[i++] = longMethodParam;
            }
        }
        this.displayName = shortMethodName + "(" + StringUtils.join(shortMethodParams, ", ") + ")";
    }

    @Override
    public String value() {
        return this.displayName;
    }
}
