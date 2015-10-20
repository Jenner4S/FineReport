package com.fr.design.style.background.gradient;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectConfigManager;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;

/**
 * TODO:������ŵĹ���û�п��ǣ�����βֵ���󣬵��³���������ʾ���������ԭ�����Ǹ�ʵ����ȫ�Ǹ�BUG��Ҫ���ŵ����Ҳ�Ƚ��٣��͸ɴ��Ժ�Ū��
 */
public class GradientBar extends JComponent implements UIObserver,ColorSelectable{

    /**
     *
     */
    private static final long serialVersionUID = -8503629815871053585L;

    private List<SelectColorPointBtn> list = new ArrayList<SelectColorPointBtn>();
    private SelectColorPointBtn p1;
    private SelectColorPointBtn p2;

    private int index;// ѡ����p1����p2

    private final int min;// ��Сֵ
    private final int max;// ���ֵ

    private UINumberField startLabel;
    private UINumberField endLabel;

    private ChangeListener changeListener = null;
    private UIObserverListener uiObserverListener;
    
    private static final int MOUSE_OFFSET = 4;
    
    private static final int MAX_VERTICAL = 45;
    
    // ѡ�е���ɫ
    private Color color;

    public GradientBar(int minvalue, int maxvalue) {
        min = minvalue;
        max = maxvalue;

        startLabel = new UINumberField(11);
        startLabel.setValue(min);
        startLabel.getDocument().addDocumentListener(docListener);

        endLabel = new UINumberField(11);
        endLabel.setValue(max);
        endLabel.getDocument().addDocumentListener(docListener);

        this.setPreferredSize(new Dimension(max + 5, 50));

        p1 = new SelectColorPointBtn(startLabel.getValue(), 30, Color.WHITE);
        p2 = new SelectColorPointBtn(endLabel.getValue(), 30, Color.BLACK);
        list.add(p1);
        list.add(p2);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseClickListener();
        addMouseDragListener();
        iniListener();
    }

    protected void addMouseClickListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < max + MOUSE_OFFSET && e.getX() > 0) {
                    int select = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).contains(e.getX(), e.getY())) {
                            select = i;
                            break;
                        }
                    }

                    if (select >= 0) {
                        ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
                        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, GradientBar.this);
                        Color color = GradientBar.this.getColor();
                        if (color != null) {
                            ColorSelectConfigManager.getInstance().addToColorQueue(color);
                            list.get(select).setColorInner(color);
                            stateChanged();
                            GradientBar.this.repaint();
                        }
                    }
                    GradientBar.this.repaint();
                }
            }
        });
    }

    protected void addMouseDragListener() {
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).contains(e.getX(), e.getY())) {
                        index = i;
                        break;
                    }
                }

                boolean x = e.getX() <= max && e.getX() >= min;
                if (x && e.getY() < MAX_VERTICAL) {
                    list.get(index).x = e.getX();
                }

                GradientBar.this.repaint();
                startLabel.setText(Double.toString(p1.getX()));
                endLabel.setText(Double.toString(p2.getX()));
            }
        });
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    DocumentListener docListener = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
            stateChanged();
        }

        public void insertUpdate(DocumentEvent e) {
            stateChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            stateChanged();
        }
    };

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Point2D start = new Point2D.Float(4, 0);
        Point2D end = new Point2D.Float(max, 0);
        Collections.sort(list);
        Color[] c = new Color[list.size()];
        for (int i = 0; i < list.size(); i++) {
            c[i] = list.get(i).getColorInner();
        }
        float[] dist = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            dist[i] = (float) ((list.get(i).x - 4) / (max - 4));
        }
        LinearGradientPaint paint = new LinearGradientPaint(start, end, dist, c);

        g2.setPaint(paint);
        g2.fillRect(4, 0, max - 4, 30);
        g2.setColor(new Color(138, 138, 138));
        g2.drawRect(4, 0, max - 4, 30);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).paint(g2);
        }
    }

    /**
     * ״̬�ı�
     */
    public void stateChanged() {
        if (changeListener != null)  {
            changeListener.stateChanged(null);
        }
    }

    /**
     * ���Ӽ���
     *	
     * @param changeListener ����
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * �������������ť���ڳ�ʼλ�ã���Ϊtrue
     *  @return ͬ��
     */
    public boolean isOriginalPlace() {
        return startLabel.getValue() == min && endLabel.getValue() == max;
    }

    /**
     *
     * @return
     */
    public double getStartValue() {
        return startLabel.getValue();
    }

    /**
     *
     * @return
     */
    public double getEndValue() {
        return endLabel.getValue();
    }

    /**
     *
     * @param startValue
     */
    public void setStartValue(double startValue) {
        startLabel.setValue(startValue);
    }

    /**
     *
     * @param endValue
     */
    public void setEndValue(double endValue) {
        endLabel.setValue(endValue);
    }

    /**
     *
     * @return
     */
    public SelectColorPointBtn getSelectColorPointBtnP1() {
        return p1;
    }

    /**
     *
     * @return
     */
    public SelectColorPointBtn getSelectColorPointBtnP2() {
        return p2;
    }

    @Override
    /**
     * ע�����
     * @param UIObserverListener ����
     *
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    /**
     * �Ƿ���Ӧ����
     * @return ͬ��
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	/**
	 * ѡ����ɫ
	 * @param ColorCell ��ɫ��Ԫ��
	 */
	@Override
	public void colorSetted(ColorCell colorCell) {
		
	}
}
