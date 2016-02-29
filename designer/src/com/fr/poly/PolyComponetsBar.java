package com.fr.poly;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartGetter;
import com.fr.base.chart.BaseChartNameID;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.general.Inter;
import com.fr.report.poly.PolyECBlock;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.Serializable;
/**
 * 拖动聚合块的那个竖的动作条
 * @editor zhou
 * @since 2012-3-23下午3:42:10
 */
public class PolyComponetsBar extends JToolBar {
	private static Color FOLDER_PANE_BACKGROUND = new Color(214, 223, 247);
	private BaseChartNameID[] typeName = BaseChartGetter.getStaticAllChartBaseNames();
	private SerIcon[] serIcons;

	public PolyComponetsBar() {
		setOrientation(SwingConstants.VERTICAL);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
		setFloatable(false);
		setBackground(UIConstants.NORMAL_BACKGROUND);
		setLayout(FRGUIPaneFactory.create1ColumnGridLayout());
		serIcons = new SerIcon[typeName.length + 1];
		serIcons[0] = new SerIcon(PolyECBlock.class, Inter.getLocText("Poly-Report_Block"), "Poly-Report_Block");
		this.add(serIcons[0]);
		for (int i = 0, len = typeName.length; i < len; i++) {
			BaseChart[] rowChart = BaseChartGetter.getStaticChartTypes(typeName[i].getPlotID());
			serIcons[i + 1] = new SerIcon(rowChart[0], Inter.getLocText(typeName[i].getName()), typeName[i].getName());
			this.add(serIcons[i + 1]);
		}

	}

	/**
	 * 设置是否可用状态
	 */
	public void checkEnable() {
		for (SerIcon serIcon : serIcons) {
			serIcon.setEnabled(!BaseUtils.isAuthorityEditing());
		}
	}

	private class SerIcon extends UIButton implements DragGestureListener, DragSourceListener {
		private DragSource dragSource;
		private Serializable serializable;

		public SerIcon(Serializable serializable, String text, String iconName) {
			super(BaseUtils.readIcon("com/fr/design/images/poly/toolbar/" + iconName + ".png"));
			this.serializable = serializable;
			this.setToolTipText(text);
			this.set4ToolbarButton();
			dragSource = new DragSource();
			dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
		}

		@Override
		public JToolTip createToolTip() {
			MultiLineToolTip tip = new MultiLineToolTip();
			tip.setComponent(this);
			tip.setOpaque(false);
			return tip;
		}

		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			Transferable t = new SerializableTransferable(serializable);
			dragSource.startDrag(dge, DragSource.DefaultCopyDrop, t, this);
			getModel().setArmed(false);
			getModel().setRollover(false);
			repaint();
		}

		@Override
		public void dragEnter(DragSourceDragEvent dsde) {

		}

		@Override
		public void dragOver(DragSourceDragEvent dsde) {

		}

		@Override
		public void dropActionChanged(DragSourceDragEvent dsde) {

		}

		@Override
		public void dragExit(DragSourceEvent dse) {

		}

		@Override
		public void dragDropEnd(DragSourceDropEvent dsde) {

		}
	}

	/**
	 * 测试下
	 * @param args 参数
	 */
	public static void main(String... args) {
		try {
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
		JFrame f = new JFrame();
		JPanel p = (JPanel) f.getContentPane();
		p.setLayout(FRGUIPaneFactory.createBorderLayout());
		PolyComponetsBar pbp = new PolyComponetsBar();
		p.add(pbp, BorderLayout.CENTER);

		f.setSize(400, 300);
		f.setVisible(true);

	}
}