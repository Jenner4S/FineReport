/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignState;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePaneAuthorityEditPane;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.FormElementCaseProvider;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.Selectedable;
import com.fr.design.selection.SelectionListener;

/**
 * ���е�ElementCase�༭���
 */
public class FormElementCaseDesigner<T extends FormElementCaseProvider, E extends ElementCasePane, S extends SelectableElement> extends TargetComponent<T> implements Selectedable<S>, FormECDesignerProvider{
	protected FormElementCasePaneDelegate elementCasePane;
	public FormElementCasePaneDelegate getEditingElementCasePane() {
		return elementCasePane;
	}
	
    public FormElementCaseDesigner(T sheet) {
        super(sheet);

        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        elementCasePane = new FormElementCasePaneDelegate((FormElementCase) sheet);
        elementCasePane.setSelection(getDefaultSelectElement());
        this.add(elementCasePane, BorderLayout.CENTER);
        elementCasePane.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
            	FormElementCaseDesigner.this.fireTargetModified();
            }
        });

    }

    @Override
    public void setTarget(T t) {
        super.setTarget(t);

        this.elementCasePane.setTarget((FormElementCase) t);
    }

    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }

    /**
     * Ȩ��ϸ��������µ��Ӳ˵�
     *
     * @return �Ӳ˵�
     */
	public ShortCut[] shortCuts4Authority() {
		return new ShortCut[]{
				new NameSeparator(Inter.getLocText(new String[]{"DashBoard-Potence", "Edit"})),
				BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
		};

	}

    /**
     * ����Ȩ��ϸ�������
     *
     * @return ����Ȩ��ϸ�������
     */
    public AuthorityEditPane createAuthorityEditPane() {
        ElementCasePaneAuthorityEditPane elementCasePaneAuthorityEditPane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementCasePaneAuthorityEditPane.populateDetials();
        return elementCasePaneAuthorityEditPane;
    }
    
    /**
	 * ��ȡ��ǰElementCase������ͼ
	 * 
	 * @param size ����ͼ�Ĵ�С
	 */
    public BufferedImage getElementCaseImage(Dimension size){
	    BufferedImage image = null;
    	try{
	        image = new java.awt.image.BufferedImage(size.width, size.height, 
	        java.awt.image.BufferedImage.TYPE_INT_RGB);
	        Graphics g = image.getGraphics();
	        
	        //����ɫ����, ��Ȼ�кڿ�
			Color oldColor = g.getColor();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(oldColor);
	        
	        this.elementCasePane.paintComponents(g);
	        
    	}catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage());
		}
    	
    	return image;
    }

    /**
     * �������ڱ༭��״̬.
     *
     * @return �������ڱ༭��״̬.
     */
    public EditingState createEditingState() {
        return this.elementCasePane.createEditingState();
    }

//////////////////////////////////////////////////////////////////////
//////////////////for toolbarMenuAdapter//////////////////////////////  
//////////////////////////////////////////////////////////////////////

    /**
     *  ����
     */
    public void copy() {
        this.elementCasePane.copy();
    }

    /**
     * ճ��
     * @return   ճ���ɹ��򷵻�true
     */
    public boolean paste() {
        return this.elementCasePane.paste();
    }

    /**
     * ����
     * @return   ճ���ɹ��򷵻�true
     */
    public boolean cut() {
        return this.elementCasePane.cut();
    }

    /**
     * ֹͣ�༭
     */
    public void stopEditing() {
        this.elementCasePane.stopEditing();
    }

    /**
     * ģ��Ĺ���
     *
     * @return ����
     */
    public ToolBarDef[] toolbars4Target() {
        return this.elementCasePane.toolbars4Target();
    }

    /**
     * ���Ĺ��߰�ť
     *
     * @return ���߰�ť
     */
    public JComponent[] toolBarButton4Form() {
        return this.elementCasePane.toolBarButton4Form();
    }

    /**
     * Ŀ��Ĳ˵�
     *
     * @return �˵�
     */
    public MenuDef[] menus4Target() {
        return this.elementCasePane.menus4Target();
    }

    /**
     * ��ȡ����
     */
    public void requestFocus() {
        super.requestFocus();
        elementCasePane.requestFocus();
    }

    public JScrollBar getHorizontalScrollBar() {
        return elementCasePane.getHorizontalScrollBar();
    }

    public JScrollBar getVerticalScrollBar() {
        return elementCasePane.getVerticalScrollBar();
    }

    public JPanel getEastUpPane() {
        return elementCasePane.getEastUpPane();
    }

    public JPanel getEastDownPane() {
        return elementCasePane.getEastDownPane();
    }


    public S getSelection() {
        return (S) elementCasePane.getSelection();
    }

    public void setSelection(S selectElement) {
        if (selectElement == null) {
            selectElement = (S) new CellSelection();
        }
        this.elementCasePane.setSelection((Selection) selectElement);
    }

    /**
     * �Ƴ�ѡ��
     */
    public void removeSelection() {
        TemplateElementCase templateElementCase = this.elementCasePane.getEditingElementCase();
        if (templateElementCase instanceof WorkSheet) {
            ((WorkSheet) templateElementCase).setPaintSelection(false);
        }
        elementCasePane.repaint();
    }

    public Selection getDefaultSelectElement() {
        TemplateElementCase tpc = this.elementCasePane.getEditingElementCase();
        CellElement cellElement = tpc.getCellElement(0, 0);
        return cellElement == null ? new CellSelection() : new CellSelection(0, 0, cellElement.getColumnSpan(), cellElement.getRowSpan());
    }

    /**
     * ���ѡ�е�SelectionListener
     *
     * @param selectionListener ѡ�е�listener
     */
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.addSelectionChangeListener(selectionListener);
    }

    /**
     * �Ƴ�ѡ�е�SelectionListener
     *
     * @param selectionListener ѡ�е�listener
     */
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        elementCasePane.removeSelectionChangeListener(selectionListener);

    }

	@Override
	public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
		return new JWorkBook();
	}

    /**
     * ������ȡ����ʽˢ
     */
	public void cancelFormat() {
		return;
	}
	
	public FormElementCase getElementCase(){
		return (FormElementCase) this.getTarget();
	}

    /**
     * ģ����Ӳ˵�
     *
     * @return �Ӳ˵�
     */
	public ShortCut[] shortcut4TemplateMenu() {
		return new ShortCut[0];
	}

	public FormElementCaseProvider getEditingElementCase(){
		return this.getEditingElementCasePane().getTarget();
	}
}
