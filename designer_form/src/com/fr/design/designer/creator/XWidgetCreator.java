/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.*;
import java.beans.IntrospectionException;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;

/**
 * @author richer
 * @since 6.5.3 com.fr.rpt.web.ui.Widget��������
 */
public abstract class XWidgetCreator extends XCreator {

	protected static final float FULL_OPACITY = 1.0f;
	protected static final float HALF_OPACITY = 0.4f;

    public XWidgetCreator(Widget widget, Dimension initSize) {
        super(widget, initSize);
        setOpaque(false);
    }

    /**
     * ��˵��
     * @return    ��˵��
     * @throws IntrospectionException
     */
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
						.getLocText("Form-Widget_Name")),
				new CRPropertyDescriptor("enabled", this.data.getClass()).setI18NName(Inter.getLocText("FR-Designer_Enabled"))
						.setPropertyChangeListener(new PropertyChangeAdapter() {

							@Override
					public void propertyChange() {
						setEnabled(toData().isEnabled());
							}
						}),
				new CRPropertyDescriptor("visible", this.data.getClass()).setI18NName(
                        Inter.getLocText("FR-Designer_Widget-Visible")).setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						makeVisible(toData().isVisible());
					}
				}) };

	}

    /**
     * ��˵��
     * @return     ��˵��
     */
    public Widget toData() {
        return this.data;
    }

    /**
     * ����Widget������ֵ��ʼ��XCreator������ֵ
     */
    @Override
    protected void initXCreatorProperties() {
        this.setEnabled(toData().isEnabled());
    }

    /**
     * ��˵��
     */
    public void recalculateChildrenSize() {
    }


	protected void makeVisible(boolean visible) {
	}

	public class LimpidButton extends JButton {
		private String name;
		private String imagePath;
		private float opacity = 0.4f;
		public LimpidButton(String name, String imagePath, float opacity) {
			this.name = name;
			this.imagePath = imagePath;
			this.opacity = opacity;
			this.draw();
		}

		public void draw() {
			try {
				ImageIcon imageIcon =(ImageIcon) BaseUtils.readIcon(imagePath);
				Image img = imageIcon.getImage();
				MediaTracker mt = new MediaTracker(this);
				int w = 21;
				int h = 21;
				mt.addImage(img, 0);
				mt.waitForAll();

				GraphicsConfiguration gc = new JFrame().getGraphicsConfiguration(); // ����ͼ���豸
				Image image = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);//����͸������
				Graphics2D g = (Graphics2D) image.getGraphics(); //�ڻ����ϴ�������

				Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f); //ָ��͸����Ϊ��͸��90%
				g.setComposite(alpha);
				g.drawImage(img, 0, 0, this); //ע����,��image����g�������ڵĻ�����
				g.setColor(Color.black);//������ɫΪ��ɫ
				g.drawString(name, 25, 20);//д��
				g.dispose(); //�ͷ��ڴ�

				Composite alpha2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
				Image image1 = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
				g = (Graphics2D) image1.getGraphics();
				g.setComposite(alpha2);
				g.drawImage(img, 2, 2, this); //�ı�ͼ����ʼλ��,������̬Ч��
				g.setColor(Color.black);
				g.drawString(name, 25, 20);
				g.dispose();

				this.setIgnoreRepaint(true);
				this.setFocusable(false);//����û�н���
				this.setBorder(null);//���ò�����ť�߿�
				this.setContentAreaFilled(false);//���ò�����ť����
				this.setIcon(new ImageIcon(image1)); //�Ѹղ����ɵİ�͸��image���ImageIcon,������ť��ȥ
				this.setRolloverIcon(new ImageIcon(image1));
				this.setPressedIcon(new ImageIcon(image));//����ȥ��ͼ��
			} catch (Exception e) {
				FRContext.getLogger().error(e.getMessage());
			}
		}

        /**
         * ��˵��
         * @param visible       ��˵��
         */
		public void makeVisible(boolean visible) {
			this.opacity = visible ? FULL_OPACITY : HALF_OPACITY;
			this.draw();
		}
	}
	
	/**
	 * ������
	 * 
	 * @param designer �������
	 * @param creator ��ǰ���
	 * 
	 */
	public void ChangeCreatorName(FormDesigner designer,XCreator creator){
        String oldName = creator.toData().getWidgetName();
        String value = JOptionPane.showInputDialog(designer, Inter.getLocText("Form-Change_Widget_Name_Discription"), oldName);
        if(value != null) {
        	designer.renameCreator(creator, value);
        }
	}

}
