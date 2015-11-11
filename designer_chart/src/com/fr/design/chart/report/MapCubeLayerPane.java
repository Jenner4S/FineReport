package com.fr.design.chart.report;

import com.fr.base.MapXMLHelper;
import com.fr.base.Utils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartglyph.MapAttr;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * ��ͼ��ȡ�㼶����, Ĭ����ȡ��ͬ���ĵ�ͼ
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-11-20 ����03:39:08
 */
public class MapCubeLayerPane extends BasicBeanPane<String>{
	
	private JTree mapTree;
	private DefaultMutableTreeNode root;
	
	private String editingMap = "";

    private Set<String> editedMap = new HashSet<String>();
	
	private List<ChangeListener> fireWhenTreeChange = new ArrayList<ChangeListener>();
	
	private List<String> hasDealNames = new ArrayList<String>();

    private boolean isSvg = true;
	
	public MapCubeLayerPane() {
		initCom();
	}
	
	private void initCom() {
		this.setLayout(new BorderLayout());
		
		root = new DefaultMutableTreeNode();
		
		mapTree = new JTree(root);
		mapTree.setRootVisible(false);
		mapTree.addMouseListener(mapListener);
		
		JScrollPane treePane = new JScrollPane(mapTree);
		treePane.setPreferredSize(new Dimension(100, 100));
		this.add(treePane, BorderLayout.CENTER);
	}

    public void setSvg(boolean isSvg) {
        this.isSvg = isSvg;
    }
	
	/**
	 * ���ؽڵ�·��
	 */
	public int getTreeDepth() {
		return root.getDepth();
	}

    /**
     *  ��ʼ���ڵ����.
     * @param mapName ��ͼ����.
     */
	public void initRootTree(String mapName) {
		editingMap = mapName;

		root.removeAllChildren();
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(mapName);
		
		root.add(node);
		
		// ÿ���ڵ㶼Ҫȥ�ж��Ƿ��е�ͼ�ٰ���  Ĭ����ȡ��ͬ���ĵ�ͼ
        if(isSvg) {
            MapSvgAttr mapAttr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
            hasDealNames.clear();
            add4Node(mapAttr, node, mapName);
        } else {
            MapAttr mapAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(mapName);
            hasDealNames.clear();
            addBitMap4Node(mapAttr, node, mapName);
        }
		
		mapTree.doLayout();
		mapTree.validate();
		
		((DefaultTreeModel)mapTree.getModel()).reload();
		
		for(int i = 0; i < fireWhenTreeChange.size(); i++) {
			fireWhenTreeChange.get(i).stateChanged(new ChangeEvent(this));
		}
	}

    /**
     * �����Ӧ�¼�.
     * @param change  �ı���¼�.
     */
	public void addChangeListener(ChangeListener change) {
		fireWhenTreeChange.add(change);
	}

    private void addBitMap4Node(MapAttr editingMapAttr, DefaultMutableTreeNode node, String mapName) {
        MapAttr mapAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(mapName);
        if(mapAttr != null) {
            Iterator namesValue = mapAttr.shapeValuesIterator();
            while(namesValue.hasNext()) {
                Object names = namesValue.next();
                String nextToName = Utils.objectToString(editingMapAttr.getLayerTo(Utils.objectToString(names)));

                MapAttr tmpAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(nextToName);
                if(tmpAttr != null) {
                    DefaultMutableTreeNode currentName = new DefaultMutableTreeNode(names);
                    node.add(currentName);// ����ȡ���ݵĵ�ͼ����ӽڵ�

                    if(!hasDealNames.contains(Utils.objectToString(currentName.getUserObject()))) {
                        hasDealNames.add(Utils.objectToString(currentName.getUserObject()));
                        addBitMap4Node(editingMapAttr, currentName, nextToName);
                    }
                }
            }
        }
    }
	
	private void add4Node(MapSvgAttr editingMapAttr, DefaultMutableTreeNode node, String mapName) {
		MapSvgAttr mapAttr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
		if(mapAttr != null) {
			Iterator namesValue = mapAttr.shapeValuesIterator();
			while(namesValue.hasNext()) {
				Object names = namesValue.next();
				String nextToName = Utils.objectToString(editingMapAttr.getLayerTo(Utils.objectToString(names)));

				MapSvgAttr tmpAttr = MapSvgXMLHelper.getInstance().getMapAttr(nextToName);
				if(tmpAttr != null) {
					DefaultMutableTreeNode currentName = new DefaultMutableTreeNode(names);
					node.add(currentName);// ����ȡ���ݵĵ�ͼ����ӽڵ�
					
					if(!hasDealNames.contains(Utils.objectToString(currentName.getUserObject()))) {
						hasDealNames.add(Utils.objectToString(currentName.getUserObject()));
						add4Node(editingMapAttr, currentName, nextToName);
					}
				}
			}
		}
	}
	
	MouseListener mapListener = new MouseAdapter() {
		  public void mouseClicked(MouseEvent e) {
			  final TreePath visiblePath = mapTree.getSelectionPath(); // ȡ�õ�ǰѡ�еĽڵ� selectTreeName
			  if(visiblePath == null) {
				  return;
			  }
			  final String selectTreeName = Utils.objectToString(((DefaultMutableTreeNode)visiblePath.getLastPathComponent()).getUserObject());
			  if(SwingUtilities.isRightMouseButton(e)) {
                  if(!isSvg){
                      popBitMapDialog(e, selectTreeName);
                      return;
                  }
				  final MapSvgAttr editingAttr = MapSvgXMLHelper.getInstance().getMapAttr(editingMap);
				  editedMap.add(editingMap);
				  final MapCubeSetDataPane setDataPane = new MapCubeSetDataPane();
				  setDataPane.freshComboxNames();
				  MapSvgAttr mapAttr = MapSvgXMLHelper.getInstance().getMapAttr(selectTreeName);
                  editedMap.add(selectTreeName);
				  if(mapAttr != null) {// ����ȡ����Ӧ��ϵ  // ȡ����ǰ�ڵ���������Ӧ�ĵ�ͼ
					  List list = new ArrayList();
					  Iterator names = mapAttr.shapeValuesIterator();//��:  ��ͼ������(String) + ��Ӧ��ͼ��(String)
					  while(names.hasNext()) {
						  Object name = names.next();
                          String layName = editingAttr.getLayerTo(Utils.objectToString(name));
                          if(ArrayUtils.contains(ChartConstants.NONE_KEYS, layName)) {// kunsnat: �����л����������.
                              layName = StringUtils.EMPTY;
                          }
						  list.add(new Object[]{name, layName});
					  }
					  setDataPane.populateBean(list);
				  }

				  int x = (int)(mapTree.getLocationOnScreen().getX() + mapTree.getWidth());
				  int y = (int)e.getLocationOnScreen().getY();

                  UIDialog dialog = setDataPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(setDataPane), new DialogActionAdapter() {
					  public void doOk() {
						  List list = setDataPane.updateBean(); // ���µ�ǰ�� ��ͼ������Ӧ�� ��������ȡ��ϵ ����inittree
						  for(int i = 0; i < list.size(); i++) {
							  Object[] values = (Object[])list.get(i);
							  editingAttr.putLayerTo(Utils.objectToString(values[0]), Utils.objectToString(values[1]));
						  }
						  initRootTree(editingMap);
						  saveMapInfo();
					  }
				  });
				  dialog.setSize(300, 300);
				  GUICoreUtils.centerWindow(dialog);
				  dialog.setVisible(true);
			  }
		  }
	};


    private void popBitMapDialog(MouseEvent e, final String selectTreeName) {
        final MapAttr editingAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(editingMap);
        editedMap.add(editingMap);
        final MapCubeSetDataPane setDataPane = new MapCubeSetDataPane();
        setDataPane.freshBitMapComboxNames();
        MapAttr mapAttr = (MapAttr)MapXMLHelper.getInstance().getMapAttr(selectTreeName);
        if(mapAttr != null) {// ����ȡ����Ӧ��ϵ  // ȡ����ǰ�ڵ���������Ӧ�ĵ�ͼ
            List list = new ArrayList();
            Iterator names = mapAttr.shapeValuesIterator();//��:  ��ͼ������(String) + ��Ӧ��ͼ��(String)
            while(names.hasNext()) {
                Object name = names.next();
                String layName = editingAttr.getLayerTo(Utils.objectToString(name));
                if(ArrayUtils.contains(ChartConstants.NONE_KEYS, layName)) {// kunsnat: �����л����������.
                    layName = StringUtils.EMPTY;
                }
                list.add(new Object[]{name, layName});
            }
            setDataPane.populateBean(list);
        }

        int x = (int)(mapTree.getLocationOnScreen().getX() + mapTree.getWidth());
        int y = (int)e.getLocationOnScreen().getY();

        UIDialog dialog = setDataPane.showUnsizedWindow(SwingUtilities.getWindowAncestor(setDataPane), new DialogActionAdapter() {
            public void doOk() {
                List list = setDataPane.updateBean(); // ���µ�ǰ�� ��ͼ������Ӧ�� ��������ȡ��ϵ ����inittree
                for(int i = 0; i < list.size(); i++) {
                    Object[] values = (Object[])list.get(i);
                    editingAttr.putLayerTo(values[0], values[1]);
                }
                initRootTree(editingMap);
                saveMapInfo();
            }
        });
        dialog.setSize(300, 300);
        GUICoreUtils.centerWindow(dialog);
        dialog.setVisible(true);
    }

    private void saveMapInfo() {
        final String[] mapNames = editedMap.toArray(new String[0]);
        if(isSvg){
            editedMap.clear();
        }
        SwingWorker worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                if(isSvg){
                    MapSvgXMLHelper.getInstance().saveEditedMaps(mapNames);
                } else {
                    MapXMLHelper.getInstance().writerMapSourceWhenEditMap();
                }
                return 0;
            }

            @Override
			protected void done() {
				FRLogger.getLogger().info("Map Save End");
			}
			
		};
		worker.execute();
		DesignerEnvManager.addWorkers(worker);
	}
	
	@Override
	public void populateBean(String rootMapName) {// ���ڵ�ĵ�ͼ����  ֻ�и��ڵ���ȷ����
		// �����ݿ���ȡ����ͼ���� �� �㼶��ϵ ˢ���б�
		initRootTree(rootMapName);
	}
	
	public void updateBean(String rootMapName) {// HashMap --> ��Ӧ�㼶��   ��ͼ����
		// ˢ���б�, �������ݿ��еĵ�ͼ����
		
	}

	@Override
	public String updateBean() {
		return "";
	}
	
	/**
	 * ���ؽ������.
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Map_Drill");
	}

}
