package com.fr.design.chart.series.PlotStyle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.dialog.BasicPane;
import com.fr.stable.ArrayUtils;

/**
 *  ͼ��ѡ��demo����. ���� ѡ�е��, ����״̬, ���Լ̳�, �ı仭������.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-10-27 ����03:50:28
 */
public class ChartSelectDemoPane extends BasicPane implements UIObserver, MouseListener {
	private static final long serialVersionUID = 7715973616632567352L;

	public boolean isPressing;

	// ����ͳһ����ĵ��״̬��. �൱��Group
	protected ChartSelectDemoPane[] demoList = new ChartSelectDemoPane[0];

	private boolean isRollOver;
	private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

	public void setDemoGroup(ChartSelectDemoPane[] demos) {
		this.demoList = demos;
	}

	@Override
	protected String title4PopupWindow() {
		return "";
	}

    /**
     * �����
     * @param e �¼�
     */
	public void mouseClicked(MouseEvent e) {
		// list�е����еĶ�Ū�ɷ�ѡ��״̬.
		if(this.isEnabled()){
            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].isRollOver = false;
                demoList[i].isPressing = false;
            }

            this.isPressing = true;

            fireStateChange();

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBackground();
                demoList[i].repaint();
            }
        }
	}

    /**
     * ע�����
     * @param l ����
     */
	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}

	private void fireStateChange() {
		for (int i = 0; i < changeListeners.size(); i++) {
			changeListeners.get(i).stateChanged(new ChangeEvent(this));
		}
	}

    /**
     * ��갴ѹ
     * @param me �¼�
     */
	public void mousePressed(MouseEvent me) {
	}

    /**
     * ���ſ�
     * @param me �¼�
     */
	public void mouseReleased(MouseEvent me) {
	}

    /**
     * ������
     * @param me �¼�
     */
	public void mouseEntered(MouseEvent me) {
		if(this.isEnabled()){
            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].isRollOver = false;
            }
            isRollOver = true;

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBackground();
                demoList[i].repaint();
            }
        }
	}

    /**
     * ����Ƴ�
     * @param me �¼�
     */
	public void mouseExited(MouseEvent me) {
		// ������pane��Χ��!
		if(this.isEnabled()){
            int x = me.getX();
            int y = me.getY();

            Dimension d = this.getPreferredSize();
            if (inDimension(d, x, y)) {
                isRollOver = true;
            } else {
                isRollOver = false;
            }

            for (int i = 0; i < ArrayUtils.getLength(demoList); i++) {
                demoList[i].checkBackground();
                demoList[i].repaint();
            }
        }
	}
	
	private boolean inDimension(Dimension d, int x, int y) {
		return x < d.getWidth() && y < d.getHeight() && x > 0 && y > 0;
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		changeListeners.add(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

    /**
     * �޸ı�����ɫ
     */
	public void checkBackground() {
		if (!isRollOver &&  !isPressing) {
			this.setBackground(null);
		} else if(isRollOver && !isPressing){
			this.setBackground(new Color(182, 217, 253));
		} else {
			this.setBackground(new Color(164, 192, 220));
		}
	}
}
