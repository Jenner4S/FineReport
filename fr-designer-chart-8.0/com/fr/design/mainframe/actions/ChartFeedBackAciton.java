// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.design.actions.help.FeedBackAction;
import com.fr.design.actions.help.FeedBackPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class ChartFeedBackAciton extends FeedBackAction
{
    private class ChartFeedBackPane extends FeedBackPane
    {

        final ChartFeedBackAciton this$0;

        protected JPanel getContactPane()
        {
            double d = -1D;
            double d1 = -2D;
            Component acomponent[][] = {
                {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("email")).append(":").toString(), 4), email
                }, {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("mobile_number")).append(":").toString(), 4), phone
                }
            };
            double ad[] = {
                d1, d1, d1
            };
            double ad1[] = {
                d1, d1
            };
            int ai[][] = {
                {
                    1, 1
                }, {
                    1, 1
                }, {
                    1, 1
                }
            };
            return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
        }

        private ChartFeedBackPane()
        {
            this$0 = ChartFeedBackAciton.this;
            super();
        }

    }


    public ChartFeedBackAciton()
    {
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        com.fr.design.mainframe.DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        ChartFeedBackPane chartfeedbackpane = new ChartFeedBackPane();
        BasicDialog basicdialog = chartfeedbackpane.showWindow(designerframe, false);
        chartfeedbackpane.setFeedbackDialog(basicdialog);
        basicdialog.setVisible(true);
    }
}
