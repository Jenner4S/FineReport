package com.fr.design.gui.itree.filetree;

import java.io.File;
/**
 * �����ж��Ƿ�Ϊ��Ŀ¼
 * ��:  C:\
 * @author kunsnat
 *
 */
public class RootFile {
    private File file;

    public RootFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String toString() {
        return file.getAbsolutePath();
    }
}


