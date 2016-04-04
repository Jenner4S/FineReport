// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.mainframe.*;
import com.fr.form.ui.Connector;
import java.awt.*;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XComponent, XWAbsoluteLayout, XLayoutContainer

public class XConnector
    implements XComponent
{
    public class ConnectorDirection
        implements Direction
    {

        private Point A;
        private Point B;
        private Rectangle oldbounds;
        final XConnector this$0;

        private Cursor getCursor()
        {
            if(A == null || B == null)
                return A != B ? XConnector.moveCursor : Cursor.getPredefinedCursor(13);
            if(A.x == B.x)
                return Cursor.getPredefinedCursor(10);
            if(A.y == B.y)
                return Cursor.getPredefinedCursor(9);
            else
                return Cursor.getPredefinedCursor(13);
        }

        private void setXY(Point point, Point point1, int i, int j, Rectangle rectangle)
        {
            if(point.x == point1.x)
                point.x = point1.x = rectangle.x + i;
            else
                point.y = point1.y = rectangle.y + j;
        }

        public void drag(int i, int j, FormDesigner formdesigner)
        {
            if(A == null || B == null)
            {
                if(A != null)
                {
                    A.x = oldbounds.x + i;
                    A.y = oldbounds.y + j;
                } else
                if(B != null)
                {
                    B.x = oldbounds.x + i;
                    B.y = oldbounds.y + j;
                } else
                {
                    setBounds(new Rectangle(oldbounds.x + i, oldbounds.y + j, oldbounds.width, oldbounds.height));
                }
                formdesigner.getDrawLineHelper().resetConnector(connector);
                return;
            }
            if(A == connector.getStartPoint())
            {
                A = new Point(A.x, A.y);
                connector.addPoint(1, A);
            }
            if(connector.getEndPoint() == B)
            {
                B = new Point(B.x, B.y);
                connector.addPoint(connector.getPointCount() - 1, B);
            }
            setXY(A, B, i, j, oldbounds);
        }

        public Rectangle getBounds()
        {
            if(A == null || B == null)
            {
                if(A != null)
                    return new Rectangle(A.x, A.y, 0, 0);
                if(B != null)
                    return new Rectangle(B.x, B.y, 0, 0);
                else
                    return XConnector.this.getBounds();
            } else
            {
                return createRectangle(A, B);
            }
        }

        public int getActual()
        {
            return 0;
        }

        public void updateCursor(FormDesigner formdesigner)
        {
            formdesigner.setCursor(getCursor());
        }

        public void backupBounds(FormDesigner formdesigner)
        {
            oldbounds = getBounds();
        }

        private ConnectorDirection()
        {
            this$0 = XConnector.this;
            super();
        }

        private ConnectorDirection(Point point, Point point1)
        {
            this$0 = XConnector.this;
            super();
            A = point;
            B = point1;
        }


    }


    private XWAbsoluteLayout parent;
    private Connector connector;
    public static Cursor connectorCursor = Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/connectorcursor.png"), new Point(0, 0), "connector");
    public static Cursor moveCursor = Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/move.png"), new Point(16, 16), "move");

    public XConnector(Connector connector1, XWAbsoluteLayout xwabsolutelayout)
    {
        connector = connector1;
        parent = xwabsolutelayout;
    }

    private boolean near(int i, int j)
    {
        return i - j >= -5 && i - j <= 5;
    }

    private Rectangle createRectangle(Point point, Point point1)
    {
        return new Rectangle(Math.min(point.x, point1.x), Math.min(point.y, point1.y), Math.abs(point.x - point1.x), Math.abs(point.y - point1.y));
    }

    public XLayoutContainer getParentXLayoutContainer()
    {
        return parent;
    }

    public Connector getConnector()
    {
        return connector;
    }

    public Rectangle getBounds()
    {
        return createRectangle(connector.getStartPoint(), connector.getEndPoint());
    }

    public void setBounds(Rectangle rectangle)
    {
    }

    public JComponent createToolPane(BaseJForm basejform, FormDesigner formdesigner)
    {
        return basejform.getEditingPane();
    }

    public ConnectorDirection getDirection(int i, int j)
    {
        Point point = connector.getStartPoint();
        if(near(i, point.x) && near(j, point.y))
            return new ConnectorDirection(point, null);
        Point point1 = connector.getEndPoint();
        if(near(i, point1.x) && near(j, point1.y))
            return new ConnectorDirection(null, point1);
        Point point2 = point;
        int k = connector.getPointCount();
        for(int l = 0; l < k - 1; l++)
        {
            Point point3 = connector.getPointIndex(l + 1);
            Point point4 = connector.getMidPoint(point2, point3);
            if(near(point4.x, i) && near(point4.y, j))
                return new ConnectorDirection(point2, point3);
            point2 = point3;
        }

        return new ConnectorDirection();
    }



}
