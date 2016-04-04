// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report.share;

import com.fr.base.*;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.*;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.stable.*;
import java.awt.Desktop;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;

// Referenced classes of package com.fr.design.report.share:
//            ConfusionInfo, ConfuseTabledataAction, ConfusionManagerPane

public class ShareButton extends UIButton
{

    private static final int SHARE_COUNTS = 4;
    private static final String SHARE_KEY = "share";
    private ActionListener shareListener;
    private MouseListener modifyShareListener;
    private MouseListener directShareListener;

    public ShareButton()
    {
        shareListener = new ActionListener() {

            final ShareButton this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                UIPopupMenu uipopupmenu = new UIPopupMenu();
                boolean flag = isSharable();
                UIMenuItem uimenuitem = new UIMenuItem(Inter.getLocText("FR-Designer_Share-Template"));
                uimenuitem.setEnabled(!flag);
                uimenuitem.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/directShare.png"));
                if(uimenuitem.isEnabled())
                    uimenuitem.addMouseListener(directShareListener);
                UIMenuItem uimenuitem1 = new UIMenuItem(Inter.getLocText("FR-Designer_Finish-Modify-Share"));
                uimenuitem1.setEnabled(flag);
                uimenuitem1.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/modifyShare.png"));
                if(uimenuitem1.isEnabled())
                    uimenuitem1.addMouseListener(modifyShareListener);
                uipopupmenu.add(uimenuitem);
                uipopupmenu.add(uimenuitem1);
                GUICoreUtils.showPopupMenu(uipopupmenu, ShareButton.this, 0, 20);
            }

            private boolean isSharable()
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                FILE file = jtemplate.getEditingFILE();
                String s = file.getEnvFullName();
                return ArrayUtils.getLength(s.split("share")) >= 4;
            }

            
            {
                this$0 = ShareButton.this;
                super();
            }
        }
;
        modifyShareListener = new MouseAdapter() {

            final ShareButton this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                exportAsImage();
                openBBS();
            }

            
            {
                this$0 = ShareButton.this;
                super();
            }
        }
;
        directShareListener = new MouseAdapter() {

            final ShareButton this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jtemplate.stopEditing();
                jtemplate.saveShareFile();
                final ConfusionManagerPane managerPane = new ConfusionManagerPane();
                boolean flag = managerPane.populateTabledataManager();
                if(!flag)
                {
                    return;
                } else
                {
                    BasicDialog basicdialog = managerPane.showMediumWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                        final ConfusionManagerPane val$managerPane;
                        final _cls3 this$1;

                        public void doOk()
                        {
                            updateManagerDialog(managerPane);
                        }

                    
                    {
                        this$1 = _cls3.this;
                        managerPane = confusionmanagerpane;
                        super();
                    }
                    }
);
                    basicdialog.setModal(false);
                    basicdialog.setVisible(true);
                    return;
                }
            }

            
            {
                this$0 = ShareButton.this;
                super();
            }
        }
;
        setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/share.png"));
        setToolTipText(Inter.getLocText("FR-Designer_Share-Template"));
        set4ToolbarButton();
        addActionListener(shareListener);
    }

    private void openBBS()
    {
        try
        {
            Desktop.getDesktop().browse(new URI(BBSConstants.SHARE_URL));
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
    }

    private String exportAsImage()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        TemplateWorkBook templateworkbook = (TemplateWorkBook)jtemplate.getTarget();
        ResultWorkBook resultworkbook = templateworkbook.execute(new HashMap(), ActorFactory.getActor("page"));
        ImageExporter imageexporter = new ImageExporter();
        File file = new File(getImagePath(jtemplate));
        try
        {
            imageexporter.export(new FileOutputStream(file), resultworkbook);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        return file.getParent();
    }

    private String getImagePath(JTemplate jtemplate)
    {
        FILE file = jtemplate.getEditingFILE();
        String s = FRContext.getCurrentEnv().getPath();
        String s1 = file.getParent().getPath();
        String s2 = (new StringBuilder()).append(file.getName().replaceAll(".cpt", "")).append(".png").toString();
        return StableUtils.pathJoin(new String[] {
            s, s1, s2
        });
    }

    public void updateManagerDialog(ConfusionManagerPane confusionmanagerpane)
    {
        Nameable anameable[] = confusionmanagerpane.update();
        int i = 0;
        for(int j = anameable.length; i < j; i++)
        {
            String s = anameable[i].getName();
            ConfusionInfo confusioninfo = (ConfusionInfo)((NameObject)anameable[i]).getObject();
            JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            TableDataSource tabledatasource = (TableDataSource)jtemplate.getTarget();
            EmbeddedTableData embeddedtabledata = (EmbeddedTableData)tabledatasource.getTableData(s);
            (new ConfuseTabledataAction()).confuse(confusioninfo, embeddedtabledata);
        }

    }




}
