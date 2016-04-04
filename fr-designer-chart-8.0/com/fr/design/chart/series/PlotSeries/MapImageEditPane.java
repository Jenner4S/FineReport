// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UISelectTable;
import com.fr.design.gui.itable.UITableNoOptionUI;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MapImageEditPane extends BasicBeanPane
{
    private class EditNamePane extends BasicBeanPane
    {

        private UITextField nameText;
        private JList dataList;
        private String startName;
        private JList hasNamedList;
        private int editViewRow;
        private UILabel namedLabel;
        private JPanel listPane;
        final MapImageEditPane this$0;

        public void setEditViewRow(int i)
        {
            editViewRow = i;
        }

        private void initCom()
        {
            setLayout(new BorderLayout(0, 0));
            nameText = new UITextField();
            nameText.setPreferredSize(new Dimension(100, 20));
            add(nameText, "North");
            listPane.setLayout(new BoxLayout(listPane, 1));
            add(listPane, "Center");
            listPane.add(new UIScrollPane(dataList = new JList(new DefaultListModel())));
            dataList.addMouseListener(new MouseAdapter() {

                final EditNamePane this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    if(mouseevent.getClickCount() >= 2 && dataList.getSelectedValue() != null)
                    {
                        String s = Utils.objectToString(dataList.getSelectedValue());
                        nameText.setText(s);
                    }
                }

                
                {
                    this$1 = EditNamePane.this;
                    super();
                }
            }
);
            listPane.add(namedLabel);
            listPane.add(new UIScrollPane(hasNamedList = new JList(new DefaultListModel())));
            hasNamedList.addMouseListener(new MouseAdapter() {

                final EditNamePane this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    if(mouseevent.getClickCount() >= 2 && hasNamedList.getSelectedValue() != null)
                        nameText.setText(Utils.objectToString(hasNamedList.getSelectedValue()));
                }

                
                {
                    this$1 = EditNamePane.this;
                    super();
                }
            }
);
        }

        private void relayoutList()
        {
            listPane.removeAll();
            listPane.setLayout(new BoxLayout(listPane, 1));
            listPane.add(new UIScrollPane(dataList));
            if(hasNamedList.getModel().getSize() > 0)
            {
                listPane.add(namedLabel);
                listPane.add(new UIScrollPane(hasNamedList));
            }
        }

        private void changeList()
        {
            String s = nameText.getText();
            if(editViewRow >= recordTable.getRowCount())
                recordTable.addLine(new String[] {
                    s
                });
            else
            if(editViewRow > -1)
                recordTable.setValueAt(s, editViewRow, 0);
            recordTable.revalidate();
            recordTable.repaint();
            resetPaneWithNewNameList(fromDataList);
        }

        public void resetPaneWithNewNameList(java.util.List list)
        {
            DefaultListModel defaultlistmodel = (DefaultListModel)dataList.getModel();
            defaultlistmodel.removeAllElements();
            DefaultListModel defaultlistmodel1 = (DefaultListModel)hasNamedList.getModel();
            defaultlistmodel1.removeAllElements();
            for(int i = 0; list != null && i < list.size(); i++)
            {
                String s = (String)list.get(i);
                if(!resultAreaShape.containsKey(s))
                    defaultlistmodel.addElement(s);
            }

            Iterator iterator = resultAreaShape.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                String s1 = (String)iterator.next();
                if(!defaultlistmodel1.contains(s1))
                    defaultlistmodel1.addElement(s1);
            } while(true);
            relayoutList();
        }

        public void populateBean(String s)
        {
            nameText.setText(s);
            startName = s;
            nameText.setCaretPosition(s != null ? s.length() : 0);
        }

        public String updateBean()
        {
            return nameText.getText();
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText(new String[] {
                "Edit", "Image"
            });
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((String)obj);
        }






        public EditNamePane()
        {
            this$0 = MapImageEditPane.this;
            super();
            editViewRow = -1;
            namedLabel = new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Pre_Defined")).append("------").toString());
            listPane = new JPanel();
            initCom();
        }
    }

    private class ImageEditPane extends JComponent
        implements MouseListener, MouseMotionListener
    {

        private MapSvgAttr currentSvgMap;
        private GeneralPath selectedShape;
        private Image image;
        private double moveLeft;
        private double moveTop;
        private double mouseStartX;
        private double mouseStartY;
        private boolean dragged;
        final MapImageEditPane this$0;

        public void paintComponent(Graphics g)
        {
            Rectangle rectangle = getBounds();
            if(rectangle == null || image == null)
                return;
            dealReady4Paint(g, rectangle);
            int i = image.getWidth(new JPanel());
            int j = image.getHeight(new JPanel());
            Graphics2D graphics2d = (Graphics2D)g;
            if(image != null)
                graphics2d.drawImage(image, (int)moveLeft, (int)moveTop, i, j, new JPanel());
            graphics2d.translate(moveLeft, moveTop);
            graphics2d.setStroke(new BasicStroke(1.0F));
            if(resultAreaShape != null && !resultAreaShape.isEmpty())
            {
                GeneralPath generalpath1;
                for(Iterator iterator = resultAreaShape.keySet().iterator(); iterator.hasNext(); graphics2d.draw(generalpath1))
                {
                    String s = (String)iterator.next();
                    generalpath1 = getSelectedNodePath(s);
                    graphics2d.setColor(Color.green);
                }

            }
            graphics2d.setColor(Color.blue);
            if(StringUtils.isNotEmpty(mouseSelectListName) && resultAreaShape.containsKey(mouseSelectListName))
            {
                GeneralPath generalpath = getSelectedNodePath(mouseSelectListName);
                if(generalpath != null)
                    graphics2d.fill(generalpath);
            } else
            if(selectedShape != null)
            {
                graphics2d.setComposite(AlphaComposite.getInstance(3, 0.5F));
                graphics2d.fill(selectedShape);
            }
            graphics2d.translate(-moveLeft, -moveTop);
        }

        private void dealReady4Paint(Graphics g, Rectangle rectangle)
        {
            super.paintComponent(g);
            int i = (int)rectangle.getX();
            int j = (int)rectangle.getY();
            int k = (int)rectangle.getWidth();
            int l = (int)rectangle.getHeight();
            g.clipRect(i, j, k, l);
        }

        private void initImage()
        {
            image = currentSvgMap.getMapImage();
            CoreGraphHelper.waitForImage(image);
            selectedShape = null;
            moveLeft = 0.0D;
            moveTop = 0.0D;
        }

        public void setSvgMap(String s)
        {
            currentSvgMap = new MapSvgAttr(s);
            currentSvgMap.setMapTypeAndName(typeName, mapName);
            initImage();
        }

        public void setSvgMap(MapSvgAttr mapsvgattr)
        {
            currentSvgMap = mapsvgattr;
            initImage();
        }

        public void clearSvgMap()
        {
            currentSvgMap = null;
            image = BaseUtils.readImage("");
            selectedShape = null;
            moveLeft = 0.0D;
            moveTop = 0.0D;
            dragged = false;
            mouseStartX = 0.0D;
            mouseStartY = 0.0D;
        }

        public Image getImage()
        {
            return image;
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
            drawSelectShape(mouseevent);
            if(mouseevent.getClickCount() == 2)
                showEditNamePane(mouseevent);
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mousePressed(MouseEvent mouseevent)
        {
            mouseStartX = mouseevent.getPoint().getX();
            mouseStartY = mouseevent.getPoint().getY();
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
            drawWhenDragEnd(mouseevent);
        }

        public void mouseDragged(MouseEvent mouseevent)
        {
            dragged = true;
        }

        public void mouseMoved(MouseEvent mouseevent)
        {
        }

        private void drawWhenDragEnd(MouseEvent mouseevent)
        {
            if(image == null || getBounds() == null)
                return;
            if(dragged)
            {
                double d = mouseevent.getPoint().getX();
                double d1 = mouseevent.getPoint().getY();
                int i = image.getWidth(new JPanel());
                int j = image.getHeight(new JPanel());
                int k = (int)getBounds().getWidth();
                int l = (int)getBounds().getHeight();
                if(i > k)
                {
                    double d2 = d - mouseStartX;
                    moveLeft += d2;
                    moveLeft = Math.max(moveLeft, k - i);
                    moveLeft = Math.min(0.0D, moveLeft);
                } else
                {
                    moveLeft = 0.0D;
                }
                if(j > l)
                {
                    double d3 = d1 - mouseStartY;
                    moveTop += d3;
                    moveTop = Math.max(moveTop, l - j);
                    moveTop = Math.min(0.0D, moveTop);
                } else
                {
                    moveTop = 0.0D;
                }
                repaint();
            }
            dragged = false;
        }

        private void drawSelectShape(MouseEvent mouseevent)
        {
            selectedShape = null;
            mouseSelectListName = "";
            if(image == null)
                return;
            Point point = mouseevent.getPoint();
            Point point1 = new Point((int)(point.getX() - moveLeft), (int)(point.getY() - moveTop));
            boolean flag = false;
            Iterator iterator = resultAreaShape.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                String s = (String)iterator.next();
                GeneralPath generalpath = getSelectedNodePath(s);
                if(!generalpath.contains(point1))
                    continue;
                selectedShape = generalpath;
                currentNodeName = currentSvgMap.getSelectedPathName(point1);
                flag = true;
                break;
            } while(true);
            if(!flag && getEditType() == 1)
            {
                selectedShape = currentSvgMap.getSelectPath(point1);
                currentNodeName = currentSvgMap.getSelectedPathName(point1);
            }
            repaint();
        }

        private void showEditNamePane(MouseEvent mouseevent)
        {
            if(image == null || selectedShape == null)
                return;
            final EditNamePane namePane = new EditNamePane();
            Point point = mouseevent.getPoint();
            Point point1 = new Point((int)(point.getX() - moveLeft), (int)(point.getY() - moveTop));
            namePane.setEditViewRow(getEditViewRow(point1));
            String s = "";
            Object obj = resultAreaShape.keySet().iterator();
            do
            {
                if(!((Iterator) (obj)).hasNext())
                    break;
                String s1 = (String)((Iterator) (obj)).next();
                GeneralPath generalpath = getSelectedNodePath(s1);
                if(!generalpath.contains(point1))
                    continue;
                s = s1;
                break;
            } while(true);
            namePane.populateBean(s);
            namePane.resetPaneWithNewNameList(fromDataList);
            obj = namePane.showUnsizedWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

                final EditNamePane val$namePane;
                final ImageEditPane this$1;

                public void doOk()
                {
                    namePane.changeList();
                    String s2 = namePane.updateBean();
                    if(resultAreaShape.containsKey(s2))
                    {
                        if(ComparatorUtils.equals(s2, namePane.startName))
                            return;
                        ArrayList arraylist = (ArrayList)resultAreaShape.get(s2);
                        if(!arraylist.contains(currentNodeName))
                            arraylist.add(currentNodeName);
                    } else
                    {
                        ArrayList arraylist1 = new ArrayList();
                        resultAreaShape.put(s2, arraylist1);
                        arraylist1.add(currentNodeName);
                        ArrayList arraylist2 = (ArrayList)resultAreaShape.get(namePane.startName);
                        if(arraylist2 != null)
                        {
                            String s3;
                            for(Iterator iterator = arraylist2.iterator(); iterator.hasNext(); arraylist1.add(s3))
                                s3 = (String)iterator.next();

                            resultAreaShape.remove(namePane.startName);
                        }
                    }
                }

                
                {
                    this$1 = ImageEditPane.this;
                    namePane = editnamepane;
                    super();
                }
            }
);
            ((UIDialog) (obj)).setSize(130, 225);
            ((UIDialog) (obj)).setLocation((int)mouseevent.getLocationOnScreen().getX() + 10, (int)mouseevent.getLocationOnScreen().getY());
            ((UIDialog) (obj)).setTitle(Inter.getLocText(new String[] {
                "Edit", "Filed", "WF-Name"
            }));
            ((UIDialog) (obj)).setVisible(true);
        }

        public int getEditViewRow(Point point)
        {
            int i = recordTable.getRowCount();
            String s = "";
            Iterator iterator = resultAreaShape.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                String s1 = (String)iterator.next();
                GeneralPath generalpath = getSelectedNodePath(s1);
                if(!generalpath.contains(point))
                    continue;
                s = s1;
                break;
            } while(true);
            if(resultAreaShape.containsKey(s))
            {
                int j = 0;
                do
                {
                    if(j >= recordTable.getRowCount())
                        break;
                    String s2 = (String)recordTable.getValueAt(j, 0);
                    if(ComparatorUtils.equals(s, s2))
                    {
                        i = j;
                        break;
                    }
                    j++;
                } while(true);
            }
            return i;
        }



        public ImageEditPane()
        {
            this$0 = MapImageEditPane.this;
            super();
            image = BaseUtils.readImage("");
            moveLeft = 0.0D;
            moveTop = 0.0D;
            dragged = false;
            addMouseListener(this);
            addMouseMotionListener(this);
        }
    }


    private static final long serialVersionUID = 0xadc444b00d519dd8L;
    private static final double ARCSIZE = 7D;
    private static final int LOCATIONOFFSET = 10;
    private static final int NAME_EDIT_PANE_WIDTH = 130;
    private static final int NAME_EDIT_PANE_HEIGHT = 225;
    private ImageEditPane imageEditPane;
    private UISelectTable recordTable;
    private int editType;
    private java.util.List fromDataList;
    private HashMap resultAreaShape;
    private String currentNodeName;
    private String typeName;
    private String mapName;
    private String mouseSelectListName;
    private String editMapName;

    public MapImageEditPane()
    {
        editType = 0;
        fromDataList = new ArrayList();
        resultAreaShape = new HashMap();
        typeName = "";
        mapName = "";
        mouseSelectListName = "";
        editMapName = "";
        initCom();
    }

    private void initCom()
    {
        setLayout(new BorderLayout(0, 0));
        imageEditPane = new ImageEditPane();
        add(imageEditPane, "Center");
        recordTable = new UISelectTable(1) {

            final MapImageEditPane this$0;

            public int columnAtPoint(Point point)
            {
                return 0;
            }

            
            {
                this$0 = MapImageEditPane.this;
                super(i);
            }
        }
;
        recordTable.addSelectionChangeListener(new com.fr.design.event.ChangeListener() {

            final MapImageEditPane this$0;

            public void fireChanged(com.fr.design.event.ChangeEvent changeevent)
            {
                mouseSelectListName = Utils.objectToString(changeevent.getSource());
                imageEditPane.repaint();
                repaint();
            }

            
            {
                this$0 = MapImageEditPane.this;
                super();
            }
        }
);
        recordTable.setUI(new UITableNoOptionUI());
        recordTable.addChangeListener(new ChangeListener() {

            final MapImageEditPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                java.util.List list = recordTable.updateBean();
                ArrayList arraylist = new ArrayList();
                for(int i = 0; i < list.size(); i++)
                    arraylist.add(Utils.objectToString(recordTable.getValueAt(i, 0)));

                Iterator iterator = resultAreaShape.keySet().iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    String s = (String)iterator.next();
                    if(!arraylist.contains(s))
                        iterator.remove();
                } while(true);
                recordTable.revalidate();
                repaint();
            }

            
            {
                this$0 = MapImageEditPane.this;
                super();
            }
        }
);
        UIScrollPane uiscrollpane = new UIScrollPane(recordTable);
        uiscrollpane.setPreferredSize(new Dimension(150, 320));
        uiscrollpane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Filed", "WF-Name"
        })));
        add(uiscrollpane, "East");
    }

    public void setEditType(int i)
    {
        editType = i;
    }

    public int getEditType()
    {
        return editType;
    }

    public void setSvgMap(String s)
    {
        resultAreaShape.clear();
        mouseSelectListName = "";
        recordTable.populateBean(new ArrayList());
        recordTable.revalidate();
        imageEditPane.setSvgMap(s);
        repaint();
    }

    public void clearSvgMap()
    {
        resultAreaShape.clear();
        mouseSelectListName = "";
        recordTable.populateBean(new ArrayList());
        recordTable.revalidate();
        imageEditPane.clearSvgMap();
        repaint();
    }

    public void setSvgMap(MapSvgAttr mapsvgattr)
    {
        resultAreaShape.clear();
        mouseSelectListName = "";
        recordTable.populateBean(new ArrayList());
        recordTable.revalidate();
        imageEditPane.setSvgMap(mapsvgattr);
        repaint();
    }

    public void refreshFromDataList(java.util.List list)
    {
        fromDataList.clear();
        Object obj;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); fromDataList.add(Utils.objectToString(obj)))
            obj = iterator.next();

    }

    public void setTypeNameAndMapName(String s, String s1)
    {
        typeName = s;
        mapName = s1;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Edit", "Image"
        });
    }

    public void populateBean(String s)
    {
        editMapName = s;
        MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
        if(mapsvgxmlhelper.containsMapName(editMapName))
        {
            MapSvgAttr mapsvgattr = mapsvgxmlhelper.getMapAttr(editMapName);
            populateMapSvgAttr(mapsvgattr);
        } else
        if(mapsvgxmlhelper.getNewMapAttr(editMapName) != null)
        {
            clearSvgMap();
            imageEditPane.currentSvgMap = mapsvgxmlhelper.getMapAttr(editMapName);
        } else
        {
            clearSvgMap();
        }
    }

    public String updateBean()
    {
        if(imageEditPane.currentSvgMap != null)
            editMapName = imageEditPane.currentSvgMap.getName();
        if(StringUtils.isNotEmpty(editMapName) && imageEditPane.getImage() != null)
        {
            MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapsvgattr = imageEditPane.currentSvgMap;
            if(mapsvgxmlhelper.getNewMapAttr(editMapName) != null)
            {
                mapsvgxmlhelper.removeNewMapAttr(editMapName);
                mapsvgxmlhelper.pushMapAttr(editMapName, mapsvgattr);
            }
            if(mapsvgxmlhelper.containsMapName(editMapName))
            {
                updateMapShapePath(mapsvgattr);
                mapsvgxmlhelper.addCustomSvgMap(editMapName, mapsvgattr);
                mapsvgattr.writeBack(editMapName);
            }
        }
        return editMapName;
    }

    public void populateMapSvgAttr(MapSvgAttr mapsvgattr)
    {
        if(mapsvgattr == null || mapsvgattr.getMapImage() == null)
        {
            clearSvgMap();
            return;
        }
        setSvgMap(mapsvgattr);
        Iterator iterator = mapsvgattr.shapeValuesIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = Utils.objectToString(iterator.next());
            ArrayList arraylist = mapsvgattr.getExistedShapePathID(s);
            if(s != null)
            {
                resultAreaShape.put(s, arraylist);
                recordTable.addLine(new String[] {
                    s
                });
            }
        } while(true);
        recordTable.revalidate();
    }

    public MapSvgAttr updateWithOutSave()
    {
        if(imageEditPane.currentSvgMap != null)
            editMapName = imageEditPane.currentSvgMap.getName();
        if(StringUtils.isNotEmpty(editMapName) && imageEditPane.getImage() != null)
        {
            MapSvgXMLHelper mapsvgxmlhelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapsvgattr = imageEditPane.currentSvgMap;
            if(mapsvgxmlhelper.getNewMapAttr(editMapName) != null)
            {
                mapsvgxmlhelper.removeNewMapAttr(editMapName);
                mapsvgxmlhelper.pushMapAttr(editMapName, mapsvgattr);
            }
            if(mapsvgxmlhelper.containsMapName(editMapName))
            {
                updateMapShapePath(mapsvgattr);
                mapsvgxmlhelper.addCustomSvgMap(editMapName, mapsvgattr);
            }
            return mapsvgattr;
        } else
        {
            return null;
        }
    }

    private void updateMapShapePath(MapSvgAttr mapsvgattr)
    {
        mapsvgattr.clearExistShape();
        Iterator iterator = resultAreaShape.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            ArrayList arraylist = (ArrayList)resultAreaShape.get(s);
            if(arraylist != null)
            {
                Iterator iterator1 = arraylist.iterator();
                while(iterator1.hasNext()) 
                {
                    String s1 = (String)iterator1.next();
                    mapsvgattr.setNodeName(s1, s);
                }
            }
        } while(true);
    }

    private GeneralPath getSelectedNodePath(String s)
    {
        if(imageEditPane.currentSvgMap == null)
            return new GeneralPath();
        MapSvgAttr mapsvgattr = imageEditPane.currentSvgMap;
        ArrayList arraylist = (ArrayList)resultAreaShape.get(s);
        GeneralPath generalpath = new GeneralPath();
        String s1;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); generalpath.append(mapsvgattr.getPath4PathID(s1), false))
            s1 = (String)iterator.next();

        return generalpath;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((String)obj);
    }











}
