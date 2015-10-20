package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.StockPlot;
import com.fr.chart.chartdata.StockLabel;
import com.fr.chart.chartdata.StockTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.UIEditLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * �ɼ�ͼ ���Ա� ���ݼ� ���ݽ���
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-12-19 ����04:38:28
 */
public class StockPlotTableDataContentPane extends AbstractTableDataContentPane {

	private static final String TIMEAXIS = Inter.getLocText("Chart-Horizontal_TimeAxis");
	private static final int LABEL_WIDTH = 105;

	private UIComboBox axisBox;
	private UIComboBox volumeBox;
	private UIComboBox openBox;
	private UIComboBox highBox;
	private UIComboBox lowBox;
	private UIComboBox closeBox;

	private UIEditLabel volumeLabel = new UIEditLabel(StockLabel.VOLUMEN,SwingConstants.RIGHT){
		protected void doAfterMousePress(){
			clearBackGround();
		}
	};
	private UIEditLabel openLabel = new UIEditLabel(StockLabel.OPEN,SwingConstants.RIGHT){
		protected void doAfterMousePress(){
			clearBackGround();
		}
	};
	private UIEditLabel highLabel = new UIEditLabel(StockLabel.HIGHT,SwingConstants.RIGHT){
		protected void doAfterMousePress(){
			clearBackGround();
		}
	};
	private UIEditLabel lowLabel = new UIEditLabel(StockLabel.LOW,SwingConstants.RIGHT){
		protected void doAfterMousePress(){
			clearBackGround();
		}
	};
	private UIEditLabel closeLabel = new UIEditLabel(StockLabel.CLOSE,SwingConstants.RIGHT){
		protected void doAfterMousePress(){
			clearBackGround();
		}
	};

	public StockPlotTableDataContentPane(ChartDataPane parent) {// TODO ����ͳһ�Ķ���ģʽ: ԭ�ȵĻ���: ���ֲ����׺����Զ�Ӧ
		this.setLayout(new BorderLayout());
		
		axisBox = new UIComboBox();
		volumeBox = new UIComboBox();
		openBox = new UIComboBox();
		highBox = new UIComboBox();
		lowBox = new UIComboBox();
		closeBox = new UIComboBox();

		//ember:Ϊ��Ӣ�İ�������ʾ���������ȴ�100�ĳ���80
		axisBox.setPreferredSize(new Dimension(90, 20));
		volumeBox.setPreferredSize(new Dimension(90, 20));
		openBox.setPreferredSize(new Dimension(90, 20));
		highBox.setPreferredSize(new Dimension(90, 20));
		lowBox.setPreferredSize(new Dimension(90, 20));
		closeBox.setPreferredSize(new Dimension(90, 20));

		double p = TableLayout.PREFERRED;
		double[] columnSize = {LABEL_WIDTH,p};
		double[] rowSize = { p, p,p,p,p,p,p, p, p};

		Component[][] components = new Component[][]{
				new Component[]{new BoldFontTextLabel(TIMEAXIS+":",SwingConstants.RIGHT),axisBox},
				new Component[]{volumeLabel,volumeBox},
				new Component[]{openLabel,openBox},
				new Component[]{highLabel,highBox},
				new Component[]{lowLabel,lowBox},
				new Component[]{closeLabel,closeBox} ,

		} ;

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.add(panel,BorderLayout.CENTER);

        volumeBox.addItem(StockPlot.NONE);
		openBox.addItem(StockPlot.NONE);
		
		axisBox.addItemListener(tooltipListener);
		volumeBox.addItemListener(tooltipListener);
		openBox.addItemListener(tooltipListener);
		highBox.addItemListener(tooltipListener);
		lowBox.addItemListener(tooltipListener);
		closeBox.addItemListener(tooltipListener);
	}


	private void clearBackGround(){
		volumeLabel.resetNomalrBackground();
		openLabel.resetNomalrBackground();
		highLabel.resetNomalrBackground();
		lowLabel.resetNomalrBackground();
		closeLabel.resetNomalrBackground();
	}
	
	protected void refreshBoxListWithSelectTableData(List list) {
		refreshBoxItems(axisBox, list);
		refreshBoxItems(volumeBox, list);
		refreshBoxItems(openBox, list);
		refreshBoxItems(highBox, list);
		refreshBoxItems(lowBox, list);
		refreshBoxItems(closeBox, list);
		
		volumeBox.addItem(StockPlot.NONE);
		openBox.addItem(StockPlot.NONE);
	}

    /**
     * ������е�box����
     */
    public void clearAllBoxList(){
        clearBoxItems(axisBox);
        clearBoxItems(volumeBox);
        clearBoxItems(openBox);
        clearBoxItems(highBox);
        clearBoxItems(lowBox);
        clearBoxItems(closeBox);

        volumeBox.addItem(StockPlot.NONE);
        openBox.addItem(StockPlot.NONE);
    }

	/**
	 * ���¹ɼ�ͼ ���ݽ���.
	 */
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
		
		TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
		if(definition instanceof StockTableDefinition) {
			StockTableDefinition stock = (StockTableDefinition)definition;

			StockLabel stockLabel = stock.getStockLabel();
			volumeLabel.setText(stockLabel.getVolumeLabel());
			openLabel.setText(stockLabel.getOpenLabel());
			highLabel.setText(stockLabel.getHighLabel());
			lowLabel.setText(stockLabel.getLowLabel());
			closeLabel.setText(stockLabel.getLowLabel());

			combineCustomEditValue(axisBox, stock.getCateTime());
			combineCustomEditValue(volumeBox, stock.getVolumnString());
			combineCustomEditValue(openBox, stock.getOpenString());
			combineCustomEditValue(highBox, stock.getHighString());
			combineCustomEditValue(lowBox, stock.getLowString());
			combineCustomEditValue(closeBox, stock.getCloseString());
		}
	}
	
	@Override
	public void updateBean(ChartCollection ob) {
		if(ob != null) {
			StockTableDefinition stock = new StockTableDefinition();
			ob.getSelectedChart().setFilterDefinition(stock);

			stock.setStockLabels(new StockLabel(volumeLabel.getText(),openLabel.getText(),highLabel.getText(),lowLabel.getText(),closeLabel.getText()));

			stock.setCateTime(Utils.objectToString(axisBox.getSelectedItem()));
			stock.setVolumnString(Utils.objectToString(volumeBox.getSelectedItem()));
			stock.setOpenString(Utils.objectToString(openBox.getSelectedItem()));
			stock.setHighString(Utils.objectToString(highBox.getSelectedItem()));
			stock.setLowString(Utils.objectToString(lowBox.getSelectedItem()));
			stock.setCloseString(Utils.objectToString(closeBox.getSelectedItem()));

			StockLabel stockLabel = stock.getStockLabel();
			volumeLabel.setText(stockLabel.getVolumeLabel());
			openLabel.setText(stockLabel.getOpenLabel());
			highLabel.setText(stockLabel.getHighLabel());
			lowLabel.setText(stockLabel.getLowLabel());
			closeLabel.setText(stockLabel.getCloseLabel());
		}
	}
}
