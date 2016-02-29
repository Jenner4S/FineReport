package com.fr.design.mainframe.backgroundpane;

import com.fr.base.GraphHelper;
import com.fr.base.background.TextureBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.event.UIObserverListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class TextureBackgroundPane extends BackgroundSettingPane {

	private TexturePaint texturePaint;
	private TextureButton[] textureButtonArray;

	public TextureBackgroundPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		borderPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5));
		JPanel contentPane = new JPanel();
		borderPane.add(contentPane, BorderLayout.NORTH);
		this.add(borderPane, BorderLayout.NORTH);
		contentPane.setLayout(new GridLayout(0, 8, 1, 1));
		contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		borderPane.setPreferredSize(new Dimension(0, 145));
		ButtonGroup patternButtonGroup = new ButtonGroup();
		textureButtonArray = new TextureButton[EMBED_TEXTURE_PAINT_ARRAY.length];
		for (int i = 0; i < EMBED_TEXTURE_PAINT_ARRAY.length; i++) {
			textureButtonArray[i] = new TextureButton(EMBED_TEXTURE_PAINT_ARRAY[i], EMBED_TEXTURE_PAINT_DES_ARRAY[i]);
			patternButtonGroup.add(textureButtonArray[i]);
			contentPane.add(textureButtonArray[i]);
		}
	}

	public void populateBean(Background background) {
		TextureBackground textureBackground = (TextureBackground) background;

		this.texturePaint = textureBackground.getTexturePaint();


		for (int i = 0; i < textureButtonArray.length; i++) {
			if (ComparatorUtils.equals(textureButtonArray[i].getTexturePaint(), this.texturePaint)) {
				textureButtonArray[i].setSelected(true);
				break;
			}
		}
	}

	public Background updateBean() {
		if (this.texturePaint == null) {
			textureButtonArray[0].doClick();
		}
		return new TextureBackground(this.texturePaint);
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		for (int i = 0, count = textureButtonArray.length; i < count; i++) {
			textureButtonArray[i].addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					listener.doChange();
				}
			});
		}
	}


	/**
	 * Texture type button.
	 */
	class TextureButton extends JToggleButton implements ActionListener {

		private TexturePaint buttonTexturePaint;

		public TextureButton(TexturePaint buttonTexturePaint, String tooltip) {
			this.buttonTexturePaint = buttonTexturePaint;
			this.setToolTipText(tooltip);

			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.addActionListener(this);
			this.setBorder(null);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;

			Dimension d = getSize();

			g2d.setPaint(this.buttonTexturePaint);
			GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, d.width - 1, d.height - 1));

			if (ComparatorUtils.equals(texturePaint, this.buttonTexturePaint)) {// it's
				// selected.
				g2d.setPaint(UIConstants.LINE_COLOR);
			} else {
				g2d.setPaint(null);
			}
			GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1, d.height - 1));
		}

		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 20);
		}

		public TexturePaint getTexturePaint() {
			return this.buttonTexturePaint;
		}

		/**
		 * set Pattern index.
		 */
		public void actionPerformed(ActionEvent evt) {
			TextureBackgroundPane.this.texturePaint = this.getTexturePaint();

			fireChagneListener();
			TextureBackgroundPane.this.repaint(); // repaint.
		}

		public void addChangeListener(ChangeListener changeListener) {
			this.changeListener = changeListener;
		}

		private void fireChagneListener() {
			if (this.changeListener != null) {
				ChangeEvent evt = new ChangeEvent(this);
				this.changeListener.stateChanged(evt);
			}
		}
	}

	public static final TexturePaint[] EMBED_TEXTURE_PAINT_ARRAY = new TexturePaint[]{TextureBackground.NEWSPRINT_TEXTURE_PAINT, TextureBackground.RECYCLED_PAPER_TEXTURE_PAINT,
			TextureBackground.PARCHMENT_TEXTURE_PAINT, TextureBackground.STATIONERY_TEXTURE_PAINT, TextureBackground.GREEN_MARBLE_TEXTURE_PAINT,
			TextureBackground.WHITE_MARBLE_TEXTURE_PAINT, TextureBackground.BROWN_MARBLE_TEXTURE_PAINT, TextureBackground.GRANITE_TEXTURE_PAINT,
			TextureBackground.BLUE_TISSUE_PAPER_TEXTURE_PAINT, TextureBackground.PINK_TISSUE_PAPER_TEXTURE_PAINT, TextureBackground.PURPLE_MESH_TEXTURE_PAINT,
			TextureBackground.BOUQUET_TEXTURE_PAINT, TextureBackground.PAPYRUS_TEXTURE_PAINT, TextureBackground.CANVAS_TEXTURE_PAINT, TextureBackground.DENIM_TEXTURE_PAINT,
			TextureBackground.WOVEN_MAT_TEXTURE_PAINT, TextureBackground.WATER_DROPLETS_TEXTURE_PAINT, TextureBackground.PAPER_BAG_TEXTURE_PAINT, TextureBackground.FISH_FOSSIL_TEXTURE_PAINT,
			TextureBackground.SAND_TEXTURE_PAINT, TextureBackground.CORK_TEXTURE_PAINT, TextureBackground.WALNUT_TEXTURE_PAINT, TextureBackground.OAK_TEXTURE_PAINT,
			TextureBackground.MEDIUM_WOOD_TEXTURE_PAINT};
	private static final String[] EMBED_TEXTURE_PAINT_DES_ARRAY = new String[]{Inter.getLocText("BackgroundTexture-Newsprint"), Inter.getLocText("BackgroundTexture-RecycledPaper"),
			Inter.getLocText("BackgroundTexture-Parchment"), Inter.getLocText("BackgroundTexture-Stationery"), Inter.getLocText("BackgroundTexture-GreenMarble"),
			Inter.getLocText("BackgroundTexture-WhiteMarble"), Inter.getLocText("BackgroundTexture-BrownMarble"), Inter.getLocText("BackgroundTexture-Granite"),
			Inter.getLocText("BackgroundTexture-BlueTissuePaper"), Inter.getLocText("BackgroundTexture-PinkTissuePaper"), Inter.getLocText("BackgroundTexture-PurpleMesh"),
			Inter.getLocText("BackgroundTexture-Bouquet"), Inter.getLocText("BackgroundTexture-Papyrus"), Inter.getLocText("BackgroundTexture-Canvas"),
			Inter.getLocText("BackgroundTexture-Denim"), Inter.getLocText("BackgroundTexture-WovenMat"), Inter.getLocText("BackgroundTexture-WaterDroplets"),
			Inter.getLocText("BackgroundTexture-PaperBag"), Inter.getLocText("BackgroundTexture-FishFossil"), Inter.getLocText("BackgroundTexture-Sand"),
			Inter.getLocText("BackgroundTexture-Cork"), Inter.getLocText("BackgroundTexture-Walnut"), Inter.getLocText("BackgroundTexture-Oak"),
			Inter.getLocText("BackgroundTexture-MediumWood")};

	@Override
	public boolean accept(Background background) {
		return background instanceof TextureBackground;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Background-Texture");
	}
}