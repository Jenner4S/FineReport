/**
 * 
 */
package com.fr.poly.hanlder;

import javax.swing.event.MouseInputAdapter;

import com.fr.report.poly.PolyWorkSheet;

/**
 * @author neil
 *
 * @date: 2015-2-11-����3:48:29
 */
public abstract class BlockOperationMouseHandler extends MouseInputAdapter {
	
	/**
	 * ��ȡ���ڱ༭����
	 * 
	 * @return �༭����
	 * 
	 * @date 2015-2-12-����3:20:49
	 * 
	 */
	protected abstract PolyWorkSheet getTarget();
	
}
