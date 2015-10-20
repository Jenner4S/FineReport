package com.fr.design.gui.itextfield;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * @author Jerry
 */
public class UITextField extends JTextField implements UIObserver, GlobalNameObserver {
    private boolean isBorderPainted = true;
    private boolean isRoundBorder = true;
    private int rectDirection = Constants.NULL;
    private UIObserverListener uiObserverListener;
    private String textFeildName = "";
    private GlobalNameListener globalNameListener = null;
    private Dimension preferredSize = null;

    public UITextField() {
        super();
        initListener();
    }

    public UITextField(int columns) {
        super(columns);
        initListener();
    }

    public UITextField(String text, int columns) {
        super(text, columns);
        initListener();
    }

    public UITextField(String text) {
        super(text);
        initListener();
    }

    public UITextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        initListener();
    }

    protected void initListener() {
        if (shouldResponseChangeListener()) {
            getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    attributeChange();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    attributeChange();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    attributeChange();
                }
            });
        }
    }


    public void setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
    }

    public void setGlobalName(String name) {
        textFeildName = name;
    }

    protected void attributeChange() {
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(textFeildName);
        }
        if (uiObserverListener != null) {
            uiObserverListener.doChange();
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 4, 0, 4);
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            return new Dimension(super.getPreferredSize().width, 20);
        }
        return preferredSize;
    }

    @Override
    public UITextFieldUI getUI() {
        return (UITextFieldUI) ui;
    }

    /**
     * ���ñ仯�ı�����ɫ
     */
    public void setBackgroundUIColor(Color color) {
        ((UITextFieldUI) this.ui).setBackgroundColor4NoGiveNumber(color);
    }

    /**
     * ����UI
     */
    public void updateUI() {
        this.setUI(new UITextFieldUI(this));
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (!isBorderPainted) {
            return;
        }
        getUI().paintBorder((Graphics2D) g, getWidth(), getHeight(), isRoundBorder, rectDirection);
    }

    /**
     * @param isRoundBorder
     */
    public void setRoundBorder(boolean isRoundBorder) {
        this.isRoundBorder = isRoundBorder;
    }


    /**
     * @param rectDirection
     */
    public void setRectDirection(int rectDirection) {
        this.rectDirection = rectDirection;
    }

    /**
     * ע�����
     *
     * @param listener �۲��߼����¼�
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * �Ƿ���Ҫ��Ӧ��������Ӧ
     *
     * @return ���򷵻�true
     */
    public boolean shouldResponseNameListener() {
        return true;
    }


    /**
     * ע�������
     *
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * �Ƿ���Ҫ��Ӧ��������Ӧ
     *
     * @return ���򷵻�true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * ������
     * @param args ����
     */
    public static void main(String... args) {
        LayoutManager layoutManager = null;
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(layoutManager);

        UITextField bb = new UITextField(5);
        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
        content.add(bb);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }

    public void setBorderPainted(boolean isBorderPainted) {
        this.isBorderPainted = isBorderPainted;
    }

}
