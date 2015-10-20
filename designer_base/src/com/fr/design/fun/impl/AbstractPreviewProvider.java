package com.fr.design.fun.impl;

import com.fr.design.fun.PreviewProvider;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
public abstract class AbstractPreviewProvider implements PreviewProvider {

    @Override
    public void onClick(JTemplate<?, ?> jt) {
        jt.previewMenuActionPerformed(this);
    }

    @Override
    public Map<String, Object> parametersForPreview() {
        return Collections.EMPTY_MAP;
    }

    public boolean equals(Object obj) {
        return obj instanceof AbstractPreviewProvider
                && ComparatorUtils.equals(nameForPopupItem(), ((AbstractPreviewProvider) obj).nameForPopupItem());
    }

    @Override
    public int hashCode() {
        return nameForPopupItem().hashCode();
    }
}
