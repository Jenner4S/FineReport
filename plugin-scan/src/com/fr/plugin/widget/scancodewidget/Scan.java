package com.fr.plugin.widget.scancodewidget;

import com.fr.form.ui.Widget;

/**
 * Created by joyxu on 2016/3/7.
 */
public class Scan extends Widget{

   private int defaultHash = 7;

    @Override
    public String getXType() {
        return "scan";
    }

    @Override
    public boolean isEditor() {
        return false;
    }

    @Override
    public String[] supportedEvents() {
         return new String[]{Widget.AFTERINIT, Widget.AFTEREDIT};
    }

    public int hashCode() {
        int hash = this.defaultHash;
        hash += super.hashCode();
        return hash;
    }

    public boolean equals(Object object) {
        return object instanceof Scan && super.equals(object);
    }
}