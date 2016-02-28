package com.fr.plugin.chart.designer.component;

import com.fr.base.background.ImageBackground;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.MarkerType;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.designer.component.background.VanChartMarkerBackgroundPane;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * 标记点设置界面
 */
public class VanChartMarkerPane extends BasicPane {
    private static final long serialVersionUID = 7206339620703021514L;
    private UIButtonGroup<String> commonORCustom;
    private JPanel centerPane;
    private CardLayout cardLayout;

    private MarkerComboBox markerPane;
    private VanChartMarkerBackgroundPane markerFillColor;
    private UISpinner radius;

    private ImageBackgroundPane imageBackgroundPane;
    private UISpinner width;
    private UISpinner height;



    public VanChartMarkerPane() {
        this.setLayout(new BorderLayout(0, 4));
        String[] array = new String[]{Inter.getLocText("Plugin-ChartF_Rule"), Inter.getLocText("Plugin-ChartF_Custom")};
        commonORCustom = new UIButtonGroup<String>(array, array);

        commonORCustom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkCenterPane();
            }
        });

        final JPanel commonPane = getCommonPane();
        final JPanel customPane = getCustomPane();

        cardLayout = new CardLayout();
        centerPane = new JPanel(cardLayout) {

            @Override
            public Dimension getPreferredSize() {
                if(commonORCustom.getSelectedIndex() == 0){
                    return commonPane.getPreferredSize();
                } else {
                    return customPane.getPreferredSize();
                }
            }
        };
        centerPane.add(commonPane, Inter.getLocText("Plugin-ChartF_Rule"));
        centerPane.add(customPane, Inter.getLocText("Plugin-ChartF_Custom"));

        this.add(commonORCustom, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    private JPanel getCommonPane() {
        markerPane = new MarkerComboBox(MarkerType.getMarkers());
        markerFillColor = new VanChartMarkerBackgroundPane();
        radius = new UISpinner(0, 100, 0.5, 0);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_type")), markerPane},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_FillColor")), markerFillColor},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Radius")), radius},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    private JPanel getCustomPane() {
        imageBackgroundPane = new ImageBackgroundPane(false);
        width = new UISpinner(0, 100, 0.5, 0);
        height = new UISpinner(0, 100, 0.5, 0);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Width")), width},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Height")), height},
        };

        JPanel sizePanel = TableLayoutHelper.createTableLayoutPane(components, row, col);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(imageBackgroundPane, BorderLayout.CENTER);
        panel.add(sizePanel, BorderLayout.SOUTH);

        return panel;
    }

    private void checkCenterPane() {
        cardLayout.show(centerPane, commonORCustom.getSelectedItem());
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("Plugin-ChartF_Marker");
    }

    public void populate(VanChartAttrMarker marker) {
        if(marker == null){
            marker = new VanChartAttrMarker();
        }
        commonORCustom.setSelectedIndex(marker.isCommon() ? 0 : 1);
        if(marker.isCommon()){
            markerPane.setSelectedMarker(MarkerFactory.createMarker(marker.getMarkerType().getType()));
            markerFillColor.populate(marker.getColorBackground());
        } else {
            if(marker.getImageBackground() != null){
                imageBackgroundPane.populateBean(marker.getImageBackground());
            }
        }
        radius.setValue(marker.getRadius());
        width.setValue(marker.getWidth());
        height.setValue(marker.getHeight());

        checkCenterPane();
    }

    public VanChartAttrMarker update() {
        VanChartAttrMarker marker = new VanChartAttrMarker();
        marker.setCommon(commonORCustom.getSelectedIndex() == 0);
        if(marker.isCommon()){
            marker.setColorBackground(markerFillColor.update());
            marker.setRadius(radius.getValue());
            marker.setMarkerType(MarkerType.parse(markerPane.getSelectedMarkder().getMarkerType()));
        } else {
            marker.setMarkerType(MarkerType.MARKER_NULL);
            ImageBackground background = (ImageBackground)imageBackgroundPane.updateBean();
            background.setLayout(Constants.IMAGE_EXTEND);
            marker.setImageBackground(background);
            marker.setWidth(width.getValue());
            marker.setHeight(height.getValue());
        }
        return marker;
    }
}
