package com.fr.design.gui.ilist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.stable.StringUtils;

/**
 * CheckBoxs + JList.
 */
public class CheckBoxList extends JComponent {
	/**
	 * ѡ��״̬----ȫѡ��ȫ��ѡ
	 * 
	 * @editor zhou
	 * @since 2012-4-1����2:39:10
	 */
	public static enum SelectedState {
		ALL, NONE
	}

	private boolean[] selects;
	private JList jlist;
	private UICheckBox chooseAll;

	public CheckBoxList(Object[] items) {
		this(items, SelectedState.NONE, StringUtils.EMPTY);
	}

	public CheckBoxList(Object[] items, String name) {
		this(items, SelectedState.NONE, name);
	}

	/**
	 * Class constructor.
	 * 
	 * @param items
	 *            Items with which to populate the list.
	 * @param default_state
	 *            default state, true or false
	 */
	public CheckBoxList(Object[] items, SelectedState state, String name) {
		jlist = new BOXLIST(items);
		jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.selects = new boolean[items.length];
		boolean default_state = (state == SelectedState.ALL);
		Arrays.fill(this.selects, default_state);

		jlist.setCellRenderer(new CheckListCellRenderer());
		jlist.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doCheck();
			}

			public void mouseReleased(MouseEvent e) {
				doCheck();
			}
		});
		jlist.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					doCheck();
				}
			}

		});
		this.setLayout(new BorderLayout());
		chooseAll = new UICheckBox(name, default_state);
		chooseAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooseAll.isSelected()) {
					setSelected(true);
				} else {
					setSelected(false);
				}
			}
		});
		this.add(chooseAll, BorderLayout.NORTH);
		this.add(jlist, BorderLayout.CENTER);
	}

	/*
	 * ����CellRenderer��ʾvalueΪtext
	 */
	protected String value2Text(Object value) {
		return value != null ? value.toString() : StringUtils.EMPTY;
	}

	public void setItems(Object[] os) {
		if (os == null) {
			this.setSelected(false);
		} else {
			for (int i = 0, len = os.length; i < len; i++) {
				Object o = os[i];
				for (int j = 0, jen = jlist.getModel().getSize(); j < jen; j++) {
					if (o.equals(jlist.getModel().getElementAt(j))) {
						this.setSelected(j, true);
					}
				}
			}
		}
		this.repaint();
	}

	/**
	 * Is selected
	 */
	public boolean isSelected(int index) {
		if (selects == null || index >= selects.length) {
			return false;
		}

		return selects[index];
	}

	public void setSelected(int index, boolean isSelected) {
		if (selects == null || index >= selects.length) {
			return;
		}

		selects[index] = isSelected;
		this.repaint(this.getBounds());

		this.fireCheckBoxListSelectionChangeListener();
	}

	private void setSelected(boolean isSelected) {
		if (selects == null) {
			return;
		}
		for (int i = 0; i < selects.length; i++) {
			selects[i] = isSelected;
		}
		this.repaint(this.getBounds());

		this.fireCheckBoxListSelectionChangeListener();
	}

	/**
	 * Returns an array of the objects that have been selected. Overrides the
	 * JList method.
	 */
	public Object[] getSelectedValues() {
		return this.jlist.getSelectedValues();
	}

	private class BOXLIST extends JList {
		public BOXLIST(Object[] items) {
			super(items);
		}

		@Override
		protected void processMouseEvent(MouseEvent e) {
			if (e.getX() < 20) {
				if (e.isControlDown() || e.isAltDown() || e.isShiftDown() || e.isMetaDown()) {
					int[] indices = getSelectedIndices();
					if (indices.length == 0) {
						super.processMouseEvent(e);
					}
				} else {
					super.processMouseEvent(e);
				}
			} else {
				super.processMouseEvent(e);
			}
			int id = e.getID();
			switch (id) {
			case MouseEvent.MOUSE_PRESSED:
				break;
			case MouseEvent.MOUSE_RELEASED:
				break;
			case MouseEvent.MOUSE_CLICKED:
				doCheck();
				break;
			case MouseEvent.MOUSE_EXITED:
				break;
			case MouseEvent.MOUSE_ENTERED:
				break;
			}
		}

		@Override
		protected void processMouseMotionEvent(MouseEvent e) {
			if (e.getX() < 20) {
				return;
			}

			super.processMouseEvent(e);
		}

		@Override
		public Object[] getSelectedValues() {
			List<Object> list = new ArrayList<Object>(selects.length);
			for (int i = 0; i < selects.length; i++) {
				if (selects[i]) {
					list.add(this.getModel().getElementAt(i));
				}
			}

			return list.toArray();
		}

	}

	private void doCheck() {
		// p:�������ı�����ѡ��checkbox.
		int index = jlist.getSelectedIndex();
		boolean sValue = !selects[index];

		// p:��ʼ��������ѡ���checkbox.
		int[] indices = jlist.getSelectedIndices();
		for (int i = 0; i < indices.length; i++) {
			setSelected(indices[i], sValue);
		}
		for (boolean selected : selects) {
			if (!selected) {
				chooseAll.setSelected(false);
				return;
			}
		}
		chooseAll.setSelected(true);
		repaint();
	}

	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	private class CheckListCellRenderer extends UICheckBox implements ListCellRenderer {

		public CheckListCellRenderer() {
			this.setOpaque(true);
			this.setBorder(noFocusBorder);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(value2Text(value));
			this.setSelected(selects[index]);
			this.setFont(list.getFont());

			if (isSelected) {
				this.setBackground(list.getSelectionBackground());
				this.setForeground(list.getSelectionForeground());
			} else {
				this.setBackground(list.getBackground());
				this.setForeground(list.getForeground());
			}

			if (cellHasFocus) {
				this.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
			} else {
				this.setBorder(noFocusBorder);
			}

			return this;
		}
	}

	public void addCheckBoxListSelectionChangeListener(CheckBoxListSelectionChangeListener l) {
		this.listenerList.add(CheckBoxListSelectionChangeListener.class, l);
	}

	public void removeCheckBoxListSelectionChangeListener(CheckBoxListSelectionChangeListener l) {
		this.listenerList.remove(CheckBoxListSelectionChangeListener.class, l);
	}

	public void fireCheckBoxListSelectionChangeListener() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CheckBoxListSelectionChangeListener.class) {
				((CheckBoxListSelectionChangeListener)listeners[i + 1]).selectionChanged(this);
			}
		}

	}

	public static interface CheckBoxListSelectionChangeListener extends EventListener {
		public void selectionChanged(CheckBoxList target);
	}

	public ListModel getModel() {
		return jlist.getModel();
	}

}
