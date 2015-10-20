package com.fr.design.javascript;

import com.fr.design.constants.KeyWords;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.autocomplete.*;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxTextArea;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JSContentPane extends BasicPane {
	private RSyntaxTextArea contentTextArea;
	private UILabel funNameLabel;

	private int titleWidth = 180;

	public JSContentPane(String[] args) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		funNameLabel = new UILabel();
		this.setFunctionTitle(args);
		this.add(funNameLabel, BorderLayout.NORTH);

		contentTextArea = new RSyntaxTextArea();
		contentTextArea.setCloseCurlyBraces(true);
		contentTextArea.setLineWrap(true);
		contentTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		contentTextArea.setCodeFoldingEnabled(true);
		contentTextArea.setAntiAliasingEnabled(true);

        CompletionProvider provider = createCompletionProvider();

        AutoCompletion ac = new AutoCompletion(provider);
        String shortCuts = DesignerEnvManager.getEnvManager().getAutoCompleteShortcuts();

        ac.setTriggerKey(convert2KeyStroke(shortCuts));
        ac.install(contentTextArea);

		UIScrollPane sp = new UIScrollPane(contentTextArea);
		this.add(sp, BorderLayout.CENTER);

		UILabel funNameLabel2 = new UILabel();
		funNameLabel2.setText("}");
		this.add(funNameLabel2, BorderLayout.SOUTH);
	}

    private KeyStroke convert2KeyStroke(String ks) {
        return KeyStroke.getKeyStroke(ks.replace("+", "pressed"));
    }

	@Override
	protected String title4PopupWindow() {
		return "JS";
	}

	public void populate(String js) {
		this.contentTextArea.setText(js);
	}

	public String update() {
		return this.contentTextArea.getText();
	}

	public void setFunctionTitle(String[] args) {
		funNameLabel.setText(createFunctionTitle(args));
	}

	public void setFunctionTitle(String[] args, String[] defaultArgs) {
		String[] titles;
		if (defaultArgs == null) {
			titles = args;
		} else if (args == null) {
			titles = defaultArgs;
		} else {
			ArrayList list  = new ArrayList();
			for (String s : defaultArgs) {
				list.add(s);
			}
			for (String s : args) {
				list.add(s);
			}
			titles = (String[])list.toArray(new String[list.size()]);
		}
		setFunctionTitle(titles);
	}

	/**
	 * ��html�����㻻��
	 *
	 * @param args
	 * @return
	 */
	private String createFunctionTitle(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html> <body> <div style='height:16px'>function(");
		int width = titleWidth;
		FontMetrics cellFM = this.getFontMetrics(this.getFont());
		int tempwidth = 0;
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] == null) {
					continue;
				}
				if (cellFM.stringWidth(args[i]) < width) {
					tempwidth = tempwidth + cellFM.stringWidth(args[i]);
					if (tempwidth < width) {
						sb.append(args[i]);
						if (i != args.length - 1) {
							sb.append(",");
						}
					} else {
						tempwidth = 0;
						i = i - 1;// ����һ��
						sb.append("</p><p>&nbsp&nbsp&nbsp&nbsp&nbsp;");
					}
				} else {
					sb.append("</p><p>&nbsp&nbsp&nbsp&nbsp&nbsp;");
					sb.append(args[i]);
					sb.append("</p>");
				}
			}
		}
		sb.append("){</div><body> </html>");
		return sb.toString();
	}

    private CompletionProvider createCompletionProvider() {

        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        for (String key : KeyWords.JAVASCRIPT) {
            provider.addCompletion(new BasicCompletion(provider, key));
        }

        for (String[] key : KeyWords.JAVASCRIPT_SHORT) {
            provider.addCompletion(new ShorthandCompletion(provider, key[0],
                    key[1], key[1]));
        }

        return provider;

    }
}
