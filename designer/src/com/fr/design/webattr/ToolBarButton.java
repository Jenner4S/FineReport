package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetManager;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ToolBarButton extends UIButton implements MouseListener {
    private Widget widget;
    private WidgetOption no;

    public ToolBarButton(Icon icon, Widget widget) {
        this(null, icon, widget);
    }

    public ToolBarButton(String text, Icon icon, Widget widget) {
        super(text, icon);
        this.widget = widget;
        if (widget instanceof com.fr.form.ui.Button) {
            com.fr.form.ui.Button button = (com.fr.form.ui.Button) widget;
            String iconName = button.getIconName();
            if (StringUtils.isNotEmpty(iconName)) {
                Image iimage = WidgetManager.getProviderInstance().getIconManager().getIconImage(iconName);
                if (iimage != null) {
                    setIcon(new ImageIcon(iimage));
                }
            }
        }
        this.addMouseListener(this);
        setMargin(new Insets(0, 0, 0, 0));
    }


    /**
     * �ı䰴ť��Ȩ��ϸ����״̬
     *
     * @param selectedRole ѡ��Ľ�ɫ
     * @param isVisible    �Ƿ�ɼ�
     */
    public void changeAuthorityState(String selectedRole, boolean isVisible) {
        this.widget.changeOnlyVisibleAuthorityState(selectedRole, isVisible);
    }

    /**
     * �Ƕ�����Ȩ��ϸ����
     *
     * @param role ѡ��Ľ�ɫ
     * @return ���Ƕ�Ӧ�ĸý�ɫ����Ȩ��ϸ���ȣ��򷵻�true
     */
    public boolean isDoneAuthorityEdited(String role) {
        return this.widget.isDoneVisibleAuthority(role);
    }

    public Widget getWidget() {
        return this.widget;
    }


    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public WidgetOption getNameOption() {
        return this.no;
    }

    public void setNameOption(WidgetOption no) {
        this.no = no;
    }


    protected void paintBorder(Graphics g) {
        this.setBorderType(UIButton.NORMAL_BORDER);
        super.paintBorder(g);
    }

    /**
     * ����������¼�
     *
     * @param e ������¼�
     */
    public void mouseClicked(MouseEvent e) {
        if (BaseUtils.isAuthorityEditing()) {
            auhtorityMouseAction();
            return;
        }
        if (e.getClickCount() >= 2) {
            if (this.getParent() instanceof ToolBarPane) {
                final ToolBarPane tb = (ToolBarPane) this.getParent();
                final EditToolBar etb = new EditToolBar();
                etb.populate(tb.getFToolBar(), this);
                BasicDialog dialog = etb.showWindow(DesignerContext.getDesignerFrame());
                dialog.addDialogActionListener(new DialogActionAdapter() {
                    public void doOk() {
                        tb.setFToolBar(etb.update());
                    }
                });
                dialog.setVisible(true);
            }
        }
    }


    private void auhtorityMouseAction() {
        if (this.getParent() instanceof ToolBarPane && this.isEnabled()) {
            this.setSelected(!this.isSelected());

        }

    }

    /**
     * �������¼�
     *
     * @param e �������¼�
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * ����˳��¼�
     *
     * @param e ����˳��¼�
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * ��갴���¼�
     *
     * @param e ����¼�
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * ����ͷ��¼�
     *
     * @param e ����¼�
     */
    public void mouseReleased(MouseEvent e) {

    }
}
