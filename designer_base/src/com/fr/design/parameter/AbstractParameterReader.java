package com.fr.design.parameter;

import com.fr.stable.StringUtils;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * ������ȡ�ĳ���ʵ��
 */
public abstract class AbstractParameterReader implements ParameterReader {

    /**
     * ���ܵ�����
     * @param tplPath ģ��·��
     * @param acceptTypes ����
     * @return ������ܸ������ͣ���ִ�ж�ȡ����
     */
    public boolean accept(String tplPath, String... acceptTypes) {
        if (StringUtils.isEmpty(tplPath)) {
            return false;
        }
        for (String accept : acceptTypes) {
            if (tplPath.endsWith(accept)) {
                return true;
            }
        }
        return false;
    }
}
