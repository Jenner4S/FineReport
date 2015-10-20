package com.fr.design.mainframe.form;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;

import com.fr.design.mainframe.BaseJForm;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-25
 * Time: ����10:42
 */
public class FormTabPane extends JComponent implements MouseListener, MouseMotionListener {

    private static final Icon WORK_SHEET_ICON = BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
    private static final Icon POLY_SHEET_ICON = BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");

    private static final int GAP = 5;       //���

    private BaseJForm form;
    private FormElementCaseContainerProvider elementCase;

    //ѡ��30�Ⱥ�60�ȵ�����Ƕȵ�x,y��Ϊ�����������������
    private double specialLocation1 = 2.5;
    private double specialLocation2 = 4.330127;

    //tab�����Է��µ�ÿ��tab��ʵ�ʿ��
    private int formTabWidth = 100;
    private int ecTabWidth = formTabWidth;
    private static final int ADD_WIDTH_BY_SHEETNAME = 20; //sheet���ֵ��ı���ͼ��߿�ľ���
    private int tabHeight = 17;


    //��ǰ��ǩҳ����ŵ����б�ǩҳ��index
    private static final int FORM_INDEX = BaseJForm.FORM_TAB;
    private static final int EC_INDEX = BaseJForm.ELEMENTCASE_TAB;
    private int mouseOveredIndex = -1;
    private int selectedIndex = -1;

    public FormTabPane(FormElementCaseContainerProvider elementCase, BaseJForm form){
        this.elementCase = elementCase;
        this.form = form;
        this.setLayout(new BorderLayout(0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBorder(null);
        this.setForeground(new Color(99, 99, 99));
    }

    public void  paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D  g2d = (Graphics2D) g;
        calculateECWidth();
        paintFormTab(g2d, 0, Inter.getLocText("Form"), POLY_SHEET_ICON);
        paintECTab(g2d, formTabWidth, elementCase.getElementCaseContainerName(), WORK_SHEET_ICON);
    }

    /**
     * ��ectab
     *
     * @param g2d
     * @param sheetName
     * @return
     */
    private void paintECTab(Graphics2D g2d,int startX, String sheetName,Icon icon) {
        double[] x = {startX, startX, startX + ecTabWidth, startX + ecTabWidth, startX};
        double[] y = {-1, tabHeight, tabHeight, -1, -1};
        if (EC_INDEX == mouseOveredIndex) {
            g2d.setPaint(new GradientPaint(1, 1, Color.WHITE, 1, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.NORMAL_BACKGROUND, 1, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        }

        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0], (float) y[0]);
        generalPath.curveTo(((float) x[0]  - specialLocation1), (y[0]  - specialLocation2), ((float) x[0] - specialLocation2), (y[0]  - specialLocation1), x[0], y[0]);

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3]);
        generalPath.curveTo(((float) x[3] + specialLocation1), ((float) y[3] - specialLocation2), ((float) x[3] + specialLocation2), ((float) y[3] - specialLocation1), (float) x[3], (float) y[3]);
        generalPath.lineTo((float) x[0], (float) y[0]);

        generalPath.closePath();
        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(UIConstants.LINE_COLOR);

        g2d.draw(new Line2D.Double(x[0], y[0], x[1], y[1]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3]));
        g2d.draw(new Line2D.Double(x[0],0,x[2],0));

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        icon.paintIcon(this, g2d, startX + GAP, 2);
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, startX + icon.getIconWidth() + GAP * 2, tabHeight - GAP);

    }

    /**
     * ��formtab
     *
     * @param g2d
     * @param startX
     * @param sheetName
     * @return
     */
    private void paintFormTab(Graphics2D g2d,  int startX, String sheetName, Icon icon) {
        double[] x = {startX, startX, startX + formTabWidth, startX + formTabWidth, startX};
        double[] y = {-1,tabHeight - 1, tabHeight - 1, -1, -1};
        if (FORM_INDEX == mouseOveredIndex) {
            g2d.setPaint(new GradientPaint(1, 1,new Color(255, 150, 0), 1, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, Color.ORANGE, 1, tabHeight - 1, UIConstants.NORMAL_BACKGROUND));
        }


        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0] , (float) y[0]);
        generalPath.curveTo(((float) x[0]  - specialLocation1), (y[0] - specialLocation2), ((float) x[0] - specialLocation2), (y[0]  - specialLocation1),x[0], y[0] );

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3] );
        generalPath.curveTo(((float) x[3]  + specialLocation1), ((float) y[3] - specialLocation2), ((float) x[3] + specialLocation2), ((float) y[3] - specialLocation1), (float) x[3], (float) y[3]);    generalPath.lineTo((float) x[0] , (float) y[0]);

        generalPath.closePath();

        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(UIConstants.LINE_COLOR);

        g2d.draw(new Line2D.Double(x[0], y[0], x[1], y[1]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3]));
        g2d.draw(new Line2D.Double(x[0],0,x[2],0));

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        icon.paintIcon(this,g2d,startX + GAP,2);
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, startX + icon.getIconWidth() + GAP * 2,tabHeight - GAP);

    }

    /**
     * �������ֺ͸������������tab�Ŀ��
     *
     * @return
     */
    private void calculateECWidth() {
        FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
        int charWidth = fm.charWidth('M');
        String ECName = elementCase.getElementCaseContainerName();
        ecTabWidth = Math.max(ecTabWidth,fm.stringWidth(ECName) + charWidth * 2 + ADD_WIDTH_BY_SHEETNAME) ;
    }


    /**
     * ��갴��
     * @param e  �¼�
     */
    public void mouseClicked(MouseEvent e) {
        selectedIndex = getTabIndex(e.getX());
        if (selectedIndex == FORM_INDEX) {
        	form.tabChanged(FORM_INDEX);
        }
        repaint();
    }

    /**
     * ��갴��
     * @param e �¼�
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * ����뿪
     * @param e   �¼�
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * ������
     * @param e   �¼�
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * ����뿪
     * @param e   �¼�
     */
    public void mouseExited(MouseEvent e) {
        mouseOveredIndex = -1;
        repaint();
    }

    /**
     * ����϶�
     * @param e   �¼�
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * ����ƶ�
     * @param e   �¼�
     */
    public void mouseMoved(MouseEvent e) {
        mouseOveredIndex = getTabIndex(e.getX());
        repaint();
    }


    /**
     *  �ж��������Tab
     * @param   evtX
     * @return    index
     */
    private int getTabIndex ( int evtX ){
        if (evtX > 0 && evtX <= formTabWidth){
            return 0;
        }
        return 1;
    }

}
