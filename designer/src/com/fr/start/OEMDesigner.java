package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.file.newReport.NewOEMWorkBookAction;
import com.fr.design.actions.file.newReport.NewPolyReportAction;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.DesignerOemFrame;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author richie
 * @date 2015-06-10
 * @since 8.0
 * OEM设计器启动类
 */
public class OEMDesigner extends Designer {

    public static void main(String... args) {
        new OEMDesigner(args);
    }

    public OEMDesigner(String[] args) {
        super(args);
    }

    @Override
    protected DesignerFrame createDesignerFrame() {
        return new DesignerOemFrame(this);
    }

    @Override
    public String buildPropertiesPath() {
        InputStream in = IOUtils.readResource("/com/fr/plugin/oem/build/build.properties");
        if (in == null) {
            return super.buildPropertiesPath();
        }
        return "/com/fr/plugin/oem/build/build.properties";
    }

    @Override
    protected SplashPane createSplashPane() {
        return new ReportSplashPane() {
            @Override
            public Image createSplashBackground() {
                Image image;
                if (GeneralContext.isChineseEnv()) {
                    image = BaseUtils.readImage("/com/fr/plugin/oem/images/splash_chinese.png");
                } else {
                    image = BaseUtils.readImage("/com/fr/plugin/oem/images/splash_english.png");
                }
                if (image != null) {
                    return image;
                } else {
                    return super.createSplashBackground();
                }
            }
        };
    }

    @Override
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new NewOEMWorkBookAction());
        shortCuts.add(new NewPolyReportAction());
        try {
            if (DesignModuleFactory.getNewFormAction() != null) {
                shortCuts.add((ShortCut) DesignModuleFactory.getNewFormAction().newInstance());
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }
}
