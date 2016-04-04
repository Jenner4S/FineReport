package com.fr.plugin.chart.designer.component.background;

import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.general.Inter;

import java.awt.*;

/**
 * ͼ��  ���Ա�.�������� ����.(���� ��, ��ɫ, ����) + Բ�ǰ뾶
 */
public class VanChartBackgroundWithOutShadowWithRadiusPane extends VanChartBackgroundWithOutImagePane {
    private static final long serialVersionUID = -3387661350545592763L;

    private UISpinner radius;

    public VanChartBackgroundWithOutShadowWithRadiusPane(){
        super();
    }

    protected Component[][] getPaneComponents() {
        radius = new UISpinner(0,1000,1,0);
        return  new Component[][]{
                new Component[]{typeComboBox, null},
                new Component[]{centerPane, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-Chart_Alpha")), transparent},
                new Component[]{new UILabel(Inter.getLocText("plugin-ChartF_Radius")+":"),radius}
        };
    }

    public void populate(GeneralInfo attr) {
        if(attr == null) {
            return;
        }
        super.populate(attr);
        radius.setValue(attr.getRoundRadius());

    }

    public void update(GeneralInfo attr) {
        super.update(attr);
        if (attr == null) {
            attr = new GeneralInfo();
        }
        attr.setRoundRadius((int)radius.getValue());
    }

}
