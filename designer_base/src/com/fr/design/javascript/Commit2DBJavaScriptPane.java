package com.fr.design.javascript;

import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.CommitTabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.js.Commit2DBJavaScript;
import com.fr.write.DBManipulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Commit2DBJavaScriptPane extends FurtherBasicBeanPane<Commit2DBJavaScript> {
    private List dbmPaneList = new ArrayList();
    private CommitTabbedPane commitTabbedPane;
    private JavaScriptActionPane javaScriptActionPane;
    private UIButton addCallbackButton;

    private JPanel cardPane;
    private String[] cardNames;

    /**
     * ���캯�����ؼ��¼����ύ������
     * @param javaScriptActionPane JS�ύ������
     * @param dbManipulationPaneList �ύ�����ύ����б�
     */
    public Commit2DBJavaScriptPane(final JavaScriptActionPane javaScriptActionPane, List dbManipulationPaneList) {
        this.dbmPaneList=dbManipulationPaneList;
        this.javaScriptActionPane = javaScriptActionPane;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        commitTabbedPane = new CommitTabbedPane(this,dbmPaneList);
        commitTabbedPane.setPreferredSize(new Dimension(commitTabbedPane.getWidth(),20));
        this.add(commitTabbedPane, BorderLayout.NORTH) ;

        cardPane = new JPanel(new CardLayout());
        cardNames = new String[dbmPaneList.size()] ;
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if(((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName() == null){
                cardNames[i] = "";
            } else{
                cardNames[i] =((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((DBManipulationPane)this.dbmPaneList.get(i),cardNames[i]);
        }
        this.add(cardPane, BorderLayout.CENTER);

        JPanel btPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		this.add(btPane, BorderLayout.SOUTH);

		addCallbackButton = javaScriptActionPane.createCallButton();
		btPane.add(addCallbackButton);
	}


    /**
     * ����DBManipulationPane
     */
    public void updateCardPane(){
        cardNames = new String[dbmPaneList.size()] ;
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if(((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName() == null){
                cardNames[i] = "";
            } else{
                cardNames[i] =((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((DBManipulationPane)this.dbmPaneList.get(i),cardNames[i]);
        }
        CardLayout cardLayout = (CardLayout)cardPane.getLayout();
        cardLayout.show(cardPane,cardNames[commitTabbedPane.getSelectedIndex()]);
    }

    public void setList(List list){
        this.dbmPaneList = list;
    }

    /**
     * �½�DBManipulationPane
     * @return    �½���DBManipulationPane
     */
    public DBManipulationPane createDBManipulationPane(){
        DBManipulationPane db = javaScriptActionPane.createDBManipulationPane();
        db.populateBean(null);
        dbmPaneList.add(db);
        return db;

    }

    /**
     * ��������
     * @return ���ش�������
     */
	public String title4PopupWindow() {
		return Inter.getLocText("JavaScript-Commit_to_Database");
	}

	/**
	 * ��������
	 */
	public void reset() {
		this.javaScriptActionPane.setCall(null);
		//���ú�ֻ������ֻ����һ��tab
        while (dbmPaneList.size() > 1){
            dbmPaneList.remove(1);
        }
        ((DBManipulationPane)dbmPaneList.get(0)).populateBean(null);
	}

	@Override
    /**
     * ��JavaBean�ڵ����������������
     */
	public void populateBean(Commit2DBJavaScript commit2db) {
		if (commit2db == null) {
			reset();
			return;
		}
        //�Ȱ�ԭ����list�����Ȼ���ٸ��ݴ����������add
        dbmPaneList.clear();
		this.javaScriptActionPane.setCall(commit2db.getCallBack());
        for(int i = 0;i < commit2db.getDBManipulation().size();i++){
            DBManipulationPane dbmp = javaScriptActionPane.createDBManipulationPane();
            dbmp.populateBean((DBManipulation)commit2db.getDBManipulation().get(i));
            dbmPaneList.add(dbmp);
        }
        commitTabbedPane.refreshTab();
    }

    /**
     * �������ݲ�JavaBean
     * @return ����JavaBean
     */
	public Commit2DBJavaScript updateBean() {
		Commit2DBJavaScript commit2dbJavaScript = new Commit2DBJavaScript();

        List dbmaniList = new ArrayList();
       for(int i = 0; i < this.dbmPaneList.size(); i++){
           DBManipulationPane dbmpane =(DBManipulationPane)this.dbmPaneList.get(i);
            if(i > dbmPaneList.size()-1){
                dbmPaneList.add(dbmpane);
            }
            DBManipulation dbManipulation = dbmpane.updateBean();
            dbmaniList.add(dbManipulation);
        }
		commit2dbJavaScript.setDBManipulation(dbmaniList);

		commit2dbJavaScript.setCallBack(this.javaScriptActionPane.getCall());

		return commit2dbJavaScript;
	}

    /**
     * �ж��Ƿ����ܽ��ܵ���������
     * @param ob ����
     * @return �����Ƿ����ܽ��ܵ���������
     */
	public boolean accept(Object ob) {
		return ob instanceof Commit2DBJavaScript;
	}

}
