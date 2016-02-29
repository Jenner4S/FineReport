package com.fr.design.designer.creator;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.FormElementCaseEditorProcessor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.CoverReportPane;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public class XElementCase extends XBorderStyleWidgetCreator implements FormElementCaseContainerProvider{
    private UILabel imageLable;
    private JPanel coverPanel;

	public XElementCase(ElementCaseEditor widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		// 报表块初始化时要加载对应的borderStyle
		initBorderStyle();
	}
	
	/**
     * 是否支持设置标题
     * @return 是返回true
     */
    public boolean hasTitleStyle() {
		return true;
	}

    /**
     * 返回组件属性值
     * @return 返回组件属性值
     * @throws IntrospectionException 异常
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {

        CRPropertyDescriptor[] propertyTableEditor = new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass())
                        .setI18NName(Inter.getLocText("Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setRendererClass(LayoutBorderStyleRenderer.class).setI18NName(
                        Inter.getLocText("FR-Designer-Widget_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                            @Override
                            public void propertyChange() {
                            	initStyle();
                            }
                        }),
                new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                        .setRendererClass(PaddingMarginCellRenderer.class).setI18NName(Inter.getLocText("FR-Layout_Padding"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("showToolBar", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("Form-EC_toolbar"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),

                      };

        FormElementCaseEditorProcessor processor = ExtraDesignClassManager.getInstance().getPropertyTableEditor();
        if (processor == null){
            return propertyTableEditor;
        }
        PropertyDescriptor[] extraEditor = processor.createPropertyDescriptor(this.data.getClass());
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(propertyTableEditor, extraEditor);
    }

	@Override
	protected String getIconName() {
		return "text_field_16.png";
	}

    /**
     * 返回组件默认名
     * @return 组件类名(小写)
     */
    public String createDefaultName() {
        return "report";
    }

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			setBorder(DEFALUTBORDER);
            editor = new JPanel();
            editor.setBackground(null);
            editor.setLayout(null);
	        imageLable = initImageBackground();

            coverPanel = new CoverReportPane();
            coverPanel.setPreferredSize(imageLable.getPreferredSize());
            coverPanel.setBounds(imageLable.getBounds());

            editor.add(coverPanel);
            coverPanel.setVisible(false);
            editor.add(imageLable);
		}
		return editor;
	}
	
	/**
	 * 从data中获取到图片背景, 并设置到Label上
	 */
	private UILabel initImageBackground(){
		UILabel imageLable = new UILabel();
		BufferedImage image = toData().getECImage();
		setLabelBackground(image, imageLable);
        
        return imageLable;
	}
	
	/**
	 * 设置指定Label的背景
	 */
	private void setLabelBackground(Image image, UILabel imageLable){
		ImageIcon icon = new ImageIcon(image);
		imageLable.setIcon(icon);
        imageLable.setOpaque(true);
        imageLable.setLayout(null);
        imageLable.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
	}

    /**
     * 是否展现覆盖的pane
     * @param display     是否
     */
    public void  displayCoverPane(boolean display){
        coverPanel.setVisible(display);
        coverPanel.setPreferredSize(editor.getPreferredSize());
        coverPanel.setBounds(editor.getBounds());
        editor.repaint();
    }

    public JComponent getCoverPane(){
        return coverPanel;
    }

    /**
     * 初始化大小
     * @return  尺寸
     */
	public Dimension initEditorSize() {
		return new Dimension(250, 100);
	}

    /**
     * 是否是报表块
     * @return  是
     */
	public boolean isReport() {
		return true;
	}

    /**
     * 该组件是否可以拖入参数面板
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }
    
    /**
     * 返回报表块对应的widget
     * @return 返回ElementCaseEditor
     */
    public ElementCaseEditor toData() {
    	return ((ElementCaseEditor) data);
    }
    
	public FormElementCaseProvider getElementCase() {
		return toData().getElementCase();
	}
	
	public String getElementCaseContainerName() {
		return toData().getWidgetName();
	}

	public void setElementCase(FormElementCaseProvider el) {
		toData().setElementCase(el);
	}
	
	public void setBackground(BufferedImage image){
		toData().setECImage(image);
		setEditorIcon(image);
	}
	
	private void setEditorIcon(BufferedImage image){
		setLabelBackground(image, imageLable);
	}
	
	public Dimension getSize(){
		return new Dimension(this.getWidth(), this.getHeight());
	}
	
	/**
	 * 响应点击事件
	 * @param editingMouseListener 事件处理器
	 * @param e 点击事件
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		super.respondClick(editingMouseListener, e);
		switchTab(e,editingMouseListener);
	}
	
	
    private void switchTab(MouseEvent e,EditingMouseListener editingMouseListener){
    	FormDesigner designer = editingMouseListener.getDesigner();
        if (e.getClickCount() == 2 || designer.getCursor().getType() == Cursor.HAND_CURSOR){
            FormElementCaseContainerProvider component = (FormElementCaseContainerProvider) designer.getComponentAt(e);
             //切换设计器
            designer.switchTab(component);
        }
     }
     
}