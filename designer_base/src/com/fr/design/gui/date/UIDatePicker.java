package com.fr.design.gui.date;

import com.fr.base.FRContext;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxUI;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UIDatePicker
 */
public class UIDatePicker extends UIComboBox implements Serializable {
	/**
	 * ���ڸ�ʽ����
	 */
	public static final int STYLE_CN_DATE = 0;
	public static final int STYLE_CN_DATE1 = 1;
	public static final int STYLE_CN_DATETIME = 2;
	public static final int STYLE_CN_DATETIME1 = 3;
    public boolean isWillHide = false;
	/**
	 * ���ڸ�ʽ����
	 */
	private int formatStyle = STYLE_CN_DATETIME;
	/**
	 * ��ǰ�������ڸ�ʽ
	 */
	private SimpleDateFormat dateFormat = null;

	/**
	 * ֻ��һ��ֵ��ComboBoxModel
	 */
	private SingleObjectComboBoxModel model = new SingleObjectComboBoxModel();
	JDateDocument dateDocument = null;

	/**
	 * ����ʽ
	 */
	public UIDatePicker() throws UnsupportedOperationException {
		this(STYLE_CN_DATE);
	}

	public UIDatePicker(int formatStyle) throws UnsupportedOperationException {
		this(formatStyle, new Date());
	}

	public UIDatePicker(int formatStyle, Date initialDatetime) throws UnsupportedOperationException {
		this.setStyle(formatStyle);
		//���ÿɱ༭
		this.setEditable(true);

		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		//���ñ༭������(ֻ��������ȷ����)
		JTextField textField = ((JTextField) getEditor().getEditorComponent());
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		dateDocument = new JDateDocument(textField, this.dateFormat);
		textField.setDocument(dateDocument);
		//����ModelΪ��ֵModel
		this.setModel(model);
		//���õ�ǰѡ������
		this.setSelectedItem(initialDatetime == null ? new Date() : initialDatetime);
        updateUI();
	}

	/**
	 * �������ڸ�ʽ
	 * STYLE_CN_DATE
	 * STYLE_CN_DATE1
	 * STYLE_CN_DATETIME
	 * STYLE_CN_DATETIME1
	 *
	 * @param formatStyle int
	 */
	public void setStyle(int formatStyle) throws UnsupportedOperationException {
		this.formatStyle = formatStyle;
		dateFormat = getDateFormat(formatStyle);
		model.setDateFormat(dateFormat);
		if (dateDocument != null) {
			dateDocument.setDateFormat(dateFormat);
		}
	}

	/**
	 * ȡ��ָ�����͵����ڸ�ʽ
	 *
	 * @param formatStyle int
	 * @return SimpleDateFormat
	 * @throws UnsupportedOperationException
	 */
	private static SimpleDateFormat getDateFormat(int formatStyle) throws
			UnsupportedOperationException {
		switch (formatStyle) {
			case STYLE_CN_DATE:
				return new SimpleDateFormat("yyyy/MM/dd");
			case STYLE_CN_DATE1:
				return new SimpleDateFormat("yyyy-MM-dd");
			case STYLE_CN_DATETIME:
				return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			case STYLE_CN_DATETIME1:
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			default:
				throw new UnsupportedOperationException(
						"invalid formatStyle parameter!");
		}
	}

	/**
	 * ȡ�����ڸ�ʽ
	 * STYLE_CN_DATE
	 * STYLE_CN_DATE1
	 * STYLE_CN_DATETIME
	 * STYLE_CN_DATETIME1
	 *
	 * @return int
	 */
	public int getStyle() {
		return formatStyle;
	}

	/**
	 * ȡ�õ�ǰѡ�������
	 *
	 * @return Date
	 */
	public Date getSelectedDate() throws ParseException {
        synchronized (this) {
            return dateFormat.parse(getSelectedItem().toString());
        }
	}

	/**
	 * ���õ�ǰѡ�������
	 */
	public void setSelectedDate(Date date) throws ParseException {
		this.setSelectedItem(dateFormat.format(date));
	}

	public void setSelectedItem(Object anObject) {
		model.setSelectedItem(anObject);
		super.setSelectedItem(anObject);
	}

	/**
	 * <p>Title: UIDatePicker</p>
	 * <p>Description: DatePopup ѡ��򵯳�������ѡ�����</p>
	 * <p>Copyright: Copyright (c) 2004</p>
	 * <p>Company: </p>
	 *
	 * @author <a href="mailto:sunkingxie@hotmail.com"'>Sunking</a>
	 * @version 1.0
	 */
	class DatePopup extends BasicComboPopup implements ChangeListener {
		UICalendarPanel calendarPanel = null;

		public DatePopup(JComboBox box) {
			super(box);

			setLayout(FRGUIPaneFactory.createBorderLayout());
			calendarPanel = new UICalendarPanel(formatStyle > 1);
			calendarPanel.addDateChangeListener(this);
			add(calendarPanel, BorderLayout.CENTER);
			setBorder(BorderFactory.createEmptyBorder());
		}

        public void hide() {
            if (isWillHide) {
                super.hide();
            }
        }

        public void show() {
            if (isWillHide || UIDatePicker.this.isEnabled() == false) {
                return;
            }
            if (calendarPanel != null) {
                calendarPanel.resetHMSPaneSelectedNumberField();
            }
            super.show();
        }

		/**
		 * ��ʾ�������
		 */
		protected void firePropertyChange(String propertyName,
										  Object oldValue,
										  Object newValue) {
			if (ComparatorUtils.equals(propertyName, "visible")) {
				if (ComparatorUtils.equals(oldValue, Boolean.FALSE)
						&& ComparatorUtils.equals(newValue, Boolean.TRUE)) { //SHOW
					try {
						String strDate = comboBox.getSelectedItem().toString();
                        synchronized (this) {
                            Date selectionDate = dateFormat.parse(strDate);
                            calendarPanel.setSelectedDate(selectionDate);
                            calendarPanel.updateHMS();
                        }
					} catch (Exception e) {
						FRContext.getLogger().error(e.getMessage(), e);
					}
				} else if (ComparatorUtils.equals(oldValue, Boolean.TRUE)
						&& ComparatorUtils.equals(newValue, Boolean.FALSE)) { //HIDE
				}
			}
			super.firePropertyChange(propertyName, oldValue, newValue);
		}

		public void stateChanged(ChangeEvent e) {
            if (calendarPanel.getSelectedDate() != null && dateFormat != null) {
                String strDate = dateFormat.format(calendarPanel.getSelectedDate());
                if (comboBox.isEditable() && comboBox.getEditor() != null) {
                    comboBox.configureEditor(comboBox.getEditor(), strDate);
                }
                comboBox.setSelectedItem(strDate);
            }
            comboBox.repaint();
            setVisible(false);
		}
	}

	protected ComboBoxUI getUIComboBoxUI() {
		return new UIComboBoxUI() {
            protected ComboPopup createPopup() {
                return new DatePopup(comboBox);
            }

            public void mousePressed(MouseEvent e) {
                if (UIDatePicker.this.isPopupVisible()) {
                    isWillHide = true;
                    UIDatePicker.this.hidePopup();
                } else {
                    isWillHide = false;
                    UIDatePicker.this.showPopup();
                }
            }
        };
	}


	
	//����dataFormat
	public void setDateFormat(SimpleDateFormat format){
		this.dateFormat = format;
	}
	
	//��ȡdateFormat
	public SimpleDateFormat getDateFormat(){
		return this.dateFormat;
	}
	
	public JDateDocument getDateDocument(){
		return this.dateDocument;
	}

    public static void main(String[] args) {
        LayoutManager layoutManager = null;
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(layoutManager);
        UIDatePicker bb = new UIDatePicker();
        if (args.length != 0) {
            bb = new UIDatePicker(STYLE_CN_DATETIME);
        }
        bb.setEditable(true);
        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
        content.add(bb);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }
}
