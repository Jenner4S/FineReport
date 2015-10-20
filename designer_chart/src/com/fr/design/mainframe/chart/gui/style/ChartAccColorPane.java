package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectConfigManager;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;
import com.fr.design.dialog.BasicPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

/**
 * ͼ����ɫ���--32�־�ȷ��ɫѡ�����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-8-22 ����09:27:03
 */
public class ChartAccColorPane extends BasicPane implements MouseListener, UIObserver,ColorSelectable{
	private static final long serialVersionUID = 7536620547840565075L;
	private static final int WIDTH = 16;
	private static final int ROWCOUNT = 8;
	
	private Color[] colors = new Color[32];
	
	private int currentIndex = 0;
	
    private ChangeListener changeListener = null;
    private UIObserverListener uiObserverListener;
    
    private Color color = null;
	
	public ChartAccColorPane() {
		this.addMouseListener(this);
		
		Color[] values = ChartConstants.CHART_COLOR_ARRAY;
		for(int i = 0; i < values.length; i++) {
			colors[i] = values[i];
		}
		
		iniListener();
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
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.repaint();
		
		Rectangle2D bounds = this.getBounds();
		if(bounds == null) {
			return;
		}
		Paint oldPaint = g2d.getPaint();
        g2d.setPaint(new Color(240, 240, 240));
        g2d.fillRect(0, 0, (int)bounds.getWidth(), (int)bounds.getHeight());
        g2d.setPaint(oldPaint);
		
		int y  = 0;
		int x = 0;
		for(int i = 0; i < colors.length; i++) {
			Color color = colors[i];
			g2d.setColor(color != null ? color : Color.white);
			
			if(i % ROWCOUNT == 0 && i != 0) {
				y += WIDTH;
			}
			x = i % ROWCOUNT;
			g2d.fillRect(x * WIDTH, y, WIDTH, WIDTH);
		}
	}
	 
	@Override
	protected String title4PopupWindow() {
		return "";
	}
	
	private int getColorIndex(double ex, double ey) {
		int x = (int)(ex / WIDTH) % ROWCOUNT;
		int y = (int)(ey / WIDTH) % ROWCOUNT;
		
		return x + y * ROWCOUNT;
	}

    /**
     * �����
     * @param e ����¼�
     */
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(!(x > 0 && x < WIDTH * ROWCOUNT)) {
			return;
		}
		if(!(y > 0 && y < WIDTH * ROWCOUNT / 2)) {
			return;
		}
		
		int index = getColorIndex(e.getX(), e.getY());
		if(index < colors.length) {
			currentIndex = index;
			
			ColorSelectDetailPane pane = new ColorSelectDetailPane(colors[currentIndex]);
			ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, colors[currentIndex], this);
			Color choosedColor = this.getColor();
	        if (choosedColor != null) {
	        	colors[currentIndex] = choosedColor;
	        	ColorSelectConfigManager.getInstance().addToColorQueue(choosedColor);
            	ChartAccColorPane.this.stateChanged();
	        }
	        ChartAccColorPane.this.repaint();
		}
	}

    /**
     * ������
     * @param e ����¼�
     */
	public void mouseEntered(MouseEvent e) {
		
	}

    /**
     * ����뿪
     * @param e ����¼�
     */
	public void mouseExited(MouseEvent e) {
		
	}

    /**
     * ��갴ѹ
     * @param e ����¼�
     */
	public void mousePressed(MouseEvent e) {
		
	}

    /**
     * ����ͷ�
     * @param e ����¼�
     */
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void populateBean(Color[] values) {
		for(int i = 0; i < colors.length; i++) {
			if(i < values.length) {
				colors[i] = values[i];
			} else {
				colors[i] = Color.white;
			}
		}
		this.repaint();
	}
	
	public Color[] updateBean() {
		return colors;
	}
	
    /**
    * ���ݽӿ� ע���¼�.
    * @param listener �����¼�
    */
   public void registerChangeListener(UIObserverListener listener) {
       uiObserverListener = listener;
   }

   /**
    * �Ƿ���Ӧ�¼�.
    * @return boolean ��Ӧ
    */
   public boolean shouldResponseChangeListener() {
       return true;
   }
   
   /**
   * ���Ըı�ʱ, ��ӦChangeListener
   */
  public void stateChanged() {
      if (changeListener != null)  {
          changeListener.stateChanged(null);
      }
  }

  /**
   *��Ӹı��¼�.
   * @param changeListener �ı��¼�
   */
  public void addChangeListener(ChangeListener changeListener) {
      this.changeListener = changeListener;
  }

@Override
public void setColor(Color color) {
	this.color = color;
}

@Override
public Color getColor() {
	return this.color;
}

@Override
/**
 * ������
 * 
 * @param colorCell ��ɫ��Ԫ��
 */
public void colorSetted(ColorCell colorCell) {
	
}

}
