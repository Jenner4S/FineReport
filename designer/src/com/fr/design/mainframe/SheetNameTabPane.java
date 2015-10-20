package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.main.impl.WorkBook;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;

/**
 * NameTabPane of sheets
 *
 * @editor zhou
 * @since 2012-3-26����1:45:53
 */
public class SheetNameTabPane extends JComponent implements MouseListener, MouseMotionListener {

    private static final Color LINE_COLOR = new Color(0xababab);

    private static final Icon ADD_WORK_SHEET = BaseUtils.readIcon("com/fr/base/images/oem/addworksheet.png");
    protected static final Icon ADD_POLY_SHEET = BaseUtils.readIcon("com/fr/design/images/sheet/addpolysheet.png");
    private static final Icon WORK_SHEET_ICON = BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
    private static final Icon POLY_SHEET_ICON = BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
    private static final Image DESIGN_IMAGE = BaseUtils.readImage("com/fr/design/images/sheet/left_right_btn.png");
    private static final Icon LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 0, 14, 14);
    private static final Icon RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 0, 14, 14);
    private static final Icon DISABLED_LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 14, 14, 14);
    private static final Icon DISABLED_RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 14, 14, 14);

    private static final int ICON_SEP_DISTANCE = 8;
    private static final int TOOLBAR_HEIGHT = 16;
    private static final int ADD_WIDTH_BY_SHEETNAME = 20; //sheet���ֵ��ı���ͼ��߿�ľ���
    private static final int GRID_TOSHEET_RIGHT = 20; // ���grid��ť�Ҳ��sheet����Ҳ�ľ���
    private static final int POLY_TOSHEET_LEFT = 30; // ���poly��ť����sheet����Ҳ�ľ���
    private static final int POLY_TOSHEET_RIGHT = 50; // ���poly��ť�Ҳ��sheet����Ҳ�ľ���
    private static final int SHEET_ICON_GAP = 5; // ÿ��sheetͼ��֮��ľ���

    private static final int GRAP = 12; // ��������Ӱ�ť���������Ԥ���ļ��
    private static final int LEFT_CORNOR = 0;// �����.
    private static final int RIGHT_CORNOR = 0;// �ҽ���

    /**
     * ���ƺ����ư�ť
     */
    private UIButton leftButton;
    private UIButton rightButton;
    /**
     * ��갴��ʱ���������顢���ſ�ʱ����������
     */
    private int[] xyPressedCoordinate = {0, 0};
    private int[] xyReleasedCoordinate = {0, 0};

    /**
     * ����ÿ��workSheet�Ŀ��.
     */
    private int[] widthArray;

    /**
     * �������������¼���drag������������ĵ�.
     */
    private List<Point> lineArray = new ArrayList<Point>();

    /**
     * ����Ƿ��Ѿ��ͷ�
     */
    private boolean isReleased = false;

    /**
     * �Ƿ�Խ��
     */
    private boolean isOvertakeWidth = false;

    /**
     * ����ʾ��tab����
     */
    private int showCount = 0;

    /**
     * �������ͼ���λ�á�
     */
    protected int iconLocation;

    /**
     * august�� ��sheet�����������ˣ�scrollIndex�������ˣ�������ʾ����ߵ�sheet��Index
     */
    private int scrollIndex = 0;

    /**
     * �����������ʱ�����ұߵ�sheet��index
     */
    private int lastOneIndex;

    /**
     * �༭�Ķ���ʵ��
     */
    private ReportComponentComposite reportComposite;
    
    private int selectedIndex = -1;

    private JPanel buttonPane;

    private boolean isAuthorityEditing = false;

    public SheetNameTabPane(ReportComponentComposite reportCompositeX) {
        this.reportComposite = reportCompositeX;
        this.setLayout(new BorderLayout(0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBorder(null);
        this.setForeground(new Color(99, 99, 99));
        leftButton = new UIButton(LEFT_ICON) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        leftButton.set4ToolbarButton();
        leftButton.setDisabledIcon(DISABLED_LEFT_ICON);
        rightButton = new UIButton(RIGHT_ICON) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        rightButton.set4ToolbarButton();
        rightButton.setDisabledIcon(DISABLED_RIGHT_ICON);
        buttonPane = new JPanel(new BorderLayout(3, 0));
        buttonPane.add(rightButton, BorderLayout.EAST);
        buttonPane.add(leftButton, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.EAST);
        leftButton.addActionListener(createLeftButtonActionListener());
        rightButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int reportCount = reportComposite.getEditingWorkBook().getReportCount();
                if (selectedIndex <= (reportCount - 1) && scrollIndex < reportCount - showCount) {
                    scrollIndex = Math.min((showCount == 0 ? 1 : showCount) + scrollIndex, reportCount - showCount - 1);
                    repaint();
                }
            }
        });
        
    }

    private ActionListener createLeftButtonActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int s = scrollIndex;
                if (s == lastOneIndex && s != 0) {
                    scrollIndex--;
                    lastOneIndex--;
                    repaint();
                } else {
                    while (s > lastOneIndex && showCount != 0) {
                        scrollIndex++;
                        lastOneIndex++;
                        repaint();
                    }
                    while (s < lastOneIndex && scrollIndex > 0) {
                        scrollIndex--;
                        lastOneIndex--;
                        repaint();
                    }
                }
            }
        };
    }

    /**
     * ����ѡ��index
     *
     * @param newIndex
     */
    public void setSelectedIndex(int newIndex) {
        doBeforeChange(selectedIndex);
        selectedIndex = newIndex;
        doAfterChange(newIndex);
    }

    /**
     * ��Ȩ��ϸ����״̬�£����sheet������༭sheet�ɼ����ɼ�״̬
     */
    private void doWithAuthority() {
        AuthoritySheetEditedPane sheetEditedPane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), selectedIndex);
        sheetEditedPane.populate();
        EastRegionContainerPane.getInstance().replaceUpPane(sheetEditedPane);
        EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
    }


    /**
     * /**
     * selectedIndex �ı�֮ǰ����������
     *
     * @param oldIndex
     */
    protected void doBeforeChange(int oldIndex) {
    	reportComposite.doBeforeChange(oldIndex);
    }

    /**
     * selectedIndex �ı�֮������������
     *
     * @param newIndex
     */
    protected void doAfterChange(int newIndex) {
    	reportComposite.doAfterChange(newIndex);
    }

    /**
     * �õ�ѡ���index
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * �����ק
     *
     * @param e ����¼�
     */
    public void mouseDragged(MouseEvent e) {
        if (isAuthorityEditing) {
            return;
        }
        lineArray.add(e.getPoint());
        repaint();
    }

    /**
     * ����ƶ�
     *
     * @param e ����¼�
     */
    public void mouseMoved(MouseEvent e) {
    }

    private void checkButton(boolean buttonEnabled) {
        leftButton.setEnabled(buttonEnabled);
        rightButton.setEnabled(buttonEnabled);
    }

    @Override
    /**
     * ��Tab
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        isAuthorityEditing = BaseUtils.isAuthorityEditing();
        showCount = 0;
        // ��ʼ����ЩTab.
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
        int charWidth = fm.charWidth('M');
        int textAscent = fm.getAscent();
        double textHeight = this.getSize().getHeight() - 1;

        widthArray = calculateWidthArray();
        int operationWidth = GRAP + ADD_WORK_SHEET.getIconWidth() + ICON_SEP_DISTANCE + ADD_POLY_SHEET.getIconWidth();
        double maxWidth = getWidth() - operationWidth - buttonPane.getWidth();// �����
        paintBackgroundAndLine(g2d, textHeight, maxWidth, charWidth, textAscent);
        checkButton(showCount < widthArray.length);

        // richie:��linearray��Ϊ��ʱ��˵����������϶�,���滭��������϶�������Ч��.
        if (!lineArray.isEmpty()) {
            paintDragTab(g2d, textHeight, charWidth, textAscent);
        }

        // richie:����ɿ�ʱ��drag�Ĺ켣����clear��.
        if (isReleased) {
            lineArray.clear();
        }
    }

    private void paintBackgroundAndLine(Graphics2D g2d, double textHeight, double maxWidth, int charWidth, int textAscent) {
        showCount = 0;
        int addIconlocation = 0;
        WorkBook workBook = reportComposite.getEditingWorkBook();
        int reportCount = workBook.getReportCount();
        double textX = 0;
        Icon sheeticon;
        for (int i = scrollIndex; i < reportCount; i++) {
            lastOneIndex = i;
            TemplateReport templateReport = workBook.getTemplateReport(i);
            boolean isNeedPaintedAuthority = false;
            if (isAuthorityEditing) {
                String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                isNeedPaintedAuthority = templateReport.getWorkSheetPrivilegeControl().checkInvisible(selectedRoles);
            }
            sheeticon = templateReport instanceof WorkSheet ? WORK_SHEET_ICON : POLY_SHEET_ICON;
            String sheetName = workBook.getReportName(i);
            if (i == selectedIndex) {
                paintSelectedTab(g2d, sheeticon, textHeight, textX, sheetName, charWidth, textAscent, isNeedPaintedAuthority);
            } else {
                paintUnSelectedTab(g2d, sheeticon, textHeight, textX, sheetName, charWidth, textAscent, i, isNeedPaintedAuthority);
            }
            int width = widthArray[i];
            textX += width + 1;
            addIconlocation += width;
            if (i < widthArray.length - 1 && textX + widthArray[i + 1] + 1 > maxWidth) {
                isOvertakeWidth = true;
                break;
            } else {
                showCount++;
                isOvertakeWidth = false;
            }
            int count = (int) (maxWidth) / width;

            if (i >= scrollIndex + count - 1) {
                isOvertakeWidth = true;
            }
        }

        // ���������sheetͼ��
        iconLocation = isOvertakeWidth ? (int) (maxWidth) : addIconlocation + GRAP;
        
        paintAddButton(g2d);
    }
    
    protected void paintAddButton(Graphics2D g2d){
    	ADD_WORK_SHEET.paintIcon(this, g2d, iconLocation, 3);
    	ADD_POLY_SHEET.paintIcon(this, g2d, iconLocation + ADD_WORK_SHEET.getIconWidth() + ICON_SEP_DISTANCE, 3);
    }

    /**
     * ��ѡ�е�tab
     *
     * @param g2d
     * @param sheeticon
     * @param textHeight
     * @param textX
     * @param sheetName
     * @param charWidth
     * @param textAscent
     */
    private void paintSelectedTab(Graphics2D g2d, Icon sheeticon, double textHeight, double textX, String sheetName, int charWidth, int textAscent, boolean isNeedPaintAuthority) {
        double[] x = {textX, textX, textX + LEFT_CORNOR, textX + widthArray[selectedIndex] - RIGHT_CORNOR, textX + widthArray[selectedIndex] + RIGHT_CORNOR};
        double[] y = {0, textHeight - LEFT_CORNOR, textHeight, textHeight, 0};
        if (isNeedPaintAuthority) {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.AUTHORITY_SHEET_LIGHT, 1, getHeight() - 1, UIConstants.AUTHORITY_SHEET_DARK));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, Color.white, 1, getHeight() - 1, Color.white));
        }
        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0], (float) y[0]);

        for (int index = 1; index < x.length; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }
        generalPath.closePath();
        g2d.fill(generalPath);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setPaint(UIConstants.LINE_COLOR);
//        if (selectedIndex == scrollIndex) {
//            g2d.draw(new Line2D.Double(x[0], y[0], x[1], y[1]));
//        }
//        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
//        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3]));
//        g2d.draw(new Line2D.Double(x[3], y[3], x[4], y[4]));
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        sheeticon.paintIcon(this, g2d, (int) textX + charWidth, 2);
        // peter:���ַ�
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, (int) textX + charWidth + 14, textAscent);
    }

    /**
     * ������ѡ��״̬��tab
     *
     * @param g2d
     * @param sheetIcon
     * @param textHeight
     * @param textX
     * @param sheetName
     * @param charWidth
     * @param textAscent
     * @param i
     */
    private void paintUnSelectedTab(Graphics2D g2d, Icon sheetIcon, double textHeight, double textX, String sheetName, int charWidth, int textAscent, int i, boolean isNeedPaintAuthority) {
        Color tabBackground = UIConstants.SHEET_NORMAL;
        int width = widthArray[i];
        double[] x = {textX, textX, textX + LEFT_CORNOR, textX + width - RIGHT_CORNOR, textX + width, textX + width};
        double[] y = {0, textHeight - LEFT_CORNOR, textHeight, textHeight, textHeight - RIGHT_CORNOR, 0};
        if (isNeedPaintAuthority) {
            g2d.setPaint(UIConstants.AUTHORITY_SHEET_UNSELECTED);
        } else{
            g2d.setPaint(tabBackground);
        }
        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0], (float) y[0]);

        for (int index = 1; index < x.length; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }
        generalPath.closePath();
        g2d.fill(generalPath);

        g2d.setPaint(LINE_COLOR);
//        if (i == scrollIndex) {
//            g2d.draw(new Line2D.Double(x[0], y[0], x[1], y[1]));
//        }
//        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
//        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3]));
//        g2d.draw(new Line2D.Double(x[3], y[3], x[4], y[4]));
//        g2d.draw(new Line2D.Double(x[4], y[4], x[5], y[5]));
        double startX = textX > 0 ? textX - 1 : textX;
        g2d.drawRect((int)startX, 0, width, (int)textHeight);

        sheetIcon.paintIcon(this, g2d, (int) textX + charWidth, 2);
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, (int) textX + charWidth + 14, textAscent);
    }

    /**
     * ����ק�Ĺ켣
     *
     * @param g2d
     * @param textHeight
     * @param charWidth
     * @param textAscent
     */
    private void paintDragTab(Graphics2D g2d, double textHeight, int charWidth, int textAscent) {
        g2d.setPaint(UIManager.getColor("TabbedPane.darkShadow"));
        Point lastPoint = lineArray.get(lineArray.size() - 1);

        // richie:����϶���ʼʱ��x����
        int startPointX = this.getPressedXY()[0];

        // richie����ǰѡ�е�workSheet�Ŀ��
        int width = widthArray[selectedIndex];
        int totalWidth = 0;
        // richie:��ǰѡ�е�workSheet֮ǰ������workSheets���ܿ��
        for (int i = 0; i < selectedIndex; i++) {
            totalWidth += widthArray[i];
        }

        int distance = startPointX - totalWidth;

        int[] x = {(int) lastPoint.getX() - distance, (int) lastPoint.getX() - distance, (int) lastPoint.getX() - distance + width, (int) lastPoint.getX() + width - distance};
        int[] y = {0, (int) (textHeight), (int) (textHeight), 0};
        g2d.drawPolygon(x, y, 4);
        // peter:���ַ�
        g2d.setPaint(getForeground());
        // richie���ѵ�ǰѡ�е�workSheet�����ֻ�������϶�������ͼ����.
        g2d.drawString(reportComposite.getEditingWorkBook().getReportName(selectedIndex), (int) lastPoint.getX() - distance + charWidth, textAscent);
    }

    /**
     * �������ֺ͸������������tab�Ŀ��
     *
     * @return
     */
    private int[] calculateWidthArray() {
        FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
        int charWidth = fm.charWidth('M');
        WorkBook workBook = reportComposite.getEditingWorkBook();
        int reportCount = workBook.getReportCount();
        int[] widthArray = new int[reportCount];
        for (int i = 0; i < reportCount; i++) {
            String sheetName = workBook.getReportName(i);
            widthArray[i] = fm.stringWidth(sheetName) + charWidth * 2 - 1 + ADD_WIDTH_BY_SHEETNAME;
        }
        return widthArray;
    }

    private int[] getPressedXY() {
        return this.xyPressedCoordinate;
    }

    private void setPressedXY(int x, int y) {
        this.xyPressedCoordinate[0] = x;
        this.xyPressedCoordinate[1] = y;
    }

    private int[] getReleasedXY() {
        return this.xyReleasedCoordinate;
    }

    private void setReleasedXY(int x, int y) {
        this.xyReleasedCoordinate[0] = x;
        this.xyReleasedCoordinate[1] = y;
    }

    /**
     * ������¼�
     *
     * @param e ����¼�
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * ��갴���¼�
     *
     * @param evt ����¼�
     */
    public void mousePressed(MouseEvent evt) {
        int reportcount = reportComposite.getEditingWorkBook().getReportCount();
        if (scrollIndex < 0 || scrollIndex >= reportcount) {
            return;
        }
        reportComposite.stopEditing();
        int evtX = evt.getX();
        int evtY = evt.getY();
        this.setPressedXY(evtX, evtY);
        boolean isBlank = true;
        int textX = 0;
        for (int i = scrollIndex; i <= lastOneIndex; i++) {
            int textWidth = widthArray[i];
            if (evtX >= textX && evtX < textX + textWidth) {
                setSelectedIndex(i);
                isBlank = false;
                reportComposite.setComposite();
                if (isAuthorityEditing) {
                    doWithAuthority();
                }
                DesignerContext.getDesignerFrame().getContentFrame().repaint();
                break;
            }
            textX += textWidth;
        }

        if (SwingUtilities.isLeftMouseButton(evt)) {
        	processLeftMouseButton(evtX);
        }
        if (isBlank) {
            return;
        }
        if (SwingUtilities.isRightMouseButton(evt) && !isAuthorityEditing) {
        	processRightMouseButton(evtX, evtY);
        }
    }
    
    private void processRightMouseButton(int evtX, int evtY){
        MenuDef def = new MenuDef();
        addInsertGridShortCut(def);
    	def.addShortCut(new PolyReportInsertAction(), SeparatorDef.DEFAULT, new RemoveSheetAction(), new RenameSheetAction(),
        		new CopySheetAction());
        JPopupMenu tabPop = def.createJMenu().getPopupMenu();
        def.updateMenu();
        GUICoreUtils.showPopupMenu(tabPop, this, evtX - 1, evtY - 1);
    }
    
    private void processLeftMouseButton(int evtX){
        if (evtX > iconLocation && evtX < iconLocation + GRID_TOSHEET_RIGHT) {
        	firstInsertActionPerformed();
        } else if (evtX > iconLocation + POLY_TOSHEET_LEFT && evtX < iconLocation + POLY_TOSHEET_RIGHT) {
            new PolyReportInsertAction().actionPerformed(null);
        }
    
    }
    
    protected void addInsertGridShortCut(MenuDef def){
    	def.addShortCut(new GridReportInsertAction());
    }
    
    protected void firstInsertActionPerformed(){
		new GridReportInsertAction().actionPerformed(null);
    }


    /**
     * ����ͷ��¼�
     *
     * @param e ����¼�
     */
    public void mouseReleased(MouseEvent e) {
        this.isReleased = true;
        this.setReleasedXY(e.getX(), e.getY());
        int width = 0;
        for (int w : widthArray) {
            width = width + w + SHEET_ICON_GAP;
        }
        if (isAuthorityEditing) {
            return;
        }
        if (this.getPressedXY()[0] != this.getReleasedXY()[0] || this.getPressedXY()[1] != this.getReleasedXY()[1]) {
            // ��Ϊ����Ĳ�����ʹ��selectedIndex�仯������Ҫ���������濪ʼ��selectedIndex
            int si = selectedIndex;
            // richie:workSheet�����϶�
            int moveRighttDistance = this.getReleasedXY()[0] - this.getPressedXY()[0];
            // richie:�����϶�
            int moveLeftDistance = -moveRighttDistance;
            // samuel:�϶���Χ������ȵ�һ����ƶ�,���Ҳ�Խ��
            if (moveRighttDistance > widthArray[si] / 2 && this.getReleasedXY()[0] < width) {
                move2Right(moveRighttDistance, si);
                //��ק��������
                reportComposite.fireTargetModified();
                this.repaint(100);
                return;
            } else if (moveLeftDistance > widthArray[si] / 2) {
                move2Left(moveLeftDistance, si);
                //��ק��������
                reportComposite.fireTargetModified();
                this.repaint(100);
                return;
            } else {
                setSelectedIndex(si);
                DesignerContext.getDesignerFrame().getContentFrame().repaint();
            }
        }
        this.repaint(100);
    }

    /**
     * ���ƶ�
     *
     * @param moveRighttDistance �Ҳ��ƶ�����
     * @param si �������
     */
    private void move2Right(int moveRighttDistance, int si) {
        int reportcount = reportComposite.getEditingWorkBook().getReportCount();
        if (selectedIndex < reportcount - 1) {
            while (moveRighttDistance > widthArray[si] / 2) {
                int i = selectedIndex;
                this.exchangeWorkSheet(selectedIndex, selectedIndex + 1);
                setSelectedIndex(selectedIndex + 1);
                // richie:�ϵ�Խ�����е�workSheetʱ��ֱ�Ӿ���Ϊ���һ��workSheet
                if (i > reportcount - 3) {
                    setSelectedIndex(reportcount - 1);
                    return;
                }
                moveRighttDistance -= widthArray[i + 1];
            }

        } else {
            return;
        }
    }

    /**
     * ���ƶ�
     *
     * @param moveLeftDistance ������
     * @param si �������
     */
    private void move2Left(int moveLeftDistance, int si) {
        if (selectedIndex > 0) {
            while (moveLeftDistance > widthArray[si] / 2) {
                int i = selectedIndex;
                this.exchangeWorkSheet(selectedIndex, selectedIndex - 1);
                setSelectedIndex(selectedIndex - 1);
                // richie:�ϵ�Խ�����е�workSheetʱ��ֱ�Ӿ���Ϊ��һ��workSheet
                if (i < 2) {
                    setSelectedIndex(0);
                    return;
                }
                moveLeftDistance -= widthArray[i - 1];
            }
        } else {
            return;
        }
    }

    /**
     * �������¼�
     *
     * @param e ����¼�
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * ����˳��¼�
     *
     * @param e ����¼�
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * exchange workSheet
     *
     * @param workBook
     * @param index1
     * @param index2
     * @return workBook
     */
    private void exchangeWorkSheet(int index1, int index2) {
        WorkBook workbook = reportComposite.getEditingWorkBook();
        TemplateReport workSheet1 = workbook.getTemplateReport(index1);
        String name1 = reportComposite.getEditingWorkBook().getReportName(index1);

        TemplateReport workSheet2 = workbook.getTemplateReport(index2);
        String name2 = workbook.getReportName(index2);
        workbook.addReport(index1, name2, workSheet2);
        workbook.removeReport(index1 + 1);
        workbook.addReport(index2, name1, workSheet1);
        workbook.removeReport(index2 + 1);
    }

    protected abstract class SheetInsertAction extends UpdateAction {
        SheetInsertAction() {
            this.setName(Inter.getLocText("Insert") + getTemplateReportType());
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (isAuthorityEditing) {
                return;
            }
            int insertPos = selectedIndex + 1;
            if (isOvertakeWidth) {
                scrollIndex++;
            }

            reportComposite.getEditingWorkBook().addReport(insertPos, newTemplateReport());
            setSelectedIndex(insertPos);

            // sheet���ֵĹ�ʽҲ��Ҫ����Ӧ�ı仯.
            reportComposite.setComposite();
            reportComposite.fireTargetModified();
            ReportComponent ReportComponent = reportComposite.centerCardPane.editingComponet;
            ReportComponent.setSelection(ReportComponent.getDefaultSelectElement());


            showCount = 1;
            WorkBook workBook = reportComposite.getEditingWorkBook();
            int reportCount = workBook.getReportCount();
            double textX = 0;
            for (int i = scrollIndex; i < reportCount; i++) {
                widthArray = calculateWidthArray();
                int width = widthArray[i];
                textX += width + 1;
                int operationWidth = GRAP + ADD_WORK_SHEET.getIconWidth() + ICON_SEP_DISTANCE + ADD_POLY_SHEET.getIconWidth();
                double maxWidth = getWidth() - operationWidth - buttonPane.getWidth();// �����
                if (i < widthArray.length - 1 && textX + widthArray[i + 1] + 1 > maxWidth) {
                    isOvertakeWidth = true;
                    scrollIndex++;
                    continue;
                } else {
                    showCount++;
                    isOvertakeWidth = false;
                }
            }

            if (scrollIndex > 0 && showCount + scrollIndex < reportCount) {
                scrollIndex++;
            }

            DesignerContext.getDesignerFrame().getContentFrame().repaint();
        }

        protected abstract TemplateReport newTemplateReport();

        protected abstract String getTemplateReportType();
    }

    protected class GridReportInsertAction extends SheetInsertAction {
    	
        @Override
        protected TemplateReport newTemplateReport() {
            return new WorkSheet();
        }

        @Override
        protected String getTemplateReportType() {
            return Inter.getLocText("Report");
        }
    }

    protected class PolyReportInsertAction extends SheetInsertAction {
        @Override
        protected TemplateReport newTemplateReport() {
            return new PolyWorkSheet();
        }

        @Override
        protected String getTemplateReportType() {
            return Inter.getLocText("Poly");
        }
    }

    private class RemoveSheetAction extends UpdateAction {
        RemoveSheetAction() {
            this.setName(Inter.getLocText("Remove"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isAuthorityEditing) {
                return;
            }
            int count = reportComposite.getEditingWorkBook().getReportCount();
            if (count <= 1) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(reportComposite), Inter.getLocText("At_least_one_visual_worksheet") + "��");
                return;
            }
            int returnValue = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(reportComposite), Inter.getLocText("Des-Remove_WorkSheet"), ProductConstants.APP_NAME,
                    JOptionPane.OK_CANCEL_OPTION);
            if (returnValue == JOptionPane.OK_OPTION) {
                if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
                    doCancelFormat();
                }
                reportComposite.getEditingWorkBook().removeReport(selectedIndex);
                int insertPos = selectedIndex > 0 ? selectedIndex - 1 : 0;
                setSelectedIndex(insertPos);
                final int s = scrollIndex;
                if (s < lastOneIndex && scrollIndex > 0) {
                    scrollIndex--;
                    lastOneIndex--;
                }
                reportComposite.setComposite();
                reportComposite.repaint();
                reportComposite.fireTargetModified();
            }
        }
    }


    private void doCancelFormat() {
        boolean isSameCase = ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane());
        boolean isPolyContains = false;
        if (reportComposite.centerCardPane.editingComponet instanceof PolyDesigner) {
            isPolyContains = ((PolyDesigner) reportComposite.centerCardPane.editingComponet).containsBlocks(DesignerContext.getReferencedElementCasePane());
        }
        boolean isDelPane = isSameCase || isPolyContains;

        if (isDelPane && this.selectedIndex == DesignerContext.getReferencedIndex()) {
            DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_NULL);
            ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            this.repaint();

        }
    }

    private class RenameSheetAction extends UpdateAction {
        RenameSheetAction() {
            this.setName(Inter.getLocText("Rename"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/rename.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedIndex < 0 || selectedIndex >= reportComposite.getEditingWorkBook().getReportCount()) {
                return;
            }

            String newName = JOptionPane.showInputDialog(reportComposite, Inter.getLocText("Rename") + ":", reportComposite.getEditingWorkBook().getReportName(selectedIndex));
            if (newName != null) {
                // marks���ж��Ƿ�����
                boolean isExisted = false;
                for (int i = 0; i < reportComposite.getEditingWorkBook().getReportCount(); i++) {
                    if (newName.equalsIgnoreCase(reportComposite.getEditingWorkBook().getReportName(i))) {
                        isExisted = true;
                        break;
                    }
                }
                if (!isExisted) {
                    reportComposite.getEditingWorkBook().setReportName(selectedIndex, newName);
                    reportComposite.getEditingReportComponent().fireTargetModified();
                    // sheet���ֵĹ�ʽҲ��Ҫ����Ӧ�ı仯.
                    reportComposite.repaint();
                } else {
                    JOptionPane.showMessageDialog(reportComposite, Inter.getLocText("Utils-The_Name_has_been_existed"));
                }
            }
        }
    }

    private class CopySheetAction extends UpdateAction {
        CopySheetAction() {
            this.setName(Inter.getLocText("Copy"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TemplateReport tr;
            try {
                tr = (TemplateReport) reportComposite.getEditingWorkBook().getReport(selectedIndex).clone();
            } catch (CloneNotSupportedException ex) {
                return;
            }

            // ����Ҫ���Ƶ�sheet�����
            int index = selectedIndex;
            reportComposite.getEditingWorkBook().addReport(index + 1, tr);

            String prefix = reportComposite.getEditingWorkBook().getReportName(index);
            int times = 0;
            for (int i = 0; i < reportComposite.getEditingWorkBook().getReportCount(); i++) {
                if (reportComposite.getEditingWorkBook().getReportName(i).startsWith(prefix)) {
                    times++;
                }
            }
            String suffix = "-" + Inter.getLocText("Copy") + times;
            reportComposite.getEditingWorkBook().setReportName(index + 1, prefix + suffix);
            setSelectedIndex(index + 1);
            reportComposite.validate();
            reportComposite.repaint(100);
            reportComposite.fireTargetModified();
        }
    }
}
