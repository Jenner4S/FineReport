package com.fr.design.chart.series.PlotSeries;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.data.core.DataCoreUtils;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import org.apache.batik.swing.svg.SVGFileFilter;

/**
 * �Զ����ͼ����.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-15 ����03:38:15
 */
public class MapCustomPane extends BasicBeanPane<String> implements AbstrctMapAttrEditPane{ // �����ͼ��Ӧ���ֶ�. ����, ����. shape (�� ��Բ�δ���)

	private FilterComboBox<String> areaString;// �����ֶ�
	private DatabaseTableDataPane tableDataNameBox;// ���ݼ�����  + �������Ԥ����ť
	private MapImageEditPane imageShowPane; // ͼƬչʾ�༭�Ľ���
	private String lastSelectPath;
	private boolean isNeedDataSource = true;

	public MapCustomPane() {
		initComp();
	}

	public MapCustomPane(boolean isNeedDataSource){
		this.isNeedDataSource = isNeedDataSource;
		initComp();
	}

	private void initComp() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.NORTH);

		pane.setLayout(new BorderLayout());

		pane.add(northPaneCreate(), BorderLayout.NORTH);

		imageShowPane = new MapImageEditPane();

		pane.add(imageShowPane, BorderLayout.CENTER);
	}

	private JPanel northPaneCreate() {
		JPanel northPane = new JPanel();

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UIButton loadMap = new UIButton(Inter.getLocText("FR-Chart-Import_Map"));
		loadMap.setPreferredSize(new Dimension(160, 20));
		northPane.add(loadMap);

		loadMap.addActionListener(selectPictureActionListener);

		if(isNeedDataSource){
			UILabel label =new UILabel(Inter.getLocText("FR-Chart-Table_Data") + ":", SwingConstants.RIGHT) ;

            tableDataNameBox = new DatabaseTableDataPane(label) {
                  protected void userEvent() {
refreshAreaNameBox();
}
            };
            tableDataNameBox.setPreferredSize(new Dimension(200, 20));
            northPane.add(tableDataNameBox);

            northPane.add(new BoldFontTextLabel(Inter.getLocText(new String[]{"Filed", "Field"}) + ":"));

            areaString = new FilterComboBox<String>();
            areaString.setPreferredSize(new Dimension(120, 20));
            areaString.addItemListener(areaChange);
            northPane.add(areaString);
		}


		return northPane;
	}

	private ActionListener selectPictureActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent evt) {
            JFileChooser svgFileChooser = new JFileChooser();
            svgFileChooser.addChoosableFileFilter(new SVGFileFilter());
			if (StringUtils.isNotBlank(lastSelectPath)) {
				svgFileChooser.setSelectedFile(new File(lastSelectPath));
			}
			int returnVal = svgFileChooser.showOpenDialog(DesignerContext.getDesignerFrame());
			if (returnVal != JFileChooser.CANCEL_OPTION) {
				File selectedFile = svgFileChooser.getSelectedFile();
				lastSelectPath = selectedFile.getAbsolutePath();
				if (selectedFile != null && selectedFile.isFile()) {
                    imageShowPane.setSvgMap(selectedFile.getPath());
                    imageShowPane.repaint();
				}
			}
		}
	};

	private ItemListener areaChange = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Object select = areaString.getSelectedItem();
			if (select != null) {
				String colName = Utils.objectToString(areaString.getSelectedItem());

				TableDataWrapper tableDataWrappe = tableDataNameBox.getTableDataWrapper();
				TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
				if (tableDataWrappe == null || source == null) {
					return;
				}

				String[] values = DataCoreUtils.getColValuesInData(source, tableDataWrappe.getTableDataName(), colName);
				ArrayList list = new ArrayList();
				for(int i = 0; i < values.length; i++) {
					list.add(values[i]);
				}
			
				imageShowPane.refreshFromDataList(list);
			}
		}
	};

	/**
	 * ѡ�з�ʽ: ������ߵ�
	 */
	public void setImageSelectType(int selectType) {
		if (imageShowPane != null) {
			imageShowPane.setEditType(selectType);
		}
	}

	private void refreshAreaNameBox() {// ˢ�����������б�
		if(!isNeedDataSource){
			return;
		}
		TableDataWrapper tableDataWrappe = tableDataNameBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();

		areaString.removeAllItems();
		areaString.setItemList(columnNameList);
	}

    /**
     * ��ǰ���ڱ༭����Ŀ�����(���ң�ʡ��)���͵�ͼ��
     * @param typeName �����
     * @param mapName ��ͼ��
     */
    public void setTypeNameAndMapName(String typeName, String mapName){
        imageShowPane.setTypeNameAndMapName(typeName, mapName);
    }

	/**
	 * ���ݵ�ͼ���� ������Ϣ
	 */
	public void populateBean(String list) {
		imageShowPane.populateBean(list);
	}

	/**
	 * ���ݵ�ͼ���� ������Ϣ
	 */
	public String updateBean() {
		// ��ͼ���͵� ����Helper
		return imageShowPane.updateBean();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Datasource-User_Defined", "Chart-Map"});
	}

	/**
      * ���½���
      * @param attr  ��ͼ����
    */
	public void populateMapAttr(MapSvgAttr attr) {
		imageShowPane.populateMapSvgAttr(attr);
	}

	/**
      * ����MapSvgAttr
      * @return  ��������
	 */
	public MapSvgAttr updateCurrentAttr() {
		return imageShowPane.updateWithOutSave();
	}
}
