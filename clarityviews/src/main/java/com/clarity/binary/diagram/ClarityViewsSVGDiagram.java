package com.clarity.binary.diagram;

public class ClarityViewsSVGDiagram implements ClassDiagram {

    public ClassDiagram diagram;
    private String clarityViewsWebSiteText = "<text xml:space=\"preserve\" style=\"font-style:normal;font-weight:normal;font-size:13.33333302px;line-height:25px;font-family:consolas;letter-spacing:0px;word-spacing:0px;fill:#00ffff;fill-opacity:1;stroke:none\" id=\"text3968\"><tspan sodipodi:role=\"line\" id=\"tspan3966\" x=\"15\" y=\"15\" style=\"font-size:10px\">www.clarityviews.io</tspan></text>";

    public ClarityViewsSVGDiagram(ClassDiagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public String svgText() throws Exception {
        String svgText = this.diagram.svgText();
        int closingSvgTagIndex = svgText.lastIndexOf("</g>");
        svgText = svgText.substring(0, closingSvgTagIndex) + clarityViewsWebSiteText
                + svgText.substring(closingSvgTagIndex);
        return svgText;
    }
}
