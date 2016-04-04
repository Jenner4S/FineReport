// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.main.impl.WorkBook;
import com.fr.poly.PolyDesigner;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.report.Report;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Referenced classes of package com.fr.design.mainframe:
//            AuthoritySheetEditedPane, ElementCasePane, ReportComponentComposite, EastRegionContainerPane, 
//            DesignerContext, DesignerFrame, ReportComponentCardPane, ReportComponent

public class SheetNameTabPane extends JComponent
    implements MouseListener, MouseMotionListener
{
    private class CopySheetAction extends UpdateAction
    {

        final SheetNameTabPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            TemplateReport templatereport;
            try
            {
                templatereport = (TemplateReport)reportComposite.getEditingWorkBook().getReport(selectedIndex).clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                return;
            }
            int i = selectedIndex;
            reportComposite.getEditingWorkBook().addReport(i + 1, templatereport);
            String s = reportComposite.getEditingWorkBook().getReportName(i);
            int j = 0;
            for(int k = 0; k < reportComposite.getEditingWorkBook().getReportCount(); k++)
                if(reportComposite.getEditingWorkBook().getReportName(k).startsWith(s))
                    j++;

            String s1 = (new StringBuilder()).append("-").append(Inter.getLocText("Copy")).append(j).toString();
            reportComposite.getEditingWorkBook().setReportName(i + 1, (new StringBuilder()).append(s).append(s1).toString());
            setSelectedIndex(i + 1);
            reportComposite.validate();
            reportComposite.repaint(100L);
            reportComposite.fireTargetModified();
        }

        CopySheetAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
            setName(Inter.getLocText("Copy"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        }
    }

    private class RenameSheetAction extends UpdateAction
    {

        final SheetNameTabPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(selectedIndex < 0 || selectedIndex >= reportComposite.getEditingWorkBook().getReportCount())
                return;
            String s = JOptionPane.showInputDialog(reportComposite, (new StringBuilder()).append(Inter.getLocText("Rename")).append(":").toString(), reportComposite.getEditingWorkBook().getReportName(selectedIndex));
            if(s != null)
            {
                boolean flag = false;
                int i = 0;
                do
                {
                    if(i >= reportComposite.getEditingWorkBook().getReportCount())
                        break;
                    if(s.equalsIgnoreCase(reportComposite.getEditingWorkBook().getReportName(i)))
                    {
                        flag = true;
                        break;
                    }
                    i++;
                } while(true);
                if(!flag)
                {
                    reportComposite.getEditingWorkBook().setReportName(selectedIndex, s);
                    reportComposite.getEditingReportComponent().fireTargetModified();
                    reportComposite.repaint();
                } else
                {
                    JOptionPane.showMessageDialog(reportComposite, Inter.getLocText("Utils-The_Name_has_been_existed"));
                }
            }
        }

        RenameSheetAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
            setName(Inter.getLocText("Rename"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/rename.png"));
        }
    }

    private class RemoveSheetAction extends UpdateAction
    {

        final SheetNameTabPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(isAuthorityEditing)
                return;
            int i = reportComposite.getEditingWorkBook().getReportCount();
            if(i <= 1)
            {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(reportComposite), (new StringBuilder()).append(Inter.getLocText("At_least_one_visual_worksheet")).append("£¡").toString());
                return;
            }
            int j = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(reportComposite), Inter.getLocText("Des-Remove_WorkSheet"), ProductConstants.APP_NAME, 2);
            if(j == 0)
            {
                if(DesignerContext.getFormatState() != 0)
                    doCancelFormat();
                reportComposite.getEditingWorkBook().removeReport(selectedIndex);
                int k = selectedIndex <= 0 ? 0 : selectedIndex - 1;
                setSelectedIndex(k);
                int l = scrollIndex;
                if(l < lastOneIndex && scrollIndex > 0)
                {
                    scrollIndex--;
                    lastOneIndex--;
                }
                reportComposite.setComposite();
                reportComposite.repaint();
                reportComposite.fireTargetModified();
            }
        }

        RemoveSheetAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
            setName(Inter.getLocText("Remove"));
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }
    }

    protected class PolyReportInsertAction extends SheetInsertAction
    {

        final SheetNameTabPane this$0;

        protected TemplateReport newTemplateReport()
        {
            return new PolyWorkSheet();
        }

        protected String getTemplateReportType()
        {
            return Inter.getLocText("Poly");
        }

        protected PolyReportInsertAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
        }
    }

    protected class GridReportInsertAction extends SheetInsertAction
    {

        final SheetNameTabPane this$0;

        protected TemplateReport newTemplateReport()
        {
            return new WorkSheet();
        }

        protected String getTemplateReportType()
        {
            return Inter.getLocText("Report");
        }

        protected GridReportInsertAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
        }
    }

    protected abstract class SheetInsertAction extends UpdateAction
    {

        final SheetNameTabPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            if(isAuthorityEditing)
                return;
            int i = selectedIndex + 1;
            if(isOvertakeWidth)
                scrollIndex++;
            reportComposite.getEditingWorkBook().addReport(i, newTemplateReport());
            setSelectedIndex(i);
            reportComposite.setComposite();
            reportComposite.fireTargetModified();
            ReportComponent reportcomponent = reportComposite.centerCardPane.editingComponet;
            reportcomponent.setSelection(reportcomponent.getDefaultSelectElement());
            showCount = 1;
            WorkBook workbook = reportComposite.getEditingWorkBook();
            int j = workbook.getReportCount();
            double d = 0.0D;
            for(int k = scrollIndex; k < j; k++)
            {
                widthArray = calculateWidthArray();
                int l = widthArray[k];
                d += l + 1;
                int i1 = 12 + SheetNameTabPane.ADD_WORK_SHEET.getIconWidth() + 8 + SheetNameTabPane.ADD_POLY_SHEET.getIconWidth();
                double d1 = getWidth() - i1 - buttonPane.getWidth();
                if(k < widthArray.length - 1 && d + (double)widthArray[k + 1] + 1.0D > d1)
                {
                    isOvertakeWidth = true;
                    scrollIndex++;
                } else
                {
                    showCount++;
                    isOvertakeWidth = false;
                }
            }

            if(scrollIndex > 0 && showCount + scrollIndex < j)
                scrollIndex++;
            DesignerContext.getDesignerFrame().getContentFrame().repaint();
        }

        protected abstract TemplateReport newTemplateReport();

        protected abstract String getTemplateReportType();

        SheetInsertAction()
        {
            this$0 = SheetNameTabPane.this;
            super();
            setName((new StringBuilder()).append(Inter.getLocText("Insert")).append(getTemplateReportType()).toString());
            setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }
    }


    private static final Color LINE_COLOR = new Color(0xababab);
    private static final Icon ADD_WORK_SHEET = BaseUtils.readIcon("com/fr/base/images/oem/addworksheet.png");
    protected static final Icon ADD_POLY_SHEET = BaseUtils.readIcon("com/fr/design/images/sheet/addpolysheet.png");
    private static final Icon WORK_SHEET_ICON = BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
    private static final Icon POLY_SHEET_ICON = BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
    private static final Image DESIGN_IMAGE;
    private static final Icon LEFT_ICON;
    private static final Icon RIGHT_ICON;
    private static final Icon DISABLED_LEFT_ICON;
    private static final Icon DISABLED_RIGHT_ICON;
    private static final int ICON_SEP_DISTANCE = 8;
    private static final int TOOLBAR_HEIGHT = 16;
    private static final int ADD_WIDTH_BY_SHEETNAME = 20;
    private static final int GRID_TOSHEET_RIGHT = 20;
    private static final int POLY_TOSHEET_LEFT = 30;
    private static final int POLY_TOSHEET_RIGHT = 50;
    private static final int SHEET_ICON_GAP = 5;
    private static final int GRAP = 12;
    private static final int LEFT_CORNOR = 0;
    private static final int RIGHT_CORNOR = 0;
    private UIButton leftButton;
    private UIButton rightButton;
    private int xyPressedCoordinate[] = {
        0, 0
    };
    private int xyReleasedCoordinate[] = {
        0, 0
    };
    private int widthArray[];
    private java.util.List lineArray;
    private boolean isReleased;
    private boolean isOvertakeWidth;
    private int showCount;
    protected int iconLocation;
    private int scrollIndex;
    private int lastOneIndex;
    private ReportComponentComposite reportComposite;
    private int selectedIndex;
    private JPanel buttonPane;
    private boolean isAuthorityEditing;

    public SheetNameTabPane(ReportComponentComposite reportcomponentcomposite)
    {
        lineArray = new ArrayList();
        isReleased = false;
        isOvertakeWidth = false;
        showCount = 0;
        scrollIndex = 0;
        selectedIndex = -1;
        isAuthorityEditing = false;
        reportComposite = reportcomponentcomposite;
        setLayout(new BorderLayout(0, 0));
        addMouseListener(this);
        addMouseMotionListener(this);
        setBorder(null);
        setForeground(new Color(99, 99, 99));
        leftButton = new UIButton(LEFT_ICON) {

            final SheetNameTabPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 16);
            }

            
            {
                this$0 = SheetNameTabPane.this;
                super(icon);
            }
        }
;
        leftButton.set4ToolbarButton();
        leftButton.setDisabledIcon(DISABLED_LEFT_ICON);
        rightButton = new UIButton(RIGHT_ICON) {

            final SheetNameTabPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 16);
            }

            
            {
                this$0 = SheetNameTabPane.this;
                super(icon);
            }
        }
;
        rightButton.set4ToolbarButton();
        rightButton.setDisabledIcon(DISABLED_RIGHT_ICON);
        buttonPane = new JPanel(new BorderLayout(3, 0));
        buttonPane.add(rightButton, "East");
        buttonPane.add(leftButton, "Center");
        add(buttonPane, "East");
        leftButton.addActionListener(createLeftButtonActionListener());
        rightButton.addActionListener(new ActionListener() {

            final SheetNameTabPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = reportComposite.getEditingWorkBook().getReportCount();
                if(selectedIndex <= i - 1 && scrollIndex < i - showCount)
                {
                    scrollIndex = Math.min((showCount != 0 ? showCount : 1) + scrollIndex, i - showCount - 1);
                    repaint();
                }
            }

            
            {
                this$0 = SheetNameTabPane.this;
                super();
            }
        }
);
    }

    private ActionListener createLeftButtonActionListener()
    {
        return new ActionListener() {

            final SheetNameTabPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                int i = scrollIndex;
                if(i == lastOneIndex && i != 0)
                {
                    scrollIndex--;
                    lastOneIndex--;
                    repaint();
                } else
                {
                    while(i > lastOneIndex && showCount != 0) 
                    {
                        scrollIndex++;
                        lastOneIndex++;
                        repaint();
                    }
                    while(i < lastOneIndex && scrollIndex > 0) 
                    {
                        scrollIndex--;
                        lastOneIndex--;
                        repaint();
                    }
                }
            }

            
            {
                this$0 = SheetNameTabPane.this;
                super();
            }
        }
;
    }

    public void setSelectedIndex(int i)
    {
        doBeforeChange(selectedIndex);
        selectedIndex = i;
        doAfterChange(i);
    }

    private void doWithAuthority()
    {
        AuthoritySheetEditedPane authoritysheeteditedpane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), selectedIndex);
        authoritysheeteditedpane.populate();
        EastRegionContainerPane.getInstance().replaceUpPane(authoritysheeteditedpane);
        EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
    }

    protected void doBeforeChange(int i)
    {
        reportComposite.doBeforeChange(i);
    }

    protected void doAfterChange(int i)
    {
        reportComposite.doAfterChange(i);
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(isAuthorityEditing)
        {
            return;
        } else
        {
            lineArray.add(mouseevent.getPoint());
            repaint();
            return;
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    private void checkButton(boolean flag)
    {
        leftButton.setEnabled(flag);
        rightButton.setEnabled(flag);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        isAuthorityEditing = BaseUtils.isAuthorityEditing();
        showCount = 0;
        Graphics2D graphics2d = (Graphics2D)g;
        FontMetrics fontmetrics = GraphHelper.getFontMetrics(getFont());
        int i = fontmetrics.charWidth('M');
        int j = fontmetrics.getAscent();
        double d = getSize().getHeight() - 1.0D;
        widthArray = calculateWidthArray();
        int k = 12 + ADD_WORK_SHEET.getIconWidth() + 8 + ADD_POLY_SHEET.getIconWidth();
        double d1 = getWidth() - k - buttonPane.getWidth();
        paintBackgroundAndLine(graphics2d, d, d1, i, j);
        checkButton(showCount < widthArray.length);
        if(!lineArray.isEmpty())
            paintDragTab(graphics2d, d, i, j);
        if(isReleased)
            lineArray.clear();
    }

    private void paintBackgroundAndLine(Graphics2D graphics2d, double d, double d1, int i, int j)
    {
        showCount = 0;
        int k = 0;
        WorkBook workbook = reportComposite.getEditingWorkBook();
        int l = workbook.getReportCount();
        double d2 = 0.0D;
        for(int i1 = scrollIndex; i1 < l; i1++)
        {
            lastOneIndex = i1;
            TemplateReport templatereport = workbook.getTemplateReport(i1);
            boolean flag = false;
            if(isAuthorityEditing)
            {
                String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                flag = templatereport.getWorkSheetPrivilegeControl().checkInvisible(s);
            }
            Icon icon = (templatereport instanceof WorkSheet) ? WORK_SHEET_ICON : POLY_SHEET_ICON;
            String s1 = workbook.getReportName(i1);
            if(i1 == selectedIndex)
                paintSelectedTab(graphics2d, icon, d, d2, s1, i, j, flag);
            else
                paintUnSelectedTab(graphics2d, icon, d, d2, s1, i, j, i1, flag);
            int j1 = widthArray[i1];
            d2 += j1 + 1;
            k += j1;
            if(i1 < widthArray.length - 1 && d2 + (double)widthArray[i1 + 1] + 1.0D > d1)
            {
                isOvertakeWidth = true;
                break;
            }
            showCount++;
            isOvertakeWidth = false;
            int k1 = (int)d1 / j1;
            if(i1 >= (scrollIndex + k1) - 1)
                isOvertakeWidth = true;
        }

        iconLocation = isOvertakeWidth ? (int)d1 : k + 12;
        paintAddButton(graphics2d);
    }

    protected void paintAddButton(Graphics2D graphics2d)
    {
        ADD_WORK_SHEET.paintIcon(this, graphics2d, iconLocation, 3);
        ADD_POLY_SHEET.paintIcon(this, graphics2d, iconLocation + ADD_WORK_SHEET.getIconWidth() + 8, 3);
    }

    private void paintSelectedTab(Graphics2D graphics2d, Icon icon, double d, double d1, String s, 
            int i, int j, boolean flag)
    {
        double ad[] = {
            d1, d1, d1 + 0.0D, (d1 + (double)widthArray[selectedIndex]) - 0.0D, d1 + (double)widthArray[selectedIndex] + 0.0D
        };
        double ad1[] = {
            0.0D, d - 0.0D, d, d, 0.0D
        };
        if(flag)
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, UIConstants.AUTHORITY_SHEET_LIGHT, 1.0F, getHeight() - 1, UIConstants.AUTHORITY_SHEET_DARK));
        else
            graphics2d.setPaint(new GradientPaint(1.0F, 1.0F, Color.white, 1.0F, getHeight() - 1, Color.white));
        GeneralPath generalpath = new GeneralPath(0, ad.length);
        generalpath.moveTo((float)ad[0], (float)ad1[0]);
        for(int k = 1; k < ad.length; k++)
            generalpath.lineTo((float)ad[k], (float)ad1[k]);

        generalpath.closePath();
        graphics2d.fill(generalpath);
        icon.paintIcon(this, graphics2d, (int)d1 + i, 2);
        graphics2d.setPaint(getForeground());
        graphics2d.drawString(s, (int)d1 + i + 14, j);
    }

    private void paintUnSelectedTab(Graphics2D graphics2d, Icon icon, double d, double d1, String s, 
            int i, int j, int k, boolean flag)
    {
        Color color = UIConstants.SHEET_NORMAL;
        int l = widthArray[k];
        double ad[] = {
            d1, d1, d1 + 0.0D, (d1 + (double)l) - 0.0D, d1 + (double)l, d1 + (double)l
        };
        double ad1[] = {
            0.0D, d - 0.0D, d, d, d - 0.0D, 0.0D
        };
        if(flag)
            graphics2d.setPaint(UIConstants.AUTHORITY_SHEET_UNSELECTED);
        else
            graphics2d.setPaint(color);
        GeneralPath generalpath = new GeneralPath(0, ad.length);
        generalpath.moveTo((float)ad[0], (float)ad1[0]);
        for(int i1 = 1; i1 < ad.length; i1++)
            generalpath.lineTo((float)ad[i1], (float)ad1[i1]);

        generalpath.closePath();
        graphics2d.fill(generalpath);
        graphics2d.setPaint(LINE_COLOR);
        double d2 = d1 <= 0.0D ? d1 : d1 - 1.0D;
        graphics2d.drawRect((int)d2, 0, l, (int)d);
        icon.paintIcon(this, graphics2d, (int)d1 + i, 2);
        graphics2d.setPaint(getForeground());
        graphics2d.drawString(s, (int)d1 + i + 14, j);
    }

    private void paintDragTab(Graphics2D graphics2d, double d, int i, int j)
    {
        graphics2d.setPaint(UIManager.getColor("TabbedPane.darkShadow"));
        Point point = (Point)lineArray.get(lineArray.size() - 1);
        int k = getPressedXY()[0];
        int l = widthArray[selectedIndex];
        int i1 = 0;
        for(int j1 = 0; j1 < selectedIndex; j1++)
            i1 += widthArray[j1];

        int k1 = k - i1;
        int ai[] = {
            (int)point.getX() - k1, (int)point.getX() - k1, ((int)point.getX() - k1) + l, ((int)point.getX() + l) - k1
        };
        int ai1[] = {
            0, (int)d, (int)d, 0
        };
        graphics2d.drawPolygon(ai, ai1, 4);
        graphics2d.setPaint(getForeground());
        graphics2d.drawString(reportComposite.getEditingWorkBook().getReportName(selectedIndex), ((int)point.getX() - k1) + i, j);
    }

    private int[] calculateWidthArray()
    {
        FontMetrics fontmetrics = GraphHelper.getFontMetrics(getFont());
        int i = fontmetrics.charWidth('M');
        WorkBook workbook = reportComposite.getEditingWorkBook();
        int j = workbook.getReportCount();
        int ai[] = new int[j];
        for(int k = 0; k < j; k++)
        {
            String s = workbook.getReportName(k);
            ai[k] = ((fontmetrics.stringWidth(s) + i * 2) - 1) + 20;
        }

        return ai;
    }

    private int[] getPressedXY()
    {
        return xyPressedCoordinate;
    }

    private void setPressedXY(int i, int j)
    {
        xyPressedCoordinate[0] = i;
        xyPressedCoordinate[1] = j;
    }

    private int[] getReleasedXY()
    {
        return xyReleasedCoordinate;
    }

    private void setReleasedXY(int i, int j)
    {
        xyReleasedCoordinate[0] = i;
        xyReleasedCoordinate[1] = j;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        int i = reportComposite.getEditingWorkBook().getReportCount();
        if(scrollIndex < 0 || scrollIndex >= i)
            return;
        reportComposite.stopEditing();
        int j = mouseevent.getX();
        int k = mouseevent.getY();
        setPressedXY(j, k);
        boolean flag = true;
        int l = 0;
        int i1 = scrollIndex;
        do
        {
            if(i1 > lastOneIndex)
                break;
            int j1 = widthArray[i1];
            if(j >= l && j < l + j1)
            {
                setSelectedIndex(i1);
                flag = false;
                reportComposite.setComposite();
                if(isAuthorityEditing)
                    doWithAuthority();
                DesignerContext.getDesignerFrame().getContentFrame().repaint();
                break;
            }
            l += j1;
            i1++;
        } while(true);
        if(SwingUtilities.isLeftMouseButton(mouseevent))
            processLeftMouseButton(j);
        if(flag)
            return;
        if(SwingUtilities.isRightMouseButton(mouseevent) && !isAuthorityEditing)
            processRightMouseButton(j, k);
    }

    private void processRightMouseButton(int i, int j)
    {
        MenuDef menudef = new MenuDef();
        addInsertGridShortCut(menudef);
        menudef.addShortCut(new ShortCut[] {
            new PolyReportInsertAction(), SeparatorDef.DEFAULT, new RemoveSheetAction(), new RenameSheetAction(), new CopySheetAction()
        });
        javax.swing.JPopupMenu jpopupmenu = menudef.createJMenu().getPopupMenu();
        menudef.updateMenu();
        GUICoreUtils.showPopupMenu(jpopupmenu, this, i - 1, j - 1);
    }

    private void processLeftMouseButton(int i)
    {
        if(i > iconLocation && i < iconLocation + 20)
            firstInsertActionPerformed();
        else
        if(i > iconLocation + 30 && i < iconLocation + 50)
            (new PolyReportInsertAction()).actionPerformed(null);
    }

    protected void addInsertGridShortCut(MenuDef menudef)
    {
        menudef.addShortCut(new ShortCut[] {
            new GridReportInsertAction()
        });
    }

    protected void firstInsertActionPerformed()
    {
        (new GridReportInsertAction()).actionPerformed(null);
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        isReleased = true;
        setReleasedXY(mouseevent.getX(), mouseevent.getY());
        int i = 0;
        int ai[] = widthArray;
        int k = ai.length;
        for(int i1 = 0; i1 < k; i1++)
        {
            int k1 = ai[i1];
            i = i + k1 + 5;
        }

        if(isAuthorityEditing)
            return;
        if(getPressedXY()[0] != getReleasedXY()[0] || getPressedXY()[1] != getReleasedXY()[1])
        {
            int j = selectedIndex;
            int l = getReleasedXY()[0] - getPressedXY()[0];
            int j1 = -l;
            if(l > widthArray[j] / 2 && getReleasedXY()[0] < i)
            {
                move2Right(l, j);
                reportComposite.fireTargetModified();
                repaint(100L);
                return;
            }
            if(j1 > widthArray[j] / 2)
            {
                move2Left(j1, j);
                reportComposite.fireTargetModified();
                repaint(100L);
                return;
            }
            setSelectedIndex(j);
            DesignerContext.getDesignerFrame().getContentFrame().repaint();
        }
        repaint(100L);
    }

    private void move2Right(int i, int j)
    {
        int k = reportComposite.getEditingWorkBook().getReportCount();
        if(selectedIndex < k - 1)
        {
            int l;
            for(; i > widthArray[j] / 2; i -= widthArray[l + 1])
            {
                l = selectedIndex;
                exchangeWorkSheet(selectedIndex, selectedIndex + 1);
                setSelectedIndex(selectedIndex + 1);
                if(l > k - 3)
                {
                    setSelectedIndex(k - 1);
                    return;
                }
            }

        } else
        {
            return;
        }
    }

    private void move2Left(int i, int j)
    {
        if(selectedIndex > 0)
        {
            int k;
            for(; i > widthArray[j] / 2; i -= widthArray[k - 1])
            {
                k = selectedIndex;
                exchangeWorkSheet(selectedIndex, selectedIndex - 1);
                setSelectedIndex(selectedIndex - 1);
                if(k < 2)
                {
                    setSelectedIndex(0);
                    return;
                }
            }

        } else
        {
            return;
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private void exchangeWorkSheet(int i, int j)
    {
        WorkBook workbook = reportComposite.getEditingWorkBook();
        TemplateReport templatereport = workbook.getTemplateReport(i);
        String s = reportComposite.getEditingWorkBook().getReportName(i);
        TemplateReport templatereport1 = workbook.getTemplateReport(j);
        String s1 = workbook.getReportName(j);
        workbook.addReport(i, s1, templatereport1);
        workbook.removeReport(i + 1);
        workbook.addReport(j, s, templatereport);
        workbook.removeReport(j + 1);
    }

    private void doCancelFormat()
    {
        boolean flag = ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane());
        boolean flag1 = false;
        if(reportComposite.centerCardPane.editingComponet instanceof PolyDesigner)
            flag1 = ((PolyDesigner)reportComposite.centerCardPane.editingComponet).containsBlocks(DesignerContext.getReferencedElementCasePane());
        boolean flag2 = flag || flag1;
        if(flag2 && selectedIndex == DesignerContext.getReferencedIndex())
        {
            DesignerContext.setFormatState(0);
            ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            repaint();
        }
    }

    static 
    {
        DESIGN_IMAGE = BaseUtils.readImage("com/fr/design/images/sheet/left_right_btn.png");
        LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 0, 14, 14);
        RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 0, 14, 14);
        DISABLED_LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 14, 14, 14);
        DISABLED_RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 14, 14, 14);
    }





















}
