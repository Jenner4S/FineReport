package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * @author richie
 * @date 2015-04-02
 * @since 8.0
 */
public class BBSDialog extends UIDialog {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int OUTER_WIDTH = 605;
    private static final int OUTER_HEIGHT = 428;


    private JFXPanel jfxPanel;


    public BBSDialog(Frame parent) {
        super(parent);
        //setUndecorated(true);
        JPanel panel = (JPanel) getContentPane();
        initComponents(panel);
        setSize(new Dimension(OUTER_WIDTH, OUTER_HEIGHT));
    }

    private void initComponents(JPanel contentPane) {
        contentPane.setLayout(new BorderLayout());
        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
    }

    /**
     * ����Ѷ��
     * @param url ��Ѷ����
     */
    public void showWindow(final String url){
        GUICoreUtils.centerWindow(this);
        this.setResizable(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group root = new Group();
                Scene scene = new Scene(root, WIDTH, HEIGHT);
                jfxPanel.setScene(scene);
                Double widthDouble = new Integer(WIDTH).doubleValue();
                Double heightDouble = new Integer(HEIGHT).doubleValue();

                WebView view = new WebView();
                view.setMinSize(widthDouble, heightDouble);
                view.setPrefSize(widthDouble, heightDouble);
                final WebEngine eng = view.getEngine();
                //webEngine��userAgentò��֧���ƶ��豸�ģ��κ������������userAngent���ᵼ�³������
                //eng.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) Apple/WebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36");
                eng.load(url);
                root.getChildren().add(view);
                eng.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                    {
                    	try{
                    		// webView�˲���ת
                    		// ��ȻwebView����ָ�������������ĳ�����ӣ����ǵ������������ת��ָ�����ӵ�ͬʱ��webViewҲ������ת��
                    		// Ϊ�˱��������һ��600*400����Ѷ�������������ҳ�������webView����ת������ҳ
                    		eng.executeScript("history.back()");
                    	}catch(Exception e){
                    		// ֻ��׽��������
                    	}
                    	// webView����Ĭ�����ֻ�����ʾ��ҳ�����������˵������ת
                		if(ComparatorUtils.equals(newValue, url) || ComparatorUtils.equals(newValue, BBSConstants.BBS_MOBILE_MOD)){
                			return;
                		}
                		openUrlAtLocalWebBrowser(eng,newValue);
					}
				});
                eng.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        if (newValue == Worker.State.SUCCEEDED){
                            setVisible(true);
                        }
                    }
                });
            }
        });
    }
    
    // �ڱ�����������url
    private void openUrlAtLocalWebBrowser(WebEngine eng,String url){
        if(Desktop.isDesktopSupported()){
            try{
                //����һ��URIʵ��,ע�ⲻ��URL
                URI uri = URI.create(url);
                //��ȡ��ǰϵͳ������չ
                Desktop desktop = Desktop.getDesktop();
                //�ж�ϵͳ�����Ƿ�֧��Ҫִ�еĹ���
                if(desktop.isSupported(Desktop.Action.BROWSE)){
                    //��ȡϵͳĬ�������������
                	desktop.browse(uri);
                }
            }catch(NullPointerException e){
                //��ΪuriΪ��ʱ�׳��쳣
            	FRLogger.getLogger().error(e.getMessage());
            }catch(IOException e){
                //��Ϊ�޷���ȡϵͳĬ�������
            	FRLogger.getLogger().error(e.getMessage());
            }
        }
    }

    /**
     * ��
     */
    @Override
    public void checkValid() throws Exception {

    }
}
