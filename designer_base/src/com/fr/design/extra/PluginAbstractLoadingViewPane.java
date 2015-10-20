package com.fr.design.extra;

import com.fr.base.BaseUtils;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richie
 * @date 2015-03-11
 * @since 8.0
 */
public abstract class PluginAbstractLoadingViewPane<V, T> extends PluginAbstractViewPane {
    private static final String LOAD_CARD = "load";
    private static final String SUCCESS_CARD = "success";
    private static final String LOAD_ERROR = "error";

    private static final int BUSYANIMATIONRATE = 30;

    private Icon[] busyIcons = new Icon[15];
    private UILabel statusAnimationLabel;
    private Timer busyIconTimer;
    private int busyIconIndex;
    private CardLayout cardLayout;

    /**
     * ��ʼ��cardlayoutҳ��
     * @param tabbedPane
     */
    public PluginAbstractLoadingViewPane(UITabbedPane tabbedPane) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(initAndStartLoadingComponent(), LOAD_CARD);
        add(createSuccessPane(), SUCCESS_CARD);
        add(createErrorPane(), LOAD_ERROR);
        showLoadingCard();
        loadDataInAnotherThread();
    }
    /**
     * �����ɹ�ҳ��
     * @return ������ҳ�����
     */
    public abstract JPanel createSuccessPane();

    /**
     * ��������ҳ��
     * @return ������ҳ�����
     */
    public abstract JPanel createErrorPane();

    /**
     * ��ʾ����ҳ��
     */
    public void showLoadingCard() {
        cardLayout.show(this, LOAD_CARD);
    }

    /**
     * ��ʾ�ɹ�ҳ��
     */
    public void showSuccessCard() {
        cardLayout.show(this, SUCCESS_CARD);
    }

    /**
     * ��ʾ���ش���ҳ��
     */
    public void showLoadErrorCard() {
        cardLayout.show(this, LOAD_ERROR);
    }

    /**
     * ֹͣ����
     */
    public void stopLoad() {
        busyIconTimer.stop();
    }


    private JPanel initAndStartLoadingComponent() {
         return new PluginStatusCheckCompletePane(){

            @Override
            public void pressInstallButton() {

            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return textForInstallFromDiskFileButton();
            }

            @Override
            public JComponent centerPane() {
                for (int i = 0; i < busyIcons.length; i++) {
                    busyIcons[i] = BaseUtils.readIcon("/com/fr/design/images/load/busy-icon" + i + ".png");
                }
                int busyAnimationRate = BUSYANIMATIONRATE;
                statusAnimationLabel = new UILabel();

                statusAnimationLabel.setText(textForLoadingLabel());
                busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

                        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
                        statusAnimationLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                });
                busyIconTimer.start();

                return statusAnimationLabel;
            }
        };


    }

    private void loadDataInAnotherThread() {
        new SwingWorker<V, T>(){

            @Override
            protected V doInBackground() throws Exception {
                return loadData();
            }

            public void done() {
                stopLoad();
                try {
                    V v = get();
                    loadOnSuccess(v);
                    showSuccessCard();
                } catch (Exception e) {
                    showLoadErrorCard();
                    loadOnFailed(e);
                }
            }

        }.execute();
    }

    protected abstract void installFromDiskFile();

    /**
     * ��������
     * @return ��������
     */
    public abstract V loadData() throws Exception;

    /**
     * ���سɹ�
     * @param v ���õ������ݴ���
     */
    public abstract void loadOnSuccess(V v);

    /**
     * ����ʧ��
     * @param e �쳣��Ϣ
     */
    public abstract void loadOnFailed(Exception e);

    /**
     * ���ڼ���ҳ�ı���
     * @return �����ַ���
     */
    public abstract String textForLoadingLabel();

    /**
     * �Ӵ��̰�װ��ť����ʾ
     * @return ��ť�����ַ���
     */
    public abstract String textForInstallFromDiskFileButton();
}
