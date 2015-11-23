package com.fr.design.designer.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.Widget;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 * form�е�ͼ��ť�����Ŀؼ�, ������ʼ��ͼ������.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-7-5 ����10:28:30
 *          ��˵��
 */
public class XChartEditor extends XBorderStyleWidgetCreator {
	private static final long serialVersionUID = -7009439442104836657L;
	//������˵��DesignerEditor<SimpleChartComponent>
	private DesignerEditor<JComponent> designerEditor;
	//	private DesignerEditor<SimpleChartComponent> designerEditor;
	//marro�����ε����ԣ���ʱ�벻���ð취
	private boolean isRefreshing = false;

	public XChartEditor(BaseChartEditor editor) {
		this(editor, new Dimension(250, 150));
	}

	public XChartEditor(BaseChartEditor editor, Dimension size) {
		super((Widget)editor, size);
	}

	@Override
	public String getIconPath() {
		return super.getIconPath();
	}


	@Override
	protected String getIconName() {
		return "Chart.png";
	}

    /**
     * �������Ĭ����
     * @return �������(Сд)
     */
    public String createDefaultName() {
        return "chart";
    }
    
    /**
     * �Ƿ�֧�����ñ���
     * @return �Ƿ���true
     */
    public boolean hasTitleStyle() {
		return true;
	}

    /**
     *  �õ�������
     * @return ������
     * @throws java.beans.IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setRendererClass(LayoutBorderStyleRenderer.class).setI18NName(
                        Inter.getLocText("Chart-Style_Name")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
                        .setPropertyChangeListener(new PropertyChangeAdapter() {

                            @Override
                            public void propertyChange() {
                            	initStyle();
                            }
                        }),
        };
    }

    /**
     * ������Ƿ��������������
     * @return ���򷵻�true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

	/**
	 * �����������Editor
	 */
	public DesignerEditor<JComponent> getDesignerEditor() {
		return designerEditor;
	}

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		initBorderStyle();
		BaseChartCollection collection = ((BaseChartEditor) data).getChartCollection();
		isRefreshing = true;
		((MiddleChartComponent) designerEditor.getEditorTarget()).populate(collection);
		isRefreshing = false;
	}

	/**
	 * ���ѡ�е�ʱ��, ˢ�½���
	 * �Ҽ� reset֮��, �����¼� populate�˷���
	 *
	 * @param jform        ��
	 * @param formDesigner �������
	 * @return �ؼ�.
	 */
	public JComponent createToolPane(final BaseJForm jform, final FormDesigner formDesigner) {
		getDesignerEditorTarget().addStopEditingListener(new PropertyChangeAdapter() {
			public void propertyChange() {
				JComponent pane = jform.getEditingPane();
				if (pane instanceof BaseChartPropertyPane) {
					((BaseChartPropertyPane) pane).setSupportCellData(true);
					((BaseChartPropertyPane) pane).populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
				}
			}
		});

		final BaseChartPropertyPane propertyPane = DesignModuleFactory.getChartWidgetPropertyPane(formDesigner);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (getDesignerEditor().getEditorTarget() != null) {
					propertyPane.setSupportCellData(true);
					propertyPane.populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
				}
			}
		});
		return (JComponent)propertyPane;
	}

	private MiddleChartComponent getDesignerEditorTarget() {
		MiddleChartComponent bcc = null;
		if (getDesignerEditor().getEditorTarget() instanceof MiddleChartComponent) {
			bcc = (MiddleChartComponent) getDesignerEditor().getEditorTarget();
		}
		return bcc;
	}

	/**
	 * ��ȾPainter
	 */
	public void paint(Graphics g) {
		super.paint(g);
		designerEditor.paintEditor(g, this.getSize());
	}

	/**
	 * ��ʼ��Editor��С.
	 *
	 * @return ���ش�С.
	 */
	public Dimension initEditorSize() {
		return new Dimension(250, 100);
	}

	@Override
	protected JComponent initEditor() {
		if (designerEditor == null) {
			final MiddleChartComponent chartComponent = DesignModuleFactory.getChartComponent(((BaseChartEditor) data).getChartCollection());
			if (chartComponent != null) {
				JComponent jChart = chartComponent;
				jChart.setBorder(BorderFactory.createLineBorder(Color.lightGray));
				designerEditor = new DesignerEditor<JComponent>(jChart);
				chartComponent.addStopEditingListener(designerEditor);
				designerEditor.addPropertyChangeListener(new PropertyChangeAdapter() {
					public void propertyChange() {
						if (!isRefreshing) {
							((BaseChartEditor) data).resetChangeChartCollection(chartComponent.update());
						}
					}
				});
			}
		}
		return null;
	}
}
