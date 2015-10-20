/**
 * 
 */
package com.fr.design.report.share;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.stable.*;
import com.fr.stable.project.ProjectConstants;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;

/**
 * @author neil
 *
 * @date: 2015-3-9-����3:14:56
 */
public class ShareButton extends UIButton{

	private static final int SHARE_COUNTS = 4;
	private static final String SHARE_KEY = "share";
	
	/**
	 * ���캯��
	 */
	public ShareButton() {
    	this.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/share.png"));
    	this.setToolTipText(Inter.getLocText("FR-Designer_Share-Template"));
    	this.set4ToolbarButton();
    	this.addActionListener(shareListener);
	}
	
	//����̳, url����bbs.properties������
	private void openBBS(){
		try {
			Desktop.getDesktop().browse(new URI(BBSConstants.SHARE_URL));
		} catch (Exception e1) {
			FRContext.getLogger().error(e1.getMessage());
		}
	}
	
	//����ͼƬ��ָ���ļ���
	private String exportAsImage(){
		//Ҫ�����������ݼ���ż��㱨��, ����ͼƬ, ��Ϊ���������ú���Ҫ�ֶ���������
		JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate(); 

		TemplateWorkBook workbook = (TemplateWorkBook) jt.getTarget();
		//������ʱ���Ѿ������������һ��������, �ٵ�����, ��������Դ�ǱߵĲ����Ѿ�����, �Ͳ�������ȡ������
		ResultWorkBook res = workbook.execute(new HashMap<String, Object>(), ActorFactory.getActor(ActorConstants.TYPE_PAGE));
		ImageExporter exporter = new ImageExporter();
		File imageFile = new File(getImagePath(jt));
		try {
			exporter.export(new FileOutputStream(imageFile), res);
		} catch (Exception e2) {
			FRContext.getLogger().error(e2.getMessage());
		}
		
		return imageFile.getParent();
	}
	
	//��ȡĬ�ϵ���ͼƬλ��
	private String getImagePath(JTemplate<?, ?> jt){
		FILE file = jt.getEditingFILE();
		String envPath = FRContext.getCurrentEnv().getPath();
		String folderPath = file.getParent().getPath();
		String imageName = file.getName().replaceAll(ProjectConstants.CPT_SUFFIX, StringUtils.EMPTY) + ".png";
		
		return StableUtils.pathJoin(envPath, folderPath, imageName);
	}
	
	private ActionListener shareListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			UIPopupMenu menu = new UIPopupMenu();
			boolean isSharable = isSharable();
			
			//�����ǰ�༭ģ�����ת���������ݼ���shareģ���Ͻ��л�������.
			UIMenuItem directShare = new UIMenuItem(Inter.getLocText("FR-Designer_Share-Template"));
			directShare.setEnabled(!isSharable);
			directShare.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/directShare.png"));
            if (directShare.isEnabled()) {
                directShare.addMouseListener(directShareListener);
            }
			
			//Ĭ�ϱ������ȵ������ģ��, Ȼ����ת�������������ݼ���ģ����, �ſ��Ե�����޸Ĳ�����
			UIMenuItem modifyShare = new UIMenuItem(Inter.getLocText("FR-Designer_Finish-Modify-Share"));
			modifyShare.setEnabled(isSharable);
			modifyShare.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/modifyShare.png"));
			if (modifyShare.isEnabled()) {
                modifyShare.addMouseListener(modifyShareListener);
            }
			menu.add(directShare);
			menu.add(modifyShare);
			
			GUICoreUtils.showPopupMenu(menu, ShareButton.this, 0, 20);
		}
		
		private boolean isSharable(){
			JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			FILE file = jt.getEditingFILE();
			String fileName = file.getEnvFullName();
			
			//eg, share/WorkBook2011_share/WorkBook2011_share.cpt
			return ArrayUtils.getLength(fileName.split(SHARE_KEY)) >= SHARE_COUNTS;
		}
	};
	
	//����޸ĺ�������
	private MouseListener modifyShareListener = new MouseAdapter() {
		
		public void mousePressed(MouseEvent e) {
			//��������ͼ
			exportAsImage();
			//����̳
			openBBS();
		};
		
	};
	
	//����������
	private MouseListener directShareListener = new MouseAdapter() {
		
		public void mousePressed(MouseEvent e) {
			JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			jt.stopEditing();
			jt.saveShareFile();
			
	        final ConfusionManagerPane managerPane = new ConfusionManagerPane();
	        boolean hasEmb = managerPane.populateTabledataManager();
	        if(!hasEmb){
	        	//���û���������ݼ�, �Ͳ�չʾ�����Ŀ������.
	        	return;
	        }
	        
	        BasicDialog managerDialog = managerPane.showMediumWindow(DesignerContext.getDesignerFrame(), 
	        		new DialogActionAdapter() {
	        	
	        		public void doOk() {
	        			//��ȷ��ʱ, ��ʼ���������������ý�workbook
	        			updateManagerDialog(managerPane);
	        		};
	        });
	        managerDialog.setModal(false);
	        managerDialog.setVisible(true);
		};
		
	};
	
	/**
	 * ����������õĻ���Ӧ�õ�workbook�����ݼ���
	 * 
	 * @param managerPane ��ǰ������������
	 * 
	 */
    public void updateManagerDialog(ConfusionManagerPane managerPane) {
    	Nameable[] confusionArray = managerPane.update();
    	for (int i = 0, length = confusionArray.length; i < length; i++) {
    		//������tabledata����
			String name = confusionArray[i].getName();
			//�����������Ϣ
			ConfusionInfo info = (ConfusionInfo) ((NameObject)confusionArray[i]).getObject();
			JTemplate<?, ?> template = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
			TableDataSource workbook = template.getTarget();
			EmbeddedTableData tabledata = (EmbeddedTableData) workbook.getTableData(name);
			//�������ݼ��������
			new ConfuseTabledataAction().confuse(info, tabledata);
    	}
    }
    
}
