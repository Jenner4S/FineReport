// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.javascript;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.event.Listener;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.Commit2DBJavaScript;
import com.fr.js.CustomActionJavaScript;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormSubmitJavaScript;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.javascript:
//            JavaScriptImplPane, Commit2DBJavaScriptPane, CustomActionPane, EmailPane, 
//            JavaScriptActionPane, FormSubmitJavaScriptPane

public class ListenerEditPane extends BasicBeanPane
{

    private UITextField nameText;
    private UIComboBox styleBox;
    private CardLayout card;
    private JPanel hyperlinkPane;
    private JavaScriptImplPane javaScriptPane;
    private FormSubmitJavaScriptPane formSubmitScriptPane;
    private Commit2DBJavaScriptPane commit2DBJavaScriptPane;
    private CustomActionPane customActionPane;
    private EmailPane emailPane;
    private static final String JS = Inter.getLocText("JavaScript");
    private static final String FORMSUBMIT = Inter.getLocText("JavaScript-Form_Submit");
    private static final String DBCOMMIT = Inter.getLocText("JavaScript-Commit_to_Database");
    private static final String CUSTOMACTION = Inter.getLocText(new String[] {
        "Custom", "RWA-Submit"
    });
    private static final String EMAIL = Inter.getLocText("Email_sentEmail");
    private Listener listener;

    public ListenerEditPane()
    {
        initComponents(new String[0]);
    }

    public ListenerEditPane(String as[])
    {
        initComponents(as);
    }

    public void initComponents(String as[])
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        nameText = new UITextField(8);
        nameText.setEditable(false);
        jpanel.add(nameText, "West");
        String as1[] = {
            JS, DBCOMMIT, CUSTOMACTION, EMAIL
        };
        styleBox = new UIComboBox(as1);
        jpanel.add(styleBox);
        jpanel = GUICoreUtils.createFlowPane(new Component[] {
            new UILabel((new StringBuilder()).append("  ").append(Inter.getLocText("Event_Name")).append(":").toString()), nameText, new UILabel((new StringBuilder()).append("    ").append(Inter.getLocText("Event_Type")).append(":").toString()), styleBox
        }, 0);
        jpanel.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Event_Name_Type")));
        add(jpanel, "North");
        card = new CardLayout();
        hyperlinkPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        hyperlinkPane.setLayout(card);
        javaScriptPane = new JavaScriptImplPane(as);
        hyperlinkPane.add(JS, javaScriptPane);
        ArrayList arraylist = new ArrayList();
        arraylist.add(autoCreateDBManipulationPane());
        commit2DBJavaScriptPane = new Commit2DBJavaScriptPane(JavaScriptActionPane.defaultJavaScriptActionPane, arraylist);
        hyperlinkPane.add(DBCOMMIT, commit2DBJavaScriptPane);
        customActionPane = new CustomActionPane();
        hyperlinkPane.add(CUSTOMACTION, customActionPane);
        emailPane = new EmailPane();
        hyperlinkPane.add(EMAIL, emailPane);
        hyperlinkPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("JavaScript_Set")));
        add(hyperlinkPane);
        styleBox.addItemListener(new ItemListener() {

            final ListenerEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                card.show(hyperlinkPane, styleBox.getSelectedItem().toString());
            }

            
            {
                this$0 = ListenerEditPane.this;
                super();
            }
        }
);
    }

    private DBManipulationPane autoCreateDBManipulationPane()
    {
        JTemplate jtemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        return jtemplate.createDBManipulationPane();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Event_Set");
    }

    public void populateBean(Listener listener1)
    {
        listener = listener1;
        if(listener == null)
            listener = new Listener();
        nameText.setText(listener1.getEventName());
        JavaScript javascript = listener1.getAction();
        if(javascript instanceof JavaScriptImpl)
        {
            styleBox.setSelectedItem(JS);
            card.show(hyperlinkPane, JS);
            javaScriptPane.populateBean((JavaScriptImpl)javascript);
        } else
        if(javascript instanceof FormSubmitJavaScript)
        {
            styleBox.setSelectedItem(FORMSUBMIT);
            card.show(hyperlinkPane, FORMSUBMIT);
            formSubmitScriptPane.populateBean((FormSubmitJavaScript)javascript);
        } else
        if(javascript instanceof Commit2DBJavaScript)
        {
            styleBox.setSelectedItem(DBCOMMIT);
            card.show(hyperlinkPane, DBCOMMIT);
            commit2DBJavaScriptPane.populateBean((Commit2DBJavaScript)javascript);
        } else
        if(javascript instanceof EmailJavaScript)
        {
            styleBox.setSelectedItem(EMAIL);
            card.show(hyperlinkPane, EMAIL);
            emailPane.populateBean((EmailJavaScript)javascript);
        } else
        if(javascript instanceof CustomActionJavaScript)
        {
            styleBox.setSelectedItem(CUSTOMACTION);
            card.show(hyperlinkPane, CUSTOMACTION);
            customActionPane.populateBean((CustomActionJavaScript)javascript);
        }
    }

    public Listener updateBean()
    {
        listener.setEventName(nameText.getText());
        if(ComparatorUtils.equals(styleBox.getSelectedItem(), JS))
            listener.setAction(javaScriptPane.updateBean());
        else
        if(ComparatorUtils.equals(styleBox.getSelectedItem(), FORMSUBMIT))
            listener.setAction(formSubmitScriptPane.updateBean());
        else
        if(ComparatorUtils.equals(styleBox.getSelectedItem(), DBCOMMIT))
            listener.setAction(commit2DBJavaScriptPane.updateBean());
        else
        if(ComparatorUtils.equals(styleBox.getSelectedItem(), EMAIL))
            listener.setAction(emailPane.updateBean());
        else
        if(ComparatorUtils.equals(styleBox.getSelectedItem(), CUSTOMACTION))
            listener.setAction(customActionPane.updateBean());
        return listener;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Listener)obj);
    }




}
