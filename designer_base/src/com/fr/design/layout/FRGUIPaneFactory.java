package com.fr.design.layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.fr.design.border.UITitledBorder;

public class FRGUIPaneFactory {
	private FRGUIPaneFactory() {
	}

	public static final float WIDTH_PARA_F = 80.0f;
	public static final int WIDTH_OFFSET_N = 60;
	public static final int WIDTH_OFFSET_M = 20;
	public static final int WIDTH_PARA_INT = 80;
	public static final float WIDTHABS_PARA_F = 2.0f;
	public static final int HEIGHT_PARA = 25;
	public static final int HEIGHT_OFFSET = 50;

    /**
     * ����һ�����ҿ����ˮƽ��϶Ϊ2����ʽ����
     * @return FlowLayout����
     */
	public static LayoutManager createBoxFlowLayout() { // createBoxFlowLayout ͼ���õ��ıȽ϶�
		return new FlowLayout(FlowLayout.LEFT, 2, 0);
	}

	/**
	 * ����һ������Ĳ���
	 * @return FlowLayout����
	 */
	public static LayoutManager createLeftZeroLayout() {
		return new FlowLayout(FlowLayout.LEFT, 0, 0);
	}

    /**
     * ����һ�������ˮƽ�ʹ�ֱ��϶��Ϊ5����ʽ����
     * @return FlowLayout����
     */
	public static LayoutManager createLabelFlowLayout() { // createLabelFlowLayout
		return new FlowLayout(FlowLayout.LEFT); // Ĭ�� 5, 5
	}

	/**
	 * ����һ��������ʽ���֣����10,10
	 * @return FlowLayout����
	 */
	public static LayoutManager createL_FlowLayout() {
		return new FlowLayout(FlowLayout.LEFT, 10, 10); 
	}

	/**
	 * ����һ��������ʽ����
	 * @return FlowLayout����
	 */
	public static LayoutManager createCenterFlowLayout() {
		return new FlowLayout(FlowLayout.CENTER); 
	}

	/**
	 * ����һ��������ʽ����
	 * @return FlowLayout����
	 */
	public static LayoutManager createRightFlowLayout() {
		return new FlowLayout(FlowLayout.RIGHT); 
	}

	/**
	 * ����һ���߿򲼾�
	 * @return BorderLayout����
	 */
	public static LayoutManager createBorderLayout() {
		return new   BorderLayout();
	}

	/**
	 * ����һ���߿򲼾֣����4,4
	 * @return BorderLayout����
	 */
	public static LayoutManager createM_BorderLayout() {
		return new   BorderLayout(4,4);
	}
	
	// TODO ɾ��

	/**
	 * ����һ��1�е����񲼾�
	 * @return FRGridLayout����
	 */
	public static LayoutManager create1ColumnGridLayout() {
		return new    FRGridLayout(1);
	}

	/**
	 * ����һ��2�е����񲼾�
	 * @return FRGridLayout����
	 */
	public static LayoutManager create2ColumnGridLayout() {
		return new    FRGridLayout(2);
	}

	/**
	 * ����һ��n�е����񲼾�
	 * @param nColumn ����
	 * @return FRGridLayout����
	 */
	public static LayoutManager createNColumnGridLayout(int nColumn) {
		return new FRGridLayout(nColumn);
	}

	/**
	 * ����һ��������߿����
	 * @param string �߿����
	 * @return JPanel����
	 */
	public static JPanel createTitledBorderPane(String string) {
		JPanel jp = new JPanel();
		UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(string);
		jp.setBorder(explainBorder);
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		return jp;
	}

	/**
	 * ����һ��������߿���岢�Ҿ�����ʾ
	 * @param borderTitle �߿����
	 * @return JPanel����
	 */
	public static JPanel createTitledBorderPaneCenter(String borderTitle) {
		JPanel jp = new JPanel();
		UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(borderTitle);
		jp.setBorder(explainBorder);
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));
		return jp;
	}

	/**
	 * ����һ������ձ߿򲼾֣������
	 * @return JPanel����
	 */
	public static JPanel createBigHGapFlowInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		jp.setLayout(new FRLeftFlowLayout(5, 60, 5));
		return jp;
	}

	/**
	 * ����һ������ձ߿���壬����е�
	 * @return JPanel����
	 */
	public static JPanel createMediumHGapFlowInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		jp.setLayout(new  FRLeftFlowLayout(5, 20, 5));
		return jp;
	}

	/**
	 * ����һ������ձ߿���壬����е�
	 * @return JPanel����
	 */
	public static JPanel createMediumHGapHighTopFlowInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(50, 5, 0, 0));
		jp.setLayout(new  FRLeftFlowLayout(5, 20, 5));
		return jp;
	}

	/**
	 * ����һ����������ձ߿����
	 * @return JPanel����
	 */
	public static JPanel createNormalFlowInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		return jp;
	}

	/**
	 * ����һ������0���߿����
	 * @return JPanel����
	 */
	public static JPanel createLeftFlowZeroGapBorderPane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jp.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		return jp;
	}

	/**
	 * ����һ��������ʽ���֣�������ʽ��Ƕ
	 * @return JPanel����
	 */
	public static JPanel createNormalFlowInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		return jp;
	}

	/**
	 * ����һ��������ʽ���֣���ʽ��Ƕ
	 * @return JPanel����
	 */
	public static JPanel createBoxFlowInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		return jp;
	}

	/**
	 * ����һ���������
	 * @return JPanel����
	 */
	public static JPanel createRightFlowInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		return jp;
	}

	/**
	 * ����һ���������
	 * @return JPanel����
	 */
	public static JPanel createCenterFlowInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.CENTER));
		return jp;
	}

	/**
	 * ����һ������0������
	 * @return JPanel����
	 */
	public static JPanel createCenterFlowZeroGapBorderPane() {
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jp.setBorder(BorderFactory.createEmptyBorder());
		return jp;
	}

	/**
	 * ���������������
	 * @return JPanel����
	 */
	public static JPanel createY_AXISBoxInnerContainer_L_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		return jp;
	}

	/**
	 * ��������߿����
	 * @return JPanel����
	 */
	public static JPanel createYBoxEmptyBorderPane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		return jp;
	}

	/**
	 * �����������
	 * @return JPanel����
	 */
	public static JPanel createX_AXISBoxInnerContainer_L_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		return jp;
	}

	/**
	 * �����������M
	 * @return JPanel����
	 */
	public static JPanel createY_AXISBoxInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		return jp;
	}

	/**
	 * ������������boxlayout�����
	 * @return JPanel����
	 */
	public static JPanel createX_AXISBoxInnerContainer_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		return jp;
	}

	/**
	 * ������������boxlayout�����
	 * @return JPanel����
	 */
	public static JPanel createY_AXISBoxInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		return jp;
	}

	/**
	 * ������������boxlayout�����
	 * @return JPanel����
	 */
	public static JPanel createX_AXISBoxInnerContainer_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		return jp;
	}

	/**
	 * ����n���������
	 * @param nColumn ����
	 * @return JPanel����
	 */
	public static JPanel createNColumnGridInnerContainer_S_Pane(int nColumn) {
		JPanel jp = new JPanel();
		jp.setLayout(new FRGridLayout(nColumn));
		return jp;
	}

	/**
	 * ����n���������
	 * @param nColumn ����
	 * @param h ˮƽ���
	 * @param v ��ֱ���
	 * @return JPanel����
	 */
	public static JPanel createNColumnGridInnerContainer_Pane(int nColumn, int h, int v) {
		JPanel jp = new JPanel();
		jp.setLayout(new FRGridLayout(nColumn, h, v));
		return jp;
	}

	/**
	 * ��������n���������
	 * @param nColumn ����
	 * @return JPanel����
	 */
	public static JPanel createFillColumnPane(int nColumn) {
		JPanel jp = new JPanel();
		jp.setLayout(new FRGridLayout(nColumn, 0, 0));
		return jp;
	}

	/**
	 * �����߿����L
	 * @return JPanel����
	 */
	public static JPanel createBorderLayout_L_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp.setLayout(FRGUIPaneFactory.createBorderLayout());
		return jp;
	}

	/**
	 * �����߿����M
	 * @return JPanel����
	 */
	public static JPanel createBorderLayout_M_Pane() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		jp.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		return jp;
	}

	/**
	 * �����߿����S
	 * @return JPanel����
	 */
	public static JPanel createBorderLayout_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(FRGUIPaneFactory.createBorderLayout());
		return jp;
	}

	/**
	 * ������Ƭʽ����
	 * @return JPanel����
	 */
	public static JPanel createCardLayout_S_Pane() {
		JPanel jp = new JPanel();
		jp.setLayout(new CardLayout());
		return jp;
	}

	/**
	 * ����ͼ��IconRadio���
	 * @param icon ͼ��
	 * @param jradiobtn ��ť
	 * @return JPanel����
	 */
	public static JPanel createIconRadio_S_Pane(Icon icon,
			JRadioButton jradiobtn) {
		jradiobtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		jradiobtn.setBackground(new Color(255, 255, 255));
		JPanel iconRadioPane = new JPanel();
		iconRadioPane.setLayout(new BoxLayout(iconRadioPane, BoxLayout.X_AXIS));

		iconRadioPane.add(new UILabel(icon));
		iconRadioPane.add(jradiobtn);

		return iconRadioPane;
	}

	/**
	 * ������
	 * @param width �������ֵ
	 * @return w ������ֵ
	 */
	public static int caculateWidth(int width) {
		int w = 0;
		float m = (width + WIDTH_OFFSET_M) / WIDTH_PARA_F;
		float n = (width + WIDTH_OFFSET_N) / WIDTH_PARA_F;
		float i = Math.abs((((int) m + (int) (m + 1)) / WIDTHABS_PARA_F) - m);
		float j = Math.abs((((int) n + (int) (n + 1)) / WIDTHABS_PARA_F) - n);
		float x = i > j ? i : j;
		if (x == i) {
			w = Math.round(m) * WIDTH_PARA_INT - WIDTH_OFFSET_M;
		} else if (x == j) {
			w = Math.round(n) * WIDTH_PARA_INT - WIDTH_OFFSET_N;
		}
		return w;
	}

	/**
	 * ����߶�
	 * @param height �߶�����ֵ
	 * @return �߶����ֵ
	 */
	public static int caculateHeight(int height) {
		int h = 0;
		float x = (height + HEIGHT_OFFSET) / HEIGHT_PARA;
		h = ((int) x + 1) * HEIGHT_PARA;
		return h;
	}

}
