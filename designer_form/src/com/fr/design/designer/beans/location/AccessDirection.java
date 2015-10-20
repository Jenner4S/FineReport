/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.beans.location.MoveUtils;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;

/**
 * @author richer
 * @since 6.5.3
 */
public abstract class AccessDirection implements Direction {
    private static final int MINHEIGHT = 21;
    private static final int MINWIDTH = 36;
    private int ymin;
    private int xmin;

	abstract int getCursor();

	protected abstract Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds);

	protected int[] sorption(int x, int y,Rectangle current_bounds, FormDesigner designer) {
		// ����Ӧ���ֲ���Ҫ�����ߣ�����Ҫ�����ߣ������ߺ��洦��
		if (!designer.hasWAbsoluteLayout()) {
			return new int[] { x, y };
		} else {
			int posy = current_bounds.y;
			if (posy >= designer.getParaHeight() && !designer.isFormParaDesigner()) {
				return new int[] { x, y };
			}
			
			Point relativePoint = getRelativePoint(x, y, current_bounds,designer);
			sorptionPoint(relativePoint,current_bounds, designer);
			return new int[] { relativePoint.x, relativePoint.y };
		}

	}

	protected Point getRelativePoint(int x, int y, Rectangle current_bounds,FormDesigner designer) {
		if (x < 0) {
			x = 0;
		} else if (x > designer.getRootComponent().getWidth() && designer.getSelectionModel().hasSelectionComponent()) {
			x = designer.getRootComponent().getWidth();
		}
        //��������������������
		if (y < 0) {
			y = 0;
		} else if (y > designer.getRootComponent().getHeight() && designer.getSelectionModel().hasSelectionComponent()
                && !designer.getSelectionModel().getSelection().getSelectedCreator().acceptType(XWParameterLayout.class)) {
			y = designer.getRootComponent().getHeight();
		}
		return new Point(x, y);
	}

	protected void sorptionPoint(Point point, Rectangle current_bounds,FormDesigner designer) {
		boolean findInX = current_bounds.getWidth() <= MoveUtils.SORPTION_UNIT ? true : false;
		boolean findInY = current_bounds.getHeight() <= MoveUtils.SORPTION_UNIT ? true : false;

		WAbsoluteLayout layout =getLayout(designer);
		FormSelection selection = designer.getSelectionModel().getSelection();
		for (int i = 0, count = layout.getWidgetCount(); i < count; i++) {
			BoundsWidget temp = (BoundsWidget) layout.getWidget(i);
			if (!temp.isVisible() || selection.contains(temp.getWidget())) {
				continue;
			}
			Rectangle bounds = temp.getBounds();
			if (!findInX) {
				int x1 = bounds.x;
				if (Math.abs(x1 - point.x) <= MoveUtils.SORPTION_UNIT) {
					point.x = x1;
					findInX = true;
				}
				int x2 = bounds.x + bounds.width;
				if (Math.abs(x2 - point.x) <= MoveUtils.SORPTION_UNIT) {
					point.x = x2;
					findInX = true;
				}
			}
			if (!findInY) {
				int y1 = bounds.y;
				if (Math.abs(y1 - point.y) <= MoveUtils.SORPTION_UNIT) {
					point.y = y1;
					findInY = true;
				}
				int y2 = bounds.y + bounds.height;
				if (Math.abs(y2 - point.y) <= MoveUtils.SORPTION_UNIT) {
					point.y = y2;
					findInY = true;
				}

			}
			if (findInX && findInY) {
				break;
			}
		}

		designer.getStateModel().setXAbsorptionline(findInX && current_bounds.getWidth() > MoveUtils.SORPTION_UNIT ? Absorptionline.createXAbsorptionline(point.x) : null);
		designer.getStateModel().setYAbsorptionline(findInY && current_bounds.getHeight() > MoveUtils.SORPTION_UNIT ? Absorptionline.createYAbsorptionline(point.y) : null);
	}

    private WAbsoluteLayout getLayout(final FormDesigner designer){
        XLayoutContainer formLayoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(
                designer.getTarget().getContainer());
        WAbsoluteLayout layout;
        if (formLayoutContainer.acceptType(XWBorderLayout.class)){
            layout = (WAbsoluteLayout) designer.getParaComponent().toData();
        } else{
            layout = (WAbsoluteLayout) designer.getTarget().getContainer();
        }
        return  layout;
    }

	/**
	 * ��ק
	 * @param dx ����x
	 * @param dy ����y
	 * @param designer ��ƽ���
	 */
	public void drag(int dx, int dy, FormDesigner designer) {
		Rectangle rec = getDraggedBounds(dx, dy, designer.getSelectionModel().getSelection().getRelativeBounds(), designer, designer.getSelectionModel().getSelection().getBackupBounds());
        //�趨�ؼ���С�߶�21����ÿ����ҷ�����ƶ�1����ֹ�ؼ��߶ȵ���21ʱ����ҷ����rec.y�ı仯ʹ�ÿؼ���ͣ�����ϻ������ƶ���
        if(rec.height == MINHEIGHT){
            ymin = rec.y;
        }
        if(rec.height == MINHEIGHT - 1){
            ymin = ymin == rec.y ? rec.y : rec.y - 1;
        }
        if(rec.height < MINHEIGHT){
            rec.height = MINHEIGHT;
            rec.y = ymin;
        }
        // �����¿��Ҳ����СΪ21
        if (rec.width == MINWIDTH) {
        	xmin = rec.x;
        }
        if(rec.width == MINWIDTH - 1){
        	xmin = xmin == rec.x ? rec.x : rec.x - 1;
        }
        if (rec.width < MINWIDTH) {
        	rec.width = MINWIDTH;
        	rec.x = xmin;
        }
		if(rec != null) {
			designer.getSelectionModel().getSelection().setSelectionBounds(rec, designer);
		}
	}

	/**
	 * �������ָ����״
	 * @param formEditor ��ƽ������
	 */
	public void updateCursor(FormDesigner formEditor) {

		// ����λ��ö�ٵĶ�̬����getCursor��ȡ�����״
		int type = getCursor();

		if (type != formEditor.getCursor().getType()) {
			// ���õ�ǰ��״
			formEditor.setCursor(Cursor.getPredefinedCursor(type));
		}
	}
	
	/**
	 * ����������õ�bound
	 * @param formEditor ��ƽ������
	 */
	public void backupBounds(FormDesigner formEditor) {
		formEditor.getSelectionModel().getSelection().backupBounds();
	}
}
