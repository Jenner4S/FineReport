package com.fr.design.mainframe.widget.wrappers;

import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class TreeModelWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        if(v instanceof TreeNodeAttr[]) {
        	return Inter.getLocText("Total") + ":" + ((TreeNodeAttr[]) v).length + Inter.getLocText("Tree-Grade");
        } else {
        	return Inter.getLocText("Auto-Build");
        }
       }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {
    }
}
