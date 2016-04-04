// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            BBSConstants

public class BBSDialog extends UIDialog
{

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int OUTER_WIDTH = 605;
    private static final int OUTER_HEIGHT = 428;
    private JFXPanel jfxPanel;

    public BBSDialog(Frame frame)
    {
        super(frame);
        JPanel jpanel = (JPanel)getContentPane();
        initComponents(jpanel);
        setSize(new Dimension(605, 428));
    }

    private void initComponents(JPanel jpanel)
    {
        jpanel.setLayout(new BorderLayout());
        jfxPanel = new JFXPanel();
        add(jfxPanel, "Center");
    }

    private void disableLink(final WebEngine eng)
    {
        try
        {
            Platform.runLater(new Runnable() {

                final WebEngine val$eng;
                final BBSDialog this$0;

                public void run()
                {
                    eng.executeScript("history.go(0)");
                }

            
            {
                this$0 = BBSDialog.this;
                eng = webengine;
                super();
            }
            }
);
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
    }

    public void showWindow(final String url)
    {
        GUICoreUtils.centerWindow(this);
        setResizable(false);
        Platform.runLater(new Runnable() {

            final String val$url;
            final BBSDialog this$0;

            public void run()
            {
                Group group = new Group();
                Scene scene = new Scene(group, 600D, 400D);
                jfxPanel.setScene(scene);
                Double double1 = Double.valueOf((new Integer(600)).doubleValue());
                Double double2 = Double.valueOf((new Integer(400)).doubleValue());
                WebView webview = new WebView();
                webview.setMinSize(double1.doubleValue(), double2.doubleValue());
                webview.setPrefSize(double1.doubleValue(), double2.doubleValue());
                final WebEngine eng = webview.getEngine();
                eng.load(url);
                group.getChildren().add(webview);
                eng.locationProperty().addListener(new ChangeListener() {

                    final WebEngine val$eng;
                    final _cls2 this$1;

                    public void changed(ObservableValue observablevalue, String s, String s1)
                    {
                        disableLink(eng);
                        if(ComparatorUtils.equals(s1, url) || ComparatorUtils.equals(s1, BBSConstants.BBS_MOBILE_MOD))
                        {
                            return;
                        } else
                        {
                            openUrlAtLocalWebBrowser(eng, s1);
                            return;
                        }
                    }

                    public volatile void changed(ObservableValue observablevalue, Object obj, Object obj1)
                    {
                        changed(observablevalue, (String)obj, (String)obj1);
                    }

                    
                    {
                        this$1 = _cls2.this;
                        eng = webengine;
                        super();
                    }
                }
);
                eng.getLoadWorker().stateProperty().addListener(new ChangeListener() {

                    final _cls2 this$1;

                    public void changed(ObservableValue observablevalue, javafx.concurrent.Worker.State state, javafx.concurrent.Worker.State state1)
                    {
                        if(state1 == javafx.concurrent.Worker.State.SUCCEEDED)
                            setVisible(true);
                    }

                    public volatile void changed(ObservableValue observablevalue, Object obj, Object obj1)
                    {
                        changed(observablevalue, (javafx.concurrent.Worker.State)obj, (javafx.concurrent.Worker.State)obj1);
                    }

                    
                    {
                        this$1 = _cls2.this;
                        super();
                    }
                }
);
            }

            
            {
                this$0 = BBSDialog.this;
                url = s;
                super();
            }
        }
);
    }

    private void openUrlAtLocalWebBrowser(WebEngine webengine, String s)
    {
        if(Desktop.isDesktopSupported())
            try
            {
                URI uri = URI.create(s);
                Desktop desktop = Desktop.getDesktop();
                if(desktop.isSupported(java.awt.Desktop.Action.BROWSE))
                    desktop.browse(uri);
            }
            catch(NullPointerException nullpointerexception)
            {
                FRLogger.getLogger().error(nullpointerexception.getMessage());
            }
            catch(IOException ioexception)
            {
                FRLogger.getLogger().error(ioexception.getMessage());
            }
    }

    public void checkValid()
        throws Exception
    {
    }



}
