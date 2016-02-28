package com.fr.plugin.chart.designer.component.background;

import com.fr.base.background.ImageBackground;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼ��  ���Ա�.�������� ����.(���� ��, ��ɫ, ͼƬ, ����)+������Ӱ
 * ͼƬ����û�в��ַ�ʽ��Ĭ��Ϊ���졣
 */
public class VanChartBackgroundPane extends BasicPane {
    private static final long serialVersionUID = 6955952013135176051L;
    private static final double ALPHA_V = 100.0;
    protected List<BackgroundSettingPane> paneList;

    protected UIComboBox typeComboBox;
    protected UINumberDragPane transparent;
    protected UIToggleButton shadow;

    protected JPanel centerPane;

    public VanChartBackgroundPane() {
        initComponents();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = {p, f};
        double[] rowSize = { p,p,p,p,p};

        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Background"}, getPaneComponents(), rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
        this.add(new JSeparator(), BorderLayout.SOUTH);
    }

    protected void initComponents() {
        typeComboBox = new UIComboBox();
        final CardLayout cardlayout = new CardLayout();
        paneList = new ArrayList<BackgroundSettingPane>();

        initList();

        centerPane = new JPanel(cardlayout) {

            @Override
            public Dimension getPreferredSize() {// AUGUST:ʹ�õ�ǰ���ĵĸ߶�
                int index = typeComboBox.getSelectedIndex();
                return new Dimension(super.getPreferredSize().width, paneList.get(index).getPreferredSize().height);
            }
        };
        for (int i = 0; i < paneList.size(); i++) {
            BackgroundSettingPane pane = paneList.get(i);
            typeComboBox.addItem(pane.title4PopupWindow());
            centerPane.add(pane, pane.title4PopupWindow());
        }

        typeComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
                fireStateChanged();
            }
        });

        transparent = new UINumberDragPane(0, 100);
    }

    protected Component[][] getPaneComponents() {
        shadow = new UIToggleButton(Inter.getLocText("plugin-ChartF_OpenShadow"));
        return  new Component[][]{
                new Component[]{typeComboBox, null},
                new Component[]{centerPane, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-Chart_Alpha")), transparent},
                new Component[]{shadow, null},
        };
    }

    protected void initList() {
        paneList.add(new NullBackgroundPane());
        paneList.add(new ColorBackgroundPane());
        paneList.add(new ImageBackgroundPane(false));
        paneList.add(new VanChartGradientPane());
    }


    private void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i + 1]).stateChanged(e);
            }
        }
    }

    /**
     * ���ر���
     * @return ����
     */
    public String title4PopupWindow() {
        return "";
    }

    public void populate(GeneralInfo attr) {
        if(attr == null) {
            return;
        }
        Background background = attr.getBackground();
        double alpha = attr.getAlpha() * ALPHA_V;
        transparent.populateBean(alpha);
        if(shadow != null){
            shadow.setSelected(attr.isShadow());
        }
        for (int i = 0; i < paneList.size(); i++) {
            BackgroundSettingPane pane = paneList.get(i);
            if (pane.accept(background)) {
                pane.populateBean(background);
                typeComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    public void update(GeneralInfo attr) {
        if (attr == null) {
            attr = new GeneralInfo();
        }
        attr.setBackground(paneList.get(typeComboBox.getSelectedIndex()).updateBean());
        if(attr.getBackground() instanceof ImageBackground){
            ((ImageBackground) attr.getBackground()).setLayout(Constants.IMAGE_EXTEND);
        }
        attr.setAlpha((float) (transparent.updateBean() / ALPHA_V));
        if(shadow != null){
            attr.setShadow(shadow.isSelected());
        }
    }
}

