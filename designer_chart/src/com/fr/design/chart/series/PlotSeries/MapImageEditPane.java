package com.fr.design.chart.series.PlotSeries;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.base.*;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartglyph.MapShapeValue;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UISelectTable;
import com.fr.design.gui.itable.UITableNoOptionUI;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * ��ͼ ͼƬ�༭���� ѡ��ͼƬ �༭. ֧�����ѡ�е��¼�
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-10-15 ����04:17:28
 */
public class MapImageEditPane extends BasicBeanPane<String> {
	private static final long serialVersionUID = -5925535686784344616L;
	private static final double ARCSIZE = 7;
	private static final int LOCATIONOFFSET = 10;
	private static final int NAME_EDIT_PANE_WIDTH  = 130;
	private static final int NAME_EDIT_PANE_HEIGHT = 225;
	
	private ImageEditPane imageEditPane;
	private UISelectTable recordTable;
	private int editType = 0;// ֻ���mark���߱�ǳ�·��
	private List<String> fromDataList = new ArrayList<String>();// �༭ʱ�����ݼ���ȡ����ʹ���ֶ�.

	private HashMap<String, ArrayList<String>> resultAreaShape = new HashMap<String, ArrayList<String>>();// ���� ��Ӧͼ�� // �ѱ༭��ȫ����ָ����ɫ
    private String currentNodeName ; //��ǰѡ�еĽڵ������
    private String typeName = "";
    private String mapName = "";

	private String mouseSelectListName = "";
	private String editMapName = "";

	public MapImageEditPane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));

		imageEditPane = new ImageEditPane();
		this.add(imageEditPane, BorderLayout.CENTER);

		recordTable = new UISelectTable(1){
			public int columnAtPoint(Point point) {
				//ֻ��һ��
				return 0;
			}
		};
		recordTable.addSelectionChangeListener(new ChangeListener() {
			@Override
			public void fireChanged(ChangeEvent event) {
				mouseSelectListName = Utils.objectToString(event.getSource());

				imageEditPane.repaint();
				MapImageEditPane.this.repaint();
			}
		});

		recordTable.setUI(new UITableNoOptionUI());

		recordTable.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				List<Object[]> names = recordTable.updateBean();//  �õ����е�List

				List<String> test = new ArrayList<String>();
				for (int i = 0; i < names.size(); i++) {
					test.add(Utils.objectToString(recordTable.getValueAt(i, 0)));
				}

				Iterator<String> keys = resultAreaShape.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					if (!test.contains(key)) {
						keys.remove();
					}
				}
				recordTable.revalidate();
				repaint();
			}
		});

		UIScrollPane pane = new UIScrollPane(recordTable);
		pane.setPreferredSize(new Dimension(150, 320));
		pane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Filed", "WF-Name"})));

		this.add(pane, BorderLayout.EAST);
	}

	/**
	 * ���ñ༭������: ���� ���� ��
	 */
	public void setEditType(int editType) {
		this.editType = editType;
	}

	/**
	 * ���ر༭������: ���� ���� ��
	 */
	public int getEditType() {
		return editType;
	}

    /**
     * �������ڱ༭��svg�ļ�
     */
    public void setSvgMap(String filePath){
        resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.setSvgMap(filePath);

        repaint();
    }

	/**
	 * ���ͼƬ����
	 */
	public void clearSvgMap(){
		resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.clearSvgMap();

        repaint();
	}

    /**
     * �������ڱ༭��svg�ļ�
     * @param attr �Ѿ���ȡ�����ļ�
     */
    public void setSvgMap(MapSvgAttr attr){
        resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.setSvgMap(attr);

        repaint();
    }

	/**
	 * ˢ�������б��е�����
     * @param list �б�
	 */
	public void refreshFromDataList(List list) {
		fromDataList.clear();

		for (Object aList : list) {
			fromDataList.add(Utils.objectToString(aList));
		}
	}

    /**
     * ��ǰ���ڱ༭����Ŀ�����(���ң�ʡ��)���͵�ͼ��
     * @param typeName �����
     * @param mapName ��ͼ��
     */
    public void setTypeNameAndMapName(String typeName, String mapName){
        this.typeName = typeName;
        this.mapName = mapName;
    }

	private class ImageEditPane extends JComponent implements MouseListener, MouseMotionListener {

        private MapSvgAttr currentSvgMap;//��ǰѡ�е�svg��ͼ
		private GeneralPath selectedShape; // ��ǰѡ�е�Shape
		private Image image = BaseUtils.readImage("");// ��ѡ���ͼƬ

        //ƽ�Ƶ�λ��
		private double moveLeft = 0;
		private double moveTop = 0;

        //�������λ��
		private double mouseStartX;
		private double mouseStartY;

		private boolean dragged = false;

		public ImageEditPane() {
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {// ѡ��ͼƬ֮��, ��¼ѡ�е�����, ���е� ��ǵ�
			Rectangle bounds = this.getBounds();
			if (bounds == null || this.image == null) {
				return;
			}

			dealReady4Paint(g, bounds);

			int imageWidth = this.image.getWidth(new JPanel());
			int imageHeight = this.image.getHeight(new JPanel());

			Graphics2D g2d = (Graphics2D) g;
			if (this.image != null) {// ̫С��Ŵ�, ̫��, ��Ĭ���϶�
				g2d.drawImage(this.image, (int) moveLeft, (int) moveTop, imageWidth, imageHeight, new JPanel()); // ֻ���ƶ���ʼλ��
			}

			g2d.translate(moveLeft, moveTop);
			g2d.setStroke(new BasicStroke(1));
			if (resultAreaShape != null && !resultAreaShape.isEmpty()) {
				for (String key : resultAreaShape.keySet()) {

					GeneralPath selectShape = getSelectedNodePath(key);
					g2d.setColor(Color.green);
                    g2d.draw(selectShape);
				}
			}

			g2d.setColor(Color.blue);
			if (StringUtils.isNotEmpty(mouseSelectListName) && resultAreaShape.containsKey(mouseSelectListName)) {// ��ǰ����ѡ�е� list, �Լ�����������ʾ, ���� �߿�, ����
				GeneralPath highSelect = getSelectedNodePath(mouseSelectListName);
				if (highSelect != null) {
                    g2d.fill(highSelect);
				}
			} else if (selectedShape != null) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.fill(selectedShape);
			}

			g2d.translate(-moveLeft, -moveTop);
		}
		
		private void dealReady4Paint(Graphics g, Rectangle bounds) {
			super.paintComponent(g);

			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			int width = (int) bounds.getWidth();
			int height = (int) bounds.getHeight();
			g.clipRect(x, y, width, height);
		}


        //��ʼ����ͼƬ��״̬
		private void initImage() {
            this.image = currentSvgMap.getMapImage();
            CoreGraphHelper.waitForImage(this.image);

			this.selectedShape = null;
			this.moveLeft = 0;
			this.moveTop = 0;
		}

        /**
         * ���õ�ǰ���ڱ༭���ļ�
         * @param filePath �ļ�·��
         */
        public void setSvgMap(String filePath){
            currentSvgMap = new MapSvgAttr(filePath);
            currentSvgMap.setMapTypeAndName(typeName, mapName);
            initImage();
        }

        /**
         * ���õ�ǰ���ڱ༭���ļ�
         * @param attr ��ȡ�����ļ�
         */
        public void setSvgMap(MapSvgAttr attr){
            currentSvgMap = attr;

            initImage();
        }

		public void clearSvgMap(){
			currentSvgMap = null;
			this.image = BaseUtils.readImage("");;
            this.selectedShape = null;
            this.moveLeft = 0;
            this.moveTop = 0;
			this.dragged = false;
			this.mouseStartX = 0;
			this.mouseStartY = 0;
		}


		public Image getImage() {
			return this.image;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			drawSelectShape(e);
			if (e.getClickCount() == 2) {// 2�α༭, ��Ȼ�����״��� �� update ����
				showEditNamePane(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.mouseStartX = e.getPoint().getX();
			this.mouseStartY = e.getPoint().getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			drawWhenDragEnd(e);
		}

		public void mouseDragged(MouseEvent e) {
			this.dragged = true;
		}

		public void mouseMoved(MouseEvent e) {

		}

		private void drawWhenDragEnd(MouseEvent e) {
			if (this.image == null || this.getBounds() == null) {
				return;
			}

			if (dragged) {
				double endX = e.getPoint().getX();
				double endY = e.getPoint().getY();

				int imageWidth = image.getWidth(new JPanel());
				int imageHeight = image.getHeight(new JPanel());

				int paneWidth = (int) this.getBounds().getWidth();
				int paneHeight = (int) this.getBounds().getHeight();

				if (imageWidth > paneWidth) {
					double offX = endX - this.mouseStartX;
					this.moveLeft += offX;

					this.moveLeft = Math.max(this.moveLeft, paneWidth - imageWidth);
					this.moveLeft = Math.min(0, this.moveLeft);
				} else {
					this.moveLeft = 0;
				}

				if (imageHeight > paneHeight) {
					double offY = endY - this.mouseStartY;
					this.moveTop += offY;

					this.moveTop = Math.max(this.moveTop, paneHeight - imageHeight);
					this.moveTop = Math.min(0, this.moveTop);
				} else {
					this.moveTop = 0;
				}
				this.repaint();
			}

			this.dragged = false;
		}

        //�������������
		private void drawSelectShape(MouseEvent e) {
			selectedShape = null;
			mouseSelectListName = StringUtils.EMPTY;
			if (this.image == null) {
				return;
			}

			Point ePoint = e.getPoint();
			Point select = new Point((int) (ePoint.getX() - moveLeft), (int) (ePoint.getY() - moveTop));// ֧��ctrl ѡ��ʱ�Ķ�ѡ..  ֻ�Ǽ�¼������λ�� Ȼ��shape�ϲ�
			boolean gotSelectedShape = false;
			for (String key : resultAreaShape.keySet()) {
				GeneralPath mapSelect = getSelectedNodePath(key);
				if (mapSelect.contains(select)) {
					selectedShape = mapSelect;
					currentNodeName = currentSvgMap.getSelectedPathName(select);
					gotSelectedShape = true;
					break;
				}
			}

			if (!gotSelectedShape) {// ��������������ͼƬ��ѡȡShape

				if (getEditType() == MapShapeValue.AREA) {
					selectedShape = currentSvgMap.getSelectPath(select);
                    currentNodeName = currentSvgMap.getSelectedPathName(select);
				} else {
                    //��ǵ����͵�Ҫ��Ҫ����

				}
			}
			this.repaint();
		}

		private void showEditNamePane(MouseEvent e) {
			if (this.image == null || selectedShape == null) {
				return;
			}
			final EditNamePane namePane = new EditNamePane();
			Point ePoint = e.getPoint();
			final Point select = new Point((int) (ePoint.getX() - moveLeft), (int) (ePoint.getY() - moveTop));
			namePane.setEditViewRow(getEditViewRow(select));
			String isSelectName = StringUtils.EMPTY;
			for (String name : resultAreaShape.keySet()) {
				GeneralPath shape = getSelectedNodePath(name);
				if (shape.contains(select)) {
					isSelectName = name;
					break;
				}
			}
			namePane.populateBean(isSelectName);
			namePane.resetPaneWithNewNameList(fromDataList);
            UIDialog bg = namePane.showUnsizedWindow(SwingUtilities.getWindowAncestor(ImageEditPane.this), new DialogActionAdapter() {
				public void doOk() {
					namePane.changeList();
					String endName = namePane.updateBean();
					if (resultAreaShape.containsKey(endName)) {
						if(ComparatorUtils.equals(endName,namePane.startName)) {
							return;
						}
						ArrayList<String> pathID = resultAreaShape.get(endName);
						if(!pathID.contains(currentNodeName)){
							pathID.add(currentNodeName);
						}
					}else{
						ArrayList<String> paths = new ArrayList<String>();
						resultAreaShape.put(endName,paths);
						paths.add(currentNodeName);
						ArrayList<String> exists = resultAreaShape.get(namePane.startName);
						if(exists!= null){
							 for(String id:exists){
								 paths.add(id);
							 }
							resultAreaShape.remove(namePane.startName);
						}
					}
				}
			});
			bg.setSize(NAME_EDIT_PANE_WIDTH, NAME_EDIT_PANE_HEIGHT);
			bg.setLocation((int) (e.getLocationOnScreen().getX()) + LOCATIONOFFSET, (int) e.getLocationOnScreen().getY());
			bg.setTitle(Inter.getLocText(new String[]{"Edit", "Filed", "WF-Name"}));
			bg.setVisible(true);
		}

		// viewList ������������ �õ�����һ��.
		public int getEditViewRow(Point point) {
			int rowIndex = recordTable.getRowCount();// ��� û���ҵ�Ӧ��Ҳ�Ƿ��� row + 1

			// ���ݱ༭��λ��, �Ƿ���shape��Ӧ, �����ҳ�, û�������µ���
			String isSelectName = StringUtils.EMPTY;
			for (String name : resultAreaShape.keySet()) {
				GeneralPath shape = getSelectedNodePath(name);
				if (shape.contains(point)) {
					isSelectName = name;
					break;
				}
			}

			if (resultAreaShape.containsKey(isSelectName)) {
				for (int i = 0; i < recordTable.getRowCount(); i++) {
					String tmp = (String) recordTable.getValueAt(i, 0);
					if (ComparatorUtils.equals(isSelectName, tmp)) {
						rowIndex = i;
						break;
					}
				}
			}// û�������һ��

			return rowIndex;
		}
	}

	// ͼƬ����  ������������������б�
	private class EditNamePane extends BasicBeanPane<String> {// ����༭����

		private UITextField nameText; //  �ı���
		private JList dataList;
		private String startName;

		private JList hasNamedList;

		private int editViewRow = -1;// ����Ӧ��view��index�� ,  ȷ����ǰ���ڱ༭��viewList����, Ȼ��ı�ֵ

		private UILabel namedLabel = new BoldFontTextLabel(Inter.getLocText("FR-Chart-Pre_Defined") + "------");
		private JPanel listPane = new JPanel();

		public EditNamePane() {
			initCom();
		}

		public void setEditViewRow(int index) {
			this.editViewRow = index;
		}

		private void initCom() {
			this.setLayout(new BorderLayout(0, 0));

			nameText = new UITextField();
			nameText.setPreferredSize(new Dimension(100, 20));

			this.add(nameText, BorderLayout.NORTH);

			listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
			this.add(listPane, BorderLayout.CENTER);

			listPane.add(new UIScrollPane(dataList = new JList(new DefaultListModel())));
			dataList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() >= 2 && dataList.getSelectedValue() != null) {
						String value = Utils.objectToString(dataList.getSelectedValue());
						nameText.setText(value);// ֱ�Ӷ��ı�ֵ�ı�, ���ı�ȥ�����¼�
					}
				}
			});

			listPane.add(namedLabel);
			listPane.add(new UIScrollPane(hasNamedList = new JList(new DefaultListModel())));

			hasNamedList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() >= 2 && hasNamedList.getSelectedValue() != null) {
						nameText.setText(Utils.objectToString(hasNamedList.getSelectedValue()));
					}
				}
			});
		}

		private void relayoutList() {
			listPane.removeAll();

			listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
			listPane.add(new UIScrollPane(dataList));

			if (hasNamedList.getModel().getSize() > 0) {
				listPane.add(namedLabel);
				listPane.add(new UIScrollPane(hasNamedList));
			}
		}


        //�б�ı䣬ѡ�е�ʱ�����shape��Ϣ
		private void changeList() {
			String textValue = nameText.getText();
			if (editViewRow >= recordTable.getRowCount()) {// ���һ��  ȷ�������� ��ʱ����viewCount
				recordTable.addLine(new String[]{textValue});
			} else if (editViewRow > -1) {
				recordTable.setValueAt(textValue, editViewRow, 0);
			}
			recordTable.revalidate();
			recordTable.repaint();

			resetPaneWithNewNameList(fromDataList);
		}

		public void resetPaneWithNewNameList(List<String> list) {// ���治��, ֻ��ˢ������list
			DefaultListModel model = (DefaultListModel) dataList.getModel();
			model.removeAllElements();

			DefaultListModel hasNameModel = (DefaultListModel) hasNamedList.getModel();
			hasNameModel.removeAllElements();

			for (int i = 0; list != null && i < list.size(); i++) {
				String value = list.get(i);
				if (!resultAreaShape.containsKey(value)) {
					model.addElement(value);
				}
			}

			for (String name : resultAreaShape.keySet()) {
				if (!hasNameModel.contains(name)) {
					hasNameModel.addElement(name);
				}
			}

			relayoutList();
		}

		public void populateBean(String list) {
			nameText.setText(list);
			startName = list;
			nameText.setCaretPosition(list == null ? 0 : list.length());
		}

		@Override
		public String updateBean() {
			return nameText.getText();
		}

		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText(new String[]{"Edit", "Image"});
		}
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Edit", "Image"});
	}

	/**
	 * ���µ�ͼ �����б��Լ����ڱ༭��ͼƬ
	 */
	public void populateBean(String ob) {// ȷ��Ҫ�༭�ĵ�ͼ����
		this.editMapName = ob;// ��ǰ�༭�ĵ�ͼ����
        MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
		if (mapHelper.containsMapName(editMapName)) {
            MapSvgAttr svgAttr = mapHelper.getMapAttr(editMapName);
			populateMapSvgAttr(svgAttr);
		}else if(mapHelper.getNewMapAttr(editMapName) != null ){
			clearSvgMap();
			imageEditPane.currentSvgMap = mapHelper.getMapAttr(editMapName);
		}else {
			clearSvgMap();
		}
	}

	/**
	 * ����༭�ĵ�ͼ ��״����Ϣ.
	 */
	public String updateBean() {
		if(imageEditPane.currentSvgMap !=null){
			this.editMapName = imageEditPane.currentSvgMap.getName();
		}
		// ����ͼ����helper �̶��洢   ��¼�̶���hShape  ԭͼƬ������λ��, Ȼ���ڸ���������ʵ�� ʵ������ չʾ
		if (StringUtils.isNotEmpty(editMapName) && this.imageEditPane.getImage() != null) {
            MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapAttr = imageEditPane.currentSvgMap;
			if(mapHelper.getNewMapAttr(editMapName)!=null){
				mapHelper.removeNewMapAttr(editMapName);
				mapHelper.pushMapAttr(editMapName,mapAttr);
			}
            if(mapHelper.containsMapName(editMapName)){
                //������mapAttr��ڵ�name����
                updateMapShapePath(mapAttr);
                mapHelper.addCustomSvgMap(editMapName, mapAttr);

                //�����Ժ�Ҫд��
                mapAttr.writeBack(editMapName);
            }
		}

		return editMapName;
	}

	/**
      * ���½���
      * @param svgAttr  ��ͼ����
	 * */
	public void populateMapSvgAttr(MapSvgAttr svgAttr){
		if(svgAttr == null || svgAttr.getMapImage() == null){
			clearSvgMap();
			return;
		}
		setSvgMap(svgAttr);
		Iterator names = svgAttr.shapeValuesIterator();
		while (names.hasNext()) {
			String name = Utils.objectToString(names.next());
			ArrayList<String> pathIDs= svgAttr.getExistedShapePathID(name);
			if (name != null) {
				resultAreaShape.put(name,pathIDs);
				recordTable.addLine(new String[]{name});
			}
		}
		recordTable.revalidate();
	}

	/**
      * ����MapSvgAttr
      * @return  ��������
	 * */
	public MapSvgAttr updateWithOutSave(){
		if(imageEditPane.currentSvgMap !=null){
			this.editMapName = imageEditPane.currentSvgMap.getName();
		}
		// ����ͼ����helper �̶��洢   ��¼�̶���hShape  ԭͼƬ������λ��, Ȼ���ڸ���������ʵ�� ʵ������ չʾ
		if (StringUtils.isNotEmpty(editMapName) && this.imageEditPane.getImage() != null) {
            MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapAttr = imageEditPane.currentSvgMap;
			if(mapHelper.getNewMapAttr(editMapName)!=null){
				mapHelper.removeNewMapAttr(editMapName);
				mapHelper.pushMapAttr(editMapName,mapAttr);
			}
            if(mapHelper.containsMapName(editMapName)){
                //������mapAttr��ڵ�name����
                updateMapShapePath(mapAttr);
                mapHelper.addCustomSvgMap(editMapName, mapAttr);
            }
			return mapAttr;
		}
		return null;
	}

	private void updateMapShapePath(MapSvgAttr mapSvgAttr){
		mapSvgAttr.clearExistShape();
		for(String key : resultAreaShape.keySet()){
			ArrayList<String> nodeName = resultAreaShape.get(key);
			if(nodeName == null){
				continue;
			}
			for(String node :nodeName){
				mapSvgAttr.setNodeName(node, key);
			}
		}
	}

	private GeneralPath getSelectedNodePath(String nodeName){
		if(imageEditPane.currentSvgMap == null){
			return new GeneralPath();
		}
		MapSvgAttr attr = imageEditPane.currentSvgMap;
		ArrayList<String> pathsID = resultAreaShape.get(nodeName);
		GeneralPath unionPath =new GeneralPath();
        //��߱������½���һ��path��append����Ȼֱ���ڴ��path��append����Ȼ����ɾ������ˣ�Ҳ����������ѡ����
        for(String id :pathsID){
			unionPath.append(attr.getPath4PathID(id),false);

        }
        return unionPath;
	}

}
