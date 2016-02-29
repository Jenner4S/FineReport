package com.fr.design.style.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

/**
 * 颜色选择器更多颜色面板
 * @author focus
 *
 */
public class ColorSelectDetailPane extends BasicPane{
	private static final int  SELECT_PANEL_HEIGHT = 245;
	// Selected color
	private Color color;
	
	// 颜色选择器面板
	JColorChooser selectedPanel;
	
	// 最近使用颜色面板
	JPanel recentUsePanel;
	
	// 预览
	JPanel previewPanel;
	
	public JColorChooser getSelectedPanel() {
		return selectedPanel;
	}

	public void setSelectedPanel(JColorChooser selectedPanel) {
		this.selectedPanel = selectedPanel;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ColorSelectDetailPane() {
		super();
	}
	
	public ColorSelectDetailPane(Color color){
		if(color == null){
			color = Color.white;
		}
		this.color = color;
		initComponents();
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}
	
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		// 颜色选择器面板
		selectedPanel = new JColorChooser(this.color);
		selectedPanel.setPreferredSize(new Dimension(selectedPanel.getWidth(),SELECT_PANEL_HEIGHT));
		AbstractColorChooserPanel[] choosers = selectedPanel.getChooserPanels();
		for(int i=0;i<choosers.length;i++){
			selectedPanel.removeChooserPanel(choosers[i]);
		}
		selectedPanel.setPreviewPanel(new JPanel());
		
		AbstractColorChooserPanel swatchChooserPanel = new SwatchChooserPanel();
		AbstractColorChooserPanel customChooserPanel = new CustomChooserPanel();
		selectedPanel.addChooserPanel(swatchChooserPanel);
		selectedPanel.addChooserPanel(customChooserPanel);
		this.add(selectedPanel, BorderLayout.NORTH);
		
		// 最近使用面板
		recentUsePanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Used"));
		RecentUseColorPane recent = new RecentUseColorPane(selectedPanel);
		recentUsePanel.add(recent);
		
		this.add(recentUsePanel,BorderLayout.CENTER);
		
		selectedPanel.setPreviewPanel(new JPanel());

		// 预览
		previewPanel = FRGUIPaneFactory.createTitledBorderPaneCenter(Inter.getLocText("FR-Designer_Preview"));
		final ColorChooserPreview colorChooserPreview = new ColorChooserPreview();
		ColorSelectionModel model = selectedPanel.getSelectionModel();
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ColorSelectionModel model = (ColorSelectionModel) e.getSource();
				colorChooserPreview.setMyColor(model.getSelectedColor());
				colorChooserPreview.paint(colorChooserPreview.getGraphics());
			}
		});
		previewPanel.add(colorChooserPreview);
		this.add(previewPanel, BorderLayout.SOUTH);
	}
}