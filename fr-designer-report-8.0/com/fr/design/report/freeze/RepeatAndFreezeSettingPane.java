// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.freeze;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.page.ReportPageAttrProvider;
import com.fr.stable.ColumnRow;
import com.fr.stable.FT;
import com.fr.stable.bridge.StableFactory;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.report.freeze:
//            RepeatRowPane, RepeatColPane, FreezePagePane, FreezeWriteRowPane, 
//            FreezeWriteColPane

public class RepeatAndFreezeSettingPane extends BasicPane
{

    private RepeatRowPane repeatTitleRowPane;
    private RepeatColPane repeatTitleColPane;
    private RepeatRowPane repeatFinisRowPane;
    private RepeatColPane repeatFinisColPane;
    private FreezePagePane freezePageRowPane;
    private FreezePagePane freezePageColPane;
    private FreezeWriteRowPane freezeWriteRowPane;
    private FreezeWriteColPane freezeWriteColPane;
    private UICheckBox useRepeatTitleRCheckBox;
    private UICheckBox useRepeatTitleCCheckBox;
    private UICheckBox useRepeatFinisRCheckBox;
    private UICheckBox useRepeatFinisCCheckBox;
    private UICheckBox usePageFrozenCCheckBox;
    private UICheckBox usePageFrozenRCheckBox;
    private UICheckBox useWriteFrozenCCheckBox;
    private UICheckBox useWriteFrozenRCheckBox;
    ChangeListener useRepeatFinisCListener;
    ChangeListener useRepeatFinisRListener;
    ChangeListener useRepeatTitleCListener;
    ChangeListener useRepeatTitleRListener;
    ChangeListener useWriteFrozenRListener;
    ChangeListener useWriteFrozenCListener;
    ChangeListener usePageFrozenCListener;
    ChangeListener usePageFrozenRListener;
    ChangeListener freezePageRowListener;
    ChangeListener freezePageColListener;

    private JPanel initRowStartPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatTitleRCheckBox = new UICheckBox();
        jpanel.add(useRepeatTitleRCheckBox);
        jpanel.add(new UILabel(Inter.getLocText(new String[] {
            "PageSetup-Title_Start_Row", "From"
        })));
        repeatTitleRowPane = new RepeatRowPane();
        jpanel.add(repeatTitleRowPane);
        return jpanel;
    }

    private JPanel initColStartPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatTitleCCheckBox = new UICheckBox();
        jpanel.add(useRepeatTitleCCheckBox);
        jpanel.add(new UILabel(Inter.getLocText(new String[] {
            "PageSetup-Title_Start_Column", "From"
        })));
        repeatTitleColPane = new RepeatColPane();
        jpanel.add(repeatTitleColPane);
        return jpanel;
    }

    private JPanel initFootRowStarPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatFinisRCheckBox = new UICheckBox();
        jpanel.add(useRepeatFinisRCheckBox);
        jpanel.add(new UILabel(Inter.getLocText(new String[] {
            "PageSetup-Finis_Start_Row", "From"
        })));
        repeatFinisRowPane = new RepeatRowPane();
        jpanel.add(repeatFinisRowPane);
        return jpanel;
    }

    private JPanel initFootColStartPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatFinisCCheckBox = new UICheckBox();
        jpanel.add(useRepeatFinisCCheckBox);
        jpanel.add(new UILabel(Inter.getLocText(new String[] {
            "PageSetup-Finis_Start_Column", "From"
        })));
        repeatFinisColPane = new RepeatColPane();
        jpanel.add(repeatFinisColPane);
        return jpanel;
    }

    protected String getPageFrozenTitle()
    {
        return Inter.getLocText("FR-Engine_Page-Frozen");
    }

    private JPanel initPageFrozenPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel uilabel = new UILabel(getPageFrozenTitle());
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(uilabel);
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        jpanel.add(jpanel2, "Center");
        UILabel uilabel1 = new UILabel((new StringBuilder()).append("(").append(Inter.getLocText("FR-Engine_Please_Set_Repeat_First")).append(")").toString());
        uilabel1.setForeground(Color.red);
        jpanel1.add(uilabel1);
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel3.add(usePageFrozenRCheckBox = new UICheckBox());
        jpanel2.add(jpanel3);
        freezePageRowPane = new FreezePagePane(true);
        jpanel3.add(freezePageRowPane);
        addPageFrozenCol(jpanel2);
        return jpanel;
    }

    private void addPageFrozenCol(JPanel jpanel)
    {
        freezePageColPane = new FreezePagePane(false);
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(usePageFrozenCCheckBox = new UICheckBox());
        jpanel.add(jpanel1);
        jpanel1.add(freezePageColPane);
    }

    private JPanel initWriteFrozenPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Face_Write", "Frozen"
        })).append(":").toString());
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(uilabel);
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        jpanel.add(jpanel2, "Center");
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel3.add(useWriteFrozenRCheckBox = new UICheckBox());
        JPanel jpanel4 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel4.add(useWriteFrozenCCheckBox = new UICheckBox());
        jpanel2.add(jpanel3);
        freezeWriteRowPane = new FreezeWriteRowPane();
        jpanel3.add(freezeWriteRowPane);
        jpanel2.add(jpanel4);
        freezeWriteColPane = new FreezeWriteColPane();
        jpanel4.add(freezeWriteColPane);
        return jpanel;
    }

    public RepeatAndFreezeSettingPane()
    {
        useRepeatFinisCListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repeatFinisColPane.setEnabled(useRepeatFinisCCheckBox.isSelected());
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        useRepeatFinisRListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repeatFinisRowPane.setEnabled(useRepeatFinisRCheckBox.isSelected());
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        useRepeatTitleCListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                boolean flag = useRepeatTitleCCheckBox.isSelected();
                repeatTitleColPane.setEnabled(flag);
                if(!flag)
                {
                    usePageFrozenCCheckBox.setSelected(false);
                    usePageFrozenCCheckBox.setEnabled(false);
                } else
                {
                    usePageFrozenCCheckBox.setEnabled(true);
                }
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        useRepeatTitleRListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                boolean flag = useRepeatTitleRCheckBox.isSelected();
                repeatTitleRowPane.setEnabled(flag);
                if(!flag)
                {
                    usePageFrozenRCheckBox.setSelected(false);
                    usePageFrozenRCheckBox.setEnabled(false);
                } else
                {
                    usePageFrozenRCheckBox.setEnabled(true);
                }
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        useWriteFrozenRListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                boolean flag = useWriteFrozenRCheckBox.isSelected();
                freezeWriteRowPane.setEnabled(flag);
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        useWriteFrozenCListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                boolean flag = useWriteFrozenCCheckBox.isSelected();
                freezeWriteColPane.setEnabled(flag);
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        usePageFrozenCListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                freezePageColPane.setEnabled(usePageFrozenCCheckBox.isSelected());
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        usePageFrozenRListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                freezePageRowPane.setEnabled(usePageFrozenRCheckBox.isSelected());
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        freezePageRowListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                FT ft = repeatTitleRowPane.updateBean();
                int i = ft.getTo();
                freezePageRowPane.populateBean(new FT(i <= -1 ? -1 : 0, i));
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        freezePageColListener = new ChangeListener() {

            final RepeatAndFreezeSettingPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                FT ft = repeatTitleColPane.updateBean();
                int i = ft.getTo();
                freezePageColPane.populateBean(new FT(i <= -1 ? -1 : 0, i));
            }

            
            {
                this$0 = RepeatAndFreezeSettingPane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Engine_Repeat"));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Engine_Frozen"));
        add(jpanel, "North");
        add(jpanel2, "Center");
        JPanel jpanel3 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        jpanel3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JPanel jpanel4 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(jpanel4);
        JPanel jpanel5 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel5.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Engine_Select_the_repeated_row_and_column")).append(":").toString()));
        UILabel uilabel = new UILabel((new StringBuilder()).append("(").append(Inter.getLocText("FR-Engine_FreezeWarning1")).append(")").toString());
        uilabel.setForeground(Color.red);
        jpanel5.add(uilabel);
        jpanel1.add(jpanel5, "North");
        jpanel1.add(jpanel3, "Center");
        jpanel3.add(initRowStartPane());
        addColStart(jpanel3);
        jpanel3.add(initFootRowStarPane());
        addFootColStart(jpanel3);
        jpanel4.add(initPageFrozenPane(), "North");
        addWriteFrozen(jpanel4);
        initPageRwoListener();
        initPageColListener();
        initWriteListener();
    }

    protected void initWriteListener()
    {
        useWriteFrozenCCheckBox.addChangeListener(useWriteFrozenCListener);
        useWriteFrozenRCheckBox.addChangeListener(useWriteFrozenRListener);
    }

    private void initPageRwoListener()
    {
        repeatTitleRowPane.addListener(freezePageRowListener);
        usePageFrozenRCheckBox.addChangeListener(usePageFrozenRListener);
        useRepeatTitleRCheckBox.addChangeListener(useRepeatTitleRListener);
        useRepeatFinisRCheckBox.addChangeListener(useRepeatFinisRListener);
    }

    protected void initPageColListener()
    {
        repeatTitleColPane.addListener(freezePageColListener);
        usePageFrozenCCheckBox.addChangeListener(usePageFrozenCListener);
        useRepeatTitleCCheckBox.addChangeListener(useRepeatTitleCListener);
        useRepeatFinisCCheckBox.addChangeListener(useRepeatFinisCListener);
    }

    protected void addWriteFrozen(JPanel jpanel)
    {
        jpanel.add(initWriteFrozenPane(), "Center");
    }

    protected void addFootColStart(JPanel jpanel)
    {
        jpanel.add(initFootColStartPane());
    }

    protected void addColStart(JPanel jpanel)
    {
        jpanel.add(initColStartPane());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Engine_Repeat-Freeze");
    }

    public void populate(ReportPageAttrProvider reportpageattrprovider)
    {
        if(reportpageattrprovider == null)
            reportpageattrprovider = (ReportPageAttrProvider)StableFactory.createXmlObject("ReportPageAttr");
        FT ft = new FT(0, 0);
        populatColPane(reportpageattrprovider, ft);
        populateRowPane(reportpageattrprovider, ft);
    }

    protected void populateRowPane(ReportPageAttrProvider reportpageattrprovider, FT ft)
    {
        FT ft1 = new FT((new Integer(reportpageattrprovider.getRepeatHeaderRowFrom())).intValue(), (new Integer(reportpageattrprovider.getRepeatHeaderRowTo())).intValue());
        if(isDefalut(ft1))
        {
            repeatTitleRowPane.populateBean(ft);
            repeatTitleRowPane.setEnabled(false);
            usePageFrozenRCheckBox.setEnabled(false);
        } else
        {
            repeatTitleRowPane.populateBean(ft1);
            useRepeatTitleRCheckBox.setSelected(true);
        }
        ft1 = new FT((new Integer(reportpageattrprovider.getRepeatFooterRowFrom())).intValue(), (new Integer(reportpageattrprovider.getRepeatFooterRowTo())).intValue());
        if(isDefalut(ft1))
        {
            repeatFinisRowPane.populateBean(ft);
            repeatFinisRowPane.setEnabled(false);
            useRepeatFinisRCheckBox.setSelected(false);
        } else
        {
            repeatFinisRowPane.populateBean(ft1);
            useRepeatFinisRCheckBox.setSelected(true);
        }
        usePageFrozenRCheckBox.setSelected(reportpageattrprovider.isUsePageFrozenRow());
        freezePageRowPane.setEnabled(reportpageattrprovider.isUsePageFrozenRow());
    }

    protected void populatColPane(ReportPageAttrProvider reportpageattrprovider, FT ft)
    {
        FT ft1 = new FT((new Integer(reportpageattrprovider.getRepeatHeaderColumnFrom())).intValue(), (new Integer(reportpageattrprovider.getRepeatHeaderColumnTo())).intValue());
        if(isDefalut(ft1))
        {
            repeatTitleColPane.populateBean(ft);
            repeatTitleColPane.setEnabled(false);
            usePageFrozenCCheckBox.setEnabled(false);
        } else
        {
            repeatTitleColPane.populateBean(ft1);
            useRepeatTitleCCheckBox.setSelected(true);
        }
        ft1 = new FT((new Integer(reportpageattrprovider.getRepeatFooterColumnFrom())).intValue(), (new Integer(reportpageattrprovider.getRepeatFooterColumnTo())).intValue());
        if(isDefalut(ft1))
        {
            repeatFinisColPane.populateBean(ft);
            repeatFinisColPane.setEnabled(false);
            useRepeatFinisCCheckBox.setSelected(false);
        } else
        {
            repeatFinisColPane.populateBean(ft1);
            useRepeatFinisCCheckBox.setSelected(true);
        }
        usePageFrozenCCheckBox.setSelected(reportpageattrprovider.isUsePageFrozenColumn());
        freezePageColPane.setEnabled(reportpageattrprovider.isUsePageFrozenColumn());
    }

    private boolean isDefalut(FT ft)
    {
        return ft.getFrom() == -1 && ft.getTo() == -1;
    }

    public void populateWriteFrozenColumnRow(ColumnRow columnrow)
    {
        if(columnrow != null)
        {
            int i = columnrow.getColumn();
            int j = columnrow.getRow();
            if(i > 0)
                freezeWriteColPane.populateBean(new FT(1, i - 1));
            if(j > 0)
                freezeWriteRowPane.populateBean(new FT(1, j - 1));
            useWriteFrozenCCheckBox.setSelected(i > 0);
            useWriteFrozenRCheckBox.setSelected(j > 0);
            freezeWriteColPane.setEnabled(i > 0);
            freezeWriteRowPane.setEnabled(j > 0);
        } else
        {
            useWriteFrozenCCheckBox.setSelected(false);
            useWriteFrozenRCheckBox.setSelected(false);
            freezeWriteRowPane.setEnabled(false);
            freezeWriteColPane.setEnabled(false);
        }
    }

    public ReportPageAttrProvider update()
    {
        ReportPageAttrProvider reportpageattrprovider = (ReportPageAttrProvider)StableFactory.createXmlObject("ReportPageAttr");
        updateRowPane(reportpageattrprovider);
        updateColPane(reportpageattrprovider);
        return reportpageattrprovider;
    }

    protected void updateRowPane(ReportPageAttrProvider reportpageattrprovider)
    {
        int i = valid(useRepeatTitleRCheckBox, repeatTitleRowPane.updateBean().getFrom());
        int j = valid(useRepeatTitleRCheckBox, repeatTitleRowPane.updateBean().getTo());
        reportpageattrprovider.setRepeatHeaderRowFrom(i);
        reportpageattrprovider.setRepeatHeaderRowTo(j);
        int k = valid(useRepeatFinisRCheckBox, repeatFinisRowPane.updateBean().getFrom());
        int l = valid(useRepeatFinisRCheckBox, repeatFinisRowPane.updateBean().getTo());
        reportpageattrprovider.setRepeatFooterRowFrom(k);
        reportpageattrprovider.setRepeatFooterRowTo(l);
        reportpageattrprovider.setUsePageFrozenRow(usePageFrozenRCheckBox.isSelected());
    }

    private int valid(UICheckBox uicheckbox, int i)
    {
        return uicheckbox.isSelected() ? i : -1;
    }

    protected void updateColPane(ReportPageAttrProvider reportpageattrprovider)
    {
        int i = valid(useRepeatTitleCCheckBox, repeatTitleColPane.updateBean().getFrom());
        int j = valid(useRepeatTitleCCheckBox, repeatTitleColPane.updateBean().getTo());
        reportpageattrprovider.setRepeatHeaderColumnFrom(i);
        reportpageattrprovider.setRepeatHeaderColumnTo(j);
        int k = valid(useRepeatFinisCCheckBox, repeatFinisColPane.updateBean().getFrom());
        int l = valid(useRepeatFinisCCheckBox, repeatFinisColPane.updateBean().getTo());
        reportpageattrprovider.setRepeatFooterColumnFrom(k);
        reportpageattrprovider.setRepeatFooterColumnTo(l);
        reportpageattrprovider.setUsePageFrozenColumn(usePageFrozenCCheckBox.isSelected());
    }

    public ColumnRow updateWriteFrozenColumnRow()
    {
        if(useWriteFrozenCCheckBox.isSelected() || useWriteFrozenRCheckBox.isSelected())
        {
            int i = useWriteFrozenCCheckBox.isSelected() ? freezeWriteColPane.updateBean().getTo() + 1 : 0;
            int j = useWriteFrozenRCheckBox.isSelected() ? freezeWriteRowPane.updateBean().getTo() + 1 : 0;
            return ColumnRow.valueOf(i, j);
        } else
        {
            return null;
        }
    }
















}
