// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.base.BaseUtils;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.chart.gui:
//            ChartTypePane

public class ChartTypeButtonPane extends BasicBeanPane
    implements UIObserver
{
    private class ChartChangeButton extends UIToggleButton
    {

        private static final double DEL_WIDTH = 10D;
        private BufferedImage closeIcon;
        private boolean isMoveOn;
        private String buttonName;
        private UITextField nameField;
        final ChartTypeButtonPane this$0;

        public String getButtonName()
        {
            return buttonName;
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(52, 20);
        }

        private void paintDeleteButton(Graphics g)
        {
            Rectangle rectangle = getBounds();
            int i = (int)(rectangle.getWidth() - 10D);
            int j = 1;
            g.drawImage(closeIcon, i, j, closeIcon.getWidth(), closeIcon.getHeight(), null);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(isMoveOn && indexList.size() > 1)
                paintDeleteButton(g);
        }

        private void noSelected()
        {
            int i = 0;
            for(int j = indexList.size(); i < j; i++)
                ((ChartChangeButton)indexList.get(i)).setSelected(false);

        }

        private void checkMoveOn(boolean flag)
        {
            for(int i = 0; i < indexList.size(); i++)
                ((ChartChangeButton)indexList.get(i)).isMoveOn = false;

            isMoveOn = flag;
        }

        private Rectangle2D getRectBounds()
        {
            return getBounds();
        }

        private void deleteAButton()
        {
            if(indexList.contains(this) && indexList.size() > 1)
            {
                indexList.remove(this);
                if(isSelected())
                {
                    ((ChartChangeButton)indexList.get(0)).setSelected(true);
                    changeCollectionSelected(((ChartChangeButton)indexList.get(0)).getButtonName());
                }
                if(editingCollection != null)
                {
                    int i = editingCollection.getChartCount();
                    int j = 0;
                    do
                    {
                        if(j >= i)
                            break;
                        if(ComparatorUtils.equals(getButtonName(), editingCollection.getChartName(j)))
                        {
                            editingCollection.removeNameObject(j);
                            break;
                        }
                        j++;
                    } while(true);
                }
            }
            relayoutPane();
        }

        private void relayoutPane()
        {
            layoutPane(buttonPane);
        }

        protected MouseListener getMouseListener()
        {
            return new MouseAdapter() {

                final ChartChangeButton this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    mouseClick(mouseevent);
                    mouseOnChartTypeButtonPane = true;
                }

                public void mouseEntered(MouseEvent mouseevent)
                {
                    checkMoveOn(true);
                    mouseOnChartTypeButtonPane = true;
                }

                public void mouseExited(MouseEvent mouseevent)
                {
                    checkMoveOn(false);
                    mouseOnChartTypeButtonPane = false;
                }

                
                {
                    this$1 = ChartChangeButton.this;
                    super();
                }
            }
;
        }

        public void mouseClick(MouseEvent mouseevent)
        {
            Rectangle2D rectangle2d = getRectBounds();
            if(rectangle2d == null)
                return;
            if((double)mouseevent.getX() >= rectangle2d.getWidth() - 10D)
            {
                deleteAButton();
                fireSelectedChanged();
                return;
            }
            if(isSelected())
            {
                doWithRename();
                return;
            }
            if(isEnabled())
            {
                noSelected();
                changeCollectionSelected(getButtonName());
                setSelectedWithFireListener(true);
                fireSelectedChanged();
            }
        }

        private void doWithRename()
        {
            currentEditingEditor = nameField;
            Rectangle rectangle = getBounds();
            currentEditingEditor.setPreferredSize(new Dimension((int)rectangle.getWidth(), (int)rectangle.getHeight()));
            currentEditingEditor.setText(getButtonName());
            buttonPane.repaint();
            layoutRenamingPane(buttonPane, editingCollection.getSelectedIndex());
            currentEditingEditor.requestFocus();
        }


        public ChartChangeButton(String s)
        {
            this$0 = ChartTypeButtonPane.this;
            super(s);
            closeIcon = BaseUtils.readImageWithCache("com/fr/design/images/toolbarbtn/chartChangeClose.png");
            isMoveOn = false;
            buttonName = "";
            nameField = new UITextField();
            buttonName = s;
            setToolTipText(s);
            nameField.addActionListener(new ActionListener() {

                final ChartTypeButtonPane val$this$0;
                final ChartChangeButton this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    stopEditing();
                    populateBean(editingCollection);
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }


    private static final long serialVersionUID = 0x8f29958b887a057bL;
    private static final int B_W = 52;
    private static final int B_H = 20;
    private static final int COL_COUNT = 3;
    private UIButton addButton;
    private ArrayList indexList;
    private JPanel buttonPane;
    private ChartCollection editingCollection;
    private UIObserverListener uiobListener;
    private ChartTypePane.ComboBoxPane editChartType;
    private UITextField currentEditingEditor;
    private boolean mouseOnChartTypeButtonPane;
    private AWTEventListener awt;
    ActionListener addListener;
    MouseListener mouseListener;

    public boolean isMouseOnChartTypeButtonPane()
    {
        return mouseOnChartTypeButtonPane;
    }

    public ChartTypeButtonPane()
    {
        indexList = new ArrayList();
        uiobListener = null;
        currentEditingEditor = null;
        mouseOnChartTypeButtonPane = false;
        awt = new AWTEventListener() {

            final ChartTypeButtonPane this$0;

            public void eventDispatched(AWTEvent awtevent)
            {
                if((awtevent instanceof MouseEvent) && ((MouseEvent)awtevent).getClickCount() > 0 && currentEditingEditor != null && !ComparatorUtils.equals(awtevent.getSource(), currentEditingEditor))
                {
                    stopEditing();
                    if(awtevent.getSource() instanceof ChartChangeButton)
                        ((ChartChangeButton)awtevent.getSource()).mouseClick((MouseEvent)awtevent);
                    populateBean(editingCollection);
                }
            }

            
            {
                this$0 = ChartTypeButtonPane.this;
                super();
            }
        }
;
        addListener = new ActionListener() {

            final ChartTypeButtonPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                mouseOnChartTypeButtonPane = true;
                String s = getNewChartName();
                ChartChangeButton chartchangebutton = new ChartChangeButton(s);
                chartchangebutton.registerChangeListener(uiobListener);
                indexList.add(chartchangebutton);
                if(editingCollection != null)
                {
                    Chart achart[] = ColumnIndependentChart.columnChartTypes;
                    try
                    {
                        Chart chart = (Chart)achart[0].clone();
                        editingCollection.addNamedChart(s, chart);
                        editingCollection.addFunctionRecord(chart);
                    }
                    catch(CloneNotSupportedException clonenotsupportedexception)
                    {
                        FRLogger.getLogger().error("Error in Clone");
                    }
                }
                layoutPane(buttonPane);
            }

            
            {
                this$0 = ChartTypeButtonPane.this;
                super();
            }
        }
;
        mouseListener = new MouseAdapter() {

            final ChartTypeButtonPane this$0;

            public void mouseExited(MouseEvent mouseevent)
            {
                super.mouseExited(mouseevent);
                mouseOnChartTypeButtonPane = false;
            }

            
            {
                this$0 = ChartTypeButtonPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        buttonPane = new JPanel();
        add(buttonPane, "Center");
        JPanel jpanel = new JPanel();
        add(jpanel, "East");
        jpanel.setLayout(new BorderLayout());
        jpanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 20));
        jpanel.add(addButton, "North");
        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.addActionListener(addListener);
        addButton.addMouseListener(mouseListener);
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, 16L);
    }

    private String getNewChartName()
    {
        int i = indexList.size() + 1;
        do
        {
            String s = (new StringBuilder()).append(Inter.getLocText("FR-Chart-Module_Name")).append(i).toString();
            boolean flag = false;
            int j = 0;
            int k = indexList.size();
            do
            {
                if(j >= k)
                    break;
                ChartChangeButton chartchangebutton = (ChartChangeButton)indexList.get(j);
                if(ComparatorUtils.equals(chartchangebutton.getButtonName(), s))
                {
                    flag = true;
                    break;
                }
                j++;
            } while(true);
            if(!flag)
                return s;
            i++;
        } while(true);
    }

    private void layoutPane(JPanel jpanel)
    {
        if(jpanel == null)
            return;
        jpanel.removeAll();
        jpanel.setLayout(new BoxLayout(jpanel, 1));
        JPanel jpanel1 = null;
        for(int i = 0; i < indexList.size(); i++)
        {
            if(i % 3 == 0)
            {
                jpanel1 = new JPanel(new FlowLayout(0));
                jpanel.add(jpanel1);
            }
            jpanel1.add((Component)indexList.get(i));
        }

        revalidate();
    }

    private void layoutRenamingPane(JPanel jpanel, int i)
    {
        if(jpanel == null)
            return;
        jpanel.removeAll();
        jpanel.setLayout(new BoxLayout(jpanel, 1));
        JPanel jpanel1 = null;
        for(int j = 0; j < indexList.size(); j++)
        {
            if(j % 3 == 0)
            {
                jpanel1 = new JPanel(new FlowLayout(0));
                jpanel.add(jpanel1);
            }
            if(j != i)
                jpanel1.add((Component)indexList.get(j));
            else
                jpanel1.add(currentEditingEditor);
        }

        revalidate();
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        uiobListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Chart-Types_Switch");
    }

    private void changeCollectionSelected(String s)
    {
        if(editingCollection != null)
        {
            int i = editingCollection.getChartCount();
            int j = 0;
            do
            {
                if(j >= i)
                    break;
                if(ComparatorUtils.equals(s, editingCollection.getChartName(j)))
                {
                    editingCollection.setSelectedIndex(j);
                    break;
                }
                j++;
            } while(true);
            if(editChartType != null)
                editChartType.populateBean(editingCollection.getSelectedChart());
        }
    }

    public void setEditingChartPane(ChartTypePane.ComboBoxPane comboboxpane)
    {
        editChartType = comboboxpane;
    }

    public void populateBean(ChartCollection chartcollection)
    {
        editingCollection = chartcollection;
        indexList.clear();
        int i = chartcollection.getChartCount();
        int j = chartcollection.getSelectedIndex();
        for(int k = 0; k < i; k++)
        {
            ChartChangeButton chartchangebutton = new ChartChangeButton(chartcollection.getChartName(k));
            indexList.add(chartchangebutton);
            chartchangebutton.setSelected(k == j);
            chartchangebutton.registerChangeListener(uiobListener);
        }

        layoutPane(buttonPane);
        checkAddButtonVisible();
    }

    private void checkAddButtonVisible()
    {
        addButton.setVisible(true);
        if(editingCollection != null && editingCollection.getChartCount() == 1 && !ComparatorUtils.equals(editingCollection.getSelectedChart().getClass(), com/fr/chart/chartattr/Chart))
            addButton.setVisible(false);
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    public void update(ChartCollection chartcollection)
    {
    }

    private void stopEditing()
    {
        if(currentEditingEditor != null)
        {
            String s = currentEditingEditor.getText();
            int i = editingCollection.getSelectedIndex();
            if(!ComparatorUtils.equals(editingCollection.getChartName(i), s))
            {
                editingCollection.setChartName(i, s);
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
            }
            buttonPane.remove(currentEditingEditor);
            currentEditingEditor = null;
        }
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartCollection)obj);
    }












}
