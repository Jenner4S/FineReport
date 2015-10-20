/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import java.awt.AWTEvent;
import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.DesignState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePaneAuthorityEditPane;
import com.fr.design.mainframe.FormScrollBar;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.NoSupportAuthorityEdit;
import com.fr.design.mainframe.ReportComponent;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.scrollruler.HorizontalRuler;
import com.fr.design.scrollruler.ScrollRulerComponent;
import com.fr.design.scrollruler.VerticalRuler;
import com.fr.design.selection.QuickEditor;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.Selection;
import com.fr.poly.actions.DeleteBlockAction;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.BlockEditor;
import com.fr.poly.creator.ECBlockCreator;
import com.fr.poly.creator.ECBlockEditor;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.poly.hanlder.DataEditingListener;
import com.fr.poly.hanlder.PolyDesignerDropTarget;
import com.fr.poly.model.AddedData;
import com.fr.poly.model.AddingData;
import com.fr.report.ReportHelper;
import com.fr.report.block.Block;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UnitRectangle;

/**
 * @author richer
 * @since 6.5.3 �ۺϱ������ƿ飬�������ײ����ұߵĹ������Լ���ߺ��ϱߵ����ر�ʶ��
 */
/*
 * August: 1.PolyDesignerӦ���и�SelectableElement��������ElementCasePane��selection,
 * ��ô������creator��ѡ����editor��ѡ�أ� 2.���ǵ�creator�������Ǵ��ڵģ���editor�����ڵ����creator�����ɵ�,
 * PolyDesigner��SelectableElement��creator
 * 3.����ۺϿ���ECBLOCKʱ����ô��ѡ�еĶ���Ӧ����ʲô�أ��ǾۺϿ黹������ĵ�Ԫ���������Ԫ�أ�
 */
public class PolyDesigner extends ReportComponent<PolyWorkSheet, PolyElementCasePane, BlockCreator> implements ScrollRulerComponent {
    /**
     * ѡ�е�����--1.ʲô��ûѡ��2.ѡ��һ���ۺϿ��ڲ�������3.ѡ�оۺϿ鱾ʡ
     */
    public static enum SelectionType {
        NONE, INNER, BLOCK
    }

    private SelectionType selectedtype = SelectionType.NONE;
    private AddingData addingData;
    private AddedData addedData;
    private DataEditingListener editingListener;
    private BlockCreator selection;
    // ˮƽ�ʹ�ֱ��������ֵ
    private int horizontalValue = 0;
    private int verticalValue = 0;
    private transient ArrayList<TemplateBlock> clip_board = new ArrayList<TemplateBlock>();

    // richer:������ÿ����һ�£�PolyDesignPane�ĳߴ�͸ı�ROTATIONS��ô��
    private static final int ROTATIONS = 50;
    private JScrollBar verScrollBar;
    private JScrollBar horScrollBar;
    private JComponent polyArea;
    private PolyComponetsBar polyComponetsBar = new PolyComponetsBar();
    private JComponent[] toolBarComponent = null;

    private int resolution = ScreenResolution.getScreenResolution();

    public PolyDesigner(PolyWorkSheet report) {
        super(report);
        setDoubleBuffered(true);
        // Ϊ�˴�������¼�����ҪFormDesigner�ܹ���ȡ����
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        initInputActionMap();
        this.addedData = new AddedData(this);

        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

        setOpaque(true);
        initComponents();

        this.initDataListeners();
        this.initPolyBlocks();

        this.setFocusTraversalKeysEnabled(false);
        new PolyDesignerDropTarget(this);
        toolBarComponent = new JComponent[]{new CutAction(this).createToolBarComponent(), new CopyAction(this).createToolBarComponent(), new PasteAction(this).createToolBarComponent(), new DeleteBlockAction(this).createToolBarComponent()};
        this.addSelectionChangeListener(new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                BlockEditor current_editor = selection.getEditor();
                elementCasePane = selection.getEditingElementCasePane();
                addEditor(current_editor);
                current_editor.setBounds(selection.getBounds());
                LayoutUtils.layoutRootContainer(PolyDesigner.this);
                current_editor.resetSelectionAndChooseState();
                current_editor.requestFocus();
            }
        });
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel ployareaPane = new JPanel(new PolyDesignerLayout());
        polyArea = new PolyArea(this);
        ployareaPane.add(PolyDesignerLayout.Center, polyArea);

        horScrollBar = new FormScrollBar(Adjustable.HORIZONTAL, this);
        verScrollBar = new FormScrollBar(Adjustable.VERTICAL, this);

        ployareaPane.add(PolyDesignerLayout.Vertical, verScrollBar);

        ployareaPane.add(PolyDesignerLayout.VRuler, new VerticalRuler(this));
        ployareaPane.add(PolyDesignerLayout.HRuler, new HorizontalRuler(this));
        this.add(ployareaPane, BorderLayout.CENTER);
        this.add(polyComponetsBar, BorderLayout.WEST);
    }

    private void initPolyBlocks() {
        for (int i = 0, count = this.getTarget().getBlockCount(); i < count; i++) {
            TemplateBlock block = (TemplateBlock) getTarget().getBlock(i);
            BlockCreator creator = PolyUtils.createCreator(block);
            LayoutUtils.layoutRootContainer(creator);
            addedData.addBlockCreator(creator);
        }
        repaint();
    }

    /**
     * �Ƿ��оۺͱ����
     * @param targetComponent     Ŀ�����
     * @return        ���򷵻�true
     */
    public boolean containsBlocks(TargetComponent targetComponent) {
        for (int i = 0, count = this.getTarget().getBlockCount(); i < count; i++) {
            TemplateBlock block = (TemplateBlock) getTarget().getBlock(i);
            if (ComparatorUtils.equals(targetComponent.getTarget(), block)) {
                return true;
            }
        }
        return false;
    }

    private void initDataListeners() {
        this.polyArea.removeMouseListener(editingListener);
        this.polyArea.removeMouseMotionListener(editingListener);
        this.editingListener = new DataEditingListener(this);
        this.polyArea.addMouseMotionListener(editingListener);
        this.polyArea.addMouseListener(editingListener);

    }

    @Override
    public void setTarget(PolyWorkSheet t) {
        super.setTarget(t);
        selection = null;
        this.addedData = new AddedData(this);
        this.initPolyBlocks();
        initDataListeners();
        this.polyArea.removeAll();
        polyArea.repaint();

    }

    /**
     *  �������
     * @param currentEditor  ���
     */
    public void addEditor(BlockEditor currentEditor) {
        this.polyArea.add(currentEditor);
    }

    /**
     *  �Ƴ����
     * @param currentEditor  ���
     */
    public void removeEditor(BlockEditor currentEditor) {
        this.polyArea.remove(currentEditor);
    }

    /**
     * Ȩ�ޱ༭״̬
     * @return   Ȩ�ޱ༭���
     */
    public AuthorityEditPane createAuthorityEditPane() {
        if (elementCasePane == null) {
            return new NoSupportAuthorityEdit();
        }
        ElementCasePaneAuthorityEditPane elementCasePaneAuthorityEditPane = new ElementCasePaneAuthorityEditPane(elementCasePane);
        elementCasePaneAuthorityEditPane.populateDetials();
        return elementCasePaneAuthorityEditPane;
    }


    @Override
    public void paintComponent(Graphics g) {

        resetEditorComponentBounds();
        g.setColor(Color.black);
        GraphHelper.drawLine(g, 0, 0, this.getWidth(), 0);
        GraphHelper.drawLine(g, 0, 0, 0, this.getHeight());
        super.paintComponent(g);
    }

    /**
     *  �������
     * @param creator  ���
     */
    public void addBlockCreator(BlockCreator creator) {
        TemplateBlock block = creator.getValue();
        getTarget().addBlock(block);
        addedData.addBlockCreator(creator);
        repaint();
    }

    /**
     * @return
     */
    public AddingData getAddingData() {
        return addingData;
    }

    /**
     * @param addData
     */
    public void setAddingData(AddingData addData) {
        this.addingData = addData;
    }

    /**
     * @return
     */
    public AddedData getAddedData() {
        return addedData;
    }

    /**
     * @param addedData
     */
    public void setAddedData(AddedData addedData) {
        this.addedData = addedData;
    }

    /**
     * @return
     */
    public TemplateBlock getEditingTarget() {
        if (selection != null) {
            return selection.getValue();
        }
        return null;
    }

    @Override
    /**
     *
     */
    public BlockCreator getSelection() {
        return selection;
    }

    /**
     * ѡ��һ���ۺϿ�
     */
    @Override
    /**
     * ѡ��һ���ۺϿ�
     */
    public void setSelection(BlockCreator selectElement) {
        //�ۺϿ鲻�μ�Ȩ�ޱ༭

        if (selectElement == null) {
            if (BaseUtils.isAuthorityEditing()) {
                JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jTemplate.isJWorkBook()) {
                    //��������
                    jTemplate.removeParameterPaneSelection();
                }
                noAuthorityEdit();
            }
            QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
            setChooseType(SelectionType.NONE);
            return;
        }
        if (this.selection != selectElement) {
            this.selection = selectElement;
            fireSelectionChanged();
        }
    }

    /**
     * ѡ�е��Ƿ��Ǳ����
     * @return    ���򷵻�true
     */
    public boolean isSelectedECBolck() {
        return this.selection instanceof ECBlockCreator;
    }

    /**
     * ��֧��Ȩ�ޱ༭
     */
    public void noAuthorityEdit() {
        EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
    }

    private void stopEditingState() {
        if (selection != null) {
            // �Ƴ��༭����ʱ��ȻҲҪ�˳��༭״̬
            removeEditor(selection.getEditor());
            selection = null;
        }
        repaint();
    }

    private void resetEditorComponentBounds() {
        if (selection != null) {
            selection.getEditor().setBounds(selection.getEditorBounds());
            LayoutUtils.layoutRootContainer(this);
        }
    }

    /**
     * @return
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * @param horizontalValue
     */
    public void setHorizontalValue(int horizontalValue) {
        this.horizontalValue = horizontalValue;
    }

    /**
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }


    /**
     * @param verticalValue
     */
    public void setVerticalValue(int verticalValue) {
        this.verticalValue = verticalValue;
    }

    @Override
    /**
     *
     */
    public short getRulerLengthUnit() {
        return DesignerEnvManager.getEnvManager().getReportLengthUnit();
    }

    @Override
    /**
     * ����
     */
    public void copy() {
        if (selection != null) {
            clip_board.clear();
            clip_board.add(selection.getValue());
        }
    }

    /**
     * ���
     * @return ����ɹ�����true
     */
    public boolean paste() {
        if (!clip_board.isEmpty()) {
            // ճ�������ֺͱ߽綼��Ҫ�ı�
            TemplateBlock block = null;
            try {
                block = (TemplateBlock) clip_board.get(0).clone();
                String blockName = block.getBlockName();
                blockName += "_copy";

                List<String> nameList = new ArrayList<String>();
                boolean isRepeat = false;
                for (int l = getTarget().getBlockCount(), i = 0; i < l; i++) {
                    if (getTarget().getBlock(i).getBlockName().startsWith(blockName)) {
                        if (ComparatorUtils.equals(getTarget().getBlock(i).getBlockName(), blockName)) {
                            isRepeat = true;
                            blockName += "_copy";
                            continue;
                        }
                        nameList.add(getTarget().getBlock(i).getBlockName());
                    }
                }
                while (isRepeat) {
                    if (nameList.contains(blockName)) {
                        blockName += "_copy";
                    } else {
                        isRepeat = false;
                    }
                }
                block.setBlockName(blockName);
                clip_board.set(0, block);
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().error(e.getMessage(), e);
                return false;
            }
            UnitRectangle bounds = block.getBounds();
            bounds.x = bounds.x.add(new OLDPIX(20));
            bounds.y = bounds.y.add(new OLDPIX(20));
            block.setBounds(bounds);
            BlockCreator creator = PolyUtils.createCreator(block);
            addBlockCreator(creator);
            stopEditingState();
            setSelection(creator);
            setChooseType(SelectionType.BLOCK);
            return true;
        }
        return false;
    }

    /**
     * ֹͣ�༭
     */
    public void stopAddingState() {
        this.addingData = null;
    }

    /**
     * ɾ��
     * @return ɾ���ɹ�����true
     */
    public boolean delete() {
        if (selection != null) {
            TemplateBlock block = selection.getValue();
            if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
                doCancelFormat(block);
            }
            getTarget().removeBlock(block);
            addedData.removeBlockCreator(selection);
            this.removeEditor(selection.getEditor());
            selection = null;
            this.repaint();
            this.requestFocus();
            return true;
        }
        return false;
    }

    private void doCancelFormat(TemplateBlock bolck) {
        TargetComponent casePane = DesignerContext.getReferencedElementCasePane();
        TemplateBlock referencedBlock = null;
        if (casePane instanceof PolyElementCasePane) {
            referencedBlock = ((PolyElementCasePane) casePane).getTarget();
        }
        if (ComparatorUtils.equals(referencedBlock, bolck)) {
            DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_NULL);
            elementCasePane.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
            ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            repaint();
            HistoryTemplateListPane.getInstance().repaint();
        }
    }

    /**
     * ����
     * @return ���гɹ�����true
     */
    public boolean cut() {
        copy();
        delete();
        return true;
    }

    /**
     * �ƶ�
     * @param x    ������
     * @param y    ������
     */
    public void move(int x, int y) {
        if (selection == null) {
            return;
        }
        TemplateBlock block = selection.getValue();
        UnitRectangle ub = block.getBounds();
        ub.x = FU.valueOfPix(ub.x.toPixI(resolution) + x, resolution);
        ub.y = FU.valueOfPix(ub.y.toPixI(resolution) + y, resolution);
        if (ub.x.less_than_zero()) {
            ub.x = UNIT.ZERO;
        }
        if (ub.y.less_than_zero()) {
            ub.y = UNIT.ZERO;
        }
        block.setBounds(ub);
        this.fireTargetModified();
        this.repaint();
    }

    /**
     * ֹͣ�༭
     */
    public void stopEditing() {
        if (selection != null) {
            // ��ֹͣ�༭��ʱ��Ҫ�ѱ༭����ֵ��ֵ����ʾ��
            TemplateBlock block = selection.getValue();
            selection.setValue(block);
            this.removeEditor(selection.getEditor());
            selection = null;
            this.repaint();
        }
    }

    /**
     * @return
     */
    public JScrollBar getVerticalScrollBar() {
        return this.verScrollBar;
    }

    /**
     * Sets vertical scroll bar
     */
    public void setVerticalScrollBar(JScrollBar verScrollBar) {
        this.verScrollBar = verScrollBar;
    }


    @Override
    /**
     * Gets horizontal scroll bar
     */
    public JScrollBar getHorizontalScrollBar() {
        return this.horScrollBar;
    }

    /**
     * Sets horizontal scroll bar
     */
    public void setHorizontalScrollBar(JScrollBar horScrollBar) {
        this.horScrollBar = horScrollBar;
    }

    @Override
    /**
     *
     */
    public int getMinWidth() {
        return ReportHelper.calculateOccupiedArea(getTarget()).width.toPixI(resolution);
    }

    @Override
    /**
     *
     */
    public int getMinHeight() {
        return ReportHelper.calculateOccupiedArea(getTarget()).width.toPixI(resolution);
    }

    @Override
    /**
     *
     */
    public int getDesignerHeight() {
        return this.getHeight();
    }

    @Override
    /**
     *
     */
    public int getDesignerWidth() {
        return this.getWidth();
    }

    @Override
    protected void processMouseWheelEvent(java.awt.event.MouseWheelEvent evt) {
        int id = evt.getID();
        switch (id) {
            case MouseEvent.MOUSE_WHEEL: {
                int rotations = evt.getWheelRotation();
                this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getValue() + rotations * ROTATIONS);
                break;
            }
        }
    }

    /**
     * ��ʼ�༭
     * @param blockName  �ۺϿ�����
     */
    public void startEditing(String blockName) {
        if (StringUtils.isBlank(blockName)) {
            return;
        }
        for (int i = 0; i < addedData.getAddedCount(); i++) {
            BlockCreator creator = addedData.getAddedAt(i);
            if (ComparatorUtils.equals(blockName, creator.getValue().getBlockName())) {
                this.setSelection(creator);
                return;
            }
        }
    }

    /**
     * �������ڱ༭��״̬.
     *
     * @return �������ڱ༭��״̬.
     */
    public EditingState createEditingState() {
        return new PolyDesignerEditingState(selection);
    }

    private class PolyDesignerEditingState implements EditingState {
        private String blockName;
        private Selection select;

        public PolyDesignerEditingState(BlockCreator creator) {
            if (creator == null) {
                return;
            }
            this.blockName = creator.getValue().getBlockName();

            // TODO: д���µ�Selection��ͼ�������ܶ�Ҫ
            if (creator.getEditor() instanceof ECBlockEditor) {
                select = ((ECBlockEditor) creator.getEditor()).createEffective().getSelection();
            }
        }

        @Override
        public void revert() {
            PolyDesigner.this.addedData = new AddedData(PolyDesigner.this);
            stopEditingState();
            initPolyBlocks();
            startEditing(blockName);
            if (selection == null) {
                if (BaseUtils.isAuthorityEditing()) {
                    EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
                } else {
                    EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
                    QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
                }
                return;
            }
            if (selection.getEditor() instanceof ECBlockEditor) {
                ((ECBlockEditor) selection.getEditor()).createEffective().setSelection(select);
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    /**
     * ģ��Ĺ���
     *
     * @return ����
     */
    public ToolBarDef[] toolbars4Target() {
        return selection == null || isChooseBlock() ? null : this.selection.toolbars4Target();
    }

    /**
     * ���Ĺ��߰�ť
     *
     * @return ���߰�ť
     */
    public JComponent[] toolBarButton4Form() {
        polyComponetsBar.checkEnable();
        if (selection != null) {
            selection.checkButtonEnable();
        }


        if (selection == null || isChooseBlock()) {
            setToolBarElemEnabled(selection != null);
            return toolBarComponent;
        } else {
            return this.selection.toolBarButton4Form();
        }
    }

    private void setToolBarElemEnabled(boolean isEnabled) {
        toolBarComponent[0].setEnabled(isEnabled);
        toolBarComponent[1].setEnabled(isEnabled);
        toolBarComponent[3].setEnabled(isEnabled);
    }

    /**
     * ģ����Ӳ˵�
     *
     * @return �Ӳ˵�
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return (ShortCut[]) ArrayUtils.addAll(super.shortcut4TemplateMenu(), new ShortCut[]{});
    }

    public int getMenuState() {
        return selection == null ? DesignState.POLY_SHEET : this.selection.getMenuState();
    }

    /**
     * Ŀ��Ĳ˵�
     *
     * @return �˵�
     */
    public MenuDef[] menus4Target() {
        return selection == null ? new MenuDef[0] : this.selection.menus4Target();
    }

//	@Override
//	public DockingView[] views4Target() {
//		return selection == null ? new DockingView[0] : this.selection.views4Target();
//	}

    private void initInputActionMap() {
        InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMapAncestor.clear();
        actionMap.clear();

        inputMapAncestor.put(KeyStroke.getKeyStroke("UP"), "moveup");
        actionMap.put("moveup", new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                move(0, -1);
            }
        });

        inputMapAncestor.put(KeyStroke.getKeyStroke("DOWN"), "movedown");
        actionMap.put("movedown", new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                move(0, 1);
            }
        });

        inputMapAncestor.put(KeyStroke.getKeyStroke("LEFT"), "moveleft");
        actionMap.put("moveleft", new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                move(-1, 0);
            }
        });

        inputMapAncestor.put(KeyStroke.getKeyStroke("RIGHT"), "moveright");
        actionMap.put("moveright", new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                move(1, 0);
            }
        });
    }

    @Override
    /**
     *
     */
    public BlockCreator getDefaultSelectElement() {
        return null;
    }


    /**
     * @return
     */
    public SelectionType getSelectionType() {
        return this.selectedtype;
    }

    /**
     * @param type
     */
    public void setChooseType(SelectionType type) {
        if (!ComparatorUtils.equals(selectedtype, type) || type == SelectionType.NONE) {
            selectedtype = type;
            JTemplate<?, ?> jt = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            if(jt != null){
            	jt.setComposite();
            }            
            DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
            if (BaseUtils.isAuthorityEditing()) {
                EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
            } else if (isChooseBlock()) {
                EastRegionContainerPane.getInstance().replaceDownPane(PolyBlockProperPane.getInstance(PolyDesigner.this));
            } else if (type != SelectionType.NONE) {
                EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
            } else {
                EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
            }
        }
    }


    public JPanel getEastUpPane() {
        if (selection == null || selection.getEditor() == null) {
            return QuickEditorRegion.getInstance();
        }
        //����ǽ��и���
        BlockEditor current_editor = selection.getEditor();
        current_editor.resetSelectionAndChooseState();
        return QuickEditorRegion.getInstance();
    }

    public JPanel getEastDownPane() {
        if (isChooseBlock()) {
            return PolyBlockProperPane.getInstance(PolyDesigner.this);
        } else if (selectedtype != SelectionType.NONE) {
            return CellElementPropertyPane.getInstance();
        } else {
            return new JPanel();
        }
    }

    /**
     *   �Ƿ�ѡ�оۺϿ鱾��
     * @return   ���򷵻�true
     */
    public boolean isChooseBlock() {
        return selectedtype == SelectionType.BLOCK;
    }

    /**
     * Fire gridSelection Changed
     */
    private void fireSelectionChanged() {
        this.fireSelectionChangeListener();
        PolyBlockProperPane.getInstance(this).refreshDockingView();
        this.repaint(15);
    }

    /**
     * ���ѡ�е�SelectionListener
     *
     * @param selectionListener ѡ�е�listener
     */
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        this.listenerList.add(SelectionListener.class, selectionListener);
    }

    /**
     * �Ƴ�ѡ�е�SelectionListener
     *
     * @param selectionListener ѡ�е�listener
     */
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        this.listenerList.remove(SelectionListener.class, selectionListener);
    }

    private void fireSelectionChangeListener() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SelectionListener.class) {
                ((SelectionListener) listeners[i + 1]).selectionChanged(new SelectionEvent(this));
            }
        }
    }

    /**
     * �����������ֵ��max
     * @param oldmax ֮ǰ���ֵ
     * @param max ��ǰ���ֵ
     * @param newValue ��ǰvalue
     * @param oldValue ֮ǰvalue
     * @param visi designer�Ĵ�С
     * @param orientation ����������
     * @return ������ֵ��max
     */
	public Point calculateScroll(int oldmax, int max, int newValue, int oldValue, int visi, int orientation) {
		return new Point(newValue, max);
	}
	
	/**
	 * ��ȡ��ǰ�ۺϱ����������Ͻ�x����
	 * 
	 * @return ��ǰ�ۺϱ����������Ͻ�x����
	 * 
	 */
	public double getAreaLocationX(){
		return polyArea.getLocationOnScreen().getX();
	}
	
	/**
	 * ��ȡ��ǰ�ۺϱ����������Ͻ�y����
	 * 
	 * @return ��ǰ�ۺϱ����������Ͻ�y����
	 * 
	 */
	public double getAreaLocationY(){
		return polyArea.getLocationOnScreen().getY();
	}
	
	/**
	 * ���ָ�����Ƿ��������Ŀ����ص�����
	 * 
	 * @param creator ָ���Ŀ�༭��
	 * 
	 * @return �Ƿ��������Ŀ����ص�����
	 * 
	 */
	public boolean intersectsAllBlock(BlockCreator creator){
		return intersectsAllBlock(creator.getValue());
	}
	
	/**
	 * ���ָ�����Ƿ��������Ŀ����ص�����
	 * 
	 * @param block ָ���Ŀ�
	 * 
	 * @return �Ƿ��������Ŀ����ص�����
	 * 
	 */
	public boolean intersectsAllBlock(TemplateBlock block){
		UnitRectangle rec = block.getBounds();
		String blockName = block.getBlockName();
		return intersectsAllBlock(rec, blockName);
	}
	
	/**
	 * ���ָ�����Ƿ��������Ŀ����ص�����
	 * 
	 * @param rec ָ���Ŀ�����
	 * @param blockName ָ���Ŀ�����
	 * 
	 * @return �Ƿ��������Ŀ����ص�����
	 * 
	 */
	public boolean intersectsAllBlock(UnitRectangle rec, String blockName){
		PolyWorkSheet worksheet = this.getTarget();
		int count = worksheet.getBlockCount();
		for (int i = 0; i < count; i++) {
			Block temp_block = worksheet.getBlock(i);
			UnitRectangle temp_rect = temp_block.getBounds();
			
			if(ComparatorUtils.equals(blockName, temp_block.getBlockName())){
				continue;
			}
			
			if(temp_rect.intersects(rec)){
				return true;
			}
		}
		
		return false;
	}
}
