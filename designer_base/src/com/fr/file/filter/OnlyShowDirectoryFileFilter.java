package com.fr.file.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * �������е��ļ���FileFilter.()
 */
public class OnlyShowDirectoryFileFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
