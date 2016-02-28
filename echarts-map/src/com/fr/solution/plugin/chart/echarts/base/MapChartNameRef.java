package com.fr.solution.plugin.chart.echarts.base;

import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.web.Repository;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLable;
import com.fr.stable.xml.XMLableReader;

public class MapChartNameRef implements XMLable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 7747795991409026124L;
	public JSONObject listName;

	public static final String XML_TAG = "MAPNAME";
	public Object clone() throws CloneNotSupportedException {
        return null;
    }
	@Override
	public void readXML(XMLableReader reader) {
		// TODO Auto-generated method stub
		if (reader.isChildNode()) {
            String name = reader.getTagName();
            if("MAPNAMEREF".equals(name)){
            	try {
            		JSONObject json = new JSONObject(reader.getAttrAsString("name", "{}").replace("*", "\""));
					setListName(json);
					System.out.println("jsonooooooo"+json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }
	}

	@Override
	public void writeXML(XMLPrintWriter writer) {
		// TODO Auto-generated method stub
		
		 writer.startTAG(XML_TAG);
	        	writer.startTAG("MAPNAMEREF");
	        	if(listName!=null){
//		        	System.out.println("GAAAAAA:::"+listName.toString());
		        	String str = listName.toString().replace("\"", "*");
		        	System.out.println("WRITE,XML nameref"+str);
		        	writer.attr("name",str);
	        	}
	               writer .end();
	        writer.end();
	}

	
	/**
     * ����json����
     * @return js����
     * @throws com.fr.json.JSONException �״�
     */
    public JSONObject toJSONObject(Repository repo) throws JSONException {
        
        return listName;
    }
	
	public JSONObject getListName() {
		return listName;
	}
	public void setListName(JSONObject listName) {
		this.listName = listName;
	}
}
