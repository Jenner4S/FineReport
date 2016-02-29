package com.fr.plugin.search;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.function.EXACT;
import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

public class SearchShowPane extends BasicPane {

    private JFXPanel jfxPanel;
    private WebEngine webEngine;
    private boolean load = false;
    private ActionListener listener;

    public SearchShowPane() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 30));

        add(panel, BorderLayout.NORTH);

        final UILabel button = new UILabel(IOUtils.readIcon("com/fr/plugin/search/images/close.png"));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.actionPerformed(null);
                }
            }
        });

        button.setPreferredSize(new Dimension(30, 30));
        panel.add(button, BorderLayout.WEST);


        jfxPanel = new JFXPanel();
        jfxPanel.setBorder(BorderFactory.createBevelBorder(2));
        add(jfxPanel, BorderLayout.CENTER);

        setBorder(BorderFactory.createDashedBorder(Color.GRAY));
    }

    public void setCloseListener(ActionListener listener) {
        this.listener = listener;
    }

    public void load(final String baseUrl, final String key) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webEngine.load(createSearchUrl(baseUrl, key));
            }
        });
    }

    public boolean isLoad() {
        return load;
    }

    public void create(final String baseUrl, final String key) {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group root = new Group();
                Scene scene = new Scene(root, SearchConstants.WIDTH, SearchConstants.HEIGHT);
                jfxPanel.setScene(scene);
                Double widthDouble = new Integer(SearchConstants.WIDTH).doubleValue();
                Double heightDouble = new Integer(SearchConstants.HEIGHT).doubleValue();
                WebView view = new WebView();
                view.setFontSmoothingType(FontSmoothingType.LCD);
                view.setMinSize(widthDouble, heightDouble);
                view.setPrefSize(widthDouble, heightDouble);
                webEngine = view.getEngine();
                try {
                    Method method = webEngine.getClass().getDeclaredMethod("setUserAgent", String.class);
                    method.invoke(webEngine, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36");
                } catch (Exception e) {
                   FRLogger.getLogger().error(e.getMessage());
                }
                webEngine.load(createSearchUrl(baseUrl, key));
                root.getChildren().add(view);
            }
        });
        load = true;
    }

    private String createSearchUrl(String baseUrl, String key) {
        String a = null;
        try {
            a = URLEncoder.encode(key, "gbk");
        } catch (UnsupportedEncodingException e1) {
            FRLogger.getLogger().error(e1.getMessage(), e1);
        }
        return baseUrl + a;
    }

    @Override
    protected String title4PopupWindow() {
        return "Search";
    }
}