package com.fr.design.javascript;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.JavaScriptActionProvider;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.form.ui.WebContentUtils;
import com.fr.general.Inter;
import com.fr.js.JavaScript;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class JavaScriptActionPane extends UIComboBoxPane<JavaScript> {

    private static final long serialVersionUID = 1L;

    private List contentDBManiPane;

    private JavaScript call = null;

    public JavaScriptActionPane() {
        super();
    }

    protected List<FurtherBasicBeanPane<? extends JavaScript>> initPaneList() {
        List<FurtherBasicBeanPane<? extends JavaScript>> paneList = new ArrayList<FurtherBasicBeanPane<? extends JavaScript>>();
        // JS�ű�,���ύ,�ύ���,���̹���,�����ʼ�. 703��ȥ�����ύ�����̹���
        paneList.add(new JavaScriptImplPane(getDefaultArgs()));
//		paneList.add(new FormSubmitJavaScriptPane(this));
        contentDBManiPane = new ArrayList();
        contentDBManiPane.add(createDBManipulationPane());
        paneList.add(new Commit2DBJavaScriptPane(this, contentDBManiPane));
        paneList.add(new ProcessJSImplPane() {
                         private static final long serialVersionUID = 1;

                         @Override
                         protected Editor[] getCorrespondEditors() {
                             return isForm() ? ValueEditorPaneFactory.formEditors() : ValueEditorPaneFactory.extendedEditors();
                         }
                     }
        );
        paneList.add(initEmaiPane());
        List<JavaScriptActionProvider> javaScriptActionProviders = ExtraDesignClassManager.getInstance().getJavaScriptActionProvider();
        if (javaScriptActionProviders != null) {
            for (JavaScriptActionProvider jsp : javaScriptActionProviders) {
                paneList.add(jsp.getJavaScriptActionPane());
            }
        }
        // �Զ����¼�
//		paneList.add(new CustomActionPane());
        return paneList;
    }

    protected EmailPane initEmaiPane() {
        return new EmailPane();
    }

    protected void initLayout() {
        this.setLayout(new BorderLayout(0, 6));
        JPanel northPane = new JPanel(new BorderLayout());
        northPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 0, 10));
        this.add(northPane, BorderLayout.NORTH);
        northPane.add(jcb, BorderLayout.CENTER);
        this.add(cardPane, BorderLayout.CENTER);

    }

    /**
     * ���ɻص������İ�ť
     *
     * @return ���ذ�ť����
     */
    public UIButton createCallButton() {
        UIButton callButton = new UIButton(Inter.getLocText("Set_Callback_Function"));
        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JavaScriptActionPane callPane = new JavaScriptActionPane() {
                    @Override
                    protected String title4PopupWindow() {
                        return Inter.getLocText("Set_Callback_Function");
                    }

                    @Override
                    protected DBManipulationPane createDBManipulationPane() {
                        DBManipulationPane dbPane = JavaScriptActionPane.this.createDBManipulationPane();
                        dbPane.setParentJavaScriptActionPane(JavaScriptActionPane.this);
                        return dbPane;
                    }

                    @Override
                    public boolean isForm() {
                        return JavaScriptActionPane.this.isForm();
                    }

                    public String[] getDefaultArgs() {
                        return new String[]{WebContentUtils.FR_SUBMITINFO};
                    }

                };

                callPane.populateBean(getCall());

                BasicDialog dialog = callPane.showWindow(DesignerContext.getDesignerFrame());
                dialog.addDialogActionListener(new DialogActionAdapter() {

                    @Override
                    public void doOk() {
                        super.doOk();
                        setCall(callPane.updateBean());
                    }
                });

                dialog.setVisible(true);
            }
        });

        return callButton;
    }

    public void setCall(JavaScript call) {
        this.call = call;
    }

    public JavaScript getCall() {
        return call;
    }

    // Ĭ�ϲ���
    protected abstract String[] getDefaultArgs();

    //�������𱨱����
    protected abstract boolean isForm();

    protected abstract DBManipulationPane createDBManipulationPane();

    public List getContentDBManiPane() {
        return contentDBManiPane;
    }

    public static JavaScriptActionPane defaultJavaScriptActionPane = new JavaScriptActionPane() {

        private static final long serialVersionUID = 1L;

        @Override
        public DBManipulationPane createDBManipulationPane() {
            return new DBManipulationPane();
        }

        @Override
        protected String title4PopupWindow() {
            return Inter.getLocText("Set_Callback_Function");
        }

        @Override
        public boolean isForm() {
            return false;
        }

        public String[] getDefaultArgs() {
            return new String[0];
        }
    };

    /**
     * ���ɽ���Ĭ�ϵ��齨
     *
     * @return �������ɵ����
     */
    public static JavaScriptActionPane createDefault() {
        return new JavaScriptActionPane() {

            @Override
            public DBManipulationPane createDBManipulationPane() {
                return new DBManipulationPane();
            }

            @Override
            protected String title4PopupWindow() {
                return Inter.getLocText("Set_Callback_Function");
            }

            @Override
            public boolean isForm() {
                return false;
            }

            public String[] getDefaultArgs() {
                return new String[0];
            }
        };
    }
}
