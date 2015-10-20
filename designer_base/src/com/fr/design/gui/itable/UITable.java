package com.fr.design.gui.itable;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;

public class UITable extends JTable implements UIObserver {


    private static final int OFF_LEFT = 10;
    private static final int DEFAULT_ROW_HEIGHT =20;
    private UIObserverListener uiObserverListener;
    UITableEditor editor ;
    private boolean shouldResponseAwt;
    private boolean isEditingStopped;


    /**
	 * ��û���κ����ݵ�ʱ��ʹ�ô˹��캯����Ȼ��ͨ��populate��������
	 *
	 * @param columnSize �б������
	 */
	public UITable(int columnSize) {

		super(new UITableDataModel(columnSize));
		initComponents();
        iniListener();
        shouldResponseAwt = false;
        // kunsnat: ����: ����������, �޷��ȴ�ѡ����֮����stop..
//        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
	}

    public UITable (int columnSize, boolean needAWTEventListener) {
        this(columnSize);
        shouldResponseAwt = needAWTEventListener;
        isEditingStopped = true;
        if (needAWTEventListener) {
            Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
            this.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    isEditingStopped = false;
                }
            });
        }
    }

	/**
	 * values������Ϊ�գ�
	 *
	 * @param values һ���б�����װ���ַ������飬ÿ���������һ������
	 */
	public UITable(List<Object[]> values) {
		super(new UITableDataModel(values));
		initComponents();
        iniListener();
	}

	public UITable() {

	}

	public void populateBean(List<Object[]> values) {
		getTableDataModel().populateBean(values);
	}



    private AWTEventListener awt = new AWTEventListener() {
    		public void eventDispatched(AWTEvent event) {
                if(!UITable.this.isShowing()){
                    return;
                }
    			doSomeInAll(event);
    		}
    	};

    	private void doSomeInAll(AWTEvent event) {
            Rectangle bounds = new Rectangle(getLocationOnScreen().x, getLocationOnScreen().y, getWidth(), getHeight());
            if (event instanceof MouseEvent) {
    			MouseEvent mv = (MouseEvent) event;
    			if (mv.getClickCount() > 0) {
    				Point point = new Point((int) (mv.getLocationOnScreen().getX()) - 2 * OFF_LEFT, (int) mv.getLocationOnScreen().getY());
    				// �ж�������Ƿ��ڱ߽���
    				if (!bounds.contains(point) && shouldResponseAwt) {
                        if (!isEditingStopped) {
                            this.editor.stopCellEditing();
                            isEditingStopped = true;
                        }
    				}
    			}
    		}
    	}

	public List<Object[]> updateBean() {
		return getTableDataModel().updateBean();
	}

	/**
	 * ��table�ײ�����һ����
	 */
	public void addBlankLine() {
		getTableDataModel().addBlankLine();
	}

	/**
	 * ��table�ײ�����һ������
	 * @param line ���е�����
	 */
	public void addLine(Object[] line) {
		getTableDataModel().addLine(line);
	}

	/**
	 * @param rowIndex
	 * @return ĳһ�е�����
	 */
	public Object[] getLine(int rowIndex) {
		return getTableDataModel().getLine(rowIndex);
	}

	/**
	 * ɾ��ĳ������
	 *
	 * @param rowIndex �к�
	 */
	public void removeLine(int rowIndex) {
		getTableDataModel().removeLine(rowIndex);
	}

	/**
	 * ��ĳһ���϶�ʱ��������
	 *
	 * @param rowIndex �к�
	 * @param positive ����ƶ��ľ���
	 */
	public void dragSort(int rowIndex, boolean positive) {
		((UITableDataModel) dataModel).dragSort(rowIndex, positive);
	}


	/**
	 *�����Ƿ�ɱ༭�����ö�ĳһ��column���ɱ༭
	 * @param row �к�
	 * @param column �к�
	 * @return �Ƿ�ɱ༭
	 */
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * �������
	 */
	public void clear() {
		getTableDataModel().clear();
	}
	
	private UITableDataModel getTableDataModel() {
		return (UITableDataModel) dataModel;
	}

	/**
	 * @param value  �����е�ֵ(�ַ���)
	 * @param row
	 * @param column
	 * @return �б���Ĭ����ʾ�Ķ���������кܶ����ݣ�����װ��һ��JPanel����Ƕ����
	 */
	protected JComponent getRenderCompoment(Object value, int row, int column) {
		UILabel text = new UILabel();
		if (value != null) {
			text.setText(value.toString());
		}
		return text;
	}

	protected void initComponents() {
		setShowGrid(false);
		setRowHeight(getRowHeight4Table());
		setDragEnabled(false);
		editor = createTableEditor();
		editor.addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingStopped(ChangeEvent e) {
				tableCellEditingStopped(e);
			}

			@Override
			public void editingCanceled(ChangeEvent e) {
				tableCellEditingCanceled(e);
			}

		});
		
		setBackground(UIConstants.NORMAL_BACKGROUND);
		setDefaultEditor(UITable.class, editor);
		setDefaultRenderer(UITable.class, new UITableRender());
		setUI(getUI());
		
		TableColumn deleteTableColumn = new TableColumn(getTableDataModel().getColumnCount());
		deleteTableColumn.setCellEditor(null);
		deleteTableColumn.setCellRenderer(null);
		deleteTableColumn.setMaxWidth(20);
		getColumnModel().addColumn(deleteTableColumn);
	}

	/**
	 * ���������ĳһ��ʱ�������¼�
	 * @param index �к�
	 */
	public void dealWithRollOver(int index){

	}

    private void iniListener(){
        if(shouldResponseChangeListener()){
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(uiObserverListener == null){
                        return ;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

	protected int getRowHeight4Table() {
		return DEFAULT_ROW_HEIGHT;
	}


	/**
	 *ֹͣ�༭�¼�
	 * @param e �¼�
	 */
	public void tableCellEditingStopped(ChangeEvent e) {

	}

    /**
     *ȡ���༭�¼�
     * @param e �¼�
     */
	public void tableCellEditingCanceled(ChangeEvent e) {

	}

	/**
	 * �༭��
	 * @return �༭��
	 */
	public UITableEditor createTableEditor() {
		return new UIDefaultTableCellEditor(new UITextField());
	}

	@Override
    /**
     *
     */
	public TableUI getUI() {
		return new UITableUI();
	}

	@Override
    /**
     *
     */
	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		columnClass = UITable.class;
		return super.getDefaultEditor(columnClass);
	}

	@Override
    /**
     *
     */
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		columnClass = UITable.class;
		return super.getDefaultRenderer(columnClass);
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    protected class UITableRender implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
													   Object value, boolean isSelected, boolean hasFocus, int row,
													   int column) {
			JComponent comp = getRenderCompoment(value, row, column);
			comp.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
			return comp;
		}
	}

	protected void fireTargetChanged() {
		repaint();
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	/**
	 * ���Ӽ���
	 * @param l ����
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

    /**
     *�Ƴ�����
     * @param l ����
     */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

    /**
     *����������
     * @param args ����
     */
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(new BorderLayout());
		List<Object[]> data = new ArrayList<Object[]>();
		String[] a = {"1", "11"};
		String[] b = {"2", "22"};
		String[] c = {"3", "33"};
		String[] d = {"4", "44"};
		data.add(a);
		data.add(b);
		data.add(c);
		data.add(d);
		UITable pane = new UITable(2);
		pane.populateBean(data);
		content.add(pane, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}  
