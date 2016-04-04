// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.PlotSeries;

import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.data.TableDataSource;
import com.fr.data.core.DataCoreUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.apache.batik.swing.svg.SVGFileFilter;

// Referenced classes of package com.fr.design.chart.series.PlotSeries:
//            MapImageEditPane, AbstrctMapAttrEditPane

public class MapCustomPane extends BasicBeanPane
    implements AbstrctMapAttrEditPane
{

    private FilterComboBox areaString;
    private DatabaseTableDataPane tableDataNameBox;
    private MapImageEditPane imageShowPane;
    private String lastSelectPath;
    private boolean isNeedDataSource;
    private ActionListener selectPictureActionListener = new ActionListener() {

        final MapCustomPane this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            JFileChooser jfilechooser = new JFileChooser();
            jfilechooser.addChoosableFileFilter(new SVGFileFilter());
            if(StringUtils.isNotBlank(lastSelectPath))
                jfilechooser.setSelectedFile(new File(lastSelectPath));
            int i = jfilechooser.showOpenDialog(DesignerContext.getDesignerFrame());
            if(i != 1)
            {
                File file = jfilechooser.getSelectedFile();
                lastSelectPath = file.getAbsolutePath();
                if(file != null && file.isFile())
                {
                    imageShowPane.setSvgMap(file.getPath());
                    imageShowPane.repaint();
                }
            }
        }

            
            {
                this$0 = MapCustomPane.this;
                super();
            }
    }
;
    private ItemListener areaChange = new ItemListener() {

        final MapCustomPane this$0;

        public void itemStateChanged(ItemEvent itemevent)
        {
            Object obj = areaString.getSelectedItem();
            if(obj != null)
            {
                String s = Utils.objectToString(areaString.getSelectedItem());
                TableDataWrapper tabledatawrapper = tableDataNameBox.getTableDataWrapper();
                TableDataSource tabledatasource = DesignTableDataManager.getEditingTableDataSource();
                if(tabledatawrapper == null || tabledatasource == null)
                    return;
                String as[] = DataCoreUtils.getColValuesInData(tabledatasource, tabledatawrapper.getTableDataName(), s);
                ArrayList arraylist = new ArrayList();
                for(int i = 0; i < as.length; i++)
                    arraylist.add(as[i]);

                imageShowPane.refreshFromDataList(arraylist);
            }
        }

            
            {
                this$0 = MapCustomPane.this;
                super();
            }
    }
;

    public MapCustomPane()
    {
        isNeedDataSource = true;
        initComp();
    }

    public MapCustomPane(boolean flag)
    {
        isNeedDataSource = true;
        isNeedDataSource = flag;
        initComp();
    }

    private void initComp()
    {
        setLayout(new BorderLayout(0, 0));
        JPanel jpanel = new JPanel();
        add(jpanel, "North");
        jpanel.setLayout(new BorderLayout());
        jpanel.add(northPaneCreate(), "North");
        imageShowPane = new MapImageEditPane();
        jpanel.add(imageShowPane, "Center");
    }

    private JPanel northPaneCreate()
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(0));
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Chart-Import_Map"));
        uibutton.setPreferredSize(new Dimension(160, 20));
        jpanel.add(uibutton);
        uibutton.addActionListener(selectPictureActionListener);
        if(isNeedDataSource)
        {
            UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Chart-Table_Data")).append(":").toString(), 4);
            tableDataNameBox = new DatabaseTableDataPane(uilabel) {

                final MapCustomPane this$0;

                protected void userEvent()
                {
                    refreshAreaNameBox();
                }

            
            {
                this$0 = MapCustomPane.this;
                super(uilabel);
            }
            }
;
            tableDataNameBox.setPreferredSize(new Dimension(200, 20));
            jpanel.add(tableDataNameBox);
            jpanel.add(new BoldFontTextLabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                "Filed", "Field"
            })).append(":").toString()));
            areaString = new FilterComboBox();
            areaString.setPreferredSize(new Dimension(120, 20));
            areaString.addItemListener(areaChange);
            jpanel.add(areaString);
        }
        return jpanel;
    }

    public void setImageSelectType(int i)
    {
        if(imageShowPane != null)
            imageShowPane.setEditType(i);
    }

    private void refreshAreaNameBox()
    {
        if(!isNeedDataSource)
            return;
        TableDataWrapper tabledatawrapper = tableDataNameBox.getTableDataWrapper();
        if(tabledatawrapper == null)
        {
            return;
        } else
        {
            java.util.List list = tabledatawrapper.calculateColumnNameList();
            areaString.removeAllItems();
            areaString.setItemList(list);
            return;
        }
    }

    public void setTypeNameAndMapName(String s, String s1)
    {
        imageShowPane.setTypeNameAndMapName(s, s1);
    }

    public void populateBean(String s)
    {
        imageShowPane.populateBean(s);
    }

    public String updateBean()
    {
        return imageShowPane.updateBean();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Datasource-User_Defined", "Chart-Map"
        });
    }

    public void populateMapAttr(MapSvgAttr mapsvgattr)
    {
        imageShowPane.populateMapSvgAttr(mapsvgattr);
    }

    public MapSvgAttr updateCurrentAttr()
    {
        return imageShowPane.updateWithOutSave();
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
