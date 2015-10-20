package com.fr.design.webattr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;

/**
 * Created by IntelliJ IDEA.
 * User: Richer
 * Date: 11-6-29
 * Time: ����4:49
 */
public abstract class AbstractEditToolBarPane extends ReportSelectToolBarPane.EditToolBarPane<WebContent> {
    protected ToolBarManager[] toolBarManagers = null;
    private AbstractEditToolBarPane abstractEditToolBarPane;

    protected ActionListener editBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final DragToolBarPane dragToolbarPane = new DragToolBarPane();
            dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultToolBar(), getToolBarInstance());
            dragToolbarPane.populateBean(AbstractEditToolBarPane.this.toolBarManagers);

            BasicDialog toobarDialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(AbstractEditToolBarPane.this));
            toobarDialog.addDialogActionListener(new DialogActionAdapter() {
                @Override
				public void doOk() {
                    AbstractEditToolBarPane.this.toolBarManagers = dragToolbarPane.updateBean();
                }
            });

            toobarDialog.setVisible(true);
        }
    };
    
    protected abstract WidgetOption[] getToolBarInstance();
}
