// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.GradientPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;
import com.fr.general.Background;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class ChartBackgroundPane extends BasicPane
{

    private static final long serialVersionUID = 0x608881807195e173L;
    private static final double ALPHA_V = 100D;
    protected static final int CHART_GRADIENT_WIDTH = 150;
    protected java.util.List paneList;
    private UIComboBox typeComboBox;
    private UINumberDragPane transparent;

    public ChartBackgroundPane()
    {
        typeComboBox = new UIComboBox();
        final CardLayout cardlayout = new CardLayout();
        paneList = new ArrayList();
        initList();
        final JPanel centerPane = new JPanel(cardlayout) {

            final ChartBackgroundPane this$0;

            public Dimension getPreferredSize()
            {
                int j = typeComboBox.getSelectedIndex();
                return new Dimension(super.getPreferredSize().width, ((BackgroundSettingPane)paneList.get(j)).getPreferredSize().height);
            }

            
            {
                this$0 = ChartBackgroundPane.this;
                super(layoutmanager);
            }
        }
;
        for(int i = 0; i < paneList.size(); i++)
        {
            BackgroundSettingPane backgroundsettingpane = (BackgroundSettingPane)paneList.get(i);
            typeComboBox.addItem(backgroundsettingpane.title4PopupWindow());
            centerPane.add(backgroundsettingpane, backgroundsettingpane.title4PopupWindow());
        }

        typeComboBox.addItemListener(new ItemListener() {

            final CardLayout val$cardlayout;
            final JPanel val$centerPane;
            final ChartBackgroundPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
                fireStateChanged();
            }

            
            {
                this$0 = ChartBackgroundPane.this;
                cardlayout = cardlayout1;
                centerPane = jpanel;
                super();
            }
        }
);
        transparent = new UINumberDragPane(0.0D, 100D);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                typeComboBox, null
            }, {
                centerPane, null
            }, {
                new UILabel(Inter.getLocText("Chart_Alpha_Int")), null
            }, {
                null, transparent
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Background"
        }, acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        add(new JSeparator(), "South");
    }

    protected void initList()
    {
        paneList.add(new NullBackgroundPane());
        paneList.add(new ColorBackgroundPane());
        paneList.add(new ImageBackgroundPane());
        paneList.add(new GradientPane(150));
    }

    private void fireStateChanged()
    {
        Object aobj[] = listenerList.getListenerList();
        ChangeEvent changeevent = null;
        for(int i = aobj.length - 2; i >= 0; i -= 2)
        {
            if(aobj[i] != javax/swing/event/ChangeListener)
                continue;
            if(changeevent == null)
                changeevent = new ChangeEvent(this);
            ((ChangeListener)aobj[i + 1]).stateChanged(changeevent);
        }

    }

    public String title4PopupWindow()
    {
        return "";
    }

    public void populate(GeneralInfo generalinfo)
    {
        if(generalinfo == null)
            return;
        Background background = generalinfo.getBackground();
        double d = (double)generalinfo.getAlpha() * 100D;
        transparent.populateBean(Double.valueOf(d));
        for(int i = 0; i < paneList.size(); i++)
        {
            BackgroundSettingPane backgroundsettingpane = (BackgroundSettingPane)paneList.get(i);
            if(backgroundsettingpane.accept(background))
            {
                backgroundsettingpane.populateBean(background);
                typeComboBox.setSelectedIndex(i);
                return;
            }
        }

    }

    public void update(GeneralInfo generalinfo)
    {
        if(generalinfo == null)
            generalinfo = new GeneralInfo();
        generalinfo.setBackground(((BackgroundSettingPane)paneList.get(typeComboBox.getSelectedIndex())).updateBean());
        generalinfo.setAlpha((float)(transparent.updateBean().doubleValue() / 100D));
    }


}
