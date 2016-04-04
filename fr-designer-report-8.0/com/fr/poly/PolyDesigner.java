// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
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
import com.fr.design.mainframe.DesignerFrame;
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
import com.fr.design.selection.SelectableElement;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.grid.Grid;
import com.fr.grid.selection.Selection;
import com.fr.poly.actions.DeleteBlockAction;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.BlockEditor;
import com.fr.poly.creator.ECBlockCreator;
import com.fr.poly.creator.ECBlockEditor;
import com.fr.poly.creator.ECBlockPane;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.poly.hanlder.DataEditingListener;
import com.fr.poly.hanlder.PolyDesignerDropTarget;
import com.fr.poly.model.AddedData;
import com.fr.poly.model.AddingData;
import com.fr.report.ReportHelper;
import com.fr.report.block.Block;
import com.fr.report.poly.AbstractPolyReport;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UNITDimension;
import com.fr.stable.unit.UnitRectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;

// Referenced classes of package com.fr.poly:
//            PolyComponetsBar, PolyDesignerLayout, PolyArea, PolyUtils, 
//            PolyBlockProperPane

public class PolyDesigner extends ReportComponent
    implements ScrollRulerComponent
{
    private class PolyDesignerEditingState
        implements EditingState
    {

        private String blockName;
        private Selection select;
        final PolyDesigner this$0;

        public void revert()
        {
            addedData = new AddedData(PolyDesigner.this);
            stopEditingState();
            initPolyBlocks();
            startEditing(blockName);
            if(selection == null)
            {
                if(BaseUtils.isAuthorityEditing())
                {
                    EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
                } else
                {
                    EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
                    QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
                }
                return;
            }
            if(selection.getEditor() instanceof ECBlockEditor)
                ((ECBlockEditor)selection.getEditor()).createEffective().setSelection(select);
        }

        public PolyDesignerEditingState(BlockCreator blockcreator)
        {
            this$0 = PolyDesigner.this;
            super();
            if(blockcreator == null)
                return;
            blockName = blockcreator.getValue().getBlockName();
            if(blockcreator.getEditor() instanceof ECBlockEditor)
                select = ((ECBlockEditor)blockcreator.getEditor()).createEffective().getSelection();
        }
    }

    public static final class SelectionType extends Enum
    {

        public static final SelectionType NONE;
        public static final SelectionType INNER;
        public static final SelectionType BLOCK;
        private static final SelectionType $VALUES[];

        public static SelectionType[] values()
        {
            return (SelectionType[])$VALUES.clone();
        }

        public static SelectionType valueOf(String s)
        {
            return (SelectionType)Enum.valueOf(com/fr/poly/PolyDesigner$SelectionType, s);
        }

        static 
        {
            NONE = new SelectionType("NONE", 0);
            INNER = new SelectionType("INNER", 1);
            BLOCK = new SelectionType("BLOCK", 2);
            $VALUES = (new SelectionType[] {
                NONE, INNER, BLOCK
            });
        }

        private SelectionType(String s, int i)
        {
            super(s, i);
        }
    }


    private SelectionType selectedtype;
    private AddingData addingData;
    private AddedData addedData;
    private DataEditingListener editingListener;
    private BlockCreator selection;
    private int horizontalValue;
    private int verticalValue;
    private transient ArrayList clip_board;
    private static final int ROTATIONS = 50;
    private JScrollBar verScrollBar;
    private JScrollBar horScrollBar;
    private JComponent polyArea;
    private PolyComponetsBar polyComponetsBar;
    private JComponent toolBarComponent[] = {
        (new CutAction(this)).createToolBarComponent(), (new CopyAction(this)).createToolBarComponent(), (new PasteAction(this)).createToolBarComponent(), (new DeleteBlockAction(this)).createToolBarComponent()
    };
    private int resolution;

    public PolyDesigner(PolyWorkSheet polyworksheet)
    {
        super(polyworksheet);
        selectedtype = SelectionType.NONE;
        horizontalValue = 0;
        verticalValue = 0;
        clip_board = new ArrayList();
        polyComponetsBar = new PolyComponetsBar();
        toolBarComponent = null;
        resolution = ScreenResolution.getScreenResolution();
        setDoubleBuffered(true);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        initInputActionMap();
        addedData = new AddedData(this);
        enableEvents(0x20038L);
        setOpaque(true);
        initComponents();
        initDataListeners();
        initPolyBlocks();
        setFocusTraversalKeysEnabled(false);
        new PolyDesignerDropTarget(this);
        addSelectionChangeListener(new SelectionListener() {

            final PolyDesigner this$0;

            public void selectionChanged(SelectionEvent selectionevent)
            {
                BlockEditor blockeditor = selection.getEditor();
                selection.getEditingElementCasePane().getEditor = selectionevent;
                addEditor(blockeditor);
                blockeditor.setBounds(selection.getBounds());
                LayoutUtils.layoutRootContainer(PolyDesigner.this);
                blockeditor.resetSelectionAndChooseState();
                blockeditor.requestFocus();
            }

            
            {
                this$0 = PolyDesigner.this;
                super();
            }
        }
);
    }

    private void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = new JPanel(new PolyDesignerLayout());
        polyArea = new PolyArea(this);
        jpanel.add("Center", polyArea);
        horScrollBar = new FormScrollBar(0, this);
        verScrollBar = new FormScrollBar(1, this);
        jpanel.add("Vertical", verScrollBar);
        jpanel.add("VRuler", new VerticalRuler(this));
        jpanel.add("HRuler", new HorizontalRuler(this));
        add(jpanel, "Center");
        add(polyComponetsBar, "West");
    }

    private void initPolyBlocks()
    {
        int i = 0;
        for(int j = ((PolyWorkSheet)getTarget()).getBlockCount(); i < j; i++)
        {
            TemplateBlock templateblock = (TemplateBlock)((PolyWorkSheet)getTarget()).getBlock(i);
            BlockCreator blockcreator = PolyUtils.createCreator(templateblock);
            LayoutUtils.layoutRootContainer(blockcreator);
            addedData.addBlockCreator(blockcreator);
        }

        repaint();
    }

    public boolean containsBlocks(TargetComponent targetcomponent)
    {
        int i = 0;
        for(int j = ((PolyWorkSheet)getTarget()).getBlockCount(); i < j; i++)
        {
            TemplateBlock templateblock = (TemplateBlock)((PolyWorkSheet)getTarget()).getBlock(i);
            if(ComparatorUtils.equals(targetcomponent.getTarget(), templateblock))
                return true;
        }

        return false;
    }

    private void initDataListeners()
    {
        polyArea.removeMouseListener(editingListener);
        polyArea.removeMouseMotionListener(editingListener);
        editingListener = new DataEditingListener(this);
        polyArea.addMouseMotionListener(editingListener);
        polyArea.addMouseListener(editingListener);
    }

    public void setTarget(PolyWorkSheet polyworksheet)
    {
        super.setTarget(polyworksheet);
        selection = null;
        addedData = new AddedData(this);
        initPolyBlocks();
        initDataListeners();
        polyArea.removeAll();
        polyArea.repaint();
    }

    public void addEditor(BlockEditor blockeditor)
    {
        polyArea.add(blockeditor);
    }

    public void removeEditor(BlockEditor blockeditor)
    {
        polyArea.remove(blockeditor);
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        if(elementCasePane == null)
        {
            return new NoSupportAuthorityEdit();
        } else
        {
            ElementCasePaneAuthorityEditPane elementcasepaneauthorityeditpane = new ElementCasePaneAuthorityEditPane(elementCasePane);
            elementcasepaneauthorityeditpane.populateDetials();
            return elementcasepaneauthorityeditpane;
        }
    }

    public void paintComponent(Graphics g)
    {
        resetEditorComponentBounds();
        g.setColor(Color.black);
        GraphHelper.drawLine(g, 0.0D, 0.0D, getWidth(), 0.0D);
        GraphHelper.drawLine(g, 0.0D, 0.0D, 0.0D, getHeight());
        super.paintComponent(g);
    }

    public void addBlockCreator(BlockCreator blockcreator)
    {
        TemplateBlock templateblock = blockcreator.getValue();
        ((PolyWorkSheet)getTarget()).addBlock(templateblock);
        addedData.addBlockCreator(blockcreator);
        repaint();
    }

    public AddingData getAddingData()
    {
        return addingData;
    }

    public void setAddingData(AddingData addingdata)
    {
        addingData = addingdata;
    }

    public AddedData getAddedData()
    {
        return addedData;
    }

    public void setAddedData(AddedData addeddata)
    {
        addedData = addeddata;
    }

    public TemplateBlock getEditingTarget()
    {
        if(selection != null)
            return selection.getValue();
        else
            return null;
    }

    public BlockCreator getSelection()
    {
        return selection;
    }

    public void setSelection(BlockCreator blockcreator)
    {
        if(blockcreator == null)
        {
            if(BaseUtils.isAuthorityEditing())
            {
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if(jtemplate.isJWorkBook())
                    jtemplate.removeParameterPaneSelection();
                noAuthorityEdit();
            }
            QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
            setChooseType(SelectionType.NONE);
            return;
        }
        if(selection != blockcreator)
        {
            selection = blockcreator;
            fireSelectionChanged();
        }
    }

    public boolean isSelectedECBolck()
    {
        return selection instanceof ECBlockCreator;
    }

    public void noAuthorityEdit()
    {
        EastRegionContainerPane.getInstance().replaceUpPane(new NoSupportAuthorityEdit());
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
    }

    private void stopEditingState()
    {
        if(selection != null)
        {
            removeEditor(selection.getEditor());
            selection = null;
        }
        repaint();
    }

    private void resetEditorComponentBounds()
    {
        if(selection != null)
        {
            selection.getEditor().setBounds(selection.getEditorBounds());
            LayoutUtils.layoutRootContainer(this);
        }
    }

    public int getHorizontalValue()
    {
        return horizontalValue;
    }

    public void setHorizontalValue(int i)
    {
        horizontalValue = i;
    }

    public int getVerticalValue()
    {
        return verticalValue;
    }

    public void setVerticalValue(int i)
    {
        verticalValue = i;
    }

    public short getRulerLengthUnit()
    {
        return DesignerEnvManager.getEnvManager().getReportLengthUnit();
    }

    public void copy()
    {
        if(selection != null)
        {
            clip_board.clear();
            clip_board.add(selection.getValue());
        }
    }

    public boolean paste()
    {
        if(!clip_board.isEmpty())
        {
            TemplateBlock templateblock = null;
            try
            {
                templateblock = (TemplateBlock)((TemplateBlock)clip_board.get(0)).clone();
                String s = templateblock.getBlockName();
                s = (new StringBuilder()).append(s).append("_copy").toString();
                ArrayList arraylist = new ArrayList();
                boolean flag = false;
                int i = ((PolyWorkSheet)getTarget()).getBlockCount();
                for(int j = 0; j < i; j++)
                {
                    if(!((PolyWorkSheet)getTarget()).getBlock(j).getBlockName().startsWith(s))
                        continue;
                    if(ComparatorUtils.equals(((PolyWorkSheet)getTarget()).getBlock(j).getBlockName(), s))
                    {
                        flag = true;
                        s = (new StringBuilder()).append(s).append("_copy").toString();
                    } else
                    {
                        arraylist.add(((PolyWorkSheet)getTarget()).getBlock(j).getBlockName());
                    }
                }

                while(flag) 
                    if(arraylist.contains(s))
                        s = (new StringBuilder()).append(s).append("_copy").toString();
                    else
                        flag = false;
                templateblock.setBlockName(s);
                clip_board.set(0, templateblock);
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                return false;
            }
            UnitRectangle unitrectangle = templateblock.getBounds();
            unitrectangle.x = unitrectangle.x.add(new OLDPIX(20F));
            unitrectangle.y = unitrectangle.y.add(new OLDPIX(20F));
            templateblock.setBounds(unitrectangle);
            BlockCreator blockcreator = PolyUtils.createCreator(templateblock);
            addBlockCreator(blockcreator);
            stopEditingState();
            setSelection(blockcreator);
            setChooseType(SelectionType.BLOCK);
            return true;
        } else
        {
            return false;
        }
    }

    public void stopAddingState()
    {
        addingData = null;
    }

    public boolean delete()
    {
        if(selection != null)
        {
            TemplateBlock templateblock = selection.getValue();
            if(DesignerContext.getFormatState() != 0)
                doCancelFormat(templateblock);
            ((PolyWorkSheet)getTarget()).removeBlock(templateblock);
            addedData.removeBlockCreator(selection);
            removeEditor(selection.getEditor());
            selection = null;
            repaint();
            requestFocus();
            return true;
        } else
        {
            return false;
        }
    }

    private void doCancelFormat(TemplateBlock templateblock)
    {
        TargetComponent targetcomponent = DesignerContext.getReferencedElementCasePane();
        TemplateBlock templateblock1 = null;
        if(targetcomponent instanceof PolyElementCasePane)
            templateblock1 = (TemplateBlock)((PolyElementCasePane)targetcomponent).getTarget();
        if(ComparatorUtils.equals(templateblock1, templateblock))
        {
            DesignerContext.setFormatState(0);
            ((PolyElementCasePane)elementCasePane).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
            ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            repaint();
            HistoryTemplateListPane.getInstance().repaint();
        }
    }

    public boolean cut()
    {
        copy();
        delete();
        return true;
    }

    public void move(int i, int j)
    {
        if(selection == null)
            return;
        TemplateBlock templateblock = selection.getValue();
        UnitRectangle unitrectangle = templateblock.getBounds();
        unitrectangle.x = FU.valueOfPix(unitrectangle.x.toPixI(resolution) + i, resolution);
        unitrectangle.y = FU.valueOfPix(unitrectangle.y.toPixI(resolution) + j, resolution);
        if(unitrectangle.x.less_than_zero())
            unitrectangle.x = UNIT.ZERO;
        if(unitrectangle.y.less_than_zero())
            unitrectangle.y = UNIT.ZERO;
        templateblock.setBounds(unitrectangle);
        fireTargetModified();
        repaint();
    }

    public void stopEditing()
    {
        if(selection != null)
        {
            TemplateBlock templateblock = selection.getValue();
            selection.setValue(templateblock);
            removeEditor(selection.getEditor());
            selection = null;
            repaint();
        }
    }

    public JScrollBar getVerticalScrollBar()
    {
        return verScrollBar;
    }

    public void setVerticalScrollBar(JScrollBar jscrollbar)
    {
        verScrollBar = jscrollbar;
    }

    public JScrollBar getHorizontalScrollBar()
    {
        return horScrollBar;
    }

    public void setHorizontalScrollBar(JScrollBar jscrollbar)
    {
        horScrollBar = jscrollbar;
    }

    public int getMinWidth()
    {
        return ReportHelper.calculateOccupiedArea((AbstractPolyReport)getTarget()).width.toPixI(resolution);
    }

    public int getMinHeight()
    {
        return ReportHelper.calculateOccupiedArea((AbstractPolyReport)getTarget()).width.toPixI(resolution);
    }

    public int getDesignerHeight()
    {
        return getHeight();
    }

    public int getDesignerWidth()
    {
        return getWidth();
    }

    protected void processMouseWheelEvent(MouseWheelEvent mousewheelevent)
    {
        int i = mousewheelevent.getID();
        switch(i)
        {
        case 507: 
            int j = mousewheelevent.getWheelRotation();
            getVerticalScrollBar().setValue(getVerticalScrollBar().getValue() + j * 50);
            break;
        }
    }

    public void startEditing(String s)
    {
        if(StringUtils.isBlank(s))
            return;
        for(int i = 0; i < addedData.getAddedCount(); i++)
        {
            BlockCreator blockcreator = addedData.getAddedAt(i);
            if(ComparatorUtils.equals(s, blockcreator.getValue().getBlockName()))
            {
                setSelection(blockcreator);
                return;
            }
        }

    }

    public EditingState createEditingState()
    {
        return new PolyDesignerEditingState(selection);
    }

    public ToolBarDef[] toolbars4Target()
    {
        return selection != null && !isChooseBlock() ? selection.toolbars4Target() : null;
    }

    public JComponent[] toolBarButton4Form()
    {
        polyComponetsBar.checkEnable();
        if(selection != null)
            selection.checkButtonEnable();
        if(selection == null || isChooseBlock())
        {
            setToolBarElemEnabled(selection != null);
            return toolBarComponent;
        } else
        {
            return selection.toolBarButton4Form();
        }
    }

    private void setToolBarElemEnabled(boolean flag)
    {
        toolBarComponent[0].setEnabled(flag);
        toolBarComponent[1].setEnabled(flag);
        toolBarComponent[3].setEnabled(flag);
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return (ShortCut[])(ShortCut[])ArrayUtils.addAll(super.shortcut4TemplateMenu(), new ShortCut[0]);
    }

    public int getMenuState()
    {
        return selection != null ? selection.getMenuState() : 1;
    }

    public MenuDef[] menus4Target()
    {
        return selection != null ? selection.menus4Target() : new MenuDef[0];
    }

    private void initInputActionMap()
    {
        InputMap inputmap = getInputMap(2);
        ActionMap actionmap = getActionMap();
        inputmap.clear();
        actionmap.clear();
        inputmap.put(KeyStroke.getKeyStroke("UP"), "moveup");
        actionmap.put("moveup", new AbstractAction() {

            final PolyDesigner this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                move(0, -1);
            }

            
            {
                this$0 = PolyDesigner.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke("DOWN"), "movedown");
        actionmap.put("movedown", new AbstractAction() {

            final PolyDesigner this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                move(0, 1);
            }

            
            {
                this$0 = PolyDesigner.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke("LEFT"), "moveleft");
        actionmap.put("moveleft", new AbstractAction() {

            final PolyDesigner this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                move(-1, 0);
            }

            
            {
                this$0 = PolyDesigner.this;
                super();
            }
        }
);
        inputmap.put(KeyStroke.getKeyStroke("RIGHT"), "moveright");
        actionmap.put("moveright", new AbstractAction() {

            final PolyDesigner this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                move(1, 0);
            }

            
            {
                this$0 = PolyDesigner.this;
                super();
            }
        }
);
    }

    public BlockCreator getDefaultSelectElement()
    {
        return null;
    }

    public SelectionType getSelectionType()
    {
        return selectedtype;
    }

    public void setChooseType(SelectionType selectiontype)
    {
        if(!ComparatorUtils.equals(selectedtype, selectiontype) || selectiontype == SelectionType.NONE)
        {
            selectedtype = selectiontype;
            JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            if(jtemplate != null)
                jtemplate.setComposite();
            DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
            if(BaseUtils.isAuthorityEditing())
                EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
            else
            if(isChooseBlock())
                EastRegionContainerPane.getInstance().replaceDownPane(PolyBlockProperPane.getInstance(this));
            else
            if(selectiontype != SelectionType.NONE)
                EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
            else
                EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
        }
    }

    public JPanel getEastUpPane()
    {
        if(selection == null || selection.getEditor() == null)
        {
            return QuickEditorRegion.getInstance();
        } else
        {
            BlockEditor blockeditor = selection.getEditor();
            blockeditor.resetSelectionAndChooseState();
            return QuickEditorRegion.getInstance();
        }
    }

    public JPanel getEastDownPane()
    {
        if(isChooseBlock())
            return PolyBlockProperPane.getInstance(this);
        if(selectedtype != SelectionType.NONE)
            return CellElementPropertyPane.getInstance();
        else
            return new JPanel();
    }

    public boolean isChooseBlock()
    {
        return selectedtype == SelectionType.BLOCK;
    }

    private void fireSelectionChanged()
    {
        fireSelectionChangeListener();
        PolyBlockProperPane.getInstance(this).refreshDockingView();
        repaint(15L);
    }

    public void addSelectionChangeListener(SelectionListener selectionlistener)
    {
        listenerList.add(com/fr/design/selection/SelectionListener, selectionlistener);
    }

    public void removeSelectionChangeListener(SelectionListener selectionlistener)
    {
        listenerList.remove(com/fr/design/selection/SelectionListener, selectionlistener);
    }

    private void fireSelectionChangeListener()
    {
        Object aobj[] = listenerList.getListenerList();
        for(int i = aobj.length - 2; i >= 0; i -= 2)
            if(aobj[i] == com/fr/design/selection/SelectionListener)
                ((SelectionListener)aobj[i + 1]).selectionChanged(new SelectionEvent(this));

    }

    public Point calculateScroll(int i, int j, int k, int l, int i1, int j1)
    {
        return new Point(k, j);
    }

    public double getAreaLocationX()
    {
        return polyArea.getLocationOnScreen().getX();
    }

    public double getAreaLocationY()
    {
        return polyArea.getLocationOnScreen().getY();
    }

    public boolean intersectsAllBlock(BlockCreator blockcreator)
    {
        return intersectsAllBlock(blockcreator.getValue());
    }

    public boolean intersectsAllBlock(TemplateBlock templateblock)
    {
        UnitRectangle unitrectangle = templateblock.getBounds();
        String s = templateblock.getBlockName();
        return intersectsAllBlock(unitrectangle, s);
    }

    public boolean intersectsAllBlock(UnitRectangle unitrectangle, String s)
    {
        PolyWorkSheet polyworksheet = (PolyWorkSheet)getTarget();
        int i = polyworksheet.getBlockCount();
        for(int j = 0; j < i; j++)
        {
            Block block = polyworksheet.getBlock(j);
            UnitRectangle unitrectangle1 = block.getBounds();
            if(!ComparatorUtils.equals(s, block.getBlockName()) && unitrectangle1.intersects(unitrectangle))
                return true;
        }

        return false;
    }

    public volatile SelectableElement getDefaultSelectElement()
    {
        return getDefaultSelectElement();
    }

    public volatile void setSelection(SelectableElement selectableelement)
    {
        setSelection((BlockCreator)selectableelement);
    }

    public volatile SelectableElement getSelection()
    {
        return getSelection();
    }

    public volatile void setTarget(Object obj)
    {
        setTarget((PolyWorkSheet)obj);
    }





}
