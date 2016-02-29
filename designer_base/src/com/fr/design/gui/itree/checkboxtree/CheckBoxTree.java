/*
 * @(#)CheckBoxTree.java 8/11/2005
 * modified by FineReport
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.fr.design.gui.itree.checkboxtree;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.fr.design.gui.icheckbox.UICheckBox;

/**
 * CheckBoxTree is a special JTree which uses UICheckBox as the tree renderer.
 * In addition to regular JTree's features, it also allows you select any number
 * of tree nodes in the tree by selecting the check boxes.
 * <p>To select an element, user can mouse click on the check box, or
 * select one or several tree nodes and press SPACE key to toggle the
 * check box selection for all selected tree nodes.
 * <p/>
 * In order to retrieve which tree paths are selected, you need to call 
 * {@link #getCheckBoxTreeSelectionModel()}.
 * It will return the selection model that keeps track of which tree 
 * paths have been checked. For example
 * {@link CheckBoxTreeSelectionModel#getSelectionPaths()} 
 * will give the list of paths which have
 * been checked.
 */
public class CheckBoxTree extends JTree {
    public final static String PROPERTY_CHECKBOX_ENABLED = "checkBoxEnabled";
    public final static String PROPERTY_DIG_IN = "digIn";

    protected CheckBoxTreeCellRenderer _treeCellRenderer;

    private CheckBoxTreeSelectionModel _checkBoxTreeSelectionModel;

    private boolean _checkBoxEnabled = true;
    private PropertyChangeListener _modelChangeListener;

    public CheckBoxTree() {
        init();
    }

    public CheckBoxTree(Object[] value) {
        super(value);
        init();
    }

    public CheckBoxTree(Vector value) {
        super(value);
        init();
    }

    public CheckBoxTree(Hashtable value) {
        super(value);
        init();
    }

    public CheckBoxTree(TreeNode root) {
        super(root);
        init();
    }

    public CheckBoxTree(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        init();
    }

    public CheckBoxTree(TreeModel newModel) {
        super(newModel);
        init();
    }

    /**
     * Initialize the CheckBoxTree.
     */
    protected void init() {
        _checkBoxTreeSelectionModel = createCheckBoxTreeSelectionModel(getModel());
        _checkBoxTreeSelectionModel.setTree(this);
        Handler handler = createHandler();
        this.insertMouseListener(this, handler, 0);
        addKeyListener(handler);
        _checkBoxTreeSelectionModel.addTreeSelectionListener(handler);

        if (_modelChangeListener == null) {
            _modelChangeListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    updateRowMapper();
                }
            };
        }
        addPropertyChangeListener(JTree.SELECTION_MODEL_PROPERTY, _modelChangeListener);
        updateRowMapper();
    }
    
    /**
     * Inserts the mouse listener at the particular index in the listeners' chain.
     *
     * @param component
     * @param l
     * @param index
     */
    private void insertMouseListener(Component component, MouseListener l, int index) {
        MouseListener[] listeners = component.getMouseListeners();
        for (int i = 0, length = listeners.length; i < length; i++) {
        	component.removeMouseListener(listeners[i]);
        }
//        for (MouseListener listener : listeners) {
//            component.removeMouseListener(listener);
//        }
        for (int i = 0; i < listeners.length; i++) {
            MouseListener listener = listeners[i];
            if (index == i) {
                component.addMouseListener(l);
            }
            component.addMouseListener(listener);
        }
        // inex is too large, add to the end.
        if (index > listeners.length - 1) {
            component.addMouseListener(l);
        }
    }

    /**
     * Creates the CheckBoxTreeSelectionModel.
     *
     * @param model the tree model.
     * @return the CheckBoxTreeSelectionModel.
     */
    protected CheckBoxTreeSelectionModel createCheckBoxTreeSelectionModel(TreeModel model) {
        return new CheckBoxTreeSelectionModel(model);
    }

    /**
     * RowMapper is necessary for contiguous selection.
     */
    private void updateRowMapper() {
        _checkBoxTreeSelectionModel.setRowMapper(getSelectionModel().getRowMapper());
    }

    public void setModel(TreeModel newModel) {
        super.setModel(newModel);
        if (_checkBoxTreeSelectionModel != null) {
            _checkBoxTreeSelectionModel.setModel(getModel());
        }
    }

    /**
     * Gets the cell renderer with check box.
     *
     * @return CheckBoxTree's own cell renderer which has the check box. The actual cell renderer
     *         you set by setCellRenderer() can be accessed by using {@link #getActualCellRenderer()}.
     */
    public TreeCellRenderer getCellRenderer() {
        TreeCellRenderer cellRenderer = super.getCellRenderer();
        if (cellRenderer == null) {
            cellRenderer = new DefaultTreeCellRenderer();
        }
        if (_treeCellRenderer == null) {
            _treeCellRenderer = createCellRenderer(cellRenderer);
        }
        else {
            _treeCellRenderer.setActualTreeRenderer(cellRenderer);
        }
        return _treeCellRenderer;
    }

    /**
     * Gets the actual cell renderer. Since CheckBoxTree has its own check box cell renderer, this method
     * will give you access to the actual cell renderer which is either the default tree cell renderer or
     * the cell renderer you set using {@link #setCellRenderer(javax.swing.tree.TreeCellRenderer)}.
     *
     * @return the actual cell renderer
     */
    public TreeCellRenderer getActualCellRenderer() {
        if (_treeCellRenderer != null) {
            return _treeCellRenderer.getActualTreeRenderer();
        }
        else {
            return super.getCellRenderer();
        }
    }

    /**
     * Creates the cell renderer.
     *
     * @param renderer the actual renderer for the tree node. This method will
     *                 return a cell renderer that use a check box and put the actual renderer inside it.
     * @return the cell renderer.
     */
    protected CheckBoxTreeCellRenderer createCellRenderer(TreeCellRenderer renderer) {
        final CheckBoxTreeCellRenderer checkBoxTreeCellRenderer = new CheckBoxTreeCellRenderer(renderer);
        addPropertyChangeListener(CELL_RENDERER_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                checkBoxTreeCellRenderer.setActualTreeRenderer((TreeCellRenderer) evt.getNewValue());
            }
        });
        return checkBoxTreeCellRenderer;
    }

    /**
     * Creates the mouse listener and key listener used by CheckBoxTree.
     *
     * @return the Handler.
     */
    protected Handler createHandler() {
        return new Handler(this);
    }

    protected static class Handler implements MouseListener, KeyListener, TreeSelectionListener {
        protected CheckBoxTree _tree;
        int _hotspot = new UICheckBox().getPreferredSize().width;
        private int _toggleCount = -1;

        public Handler(CheckBoxTree tree) {
            _tree = tree;
        }

        protected TreePath getTreePathForMouseEvent(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return null;
            }

            if (!_tree.isCheckBoxEnabled()) {
                return null;
            }

            TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
            if (path == null)
                return null;

            if (clicksInCheckBox(e, path)) {
                return path;
            }
            else {
                return null;
            }
        }

        protected boolean clicksInCheckBox(MouseEvent e, TreePath path) {
            if (!_tree.isCheckBoxVisible(path)) {
                return false;
            }
            else {
                Rectangle bounds = _tree.getPathBounds(path);
                if (_tree.getComponentOrientation().isLeftToRight()) {
                    return e.getX() < bounds.x + _hotspot;
                }
                else {
                    return e.getX() > bounds.x + bounds.width - _hotspot;
                }
            }
        }

        private TreePath preventToggleEvent(MouseEvent e) {
            TreePath pathForMouseEvent = getTreePathForMouseEvent(e);
            if (pathForMouseEvent != null) {
                int toggleCount = _tree.getToggleClickCount();
                if (toggleCount != -1) {
                    _toggleCount = toggleCount;
                    _tree.setToggleClickCount(-1);
                }
            }
            return pathForMouseEvent;
        }

        public void mouseClicked(MouseEvent e) {
            preventToggleEvent(e);
        }

        public void mousePressed(MouseEvent e) {
            TreePath path = preventToggleEvent(e);
            if (path != null) {
                toggleSelection(path);
                e.consume();
            }
        }

        public void mouseReleased(MouseEvent e) {
            TreePath path = preventToggleEvent(e);
            if (path != null) {
                e.consume();
            }
            if (_toggleCount != -1) {
                _tree.setToggleClickCount(_toggleCount);
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e.isConsumed()) {
                return;
            }

            if (!_tree.isCheckBoxEnabled()) {
                return;
            }

            if (e.getModifiers() == 0 && e.getKeyChar() == KeyEvent.VK_SPACE)
                toggleSelections();
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        public void valueChanged(TreeSelectionEvent e) {
            _tree.treeDidChange();
        }

        private void toggleSelection(TreePath path) {
            if (!_tree.isEnabled() || !_tree.isCheckBoxEnabled(path)) {
                return;
            }
            CheckBoxTreeSelectionModel selectionModel = _tree.getCheckBoxTreeSelectionModel();
            boolean selected = selectionModel.isPathSelected(path, selectionModel.isDigIn());
            selectionModel.removeTreeSelectionListener(this);
            try {
                if (!selectionModel.isSingleEventMode()) {
                    selectionModel.setBatchMode(true);
                }
                if (selected)
                    selectionModel.removeSelectionPath(path);
                else
                    selectionModel.addSelectionPath(path);
            }
            finally {
                if (!selectionModel.isSingleEventMode()) {
                    selectionModel.setBatchMode(false);
                }
                selectionModel.addTreeSelectionListener(this);
                _tree.treeDidChange();
            }
        }

        protected void toggleSelections() {
            TreePath[] treePaths = _tree.getSelectionPaths();
            if (treePaths == null) {
                return;
            }
            for (int i = 0, length = treePaths.length; i < length; i++) {
            	TreePath tmpTreePath = treePaths[i];
            	toggleSelection(tmpTreePath);
            }
//            for (TreePath treePath : treePaths) {
//                toggleSelection(treePath);
//            }
        }
    }

    public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
        return null;
    }

    /**
     * Gets the selection model for the check boxes. To retrieve the state of check boxes, you should use this selection model.
     *
     * @return the selection model for the check boxes.
     */
    public CheckBoxTreeSelectionModel getCheckBoxTreeSelectionModel() {
        return _checkBoxTreeSelectionModel;
    }

    /**
     * Gets the value of property checkBoxEnabled. If true, user can
     * click on check boxes on each tree node to select and unselect.
     * If false, user can't click but you as developer can programatically
     * call API to select/unselect it.
     *
     * @return the value of property checkBoxEnabled.
     */
    public boolean isCheckBoxEnabled() {
        return _checkBoxEnabled;
    }

    /**
     * Sets the value of property checkBoxEnabled.
     *
     * @param checkBoxEnabled true to allow to check the check box. False to disable it
     *                        which means user can see whether a row is checked or not but they cannot change it.
     */
    public void setCheckBoxEnabled(boolean checkBoxEnabled) {
        if (checkBoxEnabled != _checkBoxEnabled) {
            Boolean oldValue = _checkBoxEnabled ? Boolean.TRUE : Boolean.FALSE;
            Boolean newValue = checkBoxEnabled ? Boolean.TRUE : Boolean.FALSE;
            _checkBoxEnabled = checkBoxEnabled;
            firePropertyChange(PROPERTY_CHECKBOX_ENABLED, oldValue, newValue);
            repaint();
        }
    }

    /**
     * Checks if check box is enabled. There is no setter for it. The only way is to override
     * this method to return true or false.
     *
     * @param path the tree path.
     * @return true or false. If false, the check box on the particular tree path will be disabled.
     */
    public boolean isCheckBoxEnabled(TreePath path) {
        return true;
    }

    /**
     * Checks if check box is visible. There is no setter for it. The only way is to override
     * this method to return true or false.
     *
     * @param path the tree path.
     * @return true or false. If false, the check box on the particular tree path will be disabled.
     */
    public boolean isCheckBoxVisible(TreePath path) {
        return true;
    }

    /**
     * Gets the dig-in mode. If the CheckBoxTree is in dig-in mode, checking the parent node
     * will check all the children. Correspondingly, getSelectionPaths() will only return the
     * parent tree path. If not in dig-in mode, each tree node can be checked or unchecked independently
     *
     * @return true or false.
     */
    public boolean isDigIn() {
        return getCheckBoxTreeSelectionModel().isDigIn();
    }

    /**
     * Sets the dig-in mode. If the CheckBoxTree is in dig-in mode, checking the parent node
     * will check all the children. Correspondingly, getSelectionPaths() will only return the
     * parent tree path. If not in dig-in mode, each tree node can be checked or unchecked independently
     *
     * @param digIn the new digIn mode.
     */
    public void setDigIn(boolean digIn) {
        boolean old = isDigIn();
        if (old != digIn) {
            getCheckBoxTreeSelectionModel().setDigIn(digIn);
        }
    }
}