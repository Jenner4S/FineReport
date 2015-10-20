/**
 * 
 */
package com.fr.design.report;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.frpane.UITextPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.RichTextConverter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * ���ı��ı༭����
 * 
 *
 *
 * @date: 2014-12-24-����9:42:15
 */
public class RichTextEditingPane extends UITextPane{
	
	// = FLAG.length() + PREFIX.length() + SUFFIX.length();
	private static final int WRAPPER_LEN = 3;
	// = FLAG.length() + PREFIX.length()
	private static final int PREFIX_LEN = 2;
	
	//�Ƿ����ڱ༭�ı�����, ��Ҫ������������, ���ܻụ�า��, һ���Ǽ��������, һ���Ǽ�������
	private boolean updating = false;
	
	/**
	 * ���캯��
	 */
	public RichTextEditingPane() {
		this.addMouseListener(doubleClickFormulaListener);
	}

	/**
	 * �Ƿ��������������ڸ��±༭����
	 * 
	 * @return �Ƿ��������������ڸ��±༭����
	 * 
	 *
	 * @date 2014-12-24-����10:01:49
	 * 
	 */
	public boolean isUpdating() {
		return updating;
	}

	/**
	 * ���ø���״̬
	 * 
	 * @param updating �Ƿ����ڸ���
	 * 
	 *
	 * @date 2014-12-24-����10:02:13
	 * 
	 */
	public void setUpdating(boolean updating) {
		this.updating = updating;
	}
	
	/**
	 * ��ʼ����
	 * 
	 *
	 * @date 2014-12-24-����10:02:31
	 * 
	 */
	public void startUpdating(){
		this.updating = true;
	}
	
	/**
	 * ��������
	 * 
	 *
	 * @date 2014-12-24-����10:02:41
	 * 
	 */
	public void finishUpdating(){
		this.updating = false;
	}
	
	//˫��ѡȡ��ʽ������
	private MouseListener doubleClickFormulaListener = new MouseAdapter() {
		
		private int findFormulaStart(int start, StyledDocument doc) throws BadLocationException{
			//��ǰ����, Ѱ��${, ����������ҵ���}, ˵�����ڹ�ʽ�ڲ�, ֱ��return.
			//�п��ܵ�ǰ�ַ��պô���{����, ����Ҫ-1
			for (int i = start - 1; i >= 0; i--) {
				String _char = doc.getText(i, 1);
				if(ComparatorUtils.equals(_char, RichText.SUFFIX)){
					return - 1;
				}
				
				//���ִ�������, ����$
				if(ComparatorUtils.equals(_char, RichText.PREFIX)){
					if(i - 1 >= 0 && ComparatorUtils.equals(doc.getText(i - 1, 1), RichText.FLAG)){
						return i - 1;
					}
				}
			}
			
			return -1;
		}
		
		private int findFormulaEnd(int start, StyledDocument doc) throws BadLocationException{
			//��������"}"
			int total = doc.getLength();
			for (int j = start; j < total; j++) {
				String _char = doc.getText(j, 1);
				//�������������, �϶��쳣
				if(ComparatorUtils.equals(_char, RichText.PREFIX)){
					return -1;
				}
				
				if(ComparatorUtils.equals(_char, RichText.SUFFIX)){
					//Ҫ�Ѻ�׺����ȥ, ����+1
					return j + 1;
				}
			}
			
			return -1;
		}
		
		private void popUpFormulaEditPane(final String formulaContent, final int formulaStart, 
				final AttributeSet attrs){
			final UIFormula formulaPane = FormulaFactory.createFormulaPane();
			formulaPane.populate(new Formula(formulaContent));
			formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				@Override
				public void doOk() {
					StyledDocument doc = (StyledDocument) RichTextEditingPane.this.getDocument();
					Formula fm = formulaPane.update();
					String content = RichTextConverter.asFormula(fm.getContent());
					try {
						doc.remove(formulaStart, formulaContent.length() + WRAPPER_LEN);
						doc.insertString(formulaStart, content, attrs);
					} catch (BadLocationException e) {
						FRContext.getLogger().error(e.getMessage());
					}
				}
			}).setVisible(true);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getClickCount() == 2){
				// ȡ��ѡ���ı�����ʼλ�úͽ���λ��
				int start = RichTextEditingPane.this.getSelectionStart();
				
				if(start <= 0){
					return;
				}
				
				StyledDocument doc = (StyledDocument) RichTextEditingPane.this.getDocument();
				try {
					//��ʽ���
					final int formulaStart = findFormulaStart(start, doc);
					if(formulaStart == -1){
						return;
					}
					
					//��ʽ�յ�
					int formulaEnd = findFormulaEnd(start, doc);
					if(formulaEnd == -1){
						return;
					}
					
					//�ҵ���ʽ��������յ���, �������ѡ��, �������༭����
					RichTextEditingPane.this.select(formulaStart, formulaEnd);
					//�����һ���ַ�����ʽ, ���ڸ��¹�ʽ������ʽ
					Element ele = doc.getCharacterElement(formulaStart);
					final AttributeSet attrs = ele.getAttributes();
					
					final String formulaContent = doc.getText(formulaStart + PREFIX_LEN, formulaEnd - formulaStart - WRAPPER_LEN);
					//������ʽ�༭����
					popUpFormulaEditPane(formulaContent, formulaStart, attrs);
				} catch (BadLocationException e1) {
					FRContext.getLogger().error(e1.getMessage());
				}
			}
		}
	};
}
