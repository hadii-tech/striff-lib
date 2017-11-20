package com.clarity.binary.diagram;

public class SignedSDDiagram implements Diagram {

    public Diagram diagram;
    private String websiteURL;
    private String badgeURL;

    public SignedSDDiagram(Diagram diagram, String websiteURL, String badgeURL) {
        this.diagram = diagram;
        this.websiteURL = websiteURL;
        this.badgeURL = badgeURL;
    }

    @Override
    public String svgText() throws Exception {
        String svgText = "";
        if (this.diagram != null) {
            String svg = "<a xlink:href=\"" + this.websiteURL + "\"><image  href=\"" + this.badgeURL + "\"></a>";
            svgText = this.diagram.svgText();
            if (svgText != null && !svgText.isEmpty()) {
                int closingSvgTagIndex = svgText.lastIndexOf("</g>");
                svgText = svgText.substring(0, closingSvgTagIndex) + svg + svgText.substring(closingSvgTagIndex);
            }
        }
        return svgText;
    }
}
