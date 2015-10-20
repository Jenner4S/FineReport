package com.fr.grid;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.base.GraphHelper;
import com.fr.base.Margin;
import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellPageAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.core.ReportUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.Report;
import com.fr.report.stable.ReportConstants;
import com.fr.report.stable.ReportSettings;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import com.fr.stable.Constants;
import com.fr.stable.script.CalculatorUtils;
import com.fr.stable.unit.FU;
import com.fr.third.antlr.ANTLRException;

public class GridUI extends ComponentUI {

    public static int INVALID_INTEGER = Integer.MIN_VALUE;// ��Ϊ���Ϸ�����ֵ.
    protected Dimension gridSize;
    protected int verticalValue;
    protected int horizontalValue;
    protected double paperPaintWidth;
    protected double paperPaintHeight;
    protected DynamicUnitList rowHeightList;
    protected DynamicUnitList columnWidthList;
    protected int verticalEndValue;
    protected int horizontalEndValue;
    protected DrawFlowRect drawFlowRect;
    // paint�ĸ�����
    protected List paintCellElementList = new ArrayList();
    protected List paintCellElementRectangleList = new ArrayList();
    protected List paginateLineList = new ArrayList(); // ��ҳ��
    // Ϊ�˻���ɫ�ı���.
    protected static Background WHITE_Backgorund = ColorBackground.getInstance(Color.WHITE);
    // CellElementPainter
    protected CellElementPainter painter = new CellElementPainter();
    // Left
    protected Rectangle2D.Double left_col_row_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // Top
    protected Rectangle2D.Double top_col_row_rect = new Rectangle2D.Double(0, 0, 0, 0);
    protected Rectangle2D.Double back_or_selection_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // alex:��������ק�еĸ��ӵı߿�
    protected Rectangle2D.Double drag_cell_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // alex:��������Ԫ��İ�ɫ����,�����ԭ����������
    protected Rectangle2D.Double cell_back_rect = new Rectangle2D.Double(0, 0, 0, 0);
    // ���ڱ������ʱ��,���ж����ߴ���merge�ĸ��ӵ�ʱ��,�����Ҫ��8��rectangle������.
    protected Rectangle2D.Double tmpRectangle = new Rectangle2D.Double(INVALID_INTEGER,
            INVALID_INTEGER, INVALID_INTEGER, INVALID_INTEGER);

    protected int resolution;

    private boolean isAuthority = false;

    public GridUI(int resolution) {
        super();
        this.resolution = resolution;
    }

    protected ReportSettingsProvider getReportSettings(ElementCase elementCase) {
        if (elementCase instanceof Report) {
            return ReportUtils.getReportSettings((Report) elementCase);
        } else if(elementCase instanceof FormElementCase){
        	return ((FormElementCase) elementCase).getReportSettings();
        } else {
            return new ReportSettings();
        }
    }

    protected void paintBackground(Graphics g, Grid grid, ElementCase elementCase, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        // ��ǰ��Grid���Ĵ�С
        this.back_or_selection_rect.setRect(0, 0, gridSize.getWidth(), gridSize.getHeight());

        // ��Ҫ�ð�ɫ��������ձ���.
        clearBackground(g2d, grid);

        // paint print dash line.
        this.paperPaintWidth = 0;
        this.paperPaintHeight = 0;

        // richer;�ۺϱ�������У������ElementCase��û�мӵ�Report��,����elementCase.getReport()����Ϊ��
        ReportSettingsProvider reportSettings = getReportSettings(elementCase);
        PaperSettingProvider psetting = reportSettings.getPaperSetting();
        if (grid.isShowPaginateLine()) {// paint paper margin line.
            PaperSize paperSize = psetting.getPaperSize();
            Margin margin = psetting.getMargin();

            double paperWidth = paperSize.getWidth().toPixD(resolution);
            double paperHeight = paperSize.getHeight().toPixD(resolution);
            // carl:����ͷ�����
            if (psetting.getOrientation() == ReportConstants.LANDSCAPE) {
                paperWidth = paperSize.getHeight().toPixD(resolution);
                paperHeight = paperSize.getWidth().toPixD(resolution);
            }

            paperPaintWidth = paperWidth - margin.getLeft().toPixD(resolution)
                    - margin.getRight().toPixD(resolution);
            paperPaintHeight = paperHeight - margin.getTop().toPixD(resolution)
                    - margin.getBottom().toPixD(resolution)
                    - reportSettings.getHeaderHeight().toPixD(resolution)
                    - reportSettings.getFooterHeight().toPixD(resolution);
        }

        // denny:������.Background
        Background background = reportSettings.getBackground();

        if (background != null) {
            // denny: except the ColorBackground and ImageBackground
            if (grid.isEnabled() && !(background instanceof ImageBackground)) {
                background.paint(g2d, this.back_or_selection_rect);
            }

            // denny: make that the background can move with scroll
            paintScrollBackground(g2d, grid, background, psetting, reportSettings);
        }
    }

    private void clearBackground(Graphics2D g2d, Grid grid) {
        if (grid.isEnabled()) {
            g2d.setPaint(Color.WHITE);
        } else {
            g2d.setPaint(UIManager.getColor("control"));
        }
        GraphHelper.fill(g2d, this.back_or_selection_rect);
    }

    private void paintScrollBackground(Graphics2D g2d, Grid grid, Background background, PaperSettingProvider psetting, ReportSettingsProvider reportSettings) {
        boolean isCanDrawImage = grid.isEditable() || isAuthority;
        if (isCanDrawImage && (background instanceof ImageBackground)) {
            if (!grid.isShowPaginateLine()) {
                calculatePaper(psetting, reportSettings);
            }

            ImageBackground imageBackground = (ImageBackground) background;

            int hideWidth = columnWidthList.getRangeValue(0, horizontalValue)
                    .toPixI(resolution);
            int hideHeight = rowHeightList.getRangeValue(0, verticalValue).toPixI(resolution);

            for (int i = 0; i * paperPaintWidth < gridSize.getWidth(); i++) {
                for (int j = 0; j * paperPaintHeight < gridSize.getHeight(); j++) {
                    this.back_or_selection_rect.setRect(i * paperPaintWidth, j
                            * paperPaintHeight, paperPaintWidth, paperPaintHeight);
                    imageBackground.paint4Scroll(g2d, this.back_or_selection_rect, hideWidth,
                            hideHeight);
                }
            }

            this.back_or_selection_rect
                    .setRect(0, 0, gridSize.getWidth(), gridSize.getHeight());
        }
    }


    private void calculatePaper(PaperSettingProvider psetting, ReportSettingsProvider reportSettings) {
        PaperSize paperSize = psetting.getPaperSize();
        Margin margin = psetting.getMargin();

        double paperWidth = paperSize.getWidth().toPixD(resolution);
        double paperHeight = paperSize.getHeight().toPixD(resolution);
        if (psetting.getOrientation() == ReportConstants.LANDSCAPE
                && paperSize.getWidth().toPixD(resolution) < paperSize.getHeight()
                .toPixD(resolution)) {
            paperWidth = Math.max(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
            paperHeight = Math.min(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
        } else if (psetting.getOrientation() == ReportConstants.PORTRAIT
                && paperSize.getWidth().toPixD(resolution) > paperSize.getHeight()
                .toPixD(resolution)) {
            paperWidth = Math.min(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
            paperHeight = Math.max(paperSize.getWidth().toPixD(resolution), paperSize
                    .getHeight().toPixD(resolution));
        }

        paperPaintWidth = paperWidth - margin.getLeft().toPixD(resolution)
                - margin.getRight().toPixD(resolution);
        paperPaintHeight = paperHeight - margin.getTop().toPixD(resolution)
                - margin.getBottom().toPixD(resolution)
                - reportSettings.getHeaderHeight().toPixD(resolution)
                - reportSettings.getFooterHeight().toPixD(resolution);
    }

    private void paintGridLine(Graphics g, Grid grid, double realWidth, double realHeight,
                               int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        // --��ʼ��ˮƽ����ֱ��.
        g2d.setPaint(grid.getGridLineColor()); // line color.
        GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_THIN));

        // ��ҳ��
        paginateLineList.clear();

        new DrawVerticalLineHelper(grid.getVerticalBeginValue(), verticalEndValue,
                grid.isShowGridLine(), grid.isShowPaginateLine(), rowHeightList, paperPaintHeight,
                paginateLineList, realWidth, resolution).iterateStart2End(g2d);

        new DrawHorizontalLineHelper(grid.getHorizontalBeginValue(), horizontalEndValue,
                grid.isShowGridLine(), grid.isShowPaginateLine(), columnWidthList, paperPaintWidth,
                paginateLineList, realHeight, resolution).iterateStart2End(g2d);
    }

    /**
     * �����
     */
    public void finalize() {
        try {
            super.finalize();
            if (drawFlowRect != null) {
                this.drawFlowRect.exit();
            }
        } catch (Throwable e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    private static abstract class DrawLineHelper {
        private int startIndex;
        private int endIndex;

        private boolean showGridLine;
        private boolean showPaginateLine;

        private DynamicUnitList sizeList;
        private double paperPaintSize;

        private List paginateLineList;

        Line2D tmpLine2D = new Line2D.Double(0, 0, 0, 0);

        private int resolution;

        DrawLineHelper(int startIndex, int endIndex, boolean showGridLine,
                       boolean showPaginateLine, DynamicUnitList sizeList, double paperPaintSize,
                       List paginateLineList, int resolution) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.showGridLine = showGridLine;
            this.showPaginateLine = showPaginateLine;
            this.sizeList = sizeList;
            this.paperPaintSize = paperPaintSize;

            this.paginateLineList = paginateLineList;
            this.resolution = resolution;
        }

        protected void iterateStart2End(Graphics2D g2d) {
            float tmpSize = 0, paperSumSize = 0, sumSize = 0;
            for (int i = 0; i <= endIndex; i++) {
                // denny: ��ʼ
                if (i == 0) {
                    i = startIndex;

                    // denny: ���Ӵ�0��Grid��߱�hide���п�
                    for (int k = 0; k < startIndex; k++) {
                        tmpSize = sizeList.get(k).toPixF(resolution);

                        paperSumSize += tmpSize;
                        if (paperSumSize >= paperPaintSize) {
                            paperSumSize = tmpSize;
                        }
                    }
                }

                // adjust height.
                tmpSize = sizeList.get(i).toPixF(resolution);
                paperSumSize += tmpSize;

                if (showGridLine) {// paint line.
                    setLine2D((int) sumSize);
                    g2d.draw(tmpLine2D);
                }

                // paint paper margin line.
                if (showPaginateLine && paperSumSize >= paperPaintSize) {
                    paginateLineList.add(getPaginateLine2D((int) sumSize));
                    paperSumSize = tmpSize;
                }

                sumSize += tmpSize;
            }

            // paint ���һ������..
            if (showGridLine) {
                drawLastLine(g2d, (int) sumSize);
            }
        }

        protected abstract void setLine2D(int sumSize);

        protected abstract Line2D.Double getPaginateLine2D(int sumSize);

        protected abstract void drawLastLine(Graphics2D g2d, int sumSize);
    }

    private class DrawVerticalLineHelper extends DrawLineHelper {
        private double realWidth;

        DrawVerticalLineHelper(int startIndex, int endIndex, boolean showGridLine,
                               boolean showPaginateLine, DynamicUnitList unitSizeList, double paperPaintSize,
                               List paginateLineList, double realWidth, int resolution) {
            super(startIndex, endIndex, showGridLine, showPaginateLine, unitSizeList,
                    paperPaintSize, paginateLineList, resolution);
            this.realWidth = realWidth;
        }

        @Override
        protected Double getPaginateLine2D(int sumHeight) {
            return new Line2D.Double(0, sumHeight, gridSize.width, sumHeight);
        }

        @Override
        protected void setLine2D(int sumHeight) {
            tmpLine2D.setLine(0, sumHeight, realWidth, sumHeight);
        }

        @Override
        protected void drawLastLine(Graphics2D g2d, int sumHeight) {
            GraphHelper.drawLine(g2d, 0, sumHeight, realWidth, sumHeight);
        }
    }

    private class DrawHorizontalLineHelper extends DrawLineHelper {
        private double realHeight;

        DrawHorizontalLineHelper(int startIndex, int endIndex, boolean showGridLine,
                                 boolean showPaginateLine, DynamicUnitList unitSizeList, double paperPaintSize,
                                 List paginateLineList, double realHeight, int resolution) {
            super(startIndex, endIndex, showGridLine, showPaginateLine, unitSizeList,
                    paperPaintSize, paginateLineList, resolution);
            this.realHeight = realHeight;
        }

        @Override
        protected Double getPaginateLine2D(int sumWidth) {
            return new Line2D.Double(sumWidth, 0, sumWidth, gridSize.height);
        }

        @Override
        protected void setLine2D(int sumWidth) {
            tmpLine2D.setLine(sumWidth, 0, sumWidth, realHeight);
        }

        @Override
        protected void drawLastLine(Graphics2D g2d, int sumWidth) {
            GraphHelper.drawLine(g2d, sumWidth, 0, sumWidth, realHeight);
        }
    }

    private void paintCellElements(Graphics g, Grid grid, TemplateElementCase report, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        CellElement selectedCellElement = null;
        ElementCasePane reportPane = grid.getElementCasePane();
        Selection sel = reportPane.getSelection();

        if (sel instanceof CellSelection) {
            selectedCellElement = report.getCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
        }

        int horizontalBeginValue = grid.getHorizontalBeginValue();
        int verticalBeginValue = grid.getVerticalBeginValue();

        // Ԫ����Ŀ.
        Shape oldClip = null;
        TemplateCellElement tmpCellElement = null;

        Iterator cells = report.intersect(horizontalBeginValue, verticalBeginValue,
                horizontalEndValue - horizontalBeginValue, verticalEndValue - verticalBeginValue);

        // �������ص���width
        double hideWidth = columnWidthList.getRangeValue(0, horizontalValue).toPixD(resolution);
        double hideHeight = rowHeightList.getRangeValue(0, verticalValue).toPixD(resolution);

        // ���left��top�Ļ滭����.
        this.left_col_row_rect.setRect(0, 0, 0, 0);
        this.top_col_row_rect.setRect(0, 0, 0, 0);

        paintDetailedCellElements(g2d, cells, tmpCellElement, reportPane, selectedCellElement, hideWidth, hideHeight, oldClip, report);
        paintBorder(g2d, tmpCellElement, report);
        paintFatherLeft(g2d);
    }

    private void paintDetailedCellElements(Graphics2D g2d, Iterator cells, TemplateCellElement tmpCellElement, ElementCasePane reportPane,
                                           CellElement selectedCellElement, double hideWidth, double hideHeight, Shape oldClip, TemplateElementCase report) {
        while (cells.hasNext()) {
            tmpCellElement = (TemplateCellElement) cells.next();
            if (tmpCellElement == null) {
                continue;
            }
            // ǿ�Ʒ�ҳ��
            this.calculateForcedPagingOfCellElement(reportPane, tmpCellElement, hideWidth, hideHeight);
            storeFatherLocation(selectedCellElement, tmpCellElement);
            // element bounds
            this.caculateScrollVisibleBounds(this.tmpRectangle, tmpCellElement.getColumn(),
                    tmpCellElement.getRow(), tmpCellElement.getColumnSpan(),
                    tmpCellElement.getRowSpan());
            // peter:clip������.
            // peter:����clip.
            oldClip = g2d.getClip();            /*
             * alex:�˴���tmpRectangle_1��GridUtils.validate�жϱض�Ϊtrue,
			 * ��Ϊ��ЩtmpCellElement��intersect�Ľ�� ����,�����ж���
			 */
            g2d.clip(this.tmpRectangle);

            // ���ΪʲôҪ��1? ��Ϊ��Ԫ�����ߺ��������ߣ����Ϊһ�����ڴ˵�Ԫ�񣬻���Ԫ������ݲ�Ӧ�ð������߸���ס��
            g2d.translate(this.tmpRectangle.getX() + 1, this.tmpRectangle.getY() + 1);

            // peter:tmpRectangle2D_3ֻ��һ����ʱ��Rectangle2D,���ں��治�ٵط���Ҫ�õ��������
            this.cell_back_rect.setRect(0, 0, this.tmpRectangle.getWidth() - 1,
                    this.tmpRectangle.getHeight() - 1);
            // peter:���ںϲ��ĵ�Ԫ��,��Ҫ�Ȱ�ɫ�ı������������.
            if (tmpCellElement.getColumnSpan() > 1 || tmpCellElement.getRowSpan() > 1) {
                WHITE_Backgorund.paint(g2d, this.cell_back_rect);
                //daniel:����������������˰�....����ı��������֮ǰ���� �Ḳ�Ǳ�����....����ֻ��������п���Ԥ�����û����
            }
            // peter:�����Ԫ����ӵ���Ҫpaint��Ԫ���б���ȥ,���Ż��߿���..
            paintCellElementList.add(tmpCellElement);
            paintCellElementRectangleList.add(this.tmpRectangle.clone());

            int cellWidth = (int) this.tmpRectangle.getWidth(), cellHeight = (int) this.tmpRectangle
                    .getHeight();
            // denny_Grid: ��Grid�е�Ԫ�������(������Ԫ��ı���Content + Background), �������߿�

            painter.paintBackground(g2d, report, tmpCellElement, cellWidth, cellHeight);
            painter.paintContent(g2d, report, tmpCellElement, cellWidth, cellHeight, resolution);
            // denny_Grid: ע�����滹Ҫ��һ, ��Ϊ����translateʱ��һ
            g2d.translate(-this.tmpRectangle.getX() - 1, -this.tmpRectangle.getY() - 1);
            paintAuthorityCell(g2d, tmpCellElement);
            g2d.setClip(oldClip);
        }
    }

    private void paintAuthorityCell(Graphics2D g2d, TemplateCellElement tmpCellElement) {
        String selectedRole = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        boolean isWidgetAuthority = false;
        if (tmpCellElement.getWidget() != null) {
        	isWidgetAuthority = tmpCellElement.getWidget().isDirtyWidget(selectedRole);
        }
        boolean isCellDoneAuthority = tmpCellElement.isDoneAuthority(selectedRole) || tmpCellElement.isDoneNewValueAuthority(selectedRole);
        boolean isDoneAuthority = isWidgetAuthority || isCellDoneAuthority;
        if (isAuthority && isDoneAuthority) {
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            Paint oldPaint = g2d.getPaint();
            g2d.setPaint(UIConstants.AUTHORITY_COLOR);
            GraphHelper.fill(g2d, tmpRectangle);
            g2d.setComposite(oldComposite);
            g2d.setPaint(oldPaint);
        }
    }

    private void storeFatherLocation(CellElement selectedCellElement, TemplateCellElement tmpCellElement) {
//		// ��Ҫ����Ƿ���Design״̬����.
//		CellGUIAttr cellGUIAttr = tmpCellElement.getCellGUIAttr();
//		if (cellGUIAttr == null) {
//			// ��set��curCellElement,��ֵֻ��Ϊ�˷�������Ĳ���
//			cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
//		}

		/*
         * ��¼��ǰѡ�еĵ�Ԫ��������ϸ���λ����leftColumnRowRectangle2D &
		 * topColumnRowRectangle2D
		 */
        if (selectedCellElement == tmpCellElement) {
            CellExpandAttr cellExpandAttr = tmpCellElement.getCellExpandAttr();
            if (cellExpandAttr != null) {
                ColumnRow leftColumnRow = cellExpandAttr.getLeftParentColumnRow();
                // leftColumnRow�����ڿ��ӷ�Χ��
                if (ColumnRow.validate(leftColumnRow)) {
                    this.caculateScrollVisibleBounds(this.left_col_row_rect,
                            leftColumnRow.getColumn(), leftColumnRow.getRow(), 1, 1);
                }

                ColumnRow topColumnRow = cellExpandAttr.getUpParentColumnRow();
                // topColumnRow�����ڿ��ӷ�Χ��
                if (ColumnRow.validate(topColumnRow)) {
                    this.caculateScrollVisibleBounds(this.top_col_row_rect,
                            topColumnRow.getColumn(), topColumnRow.getRow(), 1, 1);
                }
            }
        }
    }

    private void paintBorder(Graphics2D g2d, TemplateCellElement tmpCellElement, TemplateElementCase report) {
        // ���߿�
        Rectangle2D.Double tmpCellElementRectangle;
        for (int i = 0; i < paintCellElementList.size(); i++) {
            tmpCellElement = (TemplateCellElement) paintCellElementList.get(i);
            tmpCellElementRectangle = (Rectangle2D.Double) paintCellElementRectangleList.get(i);

            // ��Ҫ����Ƿ���Design״̬����.
            CellGUIAttr cellGUIAttr = tmpCellElement.getCellGUIAttr();
            if (cellGUIAttr == null) {
                // ��set��curCellElement,��ֵֻ��Ϊ�˷�������Ĳ���
                cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
            }

            g2d.translate(tmpCellElementRectangle.getX(), tmpCellElementRectangle.getY());

            painter.paintBorder(g2d, report, tmpCellElement, tmpCellElementRectangle.getWidth(),
                    tmpCellElementRectangle.getHeight());

            g2d.translate(-tmpCellElementRectangle.getX(), -tmpCellElementRectangle.getY());
        }
    }

    private void paintFatherLeft(Graphics2D g2d) {
        // ���󸸸���.
        if (validate(this.left_col_row_rect) && this.left_col_row_rect.getHeight() > 5) {
            g2d.setPaint(Color.BLUE);
            double centerX = this.left_col_row_rect.getX() + 4;
            double centerY = this.left_col_row_rect.getY() + this.left_col_row_rect.getHeight() / 2;
            GraphHelper.drawLine(g2d, centerX, centerY - 5, centerX, centerY + 5);
            GeneralPath polyline = new GeneralPath(Path2D.WIND_EVEN_ODD, 3);
            polyline.moveTo((float) centerX, (float) centerY + 5);
            polyline.lineTo((float) centerX + 3, (float) centerY + 5 - 4);
            polyline.lineTo((float) centerX - 2, (float) centerY + 5 - 4);
            GraphHelper.fill(g2d, polyline);
        }
        if (validate(this.top_col_row_rect) && this.top_col_row_rect.getWidth() > 5) {
            g2d.setPaint(Color.BLUE);
            double centerX = this.top_col_row_rect.getX() + this.top_col_row_rect.getWidth() / 2;
            double centerY = this.top_col_row_rect.getY() + 4;
            GraphHelper.drawLine(g2d, centerX - 5, centerY, centerX + 5, centerY);
            GeneralPath polyline = new GeneralPath(Path2D.WIND_EVEN_ODD, 3);
            polyline.moveTo((float) centerX + 5, (float) centerY);
            polyline.lineTo((float) centerX + 5 - 4, (float) centerY + 3);
            polyline.lineTo((float) centerX + 5 - 4, (float) centerY - 3);

            GraphHelper.fill(g2d, polyline);
        }
    }

    private void paintPaginateLines(Graphics g, Grid grid) {
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jTemplate.isJWorkBook()){
            //������޷�ҳ֮˵
            return;
        }
        Graphics2D g2d = (Graphics2D) g;

        // james ����ҳ��
        if (this.paginateLineList.size() > 0) {
            Line2D tmpLine2D = new Line2D.Double(0, 0, 0, 0);

            // james
            // ����forcedPaperMarginLineList����������ͬ����ȥ����������Grid��paintConponetʱ����setXORMode������ͬ���߸����ǵ�
            Line2D tmpLine2D2;
            for (int j = 0; j < paginateLineList.size(); j++) {
                tmpLine2D = (Line2D) paginateLineList.get(j);// ֱ��ǿ��ת������ΪList�п϶�����Line2D�͵�
                for (int k = j + 1; k < paginateLineList.size(); k++) {
                    tmpLine2D2 = (Line2D) paginateLineList.get(k);
                    if (tmpLine2D2.getX1() == tmpLine2D.getX1()
                            && tmpLine2D2.getX2() == tmpLine2D.getX2()
                            && tmpLine2D2.getY1() == tmpLine2D.getY1()
                            && tmpLine2D2.getY2() == tmpLine2D.getY2()) {
                        paginateLineList.remove(k);
                    }
                }
            }

            g2d.setPaint(grid.getPaginationLineColor());

            //g2d.setXORMode(Utils.getXORColor(grid.getPaginationLineColor()));
            GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_DASH_DOT));
            for (int i = 0, len = paginateLineList.size(); i < len; i++) {
                g2d.draw((Shape) paginateLineList.get(i));
            }

            g2d.setPaintMode();
        }
    }

    private void paintGridSelection(Graphics g, Grid grid, ElementCase report) {
        Graphics2D g2d = (Graphics2D) g;
        Selection sel = grid.getElementCasePane().getSelection();
        // ��GridSelection:CELL
        if (sel instanceof CellSelection) {// james:cell������select��ʱ��Ͳ�����
            CellSelection gridSelection = (CellSelection) sel;
            // peter:��ʼѡ���CellRectangle.
            Rectangle editRectangle = gridSelection.getEditRectangle();
            int cellRectangleCount = gridSelection.getCellRectangleCount();
            Area selectedCellRectArea = new Area();
            Area editCellRectArea = new Area();
            // denny: editCellRectArea
            ElementCasePane reportPane = grid.getElementCasePane();
            if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
                this.caculateScrollVisibleBounds(this.tmpRectangle, new Rectangle(
                        gridSelection.getColumn(), gridSelection.getRow(), gridSelection.getColumnSpan(), gridSelection.getRowSpan()
                ));
                if (validate(this.tmpRectangle)) {
                    editCellRectArea.add(new Area(this.tmpRectangle));
                }
                if (cellRectangleCount == 1) {
                    paintOne(selectedCellRectArea, gridSelection, grid, g2d);
                } else {
                    paintMore(selectedCellRectArea, gridSelection, grid, g2d, cellRectangleCount, editCellRectArea);
                }
            } else {
                Rectangle referenced = null;
                referenced = paintReferenceCell(reportPane, grid, g2d, editCellRectArea, selectedCellRectArea, referenced);
                paintFormatArea(selectedCellRectArea, gridSelection, grid, g2d, referenced);
            }
            // denny: ��ǹ�ʽ�õ��ĵ�Ԫ��
            paintGridSelectionForFormula(g2d, report, gridSelection);
        }
    }

    private Rectangle paintReferenceCell(ElementCasePane reportPane, Grid grid, Graphics2D g2d, Area editCellRectArea, Area selectedCellRectArea, Rectangle referenced) {
        CellSelection referencedCell = reportPane.getFormatReferencedCell();
        if (referencedCell == null) {
            return null;
        }

        if (DesignerContext.getReferencedIndex() !=
                ((JTemplate) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex()) {
            return null;
        }
        referenced = new Rectangle(
                referencedCell.getColumn(), referencedCell.getRow(), referencedCell.getColumnSpan(), referencedCell.getRowSpan()
        );
        this.caculateScrollVisibleBounds(this.tmpRectangle, referenced);
        if (validate(this.tmpRectangle)) {
            editCellRectArea.add(new Area(this.tmpRectangle));
        }
        paintOne(selectedCellRectArea, referencedCell, grid, g2d);
        return referenced;
    }


    private void paintFormatArea(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d, Rectangle referenced) {
        Rectangle format = new Rectangle(
                gridSelection.getColumn(), gridSelection.getRow(), gridSelection.getColumnSpan(), gridSelection.getRowSpan()
        );
        if (ComparatorUtils.equals(DesignerContext.getReferencedElementCasePane(), grid.getElementCasePane()) && ComparatorUtils.equals(format, referenced)) {
            return;
        }

        this.caculateScrollVisibleBounds(this.tmpRectangle, format);
        if (validate(this.tmpRectangle)) {
            selectedCellRectArea = new Area(this.tmpRectangle);
            double selectedCellX = this.tmpRectangle.getX();
            double selectedCellY = this.tmpRectangle.getY();
            double selectedCellWidth = this.tmpRectangle.getWidth();
            double selectedCellHeight = this.tmpRectangle.getHeight();

            // TODO ALEX_SEP �����͸����,��������?
            // peter:����͸���ı���,����SelectCellRectangle��EditCellRectangle���ظ�����Ҫ��͸������.
            if (gridSelection.getRowSpan() > 1
                    || gridSelection.getColumnSpan() > 1) {
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(g2d, selectedCellRectArea);
                g2d.setComposite(oldComposite);
            }


            paintNormal(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        }

    }

    private void paintOne(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d) {
        if (validate(this.tmpRectangle)) {
            selectedCellRectArea = new Area(this.tmpRectangle);
            double selectedCellX = this.tmpRectangle.getX();
            double selectedCellY = this.tmpRectangle.getY();
            double selectedCellWidth = this.tmpRectangle.getWidth();
            double selectedCellHeight = this.tmpRectangle.getHeight();

            // TODO ALEX_SEP �����͸����,��������?
            // peter:����͸���ı���,����SelectCellRectangle��EditCellRectangle���ظ�����Ҫ��͸������.
            if (gridSelection.getRowSpan() > 1
                    || gridSelection.getColumnSpan() > 1) {
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setPaint(grid.getSelectedBackground());
                GraphHelper.fill(g2d, selectedCellRectArea);
                g2d.setComposite(oldComposite);
            }


            // �ж��Ƿ�ʹ��������ӵ�Ԫ����ʹ��������ӵ�Ԫ���򻭶�̬����
            paintGridSelectionDependsOnTableSelectionPane(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        }
    }


    private void paintMore(Area selectedCellRectArea, CellSelection gridSelection, Grid grid, Graphics2D g2d, int cellRectangleCount, Area editCellRectArea) {

        selectedCellRectArea = new Area();
        // cellRectangleCount > 1

        // p:���洦����ѡ���������鷳һЩ.
        for (int i = 0; i < cellRectangleCount; i++) {
            Rectangle cellRectangle = gridSelection.getCellRectangle(i);

            // p:����CellSelection.
            this.caculateScrollVisibleBounds(this.tmpRectangle, cellRectangle);

            if (validate(this.tmpRectangle)) {
                selectedCellRectArea.add(new Area(this.tmpRectangle));
                paintGridSelectionDependsOnTableSelectionPane(g2d, tmpRectangle.x, tmpRectangle.y, tmpRectangle.width, tmpRectangle.height, grid);
            }
        }

        selectedCellRectArea.add(editCellRectArea);
        selectedCellRectArea.exclusiveOr(editCellRectArea);

        // denny: ����͸������
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setPaint(grid.getSelectedBackground());
        GraphHelper.fill(g2d, selectedCellRectArea);
        g2d.setComposite(oldComposite);

        // p:��edit�հ�����ı߿�.
        g2d.setPaint(Color.blue);
        // marks:���editCellRectAreaΪ�գ��Ͳ���Ҫ����˵�������ڿɼ�������
        if (editCellRectArea != null) {
            GraphHelper.draw(g2d, editCellRectArea);
        }


    }


    private void paintGridSelectionForFormula(Graphics2D g2d, ElementCase report, CellSelection cs) {
        // denny: ��ǹ�ʽ�õ��ĵ�Ԫ��
        if (report.getCellValue(cs.getColumn(), cs.getRow()) instanceof Formula) {
            Formula tmpFormula = (Formula) report
                    .getCellValue(cs.getColumn(), cs.getRow());
            String statement = tmpFormula.getContent();
            // denny: ��ù�ʽ�а��������е�Ԫ��
            ColumnRow[] columnRowArray = new ColumnRow[0];
            try {
                columnRowArray = CalculatorUtils.relatedColumnRowArray(statement.substring(1));
            } catch (ANTLRException ae) {
                // do nothing.
            }
            Area formulaCellArea = null;
            for (int i = 0; i < columnRowArray.length; i++) {
                ColumnRow columnRow = columnRowArray[i];
                int columnSpan = 1;
                int rowSpan = 1;
                CellElement columnRowCE = report.getCellElement(columnRow.getColumn(),
                        columnRow.getRow());
                if (columnRowCE != null) {
                    columnSpan = columnRowCE.getColumnSpan();
                    rowSpan = columnRowCE.getRowSpan();
                    // _denny: ���ȡ���Ǻϲ���Ԫ��ĺ���ģ���ô��Ҫ����columnRow
                    if (columnSpan > 1 || rowSpan > 1) {
                        columnRow = ColumnRow.valueOf(columnRowCE.getColumn(),
                                columnRowCE.getRow());
                    }
                }

                this.caculateScrollVisibleBounds(this.tmpRectangle, columnRow.getColumn(),
                        columnRow.getRow(), columnSpan, rowSpan);

                if (validate(this.tmpRectangle)) {
                    paintFormulaCellArea(g2d, formulaCellArea, i);
                }
            }
        }
    }

    private void paintFormulaCellArea(Graphics2D g2d, Area formulaCellArea, int i) {
        // denny: ��Ǹ��ӵı߿�
        formulaCellArea = new Area(new Rectangle2D.Double(this.tmpRectangle.getX(),
                this.tmpRectangle.getY(), this.tmpRectangle.getWidth(),
                this.tmpRectangle.getHeight()));

        // peter:ȥ��edit�ĸ�������
        formulaCellArea.exclusiveOr(new Area(
                new Rectangle2D.Double(this.tmpRectangle.getX() + 1,
                        this.tmpRectangle.getY() + 1,
                        this.tmpRectangle.getWidth() - 2, this.tmpRectangle
                        .getHeight() - 2)));

        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX(), this.tmpRectangle.getY(), 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX() + this.tmpRectangle.getWidth() - 3, this.tmpRectangle
                .getY(), 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX(), this.tmpRectangle.getY() + this.tmpRectangle.getHeight()
                - 3, 3, 3)));
        formulaCellArea.add(new Area(new Rectangle2D.Double(this.tmpRectangle
                .getX() + this.tmpRectangle.getWidth() - 3, this.tmpRectangle
                .getY() + this.tmpRectangle.getHeight() - 3, 3, 3)));
        g2d.setPaint(new Color(((i + 2) * 50) % 256, ((i + 1) * 50) % 256,
                (i * 50) % 256));
        if (formulaCellArea != null) {
            GraphHelper.fill(g2d, formulaCellArea);
        }
    }

    private void paintGridSelectionDependsOnTableSelectionPane(Graphics2D g2d, double selectedCellX, double selectedCellY,
                                                               double selectedCellWidth, double selectedCellHeight, Grid grid) {
        if (grid.IsNotShowingTableSelectPane()) {
            // peter:��������ʼ���߿�,ֻ��Ҫ�Ե�һ��Cell
            paintNormal(g2d, selectedCellX, selectedCellY, selectedCellWidth, selectedCellHeight, grid);
        } else {
            // ��̬����
            Stroke stroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(1));
            if (drawFlowRect == null) {
                drawFlowRect = new DrawFlowRect();
            }
            drawFlowRect.setGrid(grid);
            drawFlowRect.drawFlowRect(g2d, (int) selectedCellX, (int) selectedCellY,
                    (int) selectedCellWidth + (int) selectedCellX,
                    (int) selectedCellHeight + (int) selectedCellY);
            g2d.setStroke(stroke);
        }
    }


    private void paintNormal(Graphics2D g2d, double selectedCellX, double selectedCellY,
                             double selectedCellWidth, double selectedCellHeight, Grid grid) {
        this.back_or_selection_rect.setRect(selectedCellX - 1, selectedCellY - 1,
                selectedCellWidth + 3, selectedCellHeight + 3);
        Area borderLineArea = new Area(this.back_or_selection_rect);
        this.back_or_selection_rect.setRect(selectedCellX + 2, selectedCellY + 2,
                selectedCellWidth - 3, selectedCellHeight - 3);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 1,
                selectedCellY + selectedCellHeight - 3, 3, 1);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 3,
                selectedCellY + selectedCellHeight - 1, 1, 3);
        borderLineArea.exclusiveOr(new Area(this.back_or_selection_rect));
        // peter:���½�����Ǹ�С�ڷ���.
        this.back_or_selection_rect.setRect(selectedCellX + selectedCellWidth - 2,
                selectedCellY + selectedCellHeight - 2, 5, 5);
        borderLineArea.add(new Area(this.back_or_selection_rect));

        //g2d.setXORMode(Utils.getXORColor(grid.getSelectedBorderLineColor()));
        g2d.setPaint(grid.getSelectedBorderLineColor());
        GraphHelper.fill(g2d, borderLineArea);
        g2d.setPaintMode();
    }

    private void paintFloatElements(Graphics g, Grid grid, ElementCase report, int resolution) {
        Graphics2D g2d = (Graphics2D) g;

        Selection sel = grid.getElementCasePane().getSelection();

        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();

            int lastRow = rowHeightList.getValueIndex(FU.getInstance(
                    tmpFloatElement.getTopDistance().toFU() + tmpFloatElement.getHeight().toFU()
            ));

            // �������λ��.
            if (isSuitablePosition(lastRow)) {
                float floatX = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                        resolution)
                        + tmpFloatElement.getLeftDistance().toPixF(resolution);
                float floatY = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                        + tmpFloatElement.getTopDistance().toPixF(resolution);

                g2d.translate(floatX, floatY);

                PaintUtils.paintFloatElement(g2d, tmpFloatElement,
                        tmpFloatElement.getWidth().toPixI(resolution),
                        tmpFloatElement.getHeight().toPixI(resolution), resolution);

                g2d.translate(-floatX, -floatY);
            }
        }

        // p:��ѡ�е�����Ԫ�صı߿�,���������ѭ��һ�����е�����Ԫ�أ�
        // ��Ϊ��Щ����ѡ��㣬��ʱ�������Ԫ�ر����ǵ�ʱ�򣬻��ܹ�ѡ�е���ģ����ı�Ԫ�صĳߴ��.
        paintFloatElementsBorder(g2d, grid, sel, flotIt, report);

        paintAuthorityFloatElement(g2d, report, ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName(), grid);
    }

    private boolean isSuitablePosition(int lastRow) {
        // confusing logic...
        return 0 >= verticalValue || 0 <= verticalEndValue || lastRow >= verticalValue
                || lastRow <= verticalValue;
    }

    private void paintFloatElementsBorder(Graphics2D g2d, Grid grid, Selection sel, Iterator flotIt, ElementCase report) {
        // p:��ѡ�е�����Ԫ�صı߿�,���������ѭ��һ�����е�����Ԫ�أ�
        // ��Ϊ��Щ����ѡ��㣬��ʱ�������Ԫ�ر����ǵ�ʱ�򣬻��ܹ�ѡ�е���ģ����ı�Ԫ�صĳߴ��.
        if (sel instanceof FloatSelection) {
            flotIt = report.floatIterator();
            while (flotIt.hasNext()) {
                FloatElement tmpFloatElement = (FloatElement) flotIt.next();
                // p:�������ѡ�е�����Ԫ��,ֱ��continue.
                if (!ComparatorUtils.equals(tmpFloatElement.getName(), ((FloatSelection) sel).getSelectedFloatName())) {
                    continue;
                }

                Rectangle2D[] rectArray = calculateFloatElementPoints(tmpFloatElement);

                GraphHelper.setStroke(g2d, GraphHelper.getStroke(Constants.LINE_THIN));
                for (int j = 0; j < rectArray.length; j++) {
                    g2d.setPaint(Utils.getXORColor(grid.getSelectedBorderLineColor()));
                    GraphHelper.fill(g2d, rectArray[j]);
                    g2d.setPaint(grid.getSelectedBorderLineColor());
                    GraphHelper.draw(g2d, rectArray[j]);
                }
            }
        }
    }


    private Rectangle2D[] calculateFloatElementPoints(FloatElement tmpFloatElement) {
        // width and height
        float floatX1 = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                resolution) + tmpFloatElement.getLeftDistance().toPixF(resolution);
        float floatY1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                + tmpFloatElement.getTopDistance().toPixF(resolution);
        int floatX2 = (int) (floatX1 + tmpFloatElement.getWidth().toPixF(resolution));
        int floatY2 = (int) (floatY1 + tmpFloatElement.getHeight().toPixF(resolution));

        return new Rectangle2D[]{
                new Rectangle2D.Double(floatX1 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6)};
    }


    private void paintAuthorityFloatElement(Graphics2D g2d, ElementCase report, String selectedRoles, Grid grid) {
        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();
            // p:�������ѡ�е�����Ԫ��,ֱ��continue.
            if (!tmpFloatElement.getFloatPrivilegeControl().checkInvisible(selectedRoles)) {
                continue;
            }
            float floatX1 = columnWidthList.getRangeValue(horizontalValue, 0).toPixF(
                    resolution) + tmpFloatElement.getLeftDistance().toPixF(resolution);
            float floatY1 = rowHeightList.getRangeValue(verticalValue, 0).toPixF(resolution)
                    + tmpFloatElement.getTopDistance().toPixF(resolution);
            int floatX2 = (int) (floatX1 + tmpFloatElement.getWidth().toPixF(resolution));
            int floatY2 = (int) (floatY1 + tmpFloatElement.getHeight().toPixF(resolution));
            double x = floatX1;
            double y = floatY1;
            double width = floatX2 - x;
            double height = floatY2 - y;

            //������Ȩ�ޱ༭��������Ԫ�صı߿�
            if (isAuthority) {
                paintAuthorityFloatElementBorder(g2d, x, y, width, height, grid);
            }
        }
    }

    private void paintAuthorityFloatElementBorder(Graphics2D g2d, double selectedCellX, double selectedCellY,
                                                  double selectedCellWidth, double selectedCellHeight, Grid grid) {
        this.back_or_selection_rect.setRect(selectedCellX - 1, selectedCellY - 1,
                selectedCellWidth + 3, selectedCellHeight + 3);
        Area borderLineArea = new Area(this.back_or_selection_rect);
        g2d.setXORMode(Utils.getXORColor(grid.getSelectedBorderLineColor()));
        g2d.setPaint(UIConstants.AUTHORITY_COLOR);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        GraphHelper.fill(g2d, borderLineArea);
        g2d.setPaintMode();
    }

    private void paintDragCellBorder(Graphics g, Grid grid) {
        Graphics2D g2d = (Graphics2D) g;

        if ((grid.getDragType() == GridUtils.DRAG_CELLSELECTION || grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER)
                && (grid.getDragRectangle() != null)) {
            this.caculateScrollVisibleBounds(this.drag_cell_rect, grid.getDragRectangle());

            g2d.setPaint(Color.GRAY);
            GraphHelper.draw(g2d, this.drag_cell_rect, Constants.LINE_THICK);
        }
    }

    @Override
    /**
     * paint
     */
    public void paint(Graphics g, JComponent c) {
        if (!(c instanceof Grid)) {
            throw new IllegalArgumentException("The component c to paint must be a Grid!");
        }

        isAuthority = BaseUtils.isAuthorityEditing();

        Graphics2D g2d = (Graphics2D) g;

        Grid grid = (Grid) c;

        // ȡ��ElementCasePane.ElementCase
        ElementCasePane elementCasePane = grid.getElementCasePane();
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();// ȡ��ElementCase����

        dealWithSizeBeforePaint(grid, elementCase);

        double realWidth = gridSize.getWidth();// ���
        double realHeight = gridSize.getHeight();// �߶�

        // ������
        this.paintBackground(g2d, grid, elementCase, resolution);

        // ��Grid Line
        this.paintGridLine(g2d, grid, realWidth, realHeight, resolution);

        // peter:�����̵߳�֧��,��ʱ��,paintԪ�ص�ʱ��,���ܻ���Ԫ�ر�ɾ����.
        // ��Ҫ�������Border��Ҫ��Ԫ��.
        paintCellElementList.clear();
        paintCellElementRectangleList.clear();

        // ����Ԫ��Ԫ��
        this.paintCellElements(g2d, grid, elementCase, resolution);

        // ����ҳ��
        this.paintPaginateLines(g2d, grid);

        // �� GridSelection
        if (!(isAuthority && elementCase instanceof WorkSheet && !((WorkSheet) elementCase).isPaintSelection())) {
            this.paintGridSelection(g2d, grid, elementCase);
        }

        // ������Ԫ��
        this.paintFloatElements(g2d, grid, elementCase, resolution);

        // ��Drag���ӵı߿�.
        this.paintDragCellBorder(g2d, grid);

        grid.ajustEditorComponentBounds(); // refresh size
    }

    private void dealWithSizeBeforePaint(Grid grid, TemplateElementCase elementCase) {
        // ȡ�����е��иߺ��п��List
        this.rowHeightList = ReportHelper.getRowHeightList(elementCase);
        this.columnWidthList = ReportHelper.getColumnWidthList(elementCase);

        this.verticalValue = grid.getVerticalValue();
        this.horizontalValue = grid.getHorizontalValue();

        int verticalExtent = grid.getVerticalExtent();
        int horizontalExtent = grid.getHorizontalExtent();

        // denny: set the verticalBeginValue and horizontalBeginValue
        grid.setVerticalBeinValue(verticalValue);
        grid.setHorizontalBeginValue(horizontalValue);
        // denny: end

        // ��ÿؼ���ʵ�ʳߴ�
        this.gridSize = grid.getSize();

        this.verticalEndValue = verticalValue + verticalExtent + 1;
        this.horizontalEndValue = horizontalValue + horizontalExtent + 1;
    }

    /**
     * ���㵥Ԫ��ɼ��ı߿�Χ
     *
     * @param rect        ���ڼ����rect
     * @param cellElement ��Ԫ��
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, CellElement cellElement) {
        caculateScrollVisibleBounds(rect, cellElement.getColumn(), cellElement.getRow(),
                cellElement.getColumnSpan(), cellElement.getRowSpan());
    }

    /**
     * ������οɼ��ı߿�Χ
     *
     * @param rect   ���ڼ����rect
     * @param target Ŀ�����
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, Rectangle target) {
        caculateScrollVisibleBounds(rect, target.x, target.y, target.width, target.height);
    }

    /**
     * ����(int column,int row,int columnSpan,int rowSpan,),��Grid�ؼ������λ��.
     * ע��:���ص�paintRectangle��clipRectangle����Ϊnull,�������Ƿ���Ҫpaint����û��clip.
     * ��Ϊ�����������Grid����,Ϊ�˼ӿ�Grid��paint�ٶ�,���ܲ�ͣ��new Rectangle()�������ڴ������.
     * <p/>
     * �������tmpRectangle2Ds���뾭��validate�Ĵ���ſ���ʹ��!!
     *
     * @param rect       rect��һ������Ϊ8��Rectangle2D����, �滭�����򲻿ɼ� paintRectangle.x =INVALID_INTEGER
     * @param column     ��
     * @param row        ��
     * @param columnSpan ����
     * @param rowSpan    ����
     */
    public void caculateScrollVisibleBounds(Rectangle2D.Double rect, int column, int row,
                                            int columnSpan, int rowSpan) {
        // �ж��Ƿ��ڲ��ɼ�������.
        if (outOfScreen(column, row, columnSpan, rowSpan)) {
            // �������Ļ֮��,���INVALID_INTEGER
            rect.x = INVALID_INTEGER;
        } else {
            rect.x = (columnWidthList.getRangeValue(horizontalValue, column))
                    .toPixD(resolution);
            rect.y = (rowHeightList.getRangeValue(verticalValue, row)).toPixD(resolution);
            rect.width = (columnWidthList.getRangeValue(column, column + columnSpan))
                    .toPixD(resolution);
            rect.height = (rowHeightList.getRangeValue(row, row + rowSpan)).toPixD(resolution);
        }
    }

    private boolean outOfScreen(int column, int row, int columnSpan, int rowSpan) {
        return column > horizontalEndValue || row > verticalEndValue
                || column + columnSpan <= horizontalValue || row + rowSpan <= verticalValue;
    }

    /**
     * �Ƿ���Ч��Rectangle2D
     *
     * @param rect Ŀ��rect
     * @return ��Ч����true
     */
    public static boolean validate(Rectangle2D rect) {
        return rect != null && rect.getX() != INVALID_INTEGER;
    }


    /**
     * double frozenHeight�Ӷ��������ᴰ�ڵĸ��Ӹ߶�
     *
     * @param reportPane     ���ڼ���ĵ�Ԫ�����
     * @param tmpCellElement ���ڼ����element
     * @param hideWidth      ���ڶ��ᴰ�ڻ���������������˵��ܿ�� double
     * @param hideHeight     ���ڶ��ᴰ�ڻ���������������˵��ܸ߶� double frozenWidth;//����ൽ���ᴰ�ڵĸ��ӿ��
     */
    public void calculateForcedPagingOfCellElement(ElementCasePane reportPane,
                                                   CellElement tmpCellElement, double hideWidth, double hideHeight) {
        if (tmpCellElement == null) {
            return;
        }

        // �ҳ�������Ҫpaint��ǿ�Ʒ�ҳ��
        CellPageAttr cellPageAttr = tmpCellElement.getCellPageAttr();
        if (cellPageAttr == null) {
            return;
        }

        ElementCase report = reportPane.getEditingElementCase();

        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        int width = reportPane.getSize().width;
        int height = reportPane.getSize().height;

        double sumWidth;// ��ҳ�ߵ���ʼxλ��
        double sumHeight;// ��ҳ�ߵ���ʼyλ��
        if (cellPageAttr.isPageAfterColumn()) {
            sumWidth = columnWidthList.getRangeValueFromZero(
                    tmpCellElement.getColumn() + tmpCellElement.getColumnSpan()).toPixD(resolution)
                    - hideWidth;
            paginateLineList.add(new Line2D.Double(sumWidth, 0, sumWidth, height));
        }
        if (cellPageAttr.isPageBeforeColumn()) {
            sumWidth = columnWidthList.getRangeValueFromZero(tmpCellElement.getColumn()).toPixD(
                    resolution)
                    - hideWidth;
            paginateLineList.add(new Line2D.Double(sumWidth, 0, sumWidth, height));
        }
        if (cellPageAttr.isPageAfterRow()) {
            sumHeight = rowHeightList.getRangeValueFromZero(
                    tmpCellElement.getRow() + tmpCellElement.getRowSpan()).toPixD(resolution)
                    - hideHeight;
            paginateLineList.add(new Line2D.Double(0, sumHeight, width, sumHeight));
        }
        if (cellPageAttr.isPageBeforeRow()) {
            sumHeight = rowHeightList.getRangeValueFromZero(tmpCellElement.getRow()).toPixD(
                    resolution)
                    - hideHeight;
            paginateLineList.add(new Line2D.Double(0, sumHeight, width, sumHeight));
        }
    }
}
