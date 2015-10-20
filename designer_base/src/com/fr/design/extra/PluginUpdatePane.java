package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginUpdatePane extends PluginAbstractLoadingViewPane<Plugin[], Void> {

    private PluginControlPane controlPane;
    private JLabel errorMsgLabel;
    private UITabbedPane tabbedPane;
    
    private static final int PERSENT = 100;

    public PluginUpdatePane(UITabbedPane tabbedPane) {
        super(tabbedPane);
        this.tabbedPane = tabbedPane;
    }

    /**
     * ����pane
     * @return ͬ��
     */
    public JPanel createSuccessPane() {
        return new PluginStatusCheckCompletePane() {

            @Override
            public void pressInstallButton() {
                doUpdateOnline(this);
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update");
            }



            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JPanel centerPane() {
                controlPane = new PluginControlPane();
                final PluginStatusCheckCompletePane s = this;
                controlPane.addPluginSelectionListener(new PluginSelectListener() {
                    @Override
                    public void valueChanged(Plugin plugin) {
                        s.setInstallButtonEnable(true);
                    }
                });
                return controlPane;
            }
        };
    }

    /**
     * ����pane
     * @return ͬ��
     */
    @Override
    public JPanel createErrorPane() {
        errorMsgLabel = new UILabel();
        errorMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new PluginStatusCheckCompletePane() {

            @Override
            public void pressInstallButton() {
                doUpdateOnline(this);
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update");
            }



            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JComponent centerPane() {
                return errorMsgLabel;
            }
        };
    }

    /**
     * ���ز��
     * @return ���в��
     */
    public Plugin[] loadData() throws Exception {
        Thread.sleep(3000);
        return PluginsReaderFromStore.readPluginsForUpdate();
    }

    /**
     * ���سɹ�����
     * 
     * @param plugins ���
     */
    public void loadOnSuccess(Plugin[] plugins) {
        controlPane.loadPlugins(plugins);
        tabbedPane.setTitleAt(1, Inter.getLocText("FR-Designer-Plugin_Update") + "(" + plugins.length + ")");
    }

    /**
     * ����ʧ�ܴ���
     * 
     * @param e �쳣
     */
    public void loadOnFailed(Exception e) {
        errorMsgLabel.setText(e.getCause().getMessage());
    }

    /**
     * ��
     * @return ��
     */
    @Override
    public String textForLoadingLabel() {
        return Inter.getLocText("FR-Designer-Plugin_Detecting_Update");
    }

    protected void installFromDiskFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("zip", "zip"));
        int returnValue = fileChooser.showOpenDialog(PluginUpdatePane.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File chosenFile = fileChooser.getSelectedFile();
            doUpdateFromFile(chosenFile);
        }
    }

    private void doUpdateOnline(final PluginStatusCheckCompletePane pane) {
        new SwingWorker<Void, Double>(){

            @Override
            protected Void doInBackground() throws Exception {
                Plugin plugin = controlPane.getSelectedPlugin();
                String id = null;
                if (plugin != null) {
                    id = plugin.getId();
                }
                try {
                    PluginHelper.downloadPluginFile(id, new Process<Double>() {
                        @Override
                        public void process(Double integer) {
                            publish(integer);
                        }
                    });
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
                return null;
            }

            public void process(List<Double> list) {
                pane.setProgress(list.get(list.size() - 1) * PERSENT);
            }

            public void done() {
                //������ɣ���ʼִ�а�װ
                try {
                    get();
                    pane.didTaskFinished();
                    doUpdateFromFile(PluginHelper.getDownloadTempFile());
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }.execute();
    }

    private void doUpdateFromFile(File chosenFile) {
        try {
            Plugin plugin = PluginHelper.readPlugin(chosenFile);
            if (plugin == null) {
                JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Plugin oldPlugin = PluginLoader.getLoader().getPluginById(plugin.getId());
            if (oldPlugin != null) {
                // ˵����װ��ͬID�Ĳ�����ٱȽ���������İ汾
                if (PluginHelper.isNewThan(plugin, oldPlugin)) {
                    // ˵�����µĲ����ɾ���ϵ�Ȼ��װ�µ�
                    final String[] files = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), oldPlugin);
                    PluginHelper.installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, new After() {
                        @Override
                        public void done() {
                            int rv = JOptionPane.showOptionDialog(
                                    PluginUpdatePane.this,
                                    Inter.getLocText("FR-Designer-Plugin_Update_Successful"),
                                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                                            Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")
                                    },
                                    null
                            );

                            if (rv == JOptionPane.OK_OPTION) {
                                RestartHelper.restart();
                            }

                            // ������������������Ͱ�Ҫɾ�����ļ��������
                            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                                RestartHelper.saveFilesWhichToDelete(files);
                            }
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Version_Is_Lower_Than_Current"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Cannot_Update_Not_Install"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(PluginUpdatePane.this, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * �Ӵ��̰�װ��ť����ʾ
     * @return ��ť�����ַ���
     */
    @Override
    public String textForInstallFromDiskFileButton() {
        return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
    }
    @Override
    protected String title4PopupWindow() {
        return "Update";
    }
}
