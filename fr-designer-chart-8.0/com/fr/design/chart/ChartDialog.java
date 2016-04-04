// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.chart:
//            ChartTypePane

public class ChartDialog extends MiddleChartDialog
{

    private BaseChartCollection cc;
    private UIButton ok;
    private UIButton cancel;

    public ChartDialog(Frame frame)
    {
        super(frame);
        initComponent();
    }

    public ChartDialog(Dialog dialog)
    {
        super(dialog);
        initComponent();
    }

    private void initComponent()
    {
        setLayout(new BorderLayout());
        final ChartTypePane chartTypePane = new ChartTypePane();
        setTitle(Inter.getLocText("M-Popup_ChartType"));
        applyClosingAction();
        applyEscapeAction();
        setBasicDialogSize(BasicDialog.DEFAULT);
        add(chartTypePane, "Center");
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(2));
        add(jpanel, "South");
        ok = new UIButton(Inter.getLocText("OK"));
        cancel = new UIButton(Inter.getLocText("Cancel"));
        jpanel.add(ok);
        jpanel.add(cancel);
        ok.addActionListener(new ActionListener() {

            final ChartTypePane val$chartTypePane;
            final ChartDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                chartTypePane.update((ChartCollection)cc);
                doOK();
            }

            
            {
                this$0 = ChartDialog.this;
                chartTypePane = charttypepane;
                super();
            }
        }
);
        cancel.addActionListener(new ActionListener() {

            final ChartDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                doCancel();
            }

            
            {
                this$0 = ChartDialog.this;
                super();
            }
        }
);
        GUICoreUtils.setWindowCenter(getOwner(), this);
    }

    public void checkValid()
        throws Exception
    {
    }

    public void populate(BaseChartCollection basechartcollection)
    {
        if(basechartcollection == null)
        {
            return;
        } else
        {
            cc = basechartcollection;
            return;
        }
    }

    public BaseChartCollection getChartCollection()
    {
        return cc;
    }

}
