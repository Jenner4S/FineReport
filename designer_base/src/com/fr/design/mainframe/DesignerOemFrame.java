package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.stable.image4j.codec.ico.ICODecoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author richie
 * @date 2015-06-17
 * @since 8.0
 */
public class DesignerOemFrame extends DesignerFrame {

    /**
     * Constructor.
     *
     * @param ad
     */
    public DesignerOemFrame(ToolBarMenuDock ad) {
        super(ad);
    }

    @Override
    public void initTitleIcon() {
        try {
            InputStream in = DesignerFrame.class
                    .getResourceAsStream("/com/fr/plugin/oem/images/designer/logo.ico");
            if (in == null) {
                this.setIconImage(BaseUtils.readImage("/com/fr/plugin/oem/images/designer/logo.png"));
            } else {
                List<BufferedImage> image = ICODecoder.read(in);
                this.setIconImages(image);
            }
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
            this.setIconImage(BaseUtils.readImage("/com/fr/plugin/oem/images/designer/logo.png"));
        }
    }
}
