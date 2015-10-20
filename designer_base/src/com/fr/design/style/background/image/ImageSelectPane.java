package com.fr.design.style.background.image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fr.base.BaseUtils;
import com.fr.base.background.ImageBackground;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreGraphHelper;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.background.BackgroundPane4BoxChange;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-11-1 ����03:50:02
 * ��˵�� : ����ͼƬ����ѡ����� , UIComboBox�л��ķ�֧pane. bugԭ��ͼ@5471
 */
public class ImageSelectPane extends BackgroundPane4BoxChange {
	private static final long serialVersionUID = -3938766570998917557L;
	private static String layoutCenter = Inter.getLocText("Default");
	private static String layoutTitled = Inter.getLocText("Image-Titled");
	//��Ʒ���˵��ֻ��Ҫ���У�Ĭ�ϵģ���ƽ�����ַ�ʽ
	private static final String[] layoutTypes = {
		layoutCenter,
		layoutTitled,
	};

    private UIComboBox layoutComboBox;
    
    private ImageFileChooser imageFileChooser = null;
    private UILabel imageSizeLabel = new UILabel();
    
    private AlphaPane alphaPane;
    
    private transient Image selectImage;

    public ImageSelectPane() {
        this.setLayout(new BorderLayout());
        
        JPanel pane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
        this.add(pane, BorderLayout.CENTER);
        
        JPanel testPane1 = new JPanel();
        testPane1.setLayout(new BorderLayout());
        
        JPanel selectFilePane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        testPane1.add(selectFilePane, BorderLayout.CENTER);
        pane.add(testPane1);
        
        // ѡ��ͼƬ��ť
        UIButton selectPictureButton = new UIButton(Inter.getLocText("Image-Select_Picture"));
        selectFilePane.add(selectPictureButton);
        
        selectPictureButton.setPreferredSize(new Dimension(110, 20));
        
        imageFileChooser = new ImageFileChooser();
        imageFileChooser.setMultiSelectionEnabled(false);
        selectPictureButton.setMnemonic('S');
        selectPictureButton.addActionListener(selectPictureActionListener);
        
        //����
        selectFilePane.add(new UILabel(Inter.getLocText("Form-Layout") + ":"));
        layoutComboBox = new UIComboBox(layoutTypes);
        selectFilePane.add(layoutComboBox);
        
        layoutComboBox.setPreferredSize(new Dimension(60, 20));
        
        selectFilePane.add(alphaPane = new AlphaPane());
        // image size label.
        
        JPanel testPane = new JPanel();
        testPane.setLayout(new BorderLayout());
        JPanel southImagePane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        testPane.add(southImagePane, BorderLayout.CENTER);
        
        pane.add(testPane);

        imageSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southImagePane.add(imageSizeLabel);
        imageSizeLabel.setPreferredSize(new Dimension(100, 20));
    }
    
    ActionListener selectPictureActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = imageFileChooser.showOpenDialog(ImageSelectPane.this);
            if (returnVal != JFileChooser.CANCEL_OPTION) {
                File selectedFile = imageFileChooser.getSelectedFile();
                if (selectedFile != null && selectedFile.isFile()) {
                    Image image = BaseUtils.readImage(selectedFile.getPath());
                    CoreGraphHelper.waitForImage(image);
                    selectImage = image;
                } 
                chechLabelText();
            }
        }
    };
    
    private void chechLabelText() {
      if (selectImage == null) {
          imageSizeLabel.setText("");
      } else {
          imageSizeLabel.setText(selectImage.getWidth(null) + "x"
              + selectImage.getHeight(null) + Inter.getLocText("px"));
      }
    }

    public void populate(Background background) {

        if (background instanceof ImageBackground) {
            ImageBackground imageBackground = (ImageBackground) background;
            selectImage = imageBackground.getImage();
            
            if (imageBackground.getLayout() == Constants.IMAGE_TILED) {
            	layoutComboBox.setSelectedItem(layoutTitled);
            } else {
            	layoutComboBox.setSelectedItem(layoutCenter);
            }
            
        }
        
        chechLabelText();
    }
    
    public void populateAlpha(int alpha) {
    	alphaPane.populate(alpha);
    }
    
    public float updateAlpha() {
    	return alphaPane.update();
    }

    public Background update() {
    	ImageBackground imageBackground = new ImageBackground(selectImage);
    	
    	Object selectLayout = layoutComboBox.getSelectedItem();
    	if(selectLayout.equals(layoutCenter)) {
    		imageBackground.setLayout(Constants.IMAGE_CENTER);
    	} else if(selectLayout.equals(layoutTitled)) {
    		imageBackground.setLayout(Constants.IMAGE_TILED);
    	} 
    	
    	return imageBackground;
    }
    
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Image-Select_Picture");
	}
}
