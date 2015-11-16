/**
 * 
 */
package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.data.core.db.DBUtils;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.core.db.dml.Delete;
import com.fr.data.core.db.dml.Select;
import com.fr.data.core.db.dml.Table;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.record.DBRecordManager;
import com.fr.stable.*;
import com.fr.stable.xml.*;
import com.fr.third.javax.xml.stream.XMLStreamException;
import org.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;

/**
 * @author neil
 *
 * @date: 2015-4-8-����5:11:46
 */
public class InformationCollector implements XMLReadable, XMLWriter {
	
	//3���ϴ�һ��
	private static final long DELTA = 3 * 24 * 3600 * 1000L;
	private static final long SEND_DELAY = 30 * 1000L;
	private static final String FILE_NAME = "fr.info";
	private static final String XML_START_STOP_LIST = "StartStopList";
	private static final String XML_START_STOP = "StartStop";
	private static final String XML_LAST_TIME = "LastTime";
	private static final String ATTR_START = "start";
	private static final String ATTR_STOP = "stop";
	private static final String XML_JAR = "JarInfo";
	private static final String XML_VERSION = "Version";
	private static final String XML_USERNAME = "Username";
	private static final String XML_UUID = "UUID";
	private static final String XML_KEY = "ActiveKey";
	private static final String XML_OS = "OS";

    public static final String FUNCTIONS_INFO = "http://feedback.finedevelop.com:3000/monitor/function/record";

    public static final String TABLE_NAME = "fr_functionrecord";
    public static final String FUNC_COLUMNNAME = "func";

	private static InformationCollector collector;
	
	//����ʱ����ر�ʱ���б�
	private List<StartStopTime> startStop = new ArrayList<StartStopTime>();
	//��һ�εķ���ʱ��
	private String lastTime;
	private StartStopTime current = new StartStopTime();
	
	public static InformationCollector getInstance(){
		if (collector == null) {
			collector = new InformationCollector();
			
            readEncodeXMLFile(collector, collector.getInfoFile());
		}
		
		return collector;
	}
	
	private static void readEncodeXMLFile(XMLReadable xmlReadable, File xmlFile){
		if (xmlFile == null || !xmlFile.exists()) {
			return;
		}
		String charset = EncodeConstants.ENCODING_UTF_8;
		try {
			String decodeContent = getDecodeFileContent(xmlFile);
			InputStream xmlInputStream = new ByteArrayInputStream(decodeContent.getBytes(charset));
			InputStreamReader inputStreamReader = new InputStreamReader(xmlInputStream, charset);

			XMLableReader xmlReader = XMLableReader.createXMLableReader(inputStreamReader);

			if (xmlReader != null) {
				xmlReader.readXMLObject(xmlReadable);
			}
			xmlInputStream.close();
		} catch (FileNotFoundException e) {
			FRContext.getLogger().error(e.getMessage());
		} catch (IOException e) {
			FRContext.getLogger().error(e.getMessage());
		} catch (XMLStreamException e) {
			FRContext.getLogger().error(e.getMessage());
		}

	}
	
	private static String getDecodeFileContent(File xmlFile) throws FileNotFoundException, UnsupportedEncodingException{
		InputStream encodeInputStream = new FileInputStream(xmlFile);
		String encodeContent = IOUtils.inputStream2String(encodeInputStream);
		return DesUtils.getDecString(encodeContent);
	}
	
	private long getLastTimeMillis(){
		if (StringUtils.isEmpty(this.lastTime)) {
			return 0;
		}
		
		try {
			return DateUtils.string2Date(this.lastTime, true).getTime();
		} catch (Exception e) {
			return -1;
		}
		
	}
	
	private byte[] getJSONContentAsByte(){
		JSONObject content = new JSONObject();
		
		JSONArray startStopArray = new JSONArray();
		for (int i = 0; i < startStop.size(); i++) {
			JSONObject jo = new JSONObject();
			jo.put(ATTR_START, startStop.get(i).getStartDate());
			jo.put(ATTR_STOP, startStop.get(i).getStopDate());
			startStopArray.put(jo);
		}
		DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
		content.put(XML_START_STOP, startStopArray);
		content.put(XML_UUID, envManager.getUUID());
		content.put(XML_JAR, GeneralUtils.readBuildNO());
		content.put(XML_VERSION, ProductConstants.RELEASE_VERSION);
		content.put(XML_USERNAME, envManager.getBBSName());
		content.put(XML_KEY, envManager.getActivationKey());
		content.put(XML_OS, System.getProperty("os.name"));
		
		try {
			return content.toString().getBytes(EncodeConstants.ENCODING_UTF_8);
		} catch (UnsupportedEncodingException e) {
			FRContext.getLogger().error(e.getMessage());
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		}
	}

	private void sendUserInfo(){
		long currentTime = new Date().getTime();
		long lastTime = getLastTimeMillis();
		
		if (currentTime - lastTime <= DELTA) {
			return;
		}
		byte[] content = getJSONContentAsByte();
		HttpClient hc = new HttpClient(BBSConstants.COLLECT_URL);
		hc.setContent(content);
		if (!hc.isServerAlive()) {
			return;
		}
		String res = hc.getResponseText();
		//����������true, ˵���Ѿ���ȡ�ɹ�, ��յ�ǰ��¼����Ϣ
		if (Boolean.valueOf(res)) {
			this.reset();
		}
	}

    private void sendFunctionsInfo(){
        long currentTime = new Date().getTime();
        long lastTime = getLastTimeMillis();
        if (currentTime - lastTime <= DELTA) {
            return;
        }

        byte[] content = ArrayUtils.EMPTY_BYTE_ARRAY;
        Connection conn = null;
        Table table = new Table(TABLE_NAME);

        try {
            conn = DBRecordManager.getDB().createConnection();
            ResultSet rs = selectAllFromLogDB(conn, table);

            if(rs == null){
                return;
            }
            content = getFunctionsContentAsByte(rs);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        } finally {
            DBUtils.closeConnection(conn);
        }

        HttpClient httpClient = new HttpClient(FUNCTIONS_INFO);
        httpClient.setContent(content);
        httpClient.setTimeout(5000);

        if (!httpClient.isServerAlive()) {
            return;
        }

        String res =  httpClient.getResponseText();
        boolean success = ComparatorUtils.equals(new JSONObject(res).get("status"), "success");
        //����������true, ˵���Ѿ���ȡ�ɹ�, ��յ�ǰ��¼����Ϣ
        if (success) {
           deleteLogDB(conn, table);
        }

    }

    private void deleteLogDB(Connection conn, Table table) {
        try {
            conn = DBRecordManager.getDB().createConnection();
            Delete delete = new Delete(table);
            delete.execute(conn);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        } finally {
            DBUtils.closeConnection(conn);
        }
    }


    private byte[] getFunctionsContentAsByte(ResultSet rs) throws JSONException{
        com.fr.json.JSONObject content = new com.fr.json.JSONObject();
        HashMap resultMap = new HashMap();

        try {
            while (rs.next()) {
                com.fr.json.JSONObject js = new com.fr.json.JSONObject(rs.getString(FUNC_COLUMNNAME));
                Map tempMap = js.toMap();
                for (Object key : tempMap.keySet()) {
                    if(resultMap.containsKey(key)){
                        int cacheCount = Integer.parseInt(resultMap.get(key).toString());
                        int currentCount = Integer.parseInt(tempMap.get(key).toString());
                        resultMap.put(key, cacheCount + currentCount);
                    } else {
                        resultMap.put(key, tempMap.get(key));
                    }
                }
            }
            rs.close();
        } catch (SQLException e) {
            //��߲��ǣ����ܻ�û����
        }

        JSONArray functionArray = new JSONArray();
        for(Object key : resultMap.keySet()){
            com.fr.json.JSONObject jsonObject = new com.fr.json.JSONObject();
            jsonObject.put("point", key);
            jsonObject.put("times", resultMap.get(key));
            functionArray.put(jsonObject);
        }

        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        content.put("username", envManager.getBBSName());
        content.put("uuid", envManager.getUUID());
        content.put("functions", functionArray);

        try {
            return content.toString().getBytes(EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            FRContext.getLogger().error(e.getMessage());
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
    }

    private ResultSet selectAllFromLogDB(Connection conn, Table table) {

        Select select = new Select(table, DialectFactory.generateDialect(conn));
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = select.createPreparedStatement(conn);
        } catch (SQLException e) {
            return null;
        }

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            try {
                ps.close();
            } catch (SQLException e1) {
                //��߲��ǣ����ܻ�û����
            }
            return null;
        }
        return rs;
    }


    /**
     * �ռ���ʼʹ��ʱ�䣬������Ϣ
     */
	public void collectStartTime(){
		this.current.setStartDate(dateToString());
		
		sendUserInfoInOtherThread();
	}

	private void sendUserInfoInOtherThread(){
		if (!DesignerEnvManager.getEnvManager().isJoinProductImprove()) {
			return;
		}
		
    	Thread sendThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					//��ȡXML��5���Ӻ�ʼ���������ӷ�����.
					Thread.sleep(SEND_DELAY);
				} catch (InterruptedException e) {
					FRContext.getLogger().error(e.getMessage());
				}
                sendFunctionsInfo();
                sendUserInfo();
			}
		});
    	sendThread.start();
	}

    /**
     * �ռ�����ʹ��ʱ��
     */
	public void collectStopTime(){
		this.current.setStopDate(dateToString());
	}
	
	private String dateToString(){
		DateFormat df = FRContext.getDefaultValues().getDateTimeFormat();
		return df.format(new Date());
	}
	
	private void reset(){
		this.startStop.clear();
		this.lastTime = dateToString();
	}
	
    private File getInfoFile() {
    	return new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), FILE_NAME));
    }

    /**
     * ����xml�ļ�
     */
    public void saveXMLFile() {
    	File xmlFile = this.getInfoFile();
    	try{
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLTools.writeOutputStreamXML(this, out);
			out.flush();
			out.close();
			String fileContent = new String(out.toByteArray(), EncodeConstants.ENCODING_UTF_8);
			String encodeCotent = DesUtils.getEncString(fileContent);
			writeEncodeContentToFile(encodeCotent, xmlFile);
    	}catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage());
		}		
    }
    
	
	/**
	 * ���ļ�����д���������
	 */
	private static void writeEncodeContentToFile(String fileContent, File file){
		BufferedWriter bw = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, EncodeConstants.ENCODING_UTF_8);
			bw = new BufferedWriter(osw);
			bw.write(fileContent);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage());
		} finally {
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	@Override
	public void writeXML(XMLPrintWriter writer) {
		startStop.add(current);
		writer.startTAG("Info");
		//��ͣ��Ϣ
		writeStartStopList(writer);
		//��һ�θ��µ�ʱ��
		writeTag(XML_LAST_TIME, this.lastTime, writer);
		
		writer.end();
	}
	
	private void writeStartStopList(XMLPrintWriter writer){
		//��ͣ
    	writer.startTAG(XML_START_STOP_LIST);
    	for (int i = 0; i < startStop.size(); i++) {
    		startStop.get(i).writeXML(writer);
		}
    	writer.end();
	}
	
	private void writeTag(String tag, String content, XMLPrintWriter writer){
		if (StringUtils.isEmpty(content)) {
			return;
		}
		
    	writer.startTAG(tag);
    	writer.textNode(content);
    	writer.end();
	}

	@Override
	public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
        	String name = reader.getTagName();
            if (XML_START_STOP_LIST.equals(name)) {
            	readStartStopList(reader);
            } else if(XML_LAST_TIME.equals(name)){
            	readLastTime(reader);
			}
        }
	}
	
	private void readLastTime(XMLableReader reader){
		String tmpVal;
		if (StringUtils.isNotBlank(tmpVal = reader.getElementValue())) {
			this.lastTime = tmpVal;
		}
	}
	
	private void readStartStopList(XMLableReader reader){
    	startStop.clear();
		
		reader.readXMLObject(new XMLReadable() {
            public void readXML(XMLableReader reader) {
                if (XML_START_STOP.equals(reader.getTagName())) {
                    StartStopTime startStopTime = new StartStopTime();
                    reader.readXMLObject(startStopTime);
                    startStop.add(startStopTime);
                }
            }
        });
	}
	
	private class StartStopTime implements XMLReadable, XMLWriter {
		
		private String startDate;
		private String stopDate;

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getStopDate() {
			return stopDate;
		}

		public void setStopDate(String endDate) {
			this.stopDate = endDate;
		}

		public void writeXML(XMLPrintWriter writer) {
        	writer.startTAG(XML_START_STOP);
        	if (StringUtils.isNotEmpty(startDate)) {
        		writer.attr(ATTR_START, this.startDate);
			}
        	if (StringUtils.isNotEmpty(stopDate)) {
        		writer.attr(ATTR_STOP, this.stopDate);
			}
        	writer.end();
		}

		public void readXML(XMLableReader reader) {
			this.startDate = reader.getAttrAsString(ATTR_START, StringUtils.EMPTY);
			this.stopDate = reader.getAttrAsString(ATTR_STOP, StringUtils.EMPTY);
		}

	}

}
