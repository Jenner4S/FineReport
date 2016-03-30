package com.fr.plugin.widget.radio;

import com.fr.base.BaseXMLUtils;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.script.Calculator;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.core.NodeVisitor;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.awt.*;

/**
 * 地产行业单选按钮组
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class EstateRadioGroup extends ButtonGroup{
	// 是否显示默认值
	private boolean showDefault = true;
	// 默认字体
	private FRFont defaultFont = FRFont.getInstance();
	// 选中字体
	private FRFont selectedFont = FRFont.getInstance("SimSun", Font.BOLD, 10, Color.RED);
	// 默认显示的文本
	private String defaultTxt = Inter.getLocText("FR-Designer-Estate_Any");
	
	public EstateRadioGroup(){
		super();
		// 利用adaptive为false来使所有选项单排显示
		this.setAdaptive(false);
	}

	/**
	 * 略
	 * @return 略
	 */
	public boolean isShowDefault() {
		return showDefault;
	}

	public void setShowDefault(boolean showDefault) {
		this.showDefault = showDefault;
	}

	public FRFont getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(FRFont defaultFont) {
		this.defaultFont = defaultFont;
	}

	public FRFont getSelectedFont() {
		return selectedFont;
	}

	public void setSelectedFont(FRFont selectedFont) {
		this.selectedFont = selectedFont;
	}

	public String getDefaultTxt() {
		return defaultTxt;
	}

	public void setDefaultTxt(String defaultTxt) {
		this.defaultTxt = defaultTxt;
	}
	
	/**
	 * TODO: 目前字体属性先写这些，后期需要再加
	 * web端用到的参数
	 * @param repo 待说明
	 * @param c 算子
     * @param nodeVisitor 节点过滤器
     *
	 * @return JSONObject web端用到的参数
	 * 
	 */
    public JSONObject createJSONConfig(Repository repo, Calculator c, NodeVisitor nodeVisitor) throws JSONException {
    	JSONObject jo = super.createJSONConfig(repo, c, null);
    	JSONObject dataJo = jo.getJSONObject("controlAttr");
    	
		dataJo = addDefaultTxt(dataJo);
		if(this.showDefault){
			// 当勾选显示默认值的时候要将选中值设置为defalut
			dataJo.put("value", "");
			jo.put("value", "");
		}
		jo.put("controlAttr", dataJo);
		if(defaultFont != null){
			jo.put("defaultFont", createFontConfig(repo,defaultFont));
		}
    	if(selectedFont != null){
    		jo.put("selectedFont",createFontConfig(repo,selectedFont));
    	}
    	jo.put("showDefault", this.showDefault);
    	return jo;
    }
    
    
    private JSONObject createFontConfig(Repository repo,FRFont font){
    	JSONObject jo = new JSONObject();
    	try{
        	if(font != null){
                jo.put("fontColor", StableUtils.javaColorToCSSColor(font.getForeground()));
                jo.put("fontSize", font.getShowSize(repo.getResolution()));
                if (font.isBold()) {
                    jo.put("fontWeight", "bold");
                }
                if (font.isItalic()) {
                    jo.put("fontStyle", "italic");
                }
                if (StringUtils.isNotEmpty(font.getName())) {
                    jo.put("fontFamily", font.getName());
                }
        	}
    	}catch(Exception e){
    		FRLogger.getLogger().error(e.getMessage());
    	}
    	return jo;
    }
    

    
    /**
     * 地产行业的特殊性，需要在所有选项前添加一个"不限"的选项，这里取已经设置好的
     * json，往该json最前面插入构造数据{"text": this.defaultTxt,"value":"default"}
     * @param jo 配置好的json数据
     * @return  追加了数据的json
     */
    private JSONObject addDefaultTxt(JSONObject jo){
       try {
			JSONArray dataJa = jo.getJSONArray("data");
			JSONObject defaultData = new JSONObject();
			defaultData.put("text", this.defaultTxt);
			defaultData.put("value", "");
			// 重新构造一个jsonArray，把追加的选项放到最前面
			JSONArray newDataJa = new JSONArray();
			newDataJa.put(defaultData);
			for(int i=0; i<dataJa.length();i++){
				newDataJa.put(dataJa.optJSONObject(i));
			}
			// 替换原有的data
			jo.put("data", newDataJa);
			return jo;
		} catch (JSONException e) {
			FRLogger.getLogger().error(e.getMessage());
		}
    	return jo;
    }
    
    
	public String getXType() {
        return "estateradiogroup";
    }
	
    public boolean equals(Object obj) {
        return obj instanceof EstateRadioGroup && super.equals(obj)
                && ComparatorUtils.equals(this.defaultTxt, ((EstateRadioGroup) obj).defaultTxt)
                && ComparatorUtils.equals(this.defaultFont, ((EstateRadioGroup) obj).defaultFont)
                && ComparatorUtils.equals(this.selectedFont, ((EstateRadioGroup) obj).selectedFont)
                && this.showDefault == ((EstateRadioGroup) obj).showDefault;
    }

    public void readXML(XMLableReader reader){

        super.readXML(reader);
        if (reader.isChildNode()) {
            String nodeName = reader.getTagName();
            if (nodeName.equals("defaultFont")) {
                this.defaultFont = BaseXMLUtils.readFRFont(reader);
			} else if (nodeName.equals("selectedFont")) {
                this.selectedFont = BaseXMLUtils.readFRFont(reader);
			} else if (nodeName.equals("DefaultTxt")){
	            this.setDefaultTxt(reader.getAttrAsString("defaultTxt", null));
			} else if(nodeName.equals("NotShowDefault")) {
            	this.setShowDefault(false);
            }
        }
    
    }
    
    public void writeXML(XMLPrintWriter writer) {
        super.writeXML(writer);
        if (this.defaultFont != null) {
            writeFont(writer, defaultFont,"defaultFont");
        }
        if (this.selectedFont != null){
        	writeFont(writer, selectedFont, "selectedFont");
        }
        if (StringUtils.isNotEmpty(this.defaultTxt)) {
            writer.startTAG("DefaultTxt").attr("defaultTxt", this.defaultTxt).end();
        }
        if(!this.isShowDefault()) {
        	writer.startTAG("NotShowDefault").end();
        }
    }
    
    
    // 将字体信息写入xml
    private void writeFont(XMLPrintWriter writer, FRFont frFont,String tag){

		writer.startTAG(tag);
		
		if(frFont != null){
			writer.attr("name", frFont.getName());
			writer.attr("style", frFont.getStyle());
			writer.attr("size", frFont.getFRSize());
		}
		
		if (frFont.getForeground() != null && !frFont.getForeground().equals(Color.black)) {
			writer.attr("foreground", frFont.getForeground().getRGB());
		}
		if (frFont.getUnderline() != Constants.LINE_NONE) {
			writer.attr("underline", frFont.getUnderline());
		}
		if (frFont.isStrikethrough()) {
			writer.attr("isStrikethrough", frFont.isStrikethrough());
		}
		if (frFont.isShadow()) {
			writer.attr("isShadow", frFont.isShadow());
		}
		if (frFont.isSuperscript()) {
			writer.attr("isSuperscript", frFont.isSuperscript());
		}
		if (frFont.isSubscript()) {
			writer.attr("isSubscript", frFont.isSubscript());
		}
		writer.end();
    }
    
    private FRFont readFont(XMLableReader reader) {
		String tmpVal;
		String name = StringUtils.EMPTY;
		int style = 0;
		int size = 0;
		if ((tmpVal = reader.getAttrAsString("name", null)) != null) {
			name = tmpVal;
		}
		style = reader.getAttrAsInt("style", 0);
		size = reader.getAttrAsInt("size", 0);

		FRFont frFont = FRFont.getInstanceFromFRSize(name, style, size);
		frFont = frFont.applyForeground(reader.getAttrAsColor("foreground", Color.black));
		frFont = frFont.applyUnderline(reader.getAttrAsInt("underline", Constants.LINE_NONE));
		frFont = frFont.applyStrikethrough(reader.getAttrAsBoolean("isStrikethrough", false));
		frFont = frFont.applyShadow(reader.getAttrAsBoolean("isShadow", false));
		frFont = frFont.applySuperscript(reader.getAttrAsBoolean("isSuperscript", false));
		frFont = frFont.applySubscript(reader.getAttrAsBoolean("isSubscript", false));

		return frFont;
	}
}
