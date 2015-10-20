package com.fr.design.style.background.pattern;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.general.Background;
import com.fr.design.style.AbstractSelectBox;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-11-1 ����03:16:16
 * ��˵��
 */
public class PatternSelectBox extends AbstractSelectBox<Background> {
	private static final long serialVersionUID = -2159891804571748604L;

	private Background background = null;
	
	public PatternSelectBox(int preWidth) {
		initBox(preWidth);
	}

	@Override
	public Background getSelectObject() {
		return background;
	}

	@Override
	public JPanel initWindowPane(double preWidth) {
		PatternSelectPane selectPane = new PatternSelectPane(preWidth);
    	
		selectPane.addTextureChangeListener(new ChangeListener() {
    		
    		public void stateChanged(ChangeEvent e) {
    			PatternSelectPane source = (PatternSelectPane)e.getSource();
    			setSelectObject(source.getSelectBackground());
    			
    			hidePopupMenu();
    		}
    	});
    	
    	return selectPane;
	}

	@Override
	public void setSelectObject(Background t) {
		this.background = t;
		fireDisplayComponent(t);
	}
}
