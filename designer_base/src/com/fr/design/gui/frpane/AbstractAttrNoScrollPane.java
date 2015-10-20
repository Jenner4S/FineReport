package com.fr.design.gui.frpane;

import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.dialog.BasicPane;

import javax.swing.*;
import java.awt.*;


/**
 * �������Ա��е���壬��Ҫ��Ϊ�˸��������������ڲ����ڶ�UI�ؼ��Ӽ����¼�����UI�ؼ��ı��ʱ��֪ͨģ������Ӧ�ı仯
 */
public abstract class AbstractAttrNoScrollPane extends BasicPane {
	private static final int DEFAULT_HEIGHT = 250;
	private static boolean hasChangeListener;

	protected JPanel leftContentPane;
	protected Color original;

	private AttributeChangeListener listener;
	private String globalName = "";

	protected AbstractAttrNoScrollPane() {
		initAll();
	}

	protected void initAll() {
		enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		original = this.getBackground();
		this.setLayout(new BorderLayout());
		hasChangeListener = false;
		initContentPane();
		initAllListeners();
	}

	/**
	 * ��̨��ʼ�������¼�.
	 */
	public void initAllListeners() {
        initListener(AbstractAttrNoScrollPane.this);
	}

	protected void initContentPane() {
		leftContentPane = createContentPane();
		leftContentPane.setBorder(BorderFactory.createMatteBorder(10, 10, 0, 0, original));
		this.add(leftContentPane, BorderLayout.CENTER);
	}

	protected abstract JPanel createContentPane();

	/**
	 * august:�����װ� ��Ҫ�ñʻ�ͼ�������̲ż������
	 */
	protected void adjustValues() {
		doLayout();
	}

	protected void removeAttributeChangeListener() {
		this.listener = null;
		hasChangeListener = false;
	}


	protected void initListener(Container parentComponent) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof Container) {
				initListener((Container) tmpComp);
			}
			if (tmpComp instanceof GlobalNameObserver) {
				((GlobalNameObserver) tmpComp).registerNameListener(new GlobalNameListener() {
                    public void setGlobalName(String name) {
                        globalName = name;
                    }

                    public String getGlobalName() {
                        return globalName;
                    }
                });
			}
			if (tmpComp instanceof UIObserver) {
				((UIObserver) tmpComp).registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						attributeChanged();
					}
				});
			}
		}
	}

    /**
     * �Ƿ��иı����
     * @return  ���򷵻�true
     */
	public static boolean isHasChangeListener() {
		return hasChangeListener;
	}

	/**
	 * ����Ԥ����Ĵ�С.
	 */
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, DEFAULT_HEIGHT);
	}


    /**
     * ���ذ󶨵������¼�.
     * @param listener  ���Ӽ���
     */
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		this.listener = listener;
		hasChangeListener = true;
	}

	/**
	 * ��Ӧ�����¼�.
	 */
	public void attributeChanged() {
		synchronized (this) {
			if (listener != null) {
				listener.attributeChange();
			}
		}
	}

	/**
	 * ����ͼ���·��
	 */
	public abstract String getIconPath();

    /**
     * �������
     * @return ����
     */
	public abstract String title4PopupWindow();

	/**
	 * ����ѡ�е�ID, ����˫��չʾ����.
	 */
	public void setSelectedByIds(int level, String... id) {

	}

	public String getGlobalName(){
		return globalName;
	}

    /**
     * ��Ҫ����ͼ�������
     * @return ��
     */
    public boolean isNeedPresentPaneWhenFilterData(){
        return true;
    }

}
