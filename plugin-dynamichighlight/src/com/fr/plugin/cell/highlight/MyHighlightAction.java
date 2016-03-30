package com.fr.plugin.cell.highlight;

import com.fr.base.Style;
import com.fr.report.cell.cellattr.highlight.AbstractStyleHighlightAction;
import com.fr.stable.Constants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 */
public class MyHighlightAction extends AbstractStyleHighlightAction {

    private int align = Constants.RIGHT;

    public MyHighlightAction() {
        this(Constants.RIGHT, 0);
    }

    public MyHighlightAction(int align, int scope) {
        super(scope);
        this.align = align;
    }

    public int getAlign() {
        return align;
    }

    @Override
    protected Style modStyle(Style style) {
        return style.deriveHorizontalAlignment(align);
    }

    @Override
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (tagName.equals("MyAlign")) {
                align = reader.getAttrAsInt("align", Constants.RIGHT);
            }
        }
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("MyAlign").attr("align", align);
        writer.end();
    }

    public Object clone() throws CloneNotSupportedException {
        MyHighlightAction cloned = (MyHighlightAction)super.clone();
        cloned.align = align;
        return  cloned;
    }
}
