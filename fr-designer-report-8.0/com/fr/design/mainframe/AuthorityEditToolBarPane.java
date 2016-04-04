// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.webattr.ToolBarButton;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            AuthorityPropertyPane, JTemplate, AuthorityToolBarPane

public class AuthorityEditToolBarPane extends AuthorityPropertyPane
{
    private class AuthorityEditPane extends JPanel
    {

        private static final int TOP_GAP = 11;
        private static final int LEFT_GAP = 8;
        private static final int ALIGNMENT_GAP = -3;
        private UILabel type;
        private UILabel name;
        private JPanel checkPane;
        private java.util.List buttonlists;
        private UICheckBox buttonVisible;
        private ItemListener itemListener;
        final AuthorityEditToolBarPane this$0;

        private JPanel layoutText()
        {
            double d = -2D;
            Component acomponent[][] = {
                {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("Type")).append(":").toString(), 4)
                }, {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("WF-Name")).append(":").toString(), 4)
                }, {
                    new UILabel((new StringBuilder()).append(Inter.getLocText("DashBoard-Potence")).append(":").toString(), 4)
                }
            };
            double ad[] = {
                d, d, d
            };
            double ad1[] = {
                d
            };
            int ai[][] = {
                {
                    1
                }, {
                    1
                }, {
                    1
                }
            };
            return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
        }

        private JPanel layoutPane()
        {
            double d = -1D;
            double d1 = -2D;
            Component acomponent[][] = {
                {
                    type
                }, {
                    name
                }, {
                    checkPane
                }
            };
            double ad[] = {
                d1, d1, d1
            };
            double ad1[] = {
                d
            };
            int ai[][] = {
                {
                    1
                }, {
                    1
                }, {
                    1
                }
            };
            return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
        }

        public void populateDetials()
        {
            populateName();
            populateType();
            populateCheckPane();
            checkVisibleCheckBoxes();
        }

        private void checkVisibleCheckBoxes()
        {
            buttonVisible.removeItemListener(itemListener);
            String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if(s == null)
                buttonVisible.setSelected(true);
            int i = 0;
            do
            {
                if(i >= buttonlists.size())
                    break;
                if(((ToolBarButton)buttonlists.get(i)).isSelected())
                {
                    buttonVisible.setSelected(!((ToolBarButton)buttonlists.get(i)).isDoneAuthorityEdited(s));
                    break;
                }
                i++;
            } while(true);
            buttonVisible.addItemListener(itemListener);
        }

        public void populateType()
        {
            if(name.getText() == "")
                type.setText("");
            else
                type.setText(Inter.getLocText(new String[] {
                    "ReportServerP-Toolbar", "Form-Button"
                }));
        }

        public void populateName()
        {
            String s = "";
            for(int i = 0; i < buttonlists.size(); i++)
                if(((ToolBarButton)buttonlists.get(i)).isSelected())
                    s = (new StringBuilder()).append(s).append(",").append(((ToolBarButton)buttonlists.get(i)).getNameOption().optionName()).toString();

            if(s != "")
                s = s.substring(1);
            name.setText(s);
        }

        public void populateCheckPane()
        {
            checkPane.removeAll();
            if(name.getText() == "")
            {
                return;
            } else
            {
                double d = -1D;
                double d1 = -2D;
                Component acomponent[][] = {
                    {
                        new UILabel(Inter.getLocText("Form-Button"), 2), buttonVisible
                    }
                };
                double ad[] = {
                    d1, d1
                };
                double ad1[] = {
                    d1, d1, d
                };
                int ai[][] = {
                    {
                        1, 1, 1
                    }, {
                        1, 1, 1
                    }
                };
                JPanel jpanel = TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
                checkPane.add(jpanel, "Center");
                checkPane.setBorder(BorderFactory.createEmptyBorder(-3, 0, 0, 0));
                return;
            }
        }



        public AuthorityEditPane(java.util.List list)
        {
            this$0 = AuthorityEditToolBarPane.this;
            super();
            type = null;
            name = null;
            checkPane = null;
            buttonVisible = new UICheckBox(Inter.getLocText("Widget-Visible"));
            itemListener = new ItemListener() {

                final AuthorityEditPane this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                    if(ComparatorUtils.equals(s, Inter.getLocText("Role")))
                        return;
                    if(s == null)
                        return;
                    for(int i = 0; i < buttonlists.size(); i++)
                        if(((ToolBarButton)buttonlists.get(i)).isSelected())
                        {
                            ((ToolBarButton)buttonlists.get(i)).changeAuthorityState(s, buttonVisible.isSelected());
                            authorityToolBarPane.repaint();
                        }

                    HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
                    RolesAlreadyEditedPane.getInstance().refreshDockingView();
                    UICheckBox uicheckbox = (UICheckBox)itemevent.getSource();
                    java.util.List list1 = buttonlists;
                    for(int j = 0; j < list1.size(); j++)
                        if(((ToolBarButton)list1.get(j)).isSelected())
                            authorityToolBarPane.setAuthorityWebAttr(((ToolBarButton)list1.get(j)).getWidget(), uicheckbox.isSelected(), s);

                }

                
                {
                    this$1 = AuthorityEditPane.this;
                    super();
                }
            }
;
            setLayout(new BorderLayout());
            type = new UILabel();
            name = new UILabel();
            checkPane = new JPanel();
            checkPane.setLayout(new BorderLayout());
            add(layoutText(), "West");
            add(layoutPane(), "Center");
            setBorder(BorderFactory.createEmptyBorder(11, 8, 0, 0));
            buttonlists = list;
            buttonVisible.addItemListener(itemListener);
        }
    }


    private static final int TITLE_HEIGHT = 19;
    private AuthorityEditPane authorityEditPane;
    private AuthorityToolBarPane authorityToolBarPane;

    public AuthorityEditToolBarPane(java.util.List list)
    {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        authorityEditPane = null;
        setLayout(new BorderLayout());
        setBorder(null);
        UILabel uilabel = new UILabel(Inter.getLocText(new String[] {
            "DashBoard-Potence", "Edit"
        })) {

            final AuthorityEditToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 19);
            }

            
            {
                this$0 = AuthorityEditToolBarPane.this;
                super(s);
            }
        }
;
        uilabel.setHorizontalAlignment(0);
        uilabel.setVerticalAlignment(0);
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(uilabel, "Center");
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
        add(jpanel, "North");
        authorityEditPane = new AuthorityEditPane(list);
        add(authorityEditPane, "Center");
    }

    public void setAuthorityToolBarPane(AuthorityToolBarPane authoritytoolbarpane)
    {
        authorityToolBarPane = authoritytoolbarpane;
    }

    public void populate()
    {
        authorityToolBarPane.populateAuthority();
        signelSelection();
        authorityEditPane.populateDetials();
    }

    private void signelSelection()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate.isJWorkBook())
        {
            jtemplate.removeParameterPaneSelection();
            jtemplate.removeTemplateSelection();
        }
    }

}
