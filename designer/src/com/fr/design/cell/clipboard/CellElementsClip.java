/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.clipboard;

import java.util.Arrays;
import java.util.Iterator;

import com.fr.base.FRContext;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.CellElementComparator;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * The clip of CellElement.
 */
public class CellElementsClip implements Cloneable, java.io.Serializable {
    private int columnSpan = 0;
    private int rowSpan = 0;
    
    private TemplateCellElement[] clips;

    public CellElementsClip(int columnSpan, int rowSpan, TemplateCellElement[] clips) {
    	this.columnSpan = columnSpan;
    	this.rowSpan = rowSpan;
    	
    	this.clips = clips;
    }
    
    public String compateExcelPaste() {
    	Arrays.sort(this.clips, CellElementComparator.getRowFirstComparator());

		// 排序
		StringBuffer sbuf = new StringBuffer();

		int currentRow = -1;
		for (int i = 0; i < clips.length; i++) {
			CellElement cellElement = clips[i];
			if (currentRow == -1) {// 初始化当前行.
				currentRow = cellElement.getRow();
			}

			if (currentRow < cellElement.getRow()) {
				for (int r = currentRow; r < cellElement.getRow(); r++) {
					sbuf.append('\n');
				}
				currentRow = cellElement.getRow();
			}

			// 添加分隔符号.
			if (sbuf.length() > 0 && sbuf.charAt(sbuf.length() - 1) != '\n') {
				sbuf.append('\t');
			}

			sbuf.append(cellElement.getValue());
		}

		return sbuf.toString();
    }
    
    public CellSelection pasteAt(TemplateElementCase ec, int column, int row) {
    	
    	Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
		while (cells.hasNext()) {
			TemplateCellElement cellElement = (TemplateCellElement)cells.next();
			ec.removeCellElement(cellElement);
		}
    	for (int i = 0; i < clips.length; i++) {
    		TemplateCellElement cellElement;
    		try {
    			cellElement = (TemplateCellElement) clips[i].clone();
    		} catch (CloneNotSupportedException e) {
    			FRContext.getLogger().error(e.getMessage(), e);
    			return null;
    		}
    		
    		// peter:因为前面已经将这个位置的元素删除了,所以不需要override了.
    		ec.addCellElement((TemplateCellElement) cellElement.deriveCellElement(
    			column + cellElement.getColumn(), row + cellElement.getRow()		
    		), false);
    	}
    	
    	return new CellSelection(column, row, columnSpan, rowSpan);
    }
    
    public void pasteAtRegion(TemplateElementCase ec, 
    		int startColumn, int startRow, 
    		int column, int row,
            int columnSpan, int rowSpan) {
        for (int i = 0; i < clips.length; i++) {
            TemplateCellElement cellElement = clips[i];

            cellElement = (TemplateCellElement) cellElement.deriveCellElement(startColumn + cellElement.getColumn(), startRow + cellElement.getRow());
            //peter:检查是否越界,越界就不做了.
            if (cellElement.getColumn() >= column + columnSpan || cellElement.getRow() >= row + rowSpan || cellElement.getColumn() < column
                    || cellElement.getRow() < row) {
                continue;
            }

            ec.addCellElement(cellElement);
        }
    }

    /**
     * Clone.
     */
    @Override
	public Object clone() throws CloneNotSupportedException {
        CellElementsClip cloned = (CellElementsClip) super.clone();

        if (this.clips != null) {
        	cloned.clips = new TemplateCellElement[this.clips.length];
        	for (int i = 0; i < this.clips.length; i++) {
        		cloned.clips[i] = (TemplateCellElement)this.clips[i].clone();
        	}
        }

        return cloned;
    }
}