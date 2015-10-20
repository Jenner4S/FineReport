package com.fr.design.gui.ibutton;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUICoreUtils;

public class UIHeadGroup extends JPanel{
	protected List<UIToggleButton> labelButtonList;
	private boolean isNeedLeftRightOutLine = true;
	protected int selectedIndex = -1;
	
	protected void tabChanged(int newSelectedIndex) {
		
	}
	
	public UIHeadGroup(String[] textArray) {
		labelButtonList = new ArrayList<UIToggleButton>(textArray.length);
		this.setBackground(UIConstants.NORMAL_BACKGROUND);
		this.setLayout(new GridLayout(0, textArray.length, 1, 0));
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		for(int i = 0; i < textArray.length; i++) {
			final int index = i;
			String text = textArray[i];
			final UIToggleButton labelButton = new UIToggleButton(text) {
				@Override
				protected MouseListener getMouseListener() {
					return new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							setSelectedIndex(index);
							UIHeadGroup.this.repaint();
						}
					};
				}
			};
			initButton(labelButton);
		}
		setSelectedIndex(0);
	}

	public UIHeadGroup(Icon[] iconArray) {
		labelButtonList = new ArrayList<UIToggleButton>(iconArray.length);
		this.setBackground(UIConstants.NORMAL_BACKGROUND);
		this.setLayout(new GridLayout(0, iconArray.length, 1, 0));
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		for(int i = 0; i < iconArray.length; i++) {
			final int index = i;
			Icon icon = iconArray[i];
			final UIToggleButton labelButton = new UIToggleButton(icon) {
				@Override
				protected MouseListener getMouseListener() {
					return new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							setSelectedIndex(index);
							UIHeadGroup.this.repaint();
						}
					};
				}
			};
			initButton(labelButton);
		}
		setSelectedIndex(0);
	}

    public UIHeadGroup(Icon[] iconArray, String[] textArray) {
        labelButtonList = new ArrayList<UIToggleButton>(Math.min(textArray.length, iconArray.length));
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        this.setLayout(new GridLayout(0, textArray.length, 1, 0));
        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        for(int i = 0; i < textArray.length; i++) {
            final int index = i;
            String text = textArray[i];
            Icon icon = iconArray[i];
            final UIToggleButton labelButton = new UIToggleButton(text, icon) {
                @Override
                protected MouseListener getMouseListener() {
                    return new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            setSelectedIndex(index);
                            UIHeadGroup.this.repaint();
                        }
                    };
                }
            };
            initButton(labelButton);
        }
        setSelectedIndex(0);
    }

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.height = 25;
		return dim;
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(UIConstants.LINE_COLOR);

		int width = 0;
		for(int i = 0; i < labelButtonList.size() - 1; i++) {
			int height = labelButtonList.get(i).getHeight();
			width += labelButtonList.get(i).getWidth() + 1;
			g.drawLine(width, 0, width, height);
		}

		width += labelButtonList.get(labelButtonList.size() - 1).getWidth() + 1;
		if(isNeedLeftRightOutLine) {
			g2d.drawRect(0, 0, width, getHeight() - 1);
		} else {
			g2d.drawLine(1, 0, width - 1, 0);
			g2d.drawLine(1, getHeight() - 1, width - 1, getHeight() - 1);
		}
		
		
		g2d.setColor(UIConstants.NORMAL_BACKGROUND);
		UIToggleButton headButton = labelButtonList.get(selectedIndex);
		g2d.drawLine(headButton.getX(), headButton.getHeight() + 1, headButton.getX() + headButton.getWidth() - 1, headButton.getHeight() + 1);
	}
	
	private void initButton(UIToggleButton labelButton) {
		labelButton.setRoundBorder(false);
		labelButton.setBorderPainted(false);
		labelButton.setPressedPainted(false);
		labelButtonList.add(labelButton);
		this.add(labelButton);
	}
	
	public void setSelectedIndex(int newSelectedIndex) {
		selectedIndex = newSelectedIndex;
		for (int i = 0; i < labelButtonList.size(); i++) {
			UIToggleButton button = labelButtonList.get(i);
			if(i == selectedIndex) {
				button.setNormalPainted(false);
				button.setOpaque(true);
				button.setBackground(null);
				button.setSelected(true);
			} else {
				button.setOpaque(false);
				button.setNormalPainted(true);
				button.setSelected(false);
			}
		}
		
		tabChanged(newSelectedIndex);
	}
	
	public void setNeedLeftRightOutLine(boolean isNeedLeftRightOutLine) {
		this.isNeedLeftRightOutLine = isNeedLeftRightOutLine;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel)jf.getContentPane();
		content.setLayout(null);
		Icon[] a1 = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
		UIHeadGroup bb = new UIHeadGroup(a1);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		bb.setSelectedIndex(0);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}
