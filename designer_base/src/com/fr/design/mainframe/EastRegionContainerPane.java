package com.fr.design.mainframe;

import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.icontainer.UIResizableContainer;
import com.fr.stable.Constants;

public class EastRegionContainerPane extends UIResizableContainer {
    private static EastRegionContainerPane THIS;

    /**
     * �õ�ʵ��
     *
     * @return
     */
    public static final EastRegionContainerPane getInstance() {
        if (THIS == null) {
            THIS = new EastRegionContainerPane();
            THIS.setLastToolPaneY(DesignerEnvManager.getEnvManager().getLastEastRegionToolPaneY());
            THIS.setLastContainerWidth(DesignerEnvManager.getEnvManager().getLastEastRegionContainerWidth());
        }
        return THIS;
    }

    public EastRegionContainerPane() {
        super(Constants.LEFT);
        setVerticalDragEnabled(false);
        setContainerWidth(260);
    }

    /**
     * ˢ�������
     */
    public void refreshDownPane() {
        if (this.getDownPane() instanceof DockingView) {
            ((DockingView) this.getDownPane()).refreshDockingView();
        }
    }
}
