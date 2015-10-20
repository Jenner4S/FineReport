package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JComponent;

import com.fr.base.Formula;
import com.fr.design.border.UIRoundedBorder;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Label;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-22
 * Time: ����10:40
 */

public class XBorderStyleWidgetCreator extends XWidgetCreator{
    private int cornerSize = 15;
    private int noneSize = 0;

    public XBorderStyleWidgetCreator(Widget widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    /**
	 * ����������Ӧ��widget
	 * @return ͬ��
	 */
	@Override
    public AbstractBorderStyleWidget toData() {
        return (AbstractBorderStyleWidget) data;
    }
    
     protected void initStyle() {
    	LayoutBorderStyle style = toData().getBorderStyle();
     	initBorderStyle();
        if (ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)) {
          	initTitleStyle(style);
        } else {
        	clearTitleWidget();
         }
     }

  // �߿�Ĭ��ֵ��ΪNONE,��Ȼ��scalelayout����ֻ��Ĭ�ϱ߿�Ļ᲻��ʾ�߿�
    protected void initBorderStyle() {
    	LayoutBorderStyle style = toData().getBorderStyle();
        if (style != null && style.getBorder() != Constants.LINE_NONE) {
            this.setBorder(new UIRoundedBorder(style.getBorder(), style.getColor(), style.isCorner() ? cornerSize : noneSize));
        } else {
            this.setBorder(DEFALUTBORDER);
        }
    }
    
    private void clearTitleWidget() {
    	if (acceptType(XWFitLayout.class)) {
    		return;
    	}
    	XWTitleLayout parent = (XWTitleLayout) this.getParent();
    	if (parent.getComponentCount() > 1) {
    		parent.remove(parent.getTitleCreator());
    	}
    }
    
	 /**
     * ������ʽΪ������ʽʱ����Ӧ������ϱ���
     * @param style ��ʽ
     */
    protected void initTitleStyle(LayoutBorderStyle style){
    	if (style.getTitle() == null) {
    		return;
    	}
    	XWTitleLayout parent = (XWTitleLayout) this.getParent();
    	if (parent.getComponentCount() > 1) {
    		XLabel title = (XLabel) parent.getTitleCreator();
    		Label widget = title.toData();
    		updateTitleWidgetStyle(widget, style);
    		title.initXCreatorProperties();
    		return;
    	} 
    	// ��ʼ������ؼ�
    	XLabel title = new XLabel(new Label(), new Dimension());
    	Label label =  title.toData();
    	updateTitleWidgetStyle(label, style);
    	parent.add(title, WTitleLayout.TITLE);
    	// ��ʼ������߿�
    	title.initXCreatorProperties();
    	WTitleLayout layout = parent.toData();
    	layout.updateChildBounds(layout.getBodyBoundsWidget().getBounds());
    }
    
    /**
     * ���±���ؼ����е���ʽ
     */
    private void updateTitleWidgetStyle(Label title, LayoutBorderStyle style) {
    	//����ı߿���ʽĿǰ��ȡ��Ӧ�Ŀؼ��ı߿���ʽ
    	title.setBorder(style.getBorder());
    	title.setColor(style.getColor());
    	title.setCorner(style.isCorner());
    	
    	WidgetTitle wTitle = style.getTitle();
    	//���ó�������ظ���, ��Ȼ����һ�����ֵĻ�, ����ֻ������һ��
    	title.setWidgetName(wTitle.TITLE_NAME_INDEX + this.toData().getWidgetName());
    	title.setWidgetValue(getTitleValue(wTitle));
    	title.setFont(wTitle.getFrFont());
    	title.setTextalign(wTitle.getPosition());
    	title.setBackground(wTitle.getBackground());
    }
    
    private WidgetValue getTitleValue(WidgetTitle wTitle){
    	String content = String.valueOf(wTitle.getTextObject());
    	Object vlaue = content.startsWith("=") ? new Formula(content) : content;
    	return new WidgetValue(vlaue);
    }

    @Override
    protected String getIconName() {
        return StringUtils.EMPTY;
    }
    @Override
    protected JComponent initEditor() {
        return this;
    }
    
    /**
     * �ڱ߾�
     * @return ͬ��
     */
    @Override
    public Insets getInsets() {
        PaddingMargin padding = toData().getMargin();
        if (padding == null) {
        	return new Insets(0, 0, 0, 0);
        }
		return new Insets(padding.getTop(), padding.getLeft(), padding.getBottom(), padding.getRight());
    }
    
}
