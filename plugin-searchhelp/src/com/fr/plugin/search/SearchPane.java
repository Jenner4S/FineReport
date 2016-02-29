package com.fr.plugin.search;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.DesignerContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class SearchPane extends BasicPane {

    private PlaceholderTextField textField;
    private UIButtonGroup<String> buttonGroup;
    private Popup popup;
    private SearchShowPane showPane;

    private boolean closed;
    private long initTime = 0;

    @Override
    protected String title4PopupWindow() {
        return "Search";
    }

    public SearchPane() {
        setLayout(new BorderLayout(0, 4));
        textField = new PlaceholderTextField();
        textField.setPlaceholder("按Enter键在论坛和文档进行查询");
        add(textField, BorderLayout.CENTER);

        buttonGroup = new UIButtonGroup<String>(new String[]{"文档", "论坛"}, new String[]{
                "doc",
                "bbs"
        });
        add(buttonGroup, BorderLayout.WEST);
        buttonGroup.setSelectedItem("bbs");

        createPopupComponent();

        popup = createPopup();

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doChange();
                }
            }
        });
    }

    private void doChange() {
        if (showPane.isLoad()) {
            if (closed) {
                popup.show();
            }
            showPane.load(switchToBaseUrl(buttonGroup.getSelectedItem()), textField.getText());
        } else {
            popup.show();
            showPane.create(switchToBaseUrl(buttonGroup.getSelectedItem()), textField.getText());
        }
    }

    private void createPopupComponent() {
        showPane = new SearchShowPane();
        showPane.setPreferredSize(new Dimension(SearchConstants.WIDTH, SearchConstants.HEIGHT));
        showPane.setCloseListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.hide();
                closed = true;
                popup = createPopup();
            }
        });
        popup = createPopup();
    }

    private Popup createPopup() {
        return PopupFactory.getSharedInstance().getPopup(DesignerContext.getDesignerFrame(), showPane, Toolkit.getDefaultToolkit().getScreenSize().width - SearchConstants.WIDTH - SearchConstants.FIX_WIDTH, SearchConstants.FIX_GAP);
    }

    private String switchToBaseUrl(String type) {
        if ("bbs".equals(type)) {
            return "http://search.finereport.com/cse/search?s=6463061415402122757&entry=1&ie=gbk&q=";
        } else {
            return "http://zhannei.baidu.com/cse/search?s=8007358476173623455&inurl=www.finereporthelp.com%2Fhelp&q=";
        }
    }

}