// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.base.FRContext;
import com.fr.base.chart.*;
import com.fr.general.FRLogger;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.ChartBlockCreator;
import com.fr.poly.creator.ECBlockCreator;
import com.fr.poly.model.AddedData;
import com.fr.report.poly.*;
import com.fr.stable.bridge.StableFactory;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.fr.poly:
//            PolyDesigner

public class PolyUtils
{

    public static Map blockMapCls;

    public PolyUtils()
    {
    }

    public static BlockCreator createCreator(TemplateBlock templateblock)
    {
        Class class1 = templateblock.getClass();
        Class class2 = (Class)blockMapCls.get(class1);
        if(class2 == null)
            return null;
        Object obj = null;
        try
        {
            Constructor constructor = class2.getConstructor(new Class[] {
                class1
            });
            return (BlockCreator)constructor.newInstance(new Object[] {
                templateblock
            });
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return null;
    }

    public static BlockCreator createCreator(Class class1)
    {
        return createCreator(blockGenerate(class1));
    }

    public static BlockCreator createCreator(BaseChart basechart)
    {
        BaseChartCollection basechartcollection = (BaseChartCollection)StableFactory.createXmlObject("CC");
        basechartcollection.addChart(basechart);
        PolyChartBlock polychartblock = new PolyChartBlock(basechartcollection);
        return createCreator(((TemplateBlock) (polychartblock)));
    }

    private static TemplateBlock blockGenerate(Class class1)
    {
        Object obj = null;
        try
        {
            obj = (TemplateBlock)class1.newInstance();
        }
        catch(Exception exception)
        {
            try
            {
                BasePlot baseplot = (BasePlot)class1.newInstance();
                BaseChartCollection basechartcollection = (BaseChartCollection)StableFactory.createXmlObject("CC");
                BaseChart basechart = (BaseChart)StableFactory.createXmlObject("Chart");
                basechart.initChart(baseplot);
                basechartcollection.addChart(basechart);
                obj = new PolyChartBlock(basechartcollection);
            }
            catch(InstantiationException instantiationexception)
            {
                FRContext.getLogger().error(instantiationexception.getMessage(), instantiationexception);
            }
            catch(IllegalAccessException illegalaccessexception)
            {
                FRContext.getLogger().error(illegalaccessexception.getMessage(), illegalaccessexception);
            }
        }
        return ((TemplateBlock) (obj));
    }

    public static Point convertPoint2Designer(PolyDesigner polydesigner, Point point)
    {
        int i = polydesigner.getHorizontalValue();
        int j = polydesigner.getVerticalValue();
        point.x += i;
        point.y += j;
        return point;
    }

    public static Point convertPoint2Designer(PolyDesigner polydesigner, int i, int j)
    {
        return convertPoint2Designer(polydesigner, new Point(i, j));
    }

    public static int convertx2Designer(PolyDesigner polydesigner, int i)
    {
        return i += polydesigner.getHorizontalValue();
    }

    public static int converty2Designer(PolyDesigner polydesigner, int i)
    {
        return i += polydesigner.getVerticalValue();
    }

    public static BlockCreator searchAt(PolyDesigner polydesigner, int i, int j)
    {
        AddedData addeddata = polydesigner.getAddedData();
        for(int k = addeddata.getAddedCount() - 1; k >= 0; k--)
        {
            BlockCreator blockcreator = addeddata.getAddedAt(k);
            int l = blockcreator.getX();
            int i1 = blockcreator.getY();
            int j1 = blockcreator.getWidth();
            int k1 = blockcreator.getHeight();
            if(i >= l && i <= l + j1 && j >= i1 && j <= i1 + k1)
                return blockcreator;
        }

        return null;
    }

    static 
    {
        blockMapCls = new HashMap();
        blockMapCls.put(com/fr/report/poly/PolyECBlock, com/fr/poly/creator/ECBlockCreator);
        blockMapCls.put(com/fr/report/poly/PolyChartBlock, com/fr/poly/creator/ChartBlockCreator);
    }
}
