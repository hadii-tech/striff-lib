package com.clarity.binary.diagram;

public class SignedSDDiagram implements Diagram {

    public Diagram diagram;
    private String clarityViewsWebSiteText = "<text xml:space=\"preserve\" style=\"font-style:normal;font-weight:normal;font-size:13.33333302px;line-height:25px;font-family:consolas;letter-spacing:0px;word-spacing:0px;fill:#C0D9D9;fill-opacity:1;stroke:none\" id=\"text3968\"><tspan id=\"tspan3966\" x=\"15\" y=\"15\" style=\"font-size:15px\"> clarity_language Diff View - www.clarityviews.io</tspan></text>";
    private String language;

    public SignedSDDiagram(Diagram diagram, String language) {
        this.diagram = diagram;
        this.language = language;
    }

    @Override
    public String svgText() throws Exception {
        String svgText = "";
        if (this.diagram != null) {
            svgText = this.diagram.svgText();
            if (svgText != null && !svgText.isEmpty()) {
                int closingSvgTagIndex = svgText.lastIndexOf("</g>");
                svgText = svgText.substring(0, closingSvgTagIndex)
                        + clarityViewsWebSiteText.replace("clarity_language", this.language)
                        + svgText.substring(closingSvgTagIndex);
            }
        }
        return svgText;
    }
}
