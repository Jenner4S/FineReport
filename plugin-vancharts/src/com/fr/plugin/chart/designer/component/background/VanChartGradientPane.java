package com.fr.plugin.chart.designer.component.background;

import com.fr.base.background.GradientBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.GradientPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Background;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * ����ɫ���ý��棬bar�޷���ק
 */
public class VanChartGradientPane extends GradientPane {
    protected static final int CHART_GRADIENT_WIDTH = 150;
    private static final long serialVersionUID = 256594362341221087L;

    private VanChartGradientBar gradientBar;
    private UIButtonGroup<Integer> directionPane;

    public VanChartGradientPane() {
        constructPane();
    }

    protected void constructPane(){
        String[] textArray = {Inter.getLocText("Utils-Left_to_Right"), Inter.getLocText("Utils-Top_to_Bottom")};
        Integer[] valueArray = {GradientBackground.LEFT2RIGHT, GradientBackground.TOP2BOTTOM};
        directionPane = new UIButtonGroup<Integer>(textArray, valueArray);
        directionPane.setSelectedIndex(0);
        gradientBar = new VanChartGradientBar(4, CHART_GRADIENT_WIDTH);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p,};

        Component[][] components = new Component[][]{
                new Component[]{gradientBar, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-Chart_Gradient_Direction") + ":"), directionPane}
        };
        JPanel Gradient = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(Gradient, BorderLayout.CENTER);
    }

    public void populateBean(Background background) {
        GradientBackground bg = (GradientBackground) background;
        this.gradientBar.getSelectColorPointBtnP1().setColorInner(bg.getStartColor());
        this.gradientBar.getSelectColorPointBtnP2().setColorInner(bg.getEndColor());
        directionPane.setSelectedItem(bg.getDirection());
        this.gradientBar.repaint();
    }

    public GradientBackground updateBean() {
        GradientBackground gb = new GradientBackground(gradientBar.getSelectColorPointBtnP1().getColorInner(), gradientBar.getSelectColorPointBtnP2().getColorInner());
        gb.setDirection(directionPane.getSelectedItem());

        return gb;
    }

    /**
     * ������Ǽ�һ���۲��߼����¼�
     *
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(final UIObserverListener listener) {
        gradientBar.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
        directionPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                listener.doChange();
            }
        });
    }

    /**
     * �Ƿ����
     * @param background     ����
     * @return    ���򷵻�true
     */
    public boolean accept(Background background) {
        return background instanceof GradientBackground;
    }

    /**
     * ����
     * @return     ����
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Plugin-Chart_Gradient_Color");
    }

}
