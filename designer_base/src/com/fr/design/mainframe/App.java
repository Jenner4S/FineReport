package com.fr.design.mainframe;

import com.fr.base.io.IOFile;
import com.fr.file.FILE;
import com.fr.stable.fun.Level;

/**
 * Created by Administrator on 2016/3/17/0017.
 */
public interface App<T extends IOFile> extends Level{

    String MARK_STRING = "DesignerApp";

    int CURRENT_LEVEL = 1;
    /**
     * 默认延伸
     *
     * @return 类型
     */
    String[] defaultExtentions();

    /**
     * 打开模板
     *
     * @param tplFile
     *            文件
     * @return 报表
     */
    JTemplate<T, ?> openTemplate(FILE tplFile);

    /**
     * 做为输出文件.
     *
     * @param tplFile
     *            文件
     * @return 报表
     */
    T asIOFile(FILE tplFile);
}
