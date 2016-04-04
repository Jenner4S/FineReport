// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.*;
import com.fr.base.headerfooter.*;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.core.ReportHF;
import com.fr.stable.unit.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.headerfooter:
//            HFContainer, HFComponent, HFAttributesEditDialog

public class HeaderFooterEditPane extends JPanel
{
    public class AdjustHeightPane extends JPanel
    {

        private UISpinner valueSpinner;
        private UILabel unitLabel;
        final HeaderFooterEditPane this$0;

        private void setUnitType(int i)
        {
            if(i == 1)
                unitLabel.setText(Inter.getLocText("Unit_CM"));
            else
            if(i == 2)
                unitLabel.setText(Inter.getLocText("PageSetup-inches"));
            else
                unitLabel.setText(Inter.getLocText("PageSetup-mm"));
            Dimension dimension = new Dimension(unitLabel.getPreferredSize().width, valueSpinner.getPreferredSize().height);
            unitLabel.setMinimumSize(dimension);
            unitLabel.setMinimumSize(dimension);
            unitLabel.setSize(dimension);
            unitLabel.setPreferredSize(dimension);
            add(unitLabel);
        }

        public UNIT getUnitValue()
        {
            short word0 = DesignerEnvManager.getEnvManager().getPageLengthUnit();
            if(word0 == 1)
                return new CM(Double.valueOf(valueSpinner.getValue()).floatValue());
            if(word0 == 2)
                return new INCH(Double.valueOf(valueSpinner.getValue()).floatValue());
            else
                return new MM(Double.valueOf(valueSpinner.getValue()).floatValue());
        }

        public void setUnitValue(UNIT unit)
        {
            short word0 = DesignerEnvManager.getEnvManager().getPageLengthUnit();
            if(word0 == 1)
                valueSpinner.setValue((int)unit.toCMValue4Scale2());
            else
            if(word0 == 2)
                valueSpinner.setValue((int)unit.toINCHValue4Scale3());
            else
                valueSpinner.setValue((int)unit.toMMValue4Scale2());
            setUnitType(word0);
        }

        public AdjustHeightPane()
        {
            this$0 = HeaderFooterEditPane.this;
            super();
            setLayout(FRGUIPaneFactory.createBoxFlowLayout());
            valueSpinner = new UISpinner(0.0D, 2147483647D, 1.0D, 0.0D);
            add(valueSpinner);
            valueSpinner.addChangeListener(new ChangeListener() {

                final HeaderFooterEditPane val$this$0;
                final AdjustHeightPane this$1;

                public void stateChanged(ChangeEvent changeevent)
                {
                    refreshPreivewPane();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            unitLabel = new UILabel();
            setUnitType(0);
        }
    }

    class HFInsertButton extends UIButton
    {

        private HFElement hfElement;
        final HeaderFooterEditPane this$0;

        private void initAttributes()
        {
            setIcon(HFComponent.getHFElementIcon(hfElement));
            setToolTipText(HFComponent.getHFELementText(hfElement));
        }

        public HFElement getHFElement()
        {
            return hfElement;
        }

        public void setHFElement(HFElement hfelement)
        {
            hfElement = hfelement;
            initAttributes();
        }

        public HFInsertButton(HFElement hfelement)
        {
            this$0 = HeaderFooterEditPane.this;
            super();
            setHFElement(hfelement);
            set4ToolbarButton();
        }
    }

    class HFPreviewPane extends JPanel
        implements Scrollable
    {

        private int borderWidth;
        private int borderHeight;
        private ReportHF reportHF;
        private int hfWidth;
        private int hfHeight;
        private int pageNumber;
        private int totalPageNumber;
        private int firstPageNumber;
        final HeaderFooterEditPane this$0;

        public void refreshReportHFPaintable(ReportHF reporthf, int i, int j, int k, int l, int i1)
        {
            reportHF = reporthf;
            hfWidth = i;
            hfHeight = j;
            pageNumber = k;
            totalPageNumber = l;
            firstPageNumber = i1;
            if(scrollPreviewPane != null)
            {
                scrollPreviewPane.validate();
                scrollPreviewPane.repaint();
                scrollPreviewPane.revalidate();
            }
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(reportHF == null)
                return;
            Graphics2D graphics2d = (Graphics2D)g;
            Dimension dimension = getSize();
            if(!isEnabled())
                graphics2d.setPaint(SystemColor.control);
            else
                graphics2d.setPaint(Color.WHITE);
            graphics2d.fill(new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), dimension.getHeight()));
            if(!isEnabled())
            {
                return;
            } else
            {
                java.awt.geom.Rectangle2D.Double double1 = new java.awt.geom.Rectangle2D.Double(Math.max(0.0D, (dimension.getWidth() - (double)hfWidth) / 2D), borderHeight, hfWidth, hfHeight);
                graphics2d.setPaint(Color.BLACK);
                GraphHelper.draw(graphics2d, new java.awt.geom.Rectangle2D.Double(double1.getX() - 1.0D, double1.getY() - 1.0D, double1.getWidth() + 1.0D, double1.getHeight() + 1.0D), 3);
                reportHF.paint(graphics2d, double1, hfWidth, pageNumber, totalPageNumber, firstPageNumber, false, ScreenResolution.getScreenResolution());
                return;
            }
        }

        public Dimension getPreferredSize()
        {
            if(reportHF == null)
                return super.getPreferredSize();
            else
                return new Dimension(borderWidth * 2 + hfWidth, borderHeight * 2 + hfHeight);
        }

        public Dimension getPreferredScrollableViewportSize()
        {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle rectangle, int i, int j)
        {
            switch(i)
            {
            case 1: // '\001'
                return rectangle.height / 10;

            case 0: // '\0'
                return rectangle.width / 10;
            }
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid orientation: ").append(i).toString());
        }

        public int getScrollableBlockIncrement(Rectangle rectangle, int i, int j)
        {
            switch(i)
            {
            case 1: // '\001'
                return rectangle.height;

            case 0: // '\0'
                return rectangle.width;
            }
            throw new IllegalArgumentException((new StringBuilder()).append("Invalid orientation: ").append(i).toString());
        }

        public boolean getScrollableTracksViewportWidth()
        {
            if(getParent() instanceof JViewport)
                return getParent().getWidth() > getPreferredSize().width;
            else
                return false;
        }

        public boolean getScrollableTracksViewportHeight()
        {
            if(getParent() instanceof JViewport)
                return getParent().getHeight() > getPreferredSize().height;
            else
                return false;
        }

        public HFPreviewPane()
        {
            this(new ReportHF(), (int)FU.getInstance(PaperSize.PAPERSIZE_A4.getWidth().toFU() - (new INCH(0.75F)).toFU() - (new INCH(0.75F)).toFU()).toPixD(ScreenResolution.getScreenResolution()), 0 * ScreenResolution.getScreenResolution(), 1, 100, 1);
        }

        public HFPreviewPane(ReportHF reporthf, int i, int j, int k, int l, int i1)
        {
            this$0 = HeaderFooterEditPane.this;
            super();
            borderWidth = 8;
            borderHeight = 20;
            reportHF = null;
            refreshReportHFPaintable(reporthf, i, j, k, l, i1);
        }
    }


    private HFPreviewPane hfPreviewPane;
    private JScrollPane scrollPreviewPane;
    private HFContainer leftHFContainer;
    private HFContainer centerHFContainer;
    private HFContainer rightHFContainer;
    private HFContainer currentHFContainer;
    private AdjustHeightPane headerUnitFieldPane;
    private AdjustHeightPane footerUnitFieldPane;
    private AdjustHeightPane headFootUnitFieldPane;
    private UICheckBox printBackgroundCheckBox;
    private Background background;
    private double hfWidth;
    private ActionListener insertActionListener;
    private MouseListener focusMouseListener;

    public HeaderFooterEditPane()
    {
        insertActionListener = new ActionListener() {

            final HeaderFooterEditPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                HFInsertButton hfinsertbutton = (HFInsertButton)actionevent.getSource();
                HFElement hfelement;
                try
                {
                    hfelement = (HFElement)hfinsertbutton.getHFElement().clone();
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    hfelement = hfinsertbutton.getHFElement();
                }
                final HFComponent newHFComponent = new HFComponent(hfelement);
                if(!hfelement.getClass().equals(com/fr/base/headerfooter/NewLineHFElement))
                {
                    final HFAttributesEditDialog hfAttributesEditDialog = new HFAttributesEditDialog();
                    hfAttributesEditDialog.populate(hfelement, true);
                    hfAttributesEditDialog.showWindow(SwingUtilities.getWindowAncestor(HeaderFooterEditPane.this), new DialogActionAdapter() {

                        final HFAttributesEditDialog val$hfAttributesEditDialog;
                        final HFComponent val$newHFComponent;
                        final _cls3 this$1;

                        public void doOk()
                        {
                            hfAttributesEditDialog.update();
                            currentHFContainer.addHFComponent(newHFComponent);
                            refreshPreivewPane();
                        }

                    
                    {
                        this$1 = _cls3.this;
                        hfAttributesEditDialog = hfattributeseditdialog;
                        newHFComponent = hfcomponent;
                        super();
                    }
                    }
).setVisible(true);
                } else
                {
                    currentHFContainer.addHFComponent(newHFComponent);
                }
                refreshPreivewPane();
            }

            
            {
                this$0 = HeaderFooterEditPane.this;
                super();
            }
        }
;
        focusMouseListener = new MouseAdapter() {

            final HeaderFooterEditPane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                Object obj = mouseevent.getSource();
                if(obj instanceof HFContainer)
                {
                    if(!((HFContainer)obj).isEnabled())
                        return;
                    ((HFContainer)obj).requestFocus();
                    leftHFContainer.setBorder(null);
                    centerHFContainer.setBorder(null);
                    rightHFContainer.setBorder(null);
                    setCurrentHFContainer((HFContainer)obj);
                }
            }

            
            {
                this$0 = HeaderFooterEditPane.this;
                super();
            }
        }
;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1, "Center");
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Preview"), null));
        hfPreviewPane = new HFPreviewPane();
        scrollPreviewPane = new JScrollPane(hfPreviewPane);
        jpanel1.add(scrollPreviewPane, "Center");
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel2, "North");
        jpanel2.add(createToolbar(), "North");
        JPanel jpanel3 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(3);
        jpanel2.add(jpanel3, "Center");
        jpanel3.setPreferredSize(new Dimension(jpanel.getPreferredSize().width, 120));
        ChangeListener changelistener = new ChangeListener() {

            final HeaderFooterEditPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                refreshPreivewPane();
            }

            
            {
                this$0 = HeaderFooterEditPane.this;
                super();
            }
        }
;
        leftHFContainer = new HFContainer();
        centerHFContainer = new HFContainer();
        rightHFContainer = new HFContainer();
        leftHFContainer.addMouseListener(focusMouseListener);
        centerHFContainer.addMouseListener(focusMouseListener);
        rightHFContainer.addMouseListener(focusMouseListener);
        leftHFContainer.setContentChangeListener(changelistener);
        centerHFContainer.setContentChangeListener(changelistener);
        rightHFContainer.setContentChangeListener(changelistener);
        jpanel3.add(createContainerSection((new StringBuilder()).append(Inter.getLocText("HF-Left_Section")).append(":").toString(), leftHFContainer));
        jpanel3.add(createContainerSection((new StringBuilder()).append(Inter.getLocText("HF-Center_Section")).append(":").toString(), centerHFContainer));
        jpanel3.add(createContainerSection((new StringBuilder()).append(Inter.getLocText("HF-Right_Section")).append(":").toString(), rightHFContainer));
        setCurrentHFContainer(leftHFContainer);
    }

    private JToolBar createToolbar()
    {
        JToolBar jtoolbar = new JToolBar();
        jtoolbar.setFloatable(false);
        Dimension dimension = new Dimension(6, 18);
        jtoolbar.add(createHFInsertButton(new TextHFElement()));
        jtoolbar.add(createHFInsertButton(new FormulaHFElement()));
        jtoolbar.addSeparator(dimension);
        jtoolbar.add(createHFInsertButton(new PageNumberHFElement()));
        jtoolbar.add(createHFInsertButton(new NumberOfPageHFElement()));
        jtoolbar.addSeparator(dimension);
        jtoolbar.add(createHFInsertButton(new DateHFElement()));
        jtoolbar.add(createHFInsertButton(new TimeHFElement()));
        jtoolbar.addSeparator(dimension);
        jtoolbar.add(createHFInsertButton(new ImageHFElement()));
        jtoolbar.add(createHFInsertButton(new NewLineHFElement()));
        jtoolbar.addSeparator(dimension);
        UIButton uibutton = new UIButton();
        uibutton.setToolTipText(Inter.getLocText("Background"));
        uibutton.set4ToolbarButton();
        uibutton.setIcon(BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/background.png"));
        jtoolbar.add(uibutton);
        uibutton.addActionListener(new ActionListener() {

            final HeaderFooterEditPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final BackgroundPane backgroundPane = new BackgroundPane();
                BasicDialog basicdialog = backgroundPane.showWindow(SwingUtilities.getWindowAncestor(HeaderFooterEditPane.this));
                backgroundPane.populate(background);
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final BackgroundPane val$backgroundPane;
                    final _cls2 this$1;

                    public void doOk()
                    {
                        background = backgroundPane.update();
                        refreshPreivewPane();
                    }

                    
                    {
                        this$1 = _cls2.this;
                        backgroundPane = backgroundpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = HeaderFooterEditPane.this;
                super();
            }
        }
);
        printBackgroundCheckBox = new UICheckBox(Inter.getLocText("ReportGUI-Print_Background"));
        jtoolbar.add(printBackgroundCheckBox);
        jtoolbar.addSeparator(dimension);
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        headerUnitFieldPane = new AdjustHeightPane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-Header")).append(":").toString()));
        jpanel.add(headerUnitFieldPane);
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        footerUnitFieldPane = new AdjustHeightPane();
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-Footer")).append(":").toString()));
        jpanel1.add(footerUnitFieldPane);
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        headFootUnitFieldPane = new AdjustHeightPane();
        jpanel2.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Height")).append(":").toString()));
        jpanel2.add(headFootUnitFieldPane);
        jtoolbar.add(jpanel2);
        return jtoolbar;
    }

    public HFInsertButton createHFInsertButton(HFElement hfelement)
    {
        HFInsertButton hfinsertbutton = new HFInsertButton(hfelement);
        hfinsertbutton.addActionListener(insertActionListener);
        return hfinsertbutton;
    }

    public JPanel createContainerSection(String s, HFContainer hfcontainer)
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel uilabel = new UILabel(s);
        jpanel.add(uilabel, "North");
        uilabel.setHorizontalAlignment(2);
        jpanel.add(new JScrollPane(hfcontainer), "Center");
        return jpanel;
    }

    public void populate(ReportHF reporthf, double d, double d1)
    {
        if(reporthf == null)
            reporthf = new ReportHF();
        hfWidth = d;
        leftHFContainer.populate(reporthf.getLeftList());
        centerHFContainer.populate(reporthf.getCenterList());
        rightHFContainer.populate(reporthf.getRightList());
        background = reporthf.getBackground();
        printBackgroundCheckBox.setSelected(reporthf.isPrintBackground());
        refreshPreivewPane();
    }

    public ReportHF update()
    {
        ReportHF reporthf = new ReportHF();
        reporthf.setLeftList(leftHFContainer.update());
        reporthf.setCenterList(centerHFContainer.update());
        reporthf.setRightList(rightHFContainer.update());
        reporthf.setBackground(background);
        reporthf.setPrintBackground(printBackgroundCheckBox.isSelected());
        return reporthf;
    }

    public void populateReportSettings(ReportSettingsProvider reportsettingsprovider, boolean flag)
    {
        headerUnitFieldPane.setUnitValue(reportsettingsprovider.getHeaderHeight());
        footerUnitFieldPane.setUnitValue(reportsettingsprovider.getFooterHeight());
        if(flag)
            headFootUnitFieldPane.setUnitValue(reportsettingsprovider.getHeaderHeight());
        else
            headFootUnitFieldPane.setUnitValue(reportsettingsprovider.getFooterHeight());
        refreshPreivewPane();
    }

    public UNIT updateReportSettings()
    {
        return headFootUnitFieldPane.getUnitValue();
    }

    private void refreshPreivewPane()
    {
        ReportHF reporthf = update();
        hfPreviewPane.refreshReportHFPaintable(reporthf, (int)hfWidth, (int)headFootUnitFieldPane.getUnitValue().toPixD(ScreenResolution.getScreenResolution()), 1, 100, 1);
    }

    private void setCurrentHFContainer(HFContainer hfcontainer)
    {
        currentHFContainer = hfcontainer;
        currentHFContainer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }









}
