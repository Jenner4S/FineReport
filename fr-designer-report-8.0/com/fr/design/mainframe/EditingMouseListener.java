// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.*;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.xpane.ToolTipEditor;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelection, FormArea, ConnectorHelper

public class EditingMouseListener extends MouseInputAdapter
{

    private static final int INDEX = 0;
    private FormDesigner designer;
    private StateModel stateModel;
    private SelectionModel selectionModel;
    private XCreator last_creator;
    private MouseEvent lastPressEvent;
    private DesignerEditor current_editor;
    private XCreator current_creator;
    private int minDragSize;
    private int minMoveSize;
    private static final int GAP = 10;
    private XElementCase xElementCase;
    private JWindow promptWindow;

    public FormDesigner getDesigner()
    {
        return designer;
    }

    public SelectionModel getSelectionModel()
    {
        return selectionModel;
    }

    public int getMinMoveSize()
    {
        return minMoveSize;
    }

    public EditingMouseListener(FormDesigner formdesigner)
    {
        minDragSize = 5;
        minMoveSize = 8;
        promptWindow = new JWindow();
        designer = formdesigner;
        stateModel = formdesigner.getStateModel();
        selectionModel = formdesigner.getSelectionModel();
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Designer_Forbid_Drag_into_Adapt_Pane"), BaseUtils.readIcon("/com/fr/web/images/form/forbid.png"));
        promptWindow.add(uibutton);
    }

    private void promptUser(int i, int j, XLayoutContainer xlayoutcontainer)
    {
        if(!selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane() && xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
            promptWidgetForbidEnter(i, j, xlayoutcontainer);
        else
            cancelPromptWidgetForbidEnter();
    }

    private void promptWidgetForbidEnter(int i, int j, XLayoutContainer xlayoutcontainer)
    {
        xlayoutcontainer.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        int k = (int)designer.getArea().getLocationOnScreen().getX();
        int l = (int)designer.getArea().getLocationOnScreen().getY();
        promptWindow.setSize(promptWindow.getPreferredSize());
        promptWindow.setPreferredSize(promptWindow.getPreferredSize());
        promptWindow.setLocation(k + i + 10, l + j + 10);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter()
    {
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, 1));
        promptWindow.setVisible(false);
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(!stopEditing())
            return;
        if(!designer.isFocusOwner())
            designer.requestFocus();
        if(!mouseevent.isPopupTrigger() && mouseevent.getButton() == 1)
        {
            Direction direction = selectionModel.getDirectionAt(mouseevent);
            if(!BaseUtils.isAuthorityEditing())
                stateModel.setDirection(direction);
            if(direction == Location.outer)
            {
                if(designer.isDrawLineMode())
                    designer.updateDrawLineMode(mouseevent);
                else
                if(selectionModel.hasSelectionComponent() && selectionModel.getSelection().getRelativeBounds().contains(designer.getArea().getHorizontalValue() + mouseevent.getX(), designer.getArea().getVerticalValue() + mouseevent.getY()))
                {
                    lastPressEvent = mouseevent;
                    last_creator = selectionModel.getSelection().getSelectedCreator();
                } else
                {
                    stateModel.startSelecting(mouseevent);
                }
            } else
            {
                stateModel.startResizing(mouseevent);
            }
        }
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
        if(mouseevent.isPopupTrigger())
        {
            if(stateModel.isDragging())
                stateModel.draggingCancel();
        } else
        {
            if(designer.isDrawLineMode())
            {
                if(stateModel.prepareForDrawLining())
                {
                    designer.getDrawLineHelper().setDrawLine(false);
                    designer.getDrawLineHelper().createDefalutLine();
                }
            } else
            if(stateModel.isSelecting())
                designer.selectComponents(mouseevent);
            if(stateModel.isDragging())
            {
                XCreator xcreator = designer.getComponentAt(mouseevent.getX(), mouseevent.getY());
                if(xcreator == null && mouseevent.getY() < 0)
                    xcreator = designer.getComponentAt(0, 0);
                XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
                if(xlayoutcontainer != null)
                {
                    boolean flag = !selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane() && xlayoutcontainer.acceptType(new Class[] {
                        com/fr/design/designer/creator/XWFitLayout
                    });
                    if(!flag)
                    {
                        stateModel.releaseDragging(mouseevent);
                    } else
                    {
                        selectionModel.deleteSelection();
                        designer.setPainter(null);
                    }
                    cancelPromptWidgetForbidEnter();
                }
            }
        }
        lastPressEvent = null;
        last_creator = null;
    }

    private void trigger_popup(MouseEvent mouseevent)
    {
        XCreator xcreator = selectionModel.getSelection().getSelectedCreator();
        if(xcreator == null)
            return;
        JPopupMenu jpopupmenu = null;
        ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(designer, xcreator);
        jpopupmenu = componentadapter.getContextPopupMenu(mouseevent);
        if(jpopupmenu != null)
            jpopupmenu.show(designer, mouseevent.getX(), mouseevent.getY());
        designer.getEditListenerTable().fireCreatorModified(xcreator, 7);
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        XCreator xcreator = designer.getComponentAt(mouseevent);
        if(xcreator instanceof XEditorHolder)
        {
            XEditorHolder xeditorholder = (XEditorHolder)xcreator;
            Rectangle rectangle = xeditorholder.getBounds();
            int i = (rectangle.x + rectangle.width / 2) - minMoveSize;
            int k = rectangle.x + rectangle.width / 2 + minMoveSize;
            if(mouseevent.getX() > i && mouseevent.getX() < k)
            {
                if(designer.getCursor().getType() != 12)
                    designer.setCursor(Cursor.getPredefinedCursor(12));
                return;
            }
            if(designer.getCursor().getType() == 12)
                designer.setCursor(Cursor.getPredefinedCursor(0));
        }
        Direction direction = selectionModel.getDirectionAt(mouseevent);
        if(designer.isDrawLineMode() && stateModel.getDirection() == Location.outer)
            designer.updateDrawLineMode(mouseevent);
        if(!BaseUtils.isAuthorityEditing())
            stateModel.setDirection(direction);
        if(xElementCase != null)
            xElementCase.displayCoverPane(false);
        if(xcreator.isReport())
        {
            xElementCase = (XElementCase)xcreator;
            UIButton uibutton = (UIButton)xElementCase.getCoverPane().getComponent(0);
            if(designer.getCursor().getType() == 12)
                designer.setCursor(Cursor.getPredefinedCursor(0));
            int j = (uibutton.getX() + xcreator.getX() + xcreator.getParent().getX()) - designer.getArea().getHorizontalValue();
            int l = (uibutton.getY() + xcreator.getY() + xcreator.getParent().getY() + designer.getParaHeight()) - designer.getArea().getVerticalValue();
            if(mouseevent.getX() + 10 > j && mouseevent.getX() - 10 < j + uibutton.getWidth() && mouseevent.getY() + 10 > l && mouseevent.getY() - 10 < l + uibutton.getHeight())
                designer.setCursor(Cursor.getPredefinedCursor(12));
            xElementCase.displayCoverPane(true);
            xElementCase.setDirections(Direction.TOP_BOTTOM_LEFT_RIGHT);
            designer.repaint();
        } else
        if(xElementCase != null)
        {
            xElementCase.displayCoverPane(false);
            designer.repaint();
        }
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            return;
        if(stateModel.dragable())
        {
            if(SwingUtilities.isRightMouseButton(mouseevent))
                return;
            stateModel.dragging(mouseevent);
            XCreator xcreator = designer.getComponentAt(mouseevent.getX(), mouseevent.getY());
            if(xcreator == null)
                return;
            XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
            promptUser(mouseevent.getX(), mouseevent.getY(), xlayoutcontainer);
        } else
        if(designer.isDrawLineMode())
        {
            if(stateModel.prepareForDrawLining())
                stateModel.drawLine(mouseevent);
        } else
        if(stateModel.isSelecting() && selectionModel.getHotspotBounds() != null)
        {
            stateModel.changeSelection(mouseevent);
        } else
        {
            if(lastPressEvent == null || last_creator == null)
                return;
            if(mouseevent.getPoint().distance(lastPressEvent.getPoint()) > (double)minDragSize)
            {
                if(last_creator.isSupportDrag())
                    designer.startDraggingComponent(last_creator, lastPressEvent, mouseevent.getX(), mouseevent.getY());
                mouseevent.consume();
                lastPressEvent = null;
            }
        }
        designer.repaint();
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getButton() != 1)
            return;
        XCreator xcreator = designer.getComponentAt(mouseevent);
        if(xcreator != null)
            xcreator.respondClick(this, mouseevent);
        xcreator.doLayout();
        LayoutUtils.layoutRootContainer(designer.getRootComponent());
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        if(designer.getCursor().getType() != 0)
            designer.setCursor(Cursor.getDefaultCursor());
        if(xElementCase != null)
        {
            xElementCase.displayCoverPane(false);
            designer.repaint();
        }
        cancelPromptWidgetForbidEnter();
    }

    public void startEditing(XCreator xcreator, DesignerEditor designereditor, ComponentAdapter componentadapter)
    {
        if(designereditor != null)
        {
            Rectangle rectangle = ComponentUtils.getRelativeBounds(xcreator);
            current_editor = designereditor;
            current_creator = xcreator;
            Rectangle rectangle1 = new Rectangle(1, 1, xcreator.getWidth() - 2, xcreator.getHeight() - 2);
            rectangle1.x += rectangle.x - designer.getArea().getHorizontalValue();
            rectangle1.y += rectangle.y - designer.getArea().getVerticalValue();
            designereditor.getEditorTarget().setBounds(rectangle1);
            designer.add(designereditor.getEditorTarget());
            designer.invalidate();
            designer.setCursor(Cursor.getPredefinedCursor(0));
            designereditor.getEditorTarget().requestFocus();
            designer.repaint();
        }
    }

    public boolean stopEditing()
    {
        if(current_editor != null)
        {
            designer.remove(current_editor.getEditorTarget());
            current_editor.fireEditStoped();
            Container container = current_creator.getParent();
            if(container != null)
                LayoutUtils.layoutRootContainer(container);
            designer.invalidate();
            designer.repaint();
            current_creator = null;
            current_editor = null;
            return true;
        } else
        {
            return true;
        }
    }

    public void resetEditorComponentBounds()
    {
        if(current_editor == null)
            return;
        if(current_creator.getParent() == null)
        {
            stopEditing();
            return;
        }
        Rectangle rectangle = ComponentUtils.getRelativeBounds(current_creator);
        Rectangle rectangle1 = new Rectangle(1, 1, current_creator.getWidth() - 2, current_creator.getHeight() - 2);
        rectangle1.x += rectangle.x - designer.getArea().getHorizontalValue();
        rectangle1.y += rectangle.y - designer.getArea().getVerticalValue();
        if(current_creator instanceof XEditorHolder)
            ToolTipEditor.getInstance().resetBounds((XEditorHolder)current_creator, rectangle1, current_editor.getEditorTarget().getBounds());
        current_editor.getEditorTarget().setBounds(rectangle1);
    }
}
