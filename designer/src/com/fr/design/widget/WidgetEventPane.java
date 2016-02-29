package com.fr.design.widget;


import java.util.ArrayList;
import java.util.List;

import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.gui.frpane.ListenerUpdatePane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.form.event.Listener;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

public class WidgetEventPane extends ObjectJControlPane {
	
	public WidgetEventPane() {
	    this(null);
	}
    public WidgetEventPane(ElementCasePane pane) {
        super(pane);
        this.setNameListEditable(false);
    }

    /**
     * 生成添加按钮的NameableCreator
     * @return 按钮的NameableCreator
     */
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                EventCreator.STATECHANGE
            };
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Event");
    }
    
    public static class WidgetEventListenerUpdatePane extends ListenerUpdatePane {
    	private  ElementCasePane epane;
    	public WidgetEventListenerUpdatePane() {
    		this(null);
    	}
    	public WidgetEventListenerUpdatePane(ElementCasePane epane){
    		this.epane = epane;
    		super.initComponents();
    	}

        /**
         *  根据有无单元格创建 DBManipulationPane
         * @return   有单元格。有智能添加单元格等按钮，返回 SmartInsertDBManipulationPane
         */
        private DBManipulationPane autoCreateDBManipulationInWidgetEventPane() {
            JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            return jTemplate.createDBManipulationPaneInWidget();
        }

		@Override
		protected JavaScriptActionPane createJavaScriptActionPane() {
			return new JavaScriptActionPane() {

				@Override
				protected DBManipulationPane createDBManipulationPane() {
					if(epane == null) {
						return autoCreateDBManipulationInWidgetEventPane();
					} 
					
					return new SmartInsertDBManipulationInWidgetEventPane(epane);
				}

				@Override
				protected String title4PopupWindow() {
					return Inter.getLocText("Set_Callback_Function");
				}
				
				@Override
				protected boolean isForm() {
					return false;
				}

				protected String[] getDefaultArgs() {
					return new String[0];
				}
			};
		}

		@Override
		protected boolean supportCellAction() {
			return false;
		}
		
	}

    public void populate(Widget widget) {
        if (widget == null) {
            return;
        }
        
        this.refreshNameableCreator(EventCreator.createEventCreator(widget.supportedEvents()));

        List<NameObject> list = new ArrayList<NameObject>();
        Listener listener;
        for (int i = 0, size = widget.getListenerSize(); i < size; i++) {
            listener = widget.getListener(i);
            if (!listener.isDefault()) //name+(i+1)需要确保名字不重复
            {
                list.add(new NameObject(EventCreator.switchLang(listener.getEventName()) + (i + 1), listener));
            }
        }
        this.populate(list.toArray(new NameObject[list.size()]));
    }

	/**
	 * 更新
	 * @return 监听器
	 */
    public Listener[] updateListeners() {
        Nameable[] res = this.update();
        Listener[] res_array = new Listener[res.length];
        for (int i = 0, len = res.length; i < len; i++) {
            res_array[i] = (Listener) ((NameObject)res[i]).getObject();
        }
        return res_array;
    }
}