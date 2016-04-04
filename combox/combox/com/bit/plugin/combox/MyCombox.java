package com.bit.plugin.combox;

import com.fr.form.ui.ComboBox;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.script.Calculator;
import com.fr.stable.StringUtils;
import com.fr.stable.core.NodeVisitor;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;
import com.fr.ui.DataFilter;

public class MyCombox extends ComboBox
{

	private boolean mutiSelect;
	public MyCombox()
    {
    	mutiSelect = true;
        delimiter = ",";
        startSymbol = "";
        endSymbol = "";
        returnArray = true;
        supportTag = true;
    }
	public ComboCheckBox createComboCheckBox(){
		ComboCheckBox com = new ComboCheckBox();
		com.setDelimiter(delimiter);
		com.setEndSymbol(endSymbol);
		com.setStartSymbol(startSymbol);
		com.setReturnString(returnArray);
		return com;
	}
	public void setComboCheckBox(ComboCheckBox com){
		delimiter = com.getDelimiter();
		endSymbol = com.getEndSymbol();
		startSymbol = com.getStartSymbol();
		returnArray = com.isReturnString();
	}
    public String getDelimiter()
    {
        return delimiter;
    }

    public void setDelimiter(String s)
    {
        delimiter = s;
    }

    public String getStartSymbol()
    {
        return startSymbol;
    }

    public void setStartSymbol(String s)
    {
        startSymbol = s;
    }

    public String getEndSymbol()
    {
        return endSymbol;
    }

    public void setEndSymbol(String s)
    {
        endSymbol = s;
    }

    public boolean isReturnString()
    {
        return !returnArray;
    }

    public void setReturnString(boolean flag)
    {
        returnArray = !flag;
    }

    public String getXType()
    {
    	if(!mutiSelect){
    		return "combo";
    	}
        if(supportTag)
            return "tagcombocheckbox";
        else
            return "combocheckbox";
    }

    public boolean isSupportTag()
    {
        return supportTag;
    }

    public void setSupportTag(boolean flag)
    {
        supportTag = flag;
    }

    public JSONObject createJSONConfig(Repository repository, Calculator calculator, NodeVisitor nodevisitor)
        throws JSONException
    {
    	
    	
        JSONObject jsonobject = super.createJSONConfig(repository, calculator, nodevisitor);
        if(!mutiSelect){
    		return jsonobject;
    	}
        if(StringUtils.isNotEmpty(delimiter))
            jsonobject.put("delimiter", delimiter);
            jsonobject.put("returnArray", returnArray);
            jsonobject.put("supportTag", supportTag);
//            jsonobject.put("mutiSelect", mutiSelect);
            if(StringUtils.isNotEmpty(startSymbol))
                jsonobject.put("startSymbol", startSymbol);
            if(StringUtils.isNotEmpty(endSymbol))
                jsonobject.put("endSymbol", endSymbol);
            System.out.println("AA:::::"+jsonobject);
        return jsonobject;
    }
    @Override
    protected void filterAndPutData(Calculator calculator, JSONArray jsonarray, int i, int j, String s)
        throws Exception
    {
    	super.filterAndPutData(calculator, jsonarray, i, j, s);
//    	if(mutiSelect){
//    		createDataFilterIfNeed().filterAndPutData(calculator, jsonarray, j, i, s, isRemoveRepeat(), mvList, null, delimiter);
//    	}else{
//    		super.filterAndPutData(calculator, jsonarray, i, j, s);
//    	}
    }
    @Override
    protected DataFilter createDataFilter()
    {
    	return super.createDataFilter();
//    	if(mutiSelect){
//    		return new ComboCheckBoxDataFilter();
//    	}else{
//    		return super.createDataFilter();
//    	}
//        return new ComboCheckBoxDataFilter();
    }

    public void readXML(XMLableReader xmlablereader)
    {
        super.readXML(xmlablereader);
        if(xmlablereader.isChildNode() && xmlablereader.getTagName().equals("RAAttr"))
        {
        	mutiSelect = xmlablereader.getAttrAsBoolean("mutiSelect", mutiSelect);
            returnArray = xmlablereader.getAttrAsBoolean("isArray", returnArray);
            delimiter = xmlablereader.getAttrAsString("delimiter", delimiter);
            startSymbol = xmlablereader.getAttrAsString("start", startSymbol);
            endSymbol = xmlablereader.getAttrAsString("end", endSymbol);
            supportTag = xmlablereader.getAttrAsBoolean("supportTag", supportTag);
        }
    }

    public void writeXML(XMLPrintWriter xmlprintwriter)
    {
        super.writeXML(xmlprintwriter);
        xmlprintwriter.startTAG("RAAttr");
        xmlprintwriter.attr("mutiSelect", mutiSelect);
        if(!returnArray)
        {
            if(!",".equals(delimiter))
                xmlprintwriter.attr("delimiter", delimiter);
            xmlprintwriter.attr("isArray", returnArray);
            if(StringUtils.isNotEmpty(startSymbol))
                xmlprintwriter.attr("start", startSymbol);
            if(StringUtils.isNotEmpty(endSymbol))
                xmlprintwriter.attr("end", endSymbol);
        }
        if(!supportTag)
            xmlprintwriter.attr("supportTag", supportTag);
        xmlprintwriter.end();
    }

    public boolean equals(Object obj)
    {
        return (obj instanceof MyCombox)&& super.equals(obj) && mutiSelect == ((MyCombox)obj).mutiSelect && supportTag == ((MyCombox)obj).supportTag && ComparatorUtils.equals(delimiter, ((MyCombox)obj).delimiter) && returnArray == ((MyCombox)obj).returnArray;
    }

    private String delimiter;
    private String startSymbol;
    private String endSymbol;
    private boolean returnArray;
    private boolean supportTag;
	public boolean isMutiSelect() {
		return mutiSelect;
	}

	public void setMutiSelect(boolean flag) {
		mutiSelect =flag;
	}
}