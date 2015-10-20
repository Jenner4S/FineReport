package com.fr.design.designer.properties;

import com.fr.design.Exception.ValidationException;

/**
 * ���ַ���ת���ɿؼ��ؼ�����,�����жϸ��ַ����Ƿ�Ϻ����Թ���
 * @since 6.5.2
 */
public interface Decoder {

    Object decode(String txt);

    void validate(String txt) throws ValidationException;
}
