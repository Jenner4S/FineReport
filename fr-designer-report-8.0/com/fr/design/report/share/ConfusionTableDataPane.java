// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.share;

import com.fr.base.*;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.*;
import com.fr.stable.ArrayUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.report.share:
//            ConfusionInfo, ConfuseTabledataAction

public class ConfusionTableDataPane extends BasicBeanPane
{
    private class SpinnerWapper extends UITextField
    {

        private UIBasicSpinner spinner;
        final ConfusionTableDataPane this$0;

        public UIBasicSpinner getSpinner()
        {
            return spinner;
        }

        public void setSpinner(UIBasicSpinner uibasicspinner)
        {
            spinner = uibasicspinner;
        }

        public String getText()
        {
            return Utils.objectToString(spinner.getValue());
        }

        public SpinnerWapper(UIBasicSpinner uibasicspinner)
        {
            this$0 = ConfusionTableDataPane.this;
            super();
            spinner = uibasicspinner;
        }
    }


    private static final int TABLE_WIDTH = 300;
    private static final int TABLE_HEIGHT = 20;
    private ConfusionInfo info;
    private Component centerPane;
    private UITextField keyFields[];
    private ActionListener previewListener;

    public ConfusionTableDataPane()
    {
        previewListener = new ActionListener() {

            final ConfusionTableDataPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                ConfusionInfo confusioninfo = updateBean();
                String s = confusioninfo.getTabledataName();
                TableDataSource tabledatasource = DesignTableDataManager.getEditingTableDataSource();
                try
                {
                    EmbeddedTableData embeddedtabledata = (EmbeddedTableData)tabledatasource.getTableData(s).clone();
                    (new ConfuseTabledataAction()).confuse(confusioninfo, embeddedtabledata);
                    (new TemplateTableDataWrapper(embeddedtabledata)).previewData();
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error(clonenotsupportedexception.getMessage());
                }
            }

            
            {
                this$0 = ConfusionTableDataPane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = initNorthPane();
        add(jpanel, "North");
        centerPane = new JPanel();
        add(centerPane, "Center");
    }

    private JPanel initNorthPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_Choose-Data-Confusion-Tip"));
        jpanel.add(uilabel, "Center");
        UIButton uibutton = initPreviewButton();
        jpanel.add(uibutton, "East");
        return jpanel;
    }

    private UIButton initPreviewButton()
    {
        UIButton uibutton = new UIButton();
        uibutton.setIcon(BaseUtils.readIcon("/com/fr/web/images/preview.png"));
        uibutton.set4ToolbarButton();
        uibutton.setToolTipText(Inter.getLocText("FR-Designer_Preview-Data-Confusion"));
        uibutton.addActionListener(previewListener);
        return uibutton;
    }

    public void populateBean(ConfusionInfo confusioninfo)
    {
        remove(centerPane);
        info = confusioninfo;
        int i = ArrayUtils.getLength(info.getColumnNames());
        double d = -2D;
        double ad[] = initRowSize(i + 1);
        double ad1[] = {
            d, d, d
        };
        keyFields = new UITextField[i];
        Component acomponent[][] = initTableComponents(i, ad1.length);
        centerPane = new UIScrollPane(TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1));
        add(centerPane, "Center");
        revalidate();
    }

    private Component[][] initTableComponents(int i, int j)
    {
        Component acomponent[][] = new Component[i + 1][j];
        for(int k = 0; k < acomponent.length; k++)
            if(k == 0)
                acomponent[k] = initTableHeaderPanel();
            else
                acomponent[k] = initTableContentRow(k);

        return acomponent;
    }

    private double[] initRowSize(int i)
    {
        double d = -2D;
        double ad[] = new double[i];
        for(int j = 0; j < i; j++)
            ad[j] = d;

        return ad;
    }

    private Component[] initTableContentRow(int i)
    {
        String as[] = info.getColumnNames();
        int j = i - 1;
        boolean flag = info.isNumberColumn(j);
        Component component = getKeyComponent(flag, j);
        return (new Component[] {
            new UILabel(), new UILabel(as[j]), component
        });
    }

    private Component getKeyComponent(boolean flag, int i)
    {
        String as[] = info.getConfusionKeys();
        if(!flag)
        {
            keyFields[i] = new UITextField(as[i]);
            return keyFields[i];
        } else
        {
            JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UINumberField uinumberfield = new UINumberField();
            jpanel.add(uinumberfield);
            UIBasicSpinner uibasicspinner = populateNumberSpinner(as, i);
            jpanel.add(uibasicspinner, "Center");
            keyFields[i] = new SpinnerWapper(uibasicspinner);
            return jpanel;
        }
    }

    private UIBasicSpinner populateNumberSpinner(String as[], int i)
    {
        UIBasicSpinner uibasicspinner = new UIBasicSpinner();
        Number number = GeneralUtils.objectToNumber(as[i], false);
        uibasicspinner.setValue(number.intValue() != 0 ? ((Object) (number)) : ((Object) (Integer.valueOf(1))));
        return uibasicspinner;
    }

    private Component[] initTableHeaderPanel()
    {
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_Confusion-key"));
        UIButton uibutton = initHelpButton();
        jpanel.add(uilabel);
        jpanel.add(uibutton);
        jpanel.setPreferredSize(new Dimension(300, 20));
        UILabel uilabel1 = new UILabel(Inter.getLocText("FR-Designer_Confusion-key"));
        return (new Component[] {
            new UILabel(), uilabel1, jpanel
        });
    }

    private UIButton initHelpButton()
    {
        UIButton uibutton = new UIButton();
        uibutton.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/help.png"));
        uibutton.set4ToolbarButton();
        uibutton.setToolTipText(getConfusionTooltip());
        return uibutton;
    }

    private String getConfusionTooltip()
    {
        try
        {
            java.io.InputStream inputstream = IOUtils.readResource("/com/fr/design/report/share/shareToolTip.html");
            return IOUtils.inputStream2String(inputstream);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        return "";
    }

    public ConfusionInfo updateBean()
    {
        int i = ArrayUtils.getLength(keyFields);
        String as[] = info.getConfusionKeys();
        for(int j = 0; j < i; j++)
            as[j] = keyFields[j].getText();

        return info;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_Data-confusion");
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ConfusionInfo)obj);
    }
}
