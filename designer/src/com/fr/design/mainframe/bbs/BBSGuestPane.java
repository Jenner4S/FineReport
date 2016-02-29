/**
 * 
 */
package com.fr.design.mainframe.bbs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JPanel;

import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.start.BBSGuestPaneProvider;

/**
 * @author neil
 *
 * @date: 2015-3-13-下午12:54:45
 */
public class BBSGuestPane extends JPanel implements BBSGuestPaneProvider{

	/**
	 * 构造函数
	 */
	public BBSGuestPane() {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		initTableContent();
	}
	
	private void initTableContent(){
		JPanel guestPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		//感谢标签
		JPanel infoPane = initInfoPane();
		guestPane.add(infoPane, BorderLayout.NORTH);
		//用户名+超链
		JPanel userPane = initUserPane();
		guestPane.add(userPane, BorderLayout.CENTER);
		
		this.add(guestPane);
	}
	
	private JPanel initUserPane(){
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p, p, p, p, p};
		double[] colSize = {p};
		
		Component[][] components = new Component[rowSize.length][colSize.length];
		for (int i = 0; i < components.length; i++) {
			String userName = BBSConstants.ALL_GUEST[i];
			String url = BBSConstants.ALL_LINK[i];
			components[i][0] = getURLActionLabel(userName, url);
		}
		
		return TableLayoutHelper.createTableLayoutPane(components, rowSize, colSize);
	}
	
	private JPanel initInfoPane(){
		JPanel infoPane = FRGUIPaneFactory.createBorderLayout_S_Pane(); 
		UILabel infoNorthLabel = new UILabel(Inter.getLocText("FR-Designer_Thank_guest"));
		UILabel centerLabel = new UILabel(StringUtils.BLANK);
		infoPane.add(infoNorthLabel, BorderLayout.NORTH);
		infoPane.add(centerLabel, BorderLayout.CENTER);
		
		return infoPane;
	}
	
    private ActionLabel getURLActionLabel(final String text, final String url){
    	ActionLabel actionLabel = new ActionLabel(text);
        actionLabel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception exp) {

                }
            }
        });
        
        return actionLabel;
    }
}