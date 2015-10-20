package com.fr.design.mainframe.widget.wrappers;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.form.ui.reg.CustomReg;
import com.fr.form.ui.reg.RegExp;

/**
 * ������ʽ
 */
public class RegexWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }
        return ((RegExp) v).toRegText();
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }
        return new CustomReg(txt);
    }

    @Override
    public void validate(String txt) throws ValidationException {
       
    }
}
