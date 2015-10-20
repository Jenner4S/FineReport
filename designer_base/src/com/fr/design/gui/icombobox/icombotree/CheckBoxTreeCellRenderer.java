package com.fr.design.gui.icombobox.icombotree;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer
{
    protected JCheckBox check;
    protected CheckBoxTreeLabel label;

    public CheckBoxTreeCellRenderer()
    {
        this.setLayout(null);
        this.add(check = new JCheckBox());
        this.add(label = new CheckBoxTreeLabel());
        check.setBackground(UIManager.getColor("Tree.textBackground"));
        label.setForeground(UIManager.getColor("Tree.textForeground"));
        this.setBackground(UIManager.getColor("Tree.textBackground"));
    }

    /**
     * ���ص���һ��<code>JPanel</code>���󣬸ö����а���һ��<code>JCheckBox</code>����
     * ��һ��<code>JLabel</code>���󡣲��Ҹ���ÿ������Ƿ�ѡ��������<code>JCheckBox</code>
     * �Ƿ�ѡ�С�
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus)
    {
        String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        check.setSelected(((CheckBoxTreeNode)value).isSelected());
        label.setFont(tree.getFont());
        label.setText(stringValue);
        label.setSelected(selected);
        label.setFocus(hasFocus);
//        if(leaf)
//            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
//        else if(expanded)
//            label.setIcon(UIManager.getIcon("Tree.openIcon"));
//        else
//            label.setIcon(UIManager.getIcon("Tree.closedIcon"));

        return this;
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        return new Dimension(dCheck.width + dLabel.width, dCheck.height < dLabel.height ? dLabel.height: dCheck.height);
    }

    /**
     * ���¼���check��labelλ��
     */
    @Override
    public void doLayout()
    {
        Dimension dCheck = check.getPreferredSize();
        Dimension dLabel = label.getPreferredSize();
        int yCheck = 0;
        int yLabel = 0;
        int height = dCheck.height < dLabel.height ? dCheck.height:dLabel.height;
//        if(dCheck.height < dLabel.height)
//            yCheck = (dLabel.height - dCheck.height) / 2;
//        else
//            yLabel = (dCheck.height - dLabel.height) / 2;
        check.setLocation(0, yCheck);
        check.setBounds(0, yCheck, dCheck.width, height);
        label.setLocation(dCheck.width, yLabel);
        label.setBounds(dCheck.width, yLabel, dLabel.width, height);
    }

    @Override
    public void setBackground(Color color)
    {
        if(color instanceof ColorUIResource){
            color = null;
        }
        super.setBackground(color);
    }
}
