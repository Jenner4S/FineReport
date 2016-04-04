// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.SelectImagePane;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.unit.FU;
import java.awt.Image;
import java.io.File;
import javax.swing.KeyStroke;

public class ImageFloatAction extends ElementCaseAction
{

    private boolean returnValue;
    public static final MenuKeySet FLOAT_INSERT_IMAGE = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'I';
        }

        public String getMenuName()
        {
            return Inter.getLocText("HF-Insert_Image");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public ImageFloatAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        returnValue = false;
        setMenuKeySet(FLOAT_INSERT_IMAGE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        final ElementCasePane reportPane = (ElementCasePane)getEditingComponent();
        if(reportPane == null)
        {
            return false;
        } else
        {
            reportPane.stopEditing();
            final FloatElement floatElement = new FloatElement();
            final SelectImagePane selectImagePane = new SelectImagePane();
            selectImagePane.populate(floatElement);
            BasicDialog basicdialog = selectImagePane.showWindow(DesignerContext.getDesignerFrame());
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final SelectImagePane val$selectImagePane;
                final FloatElement val$floatElement;
                final ElementCasePane val$reportPane;
                final ImageFloatAction this$0;

                public void doOk()
                {
                    File file = selectImagePane.getSelectedImage();
                    if(file != null && file.isFile())
                    {
                        java.awt.image.BufferedImage bufferedimage = BaseUtils.readImage(file.getPath());
                        CellImage cellimage = selectImagePane.update();
                        CoreGraphHelper.waitForImage(bufferedimage);
                        floatElement.setValue(bufferedimage);
                        int i = ScreenResolution.getScreenResolution();
                        floatElement.setWidth(FU.valueOfPix(bufferedimage.getWidth(null), i));
                        floatElement.setHeight(FU.valueOfPix(bufferedimage.getHeight(null), i));
                        floatElement.setStyle(cellimage.getStyle());
                        reportPane.addFloatElementToCenterOfElementPane(floatElement);
                        reportPane.setSelection(new FloatSelection(floatElement.getName()));
                        returnValue = true;
                    }
                }

                public void doCancel()
                {
                    returnValue = false;
                }

            
            {
                this$0 = ImageFloatAction.this;
                selectImagePane = selectimagepane;
                floatElement = floatelement;
                reportPane = elementcasepane;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return returnValue;
        }
    }


}
