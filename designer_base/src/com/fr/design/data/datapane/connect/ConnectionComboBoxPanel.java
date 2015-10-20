package com.fr.design.data.datapane.connect;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.data.impl.AbstractDatabaseConnection;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
/**
 * ѡ���������ӵ�������
 *
 * @editor zhou
 * @since 2012-3-28����3:02:30
 */
public class ConnectionComboBoxPanel extends ItemEditableComboBoxPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Class<Connection> cls; // ��ȡ��Connection����cls��������
    private java.util.List<String> nameList = new ArrayList<String>();

    public ConnectionComboBoxPanel(Class<Connection> cls) {
        super();

        this.cls = cls;

        // alex:���item change����,���ı�ʱ�ı�DesignerEnvManager�е����ѡ�е���������
        this.itemComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selected = ConnectionComboBoxPanel.this.getSelectedItem();
                if (StringUtils.isNotBlank(selected)) {
                    DesignerEnvManager.getEnvManager().setRecentSelectedConnection(selected);
                }
            }
        });
        refreshItems();
    }

    /*
     * ˢ��ComboBox.items
     */
    protected java.util.Iterator<String> items() {
        nameList = new ArrayList<String>();

        DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
        java.util.Iterator<String> nameIt = mgr.getConnectionNameIterator();
        while (nameIt.hasNext()) {
            String conName = nameIt.next();
            Connection connection = mgr.getConnection(conName);
            connection.addConnection(nameList, conName, new Class[]{AbstractDatabaseConnection.class});
        }

        return nameList.iterator();
    }


    public int getConnectionSize() {
        return nameList.size();
    }

    public String getConnection(int i) {
        return nameList.get(i);
    }

    /*
     * �����Ի���༭Items
     */
    protected void editItems() {
        final ConnectionListPane connectionListPane = new ConnectionListPane();
        final DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        connectionListPane.populate(datasourceManager);
        BasicDialog connectionListDialog = connectionListPane.showLargeWindow(
                SwingUtilities.getWindowAncestor(ConnectionComboBoxPanel.this), new DialogActionAdapter() {
            public void doOk() {
                connectionListPane.update(datasourceManager);
                // marks:��������
                Env currentEnv = FRContext.getCurrentEnv();
                try {
                    currentEnv.writeResource(datasourceManager);
                } catch (Exception ex) {
                    FRContext.getLogger().error(ex.getMessage(), ex);
                }
            }
        });
        connectionListDialog.setVisible(true);
        refreshItems();
    }

    public void populate(com.fr.data.impl.Connection connection) {
        editButton.setEnabled(FRContext.getCurrentEnv().isRoot());
        if (connection instanceof NameDatabaseConnection) {
            this.setSelectedItem(((NameDatabaseConnection) connection).getName());
        } else {
            String s = DesignerEnvManager.getEnvManager().getRecentSelectedConnection();
            if (StringUtils.isNotBlank(s)) {
                for (int i = 0; i < this.getConnectionSize(); i++) {
                    String t = this.getConnection(i);
                    if (ComparatorUtils.equals(s, t)) {
                        this.setSelectedItem(s);
                        break;
                    }
                }
            }
            // alex:������ComboBox����û��ѡ��,��ôѡ�е�һ��
            if (StringUtils.isBlank(this.getSelectedItem()) && this.getConnectionSize() > 0) {
                this.setSelectedItem(this.getConnection(0));
            }
        }
    }
}
