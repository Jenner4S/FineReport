package com.fr.design.layout;


import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;

import java.awt.*;


public class TableLayoutHelper {
    public static final int FILL_NONE = 0;
    public static final int FILL_LASTCOLUMN = 1;
    public static final int FILL_LASTROW = 2;
    public static final int FILL_LASTCOL_AND_ROW = 3;
    private static final int FIVE = 5;
    private static final int TEN = 10;
	private TableLayoutHelper() {
	}

	/**
	 * <p>一个通用的TableLayoutPane生成方法<p>
	 * <p>rowSize为各行高度之间的比例<p>
	 * <p>columnSize 为各列宽度之间的比例<p>
	 * <p>Component[][] components = {<p>
	 * <p>//    column_1      column_2     column_3<p>
	 * <p>		{component11, component12, component13}	// row_1<p>
	 * <p>		{component21, component22, component23} // row_2<p>
	 * <p>}<p>
	 */

    /**
     *最常用的最后一行列fill或者都是prefer的tablelayout
     * @param components 组件
     * @return
     */
    public static JPanel createTableLayoutPane(Component[][] components, int fillType){
        return createGapTableLayoutPane(components, fillType, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
    }


    public static JPanel createTableLayoutPane(Component[][] components, double[] rowSize, double[] columnSize) {
		return createCommonTableLayoutPane(components, rowSize, columnSize, LayoutConstants.VGAP_MEDIUM);
	}

	public static JPanel createCommonTableLayoutPane(Component[][] components, double[] rowSize, double[] columnSize, double gap) {

		return createGapTableLayoutPane(components, rowSize, columnSize, gap, gap);
	}

    public static JPanel createGapTableLayoutPane(Component[][] components, int fillType, double horizontalGap, double verticalGap) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        int maxColCount = 0;
        for (int i = 0 ; i < components.length; i ++) {
            if (components[i].length > maxColCount) {
                maxColCount = components[i].length;
            }
        }
        double[] rowSize = new double[components.length];
        for (int i = 0; i < components.length; i ++) {
            rowSize[i] = p;
        }
        double[] columnSize = new double[maxColCount];
        for (int i = 0; i < maxColCount; i ++) {
            columnSize[i] = p;
        }
        if (fillType == FILL_LASTCOLUMN && columnSize.length > 0) {
            columnSize[columnSize.length - 1] = f;
        }
        if (fillType == FILL_LASTROW && rowSize.length > 0) {
            rowSize[rowSize.length - 1] = f;
        }
        if (fillType == FILL_LASTCOL_AND_ROW ) {
            if (columnSize.length > 0) {
                columnSize[columnSize.length - 1] = f;
            }
            if (rowSize.length > 0) {
                rowSize[rowSize.length - 1] = f;
            }
        }
        return createGapTableLayoutPane(components, rowSize, columnSize, horizontalGap, verticalGap);
    }

	public static JPanel createGapTableLayoutPane(Component[][] components,
												  double[] rowSize, double[] columnSize, double horizontalGap, double verticalGap) {

		JPanel resultPane = setPanelLayout(rowSize, columnSize, horizontalGap, verticalGap);

		for (int i = 0; i < components.length; i++) {
			if (i >= rowSize.length) {
				break;
			}
			Component[] rowComponents = components[i];
			for (int j = 0; j < rowComponents.length && j < columnSize.length; j++) {
				if (rowComponents[j] == null) {
					continue;
				}

				if (isNextAllNull(rowComponents, j + 1)) {
					resultPane.add(rowComponents[j], (2 * j + 1) + "," + (2 * i + 1) + "," + (2 * rowComponents.length - 1) + ",0");
				} else {
					resultPane.add(rowComponents[j], (2 * j + 1) + "," + (2 * i + 1));
				}
			}
		}

		return resultPane;
	}


	public static JPanel createGapTableLayoutPane(Component[][] components,
												  double[] rowSize, double[] columnSize, int rowCount[][], double horizontalGap, double verticalGap) {

		JPanel resultPane = setPanelLayout(rowSize, columnSize, horizontalGap, verticalGap);

		int k = components.length;
		int[] row = new int[k];    //存放每组控件在第几行开始
		int sumRow = 1;     //存放一次递增的行的数目


		for (int i = 0; i < components.length; i++) {
			row[i] = sumRow;
			int maxRowCount = 1;

			if (i >= rowSize.length) {
				break;
			}

			Component[] rowComponents = components[i];
			for (int j = 0; j < rowComponents.length && j < columnSize.length; j++) {
				if (rowComponents[j] == null) {
					continue;
				}
				if (isNextAllNull(rowComponents, j + 1)) {
					if (rowCount[i][j] != 1) {
						resultPane.add(rowComponents[j], (2 * j + 1) + "," + row[i] + "," + (2 * rowComponents.length - 1) + "," + (row[i] + rowCount[i][j] - 1));
						if (rowCount[i][j] > maxRowCount) {
							maxRowCount = rowCount[i][j];
						}
					} else {

						resultPane.add(rowComponents[j], (2 * j + 1) + "," + row[i] + "," + (2 * rowComponents.length - 1) + "," + row[i]);
					}
				} else {

					if (rowCount[i][j] != 1) {
						resultPane.add(rowComponents[j], (2 * j + 1) + "," + row[i] + "," + (2 * j + 1) + "," + (row[i] + rowCount[i][j] - 1));
						if (rowCount[i][j] > maxRowCount) {
							maxRowCount = rowCount[i][j];
						}
					} else {
						resultPane.add(rowComponents[j], (2 * j + 1) + "," + row[i]);
					}
				}
			}
			sumRow = row[i] + maxRowCount + 1;
		}

		return resultPane;
	}


	private static boolean isNextAllNull(Component[] rowComponents, int currentIndex) {
		for (int i = currentIndex; i < rowComponents.length; i++) {
			if (rowComponents[i] != null) {
				return false;
			}
		}
		return true;
	}

	private static JPanel setPanelLayout(double[] rowSize, double[] columnSize, double horizontalGap, double verticalGap) {
		double layoutSize[][] = new double[2][];
		double layoutColumnSize[] = new double[2 * columnSize.length];
		double layoutRowSize[] = new double[2 * rowSize.length];

		for (int i = 0; i < columnSize.length; i++) {
			if (i == 0) {
				layoutColumnSize[i * 2] = 0;
			} else {
				layoutColumnSize[i * 2] = horizontalGap;
			}
			layoutColumnSize[i * 2 + 1] = columnSize[i];
		}

		for (int i = 0; i < rowSize.length; i++) {
			if (i == 0) {
				layoutRowSize[i * 2] = 0;
			} else {
				layoutRowSize[i * 2] = verticalGap;
			}
			layoutRowSize[i * 2 + 1] = rowSize[i];
		}

		layoutSize[0] = layoutColumnSize;
		layoutSize[1] = layoutRowSize;

		JPanel resultPane = new JPanel();
		resultPane.setLayout(new TableLayout(layoutSize));

		return resultPane;
	}
	
	/**
	 * 图表属性表用的比较多的一种布局，第一行是title,下面的components相当于二级菜单
	 * 二级菜单和上层面板的间距在layoutconstants里定义
	 * @param title
	 * @param components 二级菜单的内容
	 * @param rowSize 二级菜单的行数
	 * @param columnSize 二级菜单的列数
	 * @return
	 */
	public static JPanel createTableLayoutPane4Chart(String[] title, Component[][] components, double[] rowSize, double[] columnSize){
		JPanel secondMenu = createTableLayoutPane(components, rowSize, columnSize);
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] column = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
		double[] row = { p,p};
		Component[][] comp = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText(title)), null},
				new Component[]{null, secondMenu},
		};
		return createTableLayoutPane(comp, row, column);
	}

    public static void main (String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new GridLayout(2,2));
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        JPanel jp1 = TableLayoutHelper.createTableLayoutPane(createTestComponents("jp1"), TableLayoutHelper.FILL_NONE);
        JPanel jp2 = TableLayoutHelper.createGapTableLayoutPane(createTestComponents("jp2"), TableLayoutHelper.FILL_LASTCOL_AND_ROW, 2 * TEN, 2 * TEN);
        JPanel jp3 = TableLayoutHelper.createGapTableLayoutPane(createTestComponents("jp3"),
                new double[] {f,p,f,p}, new double[] {f,f}, 4,4);
        JPanel jp4 = TableLayoutHelper.createGapTableLayoutPane(createTestComponents("jp4"),
                new double[] {p,FIVE * TEN,p,p,p,p},new double[] {p, f}, new int[][] {{1,3},{1,1},{1,1},{1,1},{1,1}}, 1, FIVE);
        jp1.setBorder(BorderFactory.createLineBorder(Color.red));
        jp2.setBorder(BorderFactory.createLineBorder(Color.red));
        jp3.setBorder(BorderFactory.createLineBorder(Color.red));
        jp4.setBorder(BorderFactory.createLineBorder(Color.red));

        content.add(jp1);
        content.add(jp2);
        content.add(jp3);
        content.add(jp4);

        GUICoreUtils.centerWindow(jf);
        jf.setSize(600, 600);
        jf.setVisible(true);
    }

    private static Component[][] createTestComponents (String name ) {
        UILabel label1 = new UILabel(name + "laaaable1");
        UILabel label2 = new UILabel(name + "lable2");
        UILabel label3 = new UILabel(name + "lable3");
        UILabel label4 = new UILabel(name + "lable4");
        UILabel label5 = new UILabel(name + "lable5");
        UIButton button1 = new UIButton(name + "button1");
        UIButton button2 = new UIButton(name + "button2");
        label1.setSize(label1.getPreferredSize());
        label1.setBorder(BorderFactory.createLineBorder(Color.blue));
        label2.setSize(label2.getPreferredSize());
        label2.setBorder(BorderFactory.createLineBorder(Color.blue));
        label3.setSize(label3.getPreferredSize());
        label3.setBorder(BorderFactory.createLineBorder(Color.blue));
        label4.setSize(label4.getPreferredSize());
        label4.setBorder(BorderFactory.createLineBorder(Color.blue));
        label5.setSize(label5.getPreferredSize());
        label5.setBorder(BorderFactory.createLineBorder(Color.blue));
        button1.setSize(button1.getPreferredSize());
        button2.setSize(button2.getPreferredSize());
        button1.setBackground(Color.darkGray);
        button2.setBackground(Color.darkGray);
        Component[][] components = new Component[][] {
                new Component[] {label1, button1},
                new Component[] {label2, null},
                new Component[] {label3},
                new Component[] {null,label4},
                new Component[] {button2, label5}
        } ;
        return components;
    }
}