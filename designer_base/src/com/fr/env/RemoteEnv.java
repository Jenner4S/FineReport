package com.fr.env;

import com.fr.base.*;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.impl.Connection;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.dav.DavXMLUtils;
import com.fr.dav.UserBaseEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.InformationWarnPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.loghandler.DesignerLogHandler;
import com.fr.file.CacheManager;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.file.filetree.FileNode;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.plugin.Plugin;
import com.fr.stable.*;
import com.fr.stable.file.XMLFileManagerProvider;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLTools;
import com.fr.stable.xml.XMLableReader;
import com.fr.web.ResourceConstants;

import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class RemoteEnv implements Env {
    private static final int TIME_OUT = 30 * 1000;
    private static final int PLAIN_SOCKET_PORT = 80;
    private static final int SSL_PORT = 443;
    private static final int MAX_PER_ROUTE = 20;
    private static final int MAX_TOTAL = 100;
    private static final String REMOTE_PLUGIN = "remote_plugin.info";
    private static final String CERT_KEY = "javax.net.ssl.trustStore";
    private static final String PWD_KEY = "javax.net.ssl.trustStorePassword";
    private static final String HTTPS_PREFIX = "https:";
    private final static String[] FILE_TYPE = {"cpt", "frm", "form", "cht", "chart"};
    private String path;
    private String user;
    private String password;
    private Clock clock = null;
    private String userID;
    private Timer timer;
    private int licNotSupport = 0;
    private boolean isRoot = false;
    private Timer logTimer = null;
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    private boolean isReadTimeOut = false;

    public RemoteEnv() {
        this.clock = new Clock(this);
    }

    public RemoteEnv(String path, String userName, String password) {
        this();
        this.path = path;
        this.user = userName;
        this.password = password;
    }

    /**
     * ����env����·��
     */
    @Override
    public String getPath() {
        return this.path;
    }

    public void setPath(String s) {
        this.path = s;
    }

    /**
     * ��ǰ��ƻ������û���������Զ�����
     */
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        clearUserID();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        clearUserID();
    }

    public Clock getClock() {
        return this.clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    private void clearUserID() {
        this.userID = null;
    }

    public void setThreadLocal(String value) {
        synchronized (this) {
            threadLocal.set(value);
        }

    }

    public String getThreadLocal() {
        return threadLocal.get();
    }

    /**
     * ������������˽���ǰ,��Ҫ���������������UserID
     */
    private String createUserID() throws EnvException {
        // �����¼֮��userID����null
        if (this.userID == null) {
            if (!VT4FR.REMOTE_DESIGN.support() && licNotSupport <= 0) {
                licNotSupport++;
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Lic_does_not_Support_Remote"));
            }
            throw new EnvException(Inter.getLocText("Env-Invalid_User_and_Password"));
        }

        return this.userID;
    }

    private HttpClient createHttpMethod(HashMap<String, String> para) throws EnvException, UnsupportedEncodingException {
        return createHttpMethod(para, false);
    }

    /**
     * ����nameValuePairs,Ҳ���ǲ�����,����PostMethod
     */
    private HttpClient createHttpMethod(HashMap<String, String> para, boolean isSignIn) throws EnvException, UnsupportedEncodingException {
        String methodPath = this.path;
        if (!isSignIn) {
            methodPath = methodPath + "?id=" + createUserID();
        }
        return new HttpClient(methodPath, para);
    }

    /**
     * ����nameValuePairs,Ҳ���ǲ�����,����PostMethod,��֮ͬ������,����ƴ��path����,����method.addParameters
     */
    private HttpClient createHttpMethod2(HashMap<String, String> para) throws EnvException {
        StringBuilder sb = new StringBuilder(path);

        sb.append('?');
        sb.append("id=").append(createUserID());

        return new HttpClient(sb.toString(), para, true);
    }


     /*
      * Read the response body.
	  * �ó�InputStream�����е�Byte,ת����ByteArrayInputStream����ʽ����
      *
	  * ��������Ŀ����ȷ��method.releaseConnection
	  *
	  * TODO ���������method.releaseConnection,�ж��Σ����?��ȷ��...
	  */

    /**
     * execute method֮��,ȡ���ص�inputstream
     */
    private ByteArrayInputStream execute4InputStream(HttpClient client) throws Exception {
        setHttpsParas();
        try {
            int statusCode = client.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                //���ݼ���̫�࣬���ε�
                //doWithTimeOutException();
                throw new EnvException("Method failed: " + statusCode);
            }
        } catch (Exception e) {
            FRContext.getLogger().info("Connection reset ");
        }
        InputStream in = client.getResponseStream();
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Utils.copyBinaryTo(in, out);

            // ��һ�´�������byte[]�ǲ���DesignProcessor.INVALID,����ǵĻ�,����Exception
            byte[] bytes = out.toByteArray();
            // carl����ʽһ�´�����
            String message = new String(bytes, EncodeConstants.ENCODING_UTF_8);
            if (ComparatorUtils.equals(message, RemoteDeziConstants.NO_SUCH_RESOURCE)) {
                return null;
            } else if (ComparatorUtils.equals(message, RemoteDeziConstants.INVALID_USER)) {
                throw new EnvException(RemoteDeziConstants.INVALID_USER);
            } else if (ComparatorUtils.equals(message, RemoteDeziConstants.FILE_LOCKED)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Remote_File_is_Locked"));
                return null;
            } else if (message.startsWith(RemoteDeziConstants.RUNTIME_ERROR_PREFIX)) {
            }
            return new ByteArrayInputStream(bytes);
        } finally {
            synchronized (this) {
                in.close();
                out.close();
                client.release();
            }
        }
    }

    private void doWithTimeOutException() {
        boolean isNotNeedTip = ComparatorUtils.equals(getThreadLocal(), "HEART_BEAT") || ComparatorUtils.equals(getThreadLocal(), "LOG_MESSAGE");
        if (!isReadTimeOut && !isNotNeedTip) {
            isReadTimeOut = true;
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"Data", "read_time_out"}));
            isReadTimeOut = false;
        }
        FRContext.getLogger().info("Connection reset ");
    }


    /**
     * nameValuePairs,�������Ҫ����this.path,ƴ��һ��URL,�����������req.getParameter���޷��õ���
     *
     * @param bytes ����
     * @return �Ƿ�ɹ��ύ
     * @throws Exception �쳣
     */
    private boolean postBytes2Server(byte[] bytes, HashMap<String, String> para) throws Exception {
        HttpClient client = createHttpMethod2(para);
        client.setContent(bytes);
        execute4InputStream(client);

        return true;
    }

    /**
     * ��InputStreamת��һ��String
     *
     * @param in InputStream������
     * @return ת������ַ���
     */
    public static String stream2String(InputStream in) {
        if (in == null) {
            return null;
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(in, EncodeConstants.ENCODING_UTF_8));
        } catch (UnsupportedEncodingException e) {
            br = new BufferedReader(new InputStreamReader(in));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(line);
            }
        } catch (IOException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        return sb.toString();
    }

    /**
     * �������ӷ�����
     *
     * @return �������ӳɹ�����true
     * @throws Exception �쳣
     */
    public boolean testServerConnection() throws Exception {
        return testConnection(true, true, DesignerContext.getDesignerFrame());
    }

    /**
     * ���Ե�ǰ�����Ƿ���ȷ
     *
     * @return �����Ƿ�ɹ�
     * @throws Exception �쳣
     */
    public boolean testServerConnectionWithOutShowMessagePane() throws Exception {
        return testConnection(false, true, DesignerContext.getDesignerFrame());
    }

    /**
     * ��Ҫ�����ڻ�����������еĲ������Ӱ�ťʱ����Ҫע���Զ�̻���
     *
     * @param messageParentPane ��������������
     * @return �Ƿ�������ӳɹ�
     * @throws Exception �쳣
     */
    public boolean testConnectionWithOutRegisteServer(Component messageParentPane) throws Exception {
        return testConnection(true, false, messageParentPane);
    }


    private boolean testConnection(boolean needMessage, boolean isRegisteServer, Component parentComponent) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "test_server_connection");
        para.put("user", user);
        para.put("password", password);

        if (path.startsWith("https") && (!DesignerEnvManager.getEnvManager().isHttps())) {
            return false;
        }

        HttpClient client = createHttpMethod(para, true);

        String res = stream2String(execute4InputStream(client));
        if (res == null) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText("Datasource-Connection_failed"));
            }
            return false;
        } else if (ComparatorUtils.equals(res, "true")) {
            if (!clock.connected && isRegisteServer) {
                //�������ж�����������֮��������Զ�̷�����ע��
                register2Server();
            }
            return true;
        } else if (ComparatorUtils.equals(res, "invalid username or password.")) {
            JOptionPane.showMessageDialog(parentComponent,
                    Inter.getLocText(new String[]{"Datasource-Connection_failed", "Registration-User_Name", "Password", "Error"}, new String[]{",", "", "", "!"})
                    , Inter.getLocText("FR-Server-All_Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (res.indexOf("RegistEditionException") != -1) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
            } else {
                FRLogger.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "Version-does-not-support"}, new String[]{",", "!"}));
            }
            return false;
        } else if (ComparatorUtils.equals(res, "war not support remote design.")) {
            if (needMessage) {
                JOptionPane.showMessageDialog(parentComponent, Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
            } else {
                FRLogger.getLogger().info(Inter.getLocText(new String[]{"Datasource-Connection_failed", "NS-war-remote"}, new String[]{",", "!"}));
            }
            return false;
        } else {
            throw new EnvException(res);
        }
    }

    private void setHttpsParas() {
        if (path.startsWith(HTTPS_PREFIX) && System.getProperty(CERT_KEY) == null) {
            DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
            System.setProperty(CERT_KEY, envManager.getCertificatePath());
            System.setProperty(PWD_KEY, envManager.getCertificatePass());
        }
    }

    private void register2Server() {
        try {
            SignIn.signIn(this);
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    /**
     * �������ʣ��������µ�ǰ�û��ķ���ʱ��
     *
     * @throws Exception
     */
    public void heartBeatConnection() throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "heart_beat");
        para.put("user", user);
        para.put("userid", userID);

        HttpClient client = createHttpMethod(para, true);
        execute4InputStream(client);

        //����������, 30��ˢһ��, ˢ�µ�ʱ������¹�����, ���������ӽڵ㶼��������, Ч��̫��.
        //Ϊʲô������ˢ��ǰ��������״̬, ��Ϊˢ�º������ˢ��ǰ�����Ľṹδ����һ�µ�.

        //������֪ͨ�ͻ��˸������Ͻ��ļ������
//        try {
//            if (ComparatorUtils.equals(stream2String(execute4InputStream(method)), "true")) {
//                DesignerFrameFileDealerPane.getInstance().refresh();
//            }
//        } catch (Exception e) {
//            FRLogger.getLogger().error(e.getMessage());
//        }
    }

    /**
     * �������������л���������
     *
     * @return �����������ֵ��ַ���
     */
    public String getEnvDescription() {
        return Inter.getLocText("Env-Remote_Server");
    }

    /**
     * ��¼,����userID
     */
    public void signIn() throws Exception {
        if (clock != null && clock.connected) {
            return;
        }
        String remoteVersion = getDesignerVersion();
        if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
            throw new Exception("version not match");
        }
        clearUserID();
        startLogTimer();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "r_sign_in");
        para.put("user", user);
        para.put("password", password);

        simulaRPC(para, true);

        //neil:����Clock������10�������������һ���ֽڣ�ȷ��û����
        if (clock == null) {
            Clock clock = new Clock(this);
            setClock(clock);
        }
        clock.start();

        // Զ�̵�¼����������, ��ֹ�����ǿ�ƹرն�û��Logout
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    RemoteEnv.this.setThreadLocal("HEART_BEAT");
                    RemoteEnv.this.heartBeatConnection();
                } catch (Exception e) {
                    FRContext.getLogger().error("Server may be disconnected.", e);
                }
            }
        }, RemoteDeziConstants.HEARTBEAT_DELAY, RemoteDeziConstants.HEARTBEAT_DELAY);
    }


    private void startLogTimer() {
        if (logTimer != null) {
            logTimer.cancel();
        }

        logTimer = new Timer();
        logTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    RemoteEnv.this.setThreadLocal("LOG_MESSAGE");
                    FRContext.getCurrentEnv().printLogMessage();
                } catch (Exception e) {
                    FRLogger.getLogger().info(e.getMessage());
                }
            }
        }, 10000, 10000);
    }

    private void stopLogTimer() {
        logTimer.cancel();
        logTimer = null;
    }

    /**
     * ����userID sign out
     *
     * @return �ɹ�ǩ������true
     * @throws Exception
     */
    public boolean signOut() throws Exception {
        if (userID == null) {
            return true;
        }
        stopLogTimer();
        // richer:�ǳ���ʱ��ͰѶ�ʱ���͵�ʱ��ͣ��
        clock.stop();
        // richer:����ѵʹ�õĶ�ʱ��Ҳȥ��
        timer.cancel();

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "r_sign_out");
        para.put("id", userID);

        return simulaRPC(para, false
        );
    }

    protected boolean simulaRPC(HashMap<String, String> para, boolean isSignIn) throws Exception {
        HttpClient client = createHttpMethod(para, isSignIn);

        // execute methodȡ��input stream��ת��String
        String resJSON = null;
        try {
            resJSON = stream2String(execute4InputStream(client));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }

        if (resJSON == null) {
            return false;
        }
        if (resJSON.indexOf("RegistEditionException") != -1) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Lic_does_not_Support_Remote"));
            return false;
        }
        try {
            JSONObject jo = new JSONObject(resJSON);

            if (isSignIn) {
                if (jo.has("id")) {
                    userID = jo.getString("id");
                }
                if (jo.has("isRoot")) {
                    isRoot = jo.getBoolean("isRoot");
                }

                if (userID != null) {
                    return true;
                }
            } else {
                if (jo.has("res")) {
                    return jo.getBoolean("res");
                }
            }
            String exception = jo.getString("exp");
            if (exception != null) {
                throw new EnvException(exception);
            }
        } catch (JSONException je) {
            // ���ص�resJSON����JSON��ʽ��,�Ǿ�ֱ�ӷ���resJSON��ΪuserID
            return true;
        }

        return true;
    }

    protected boolean doLockOperation(String[] filePathes, String cmd) throws Exception {
        if (filePathes == null || filePathes.length == 0) {
            return true;
        }

        JSONArray ja = new JSONArray(filePathes);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", cmd);
        para.put("pathes", ja.toString());

        return simulaRPC(para, false);
    }

    /**
     * ȡ·��filePath�����ļ���lock
     * <p/>
     * ����ͬһ��ԭ�Ӳ���,Ҫô�õ����е���,Ҫôһ����Ҳû���õ�
     */
    public boolean getLock(String[] filePathes) throws Exception {
        return doLockOperation(filePathes, "design_get_lock");
    }

    /**
     * �����ļ�
     *
     * @param filePathes �ļ�·��
     * @return �ɹ���������true
     * @throws Exception
     */
    public boolean releaseLock(String[] filePathes) throws Exception {
        return doLockOperation(filePathes, "design_release_lock");
    }

    /**
     * ��ǰEnv��,tplPathĿ¼���Ƿ����ģ��
     *
     * @param reportPath ·��
     * @return �Ƿ����
     */
    @Override
    public boolean isTemplateExist(String reportPath) throws Exception {
        if (reportPath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_report_exist");
        para.put("report_path", reportPath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        return ComparatorUtils.equals(stream2String(input), "true");
    }

    /**
     * ������ǰģ�壬����Զ����ơ���Զ�����ĳ��ģ�� ʱ���ڽ���֮ǰ��ģ�崦������״̬
     *
     * @param tplPath ·��
     * @throws Exception
     */
    @Override
    public void unlockTemplate(String tplPath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_close_report");
        para.put(RemoteDeziConstants.TEMPLATE_PATH, tplPath);
        HttpClient client = createHttpMethod(para);

        InputStream input = execute4InputStream(client);
        String info = Utils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8);

        FRContext.getLogger().error(info);
    }

    public class Bytes2ServerOutputStream extends OutputStream {
        private ByteArrayOutputStream out = new ByteArrayOutputStream();
        private HashMap<String, String> nameValuePairs;

        public Bytes2ServerOutputStream(HashMap<String, String> nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
        }

        public HashMap<String, String> getNameValuePairs() {
            return nameValuePairs;
        }

        public ByteArrayOutputStream getOut() {
            return out;
        }

        public OutputStream getZipOutputStream() throws Exception {
            return IOUtils.toZipOut(out);
        }

        /**
         * post ro Server �ύ��������
         *
         * @return �Ƿ��ύ�ɹ�
         */
        public boolean post2Server() {
            try {
                return postBytes2Server(out.toByteArray(), nameValuePairs);
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
                return false;
            }
        }

        /**
         * ˢ�������������ύ
         *
         * @throws IOException
         */
        public void flush() throws IOException {
            super.flush();
            post2Server();
        }

        /**
         * ��ָ���ֽ�д������������
         *
         * @param b д����ֽ�
         * @throws IOException
         */
        @Override
        public void write(int b) throws IOException {
            out.write(b);

        }
    }

    /**
     * �������������Ƿ��ܹ���ȷ��������
     *
     * @param database ��������
     * @return �������ȷ�����ӵ����ݿ��򷵻�true
     * @throws Exception �޷���ȷ���ӵ����ݿ����׳����쳣
     *                   TODO alex_ENV ������Ϊ,����Ӧ���ǲ�������Connection������,����Connection��TableData�ӿڵĹ�����Ҫ˼��
     */
    @Override
    public boolean testConnection(com.fr.data.impl.Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��databaseд��xml�ļ���out
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_test_con");

        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * ben:ȡschema
     */
    @Override
    public String[] getTableSchema(com.fr.data.impl.Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_schema");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return null;
        }
        return DavXMLUtils.readXMLFileSchema(input);
    }

    /**
     * b:�ֱ�ȡTable,View,Procedure,ʵ��Ӧ��ʱ��������
     */
    @Override
    public TableProcedure[] getTableProcedure(com.fr.data.impl.Connection database, String type, String schema) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_tables");
        para.put("__type__", type);
        para.put("__dbschema__", schema);
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return new TableProcedure[0];
        }
        return DavXMLUtils.readXMLSQLTables(input);
    }

    public List getProcedures(com.fr.data.impl.Connection datasource, String[] schemas, boolean isOracle, boolean isOracleSysSpace) throws Exception {
        HashMap schemaTableProcedureMap = new HashMap();
        List sqlTableObjs = new ArrayList();
        TableProcedure[] sqlTableObj = null;
        int len = schemas.length;
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                String schema = schemas[i];
                sqlTableObj = this.getTableProcedure(datasource, TableProcedure.PROCEDURE, schema);
                if (sqlTableObj == null) {
                    sqlTableObj = new TableProcedure[0];
                }
                sqlTableObjs.add(sqlTableObj);
                schemaTableProcedureMap.put(schema, sqlTableObj);
            }
        } else {
            sqlTableObj = this.getTableProcedure(datasource, TableProcedure.PROCEDURE, null);
            if (sqlTableObj == null) {
                sqlTableObj = new TableProcedure[0];
            }
            sqlTableObjs.add(sqlTableObj);
            schemaTableProcedureMap.put(null, sqlTableObj);
        }
        DataCoreUtils.putProcedureMap(datasource, schemaTableProcedureMap);
        return sqlTableObjs;
    }

    /**
     * �ڵ�ǰ·�����½��ļ���
     *
     * @param folderPath �ļ���
     * @return �ɹ���������true
     * @throws Exception
     */
    @Override
    public boolean createFolder(String folderPath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_create_folder");
        para.put("folder_path", folderPath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * �½�һ���ļ�
     *
     * @param filePath ��Ŀ���ļ����·��
     * @return �ɹ��½�����true
     * @throws Exception
     */
    @Override
    public boolean createFile(String filePath) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_create_file");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * �ж��ļ��Ƿ����
     *
     * @param filePath ��Ŀ���ļ����·��
     * @return �ļ��Ƿ����
     * @throws Exception
     */
    @Override
    public boolean fileExists(String filePath) throws Exception {
        if (filePath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_file_exists");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * �ж��ļ��Ƿ���ס
     *
     * @param filePath �ļ�·��
     * @return �ļ�����ס�ˣ�����true
     * @throws Exception
     */
    public boolean fileLocked(String filePath) throws Exception {
        if (filePath == null) {
            return false;
        }

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_file_locked");
        para.put("file_path", filePath);

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }


    /**
     * ע�ỷ�������ڼ���Ƿ�������ʱ������Ҫ���ڱ��ػ��������Զ��
     *
     * @param env �û�����
     */
    public void registerUserEnv(UserBaseEnv env) {
    }

    /**
     * ���ڼ���û�����
     * ��������ʱ��
     */
    public void startUserCheckTimer() {
    }


    /**
     * ֹͣ��ʱ��
     */
    public void stopUserCheckTimer() {
    }


    /**
     * ɾ���ļ�
     *
     * @param filePath �ļ���ַ
     * @return ɾ���ɹ�����true
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "delete_file");
            para.put("file_path", filePath);

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return false;
            }

            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return false;
    }

    /**
     * Զ����������ʱ�����翪��Ȩ�޾Ͳ���Ԥ���ˡ���߷�һ��ȫ�ֵ�map��������
     *
     * @param key   ��ֵ
     * @param value ֵ
     * @return ���д��ɹ�������true
     * @throws Exception
     */
    public boolean writePrivilegeMap(String key, String value) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "write_privilege_map");
        para.put("current_user", this.user);
        para.put("current_password", this.password);
        para.put("key", key);
        para.put("value", value);

        HttpClient client = createHttpMethod(para); //jim ������user��Զ����Ƶ��Ԥ��ʱ�����û���ɫ��Ϣ
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return false;
        }

        return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
    }

    /**
     * DataSource��ȥ����ǰ��ɫû��Ȩ�޷��ʵ�����Դ
     */
    public void removeNoPrivilegeConnection() {
        DatasourceManagerProvider dm = DatasourceManager.getProviderInstance();

        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fs_remote_design");
            para.put("cmd", "env_get_role");
            para.put("currentUsername", this.getUser());
            para.put("currentPwd", this.getPassword());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            ArrayList<String> toBeRemoveTDName = new ArrayList<String>();
            for (int i = 0; i < ja.length(); i++) {
                String toBeRemoveConnName = (String) ((JSONObject) ja.get(i)).get("name");
                dm.removeConnection(toBeRemoveConnName);
                Iterator it = dm.getTableDataNameIterator();
                while (it.hasNext()) {
                    String tdName = (String) it.next();
                    TableData td = dm.getTableData(tdName);
                    td.registerNoPrivilege(toBeRemoveTDName, toBeRemoveConnName, tdName);
                }
            }

            for (int i = 0; i < toBeRemoveTDName.size(); i++) {
                dm.removeTableData(toBeRemoveTDName.get(i));
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * �г�WEB-INFĿ¼��ָ��·�����ļ������ļ�
     *
     * @param rootFilePath ָ��Ŀ¼
     * @return WEB-INFĿ¼��ָ��·�����ļ������ļ�
     * @throws Exception
     */
    @Override
    public FileNode[] listFile(String rootFilePath) throws Exception {
        return listFile(rootFilePath, false);
    }

    /**
     * �г�WEB-INF�ϲ�Ŀ¼��ָ��·�����ļ������ļ�
     *
     * @param rootFilePath ָ��Ŀ¼
     * @return WEB-INF�ϲ�Ŀ¼��ָ��·�����ļ������ļ�
     * @throws Exception
     */
    @Override
    public FileNode[] listReportPathFile(String rootFilePath) throws Exception {
        return listFile(rootFilePath, true);
    }

    private FileNode[] listFile(String rootFilePath, boolean isWebReport) throws Exception {
        FileNode[] fileNodes;

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_list_file");
        para.put("file_path", rootFilePath);
        para.put("currentUserName", this.getUser());
        para.put("currentUserId", this.createUserID());
        para.put("isWebReport", isWebReport ? "true" : "false");

        HttpClient client = createHttpMethod(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return new FileNode[0];
        }

        // Զ�̻��������Ŀ¼���ݲ���Ҫ��xlsx��xls�ļ�
        fileNodes = DavXMLUtils.readXMLFileNodes(input);
        ArrayList<FileNode> al = new ArrayList<FileNode>();
        for (int i = 0; i < fileNodes.length; i++) {
            al.add(fileNodes[i]);
        }

        FileNode[] fileNodes2 = new FileNode[al.size()];
        for (int i = 0; i < al.size(); i++) {
            fileNodes2[i] = al.get(i);
        }

        return fileNodes2;
    }


    /**
     * �г�Ŀ��Ŀ¼������cpt�ļ����ļ���
     *
     * @param rootFilePath ָ��Ŀ¼
     * @return �г�Ŀ��Ŀ¼������cpt�ļ����ļ���
     * @throws Exception
     */
    public FileNode[] listCpt(String rootFilePath) throws Exception {
        return listCpt(rootFilePath, false);
    }

    /**
     * �г�Ŀ��Ŀ¼������cpt�ļ����ļ���
     *
     * @param rootFilePath ָ��Ŀ¼
     * @param recurse      �Ƿ�ݹ��������Ŀ¼
     * @return �г�Ŀ��Ŀ¼������cpt�ļ����ļ���
     * @throws Exception
     */
    public FileNode[] listCpt(String rootFilePath, boolean recurse) {
        List<FileNode> fileNodeList = new ArrayList<FileNode>();
        try {
            listAll(rootFilePath, fileNodeList, recurse);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return fileNodeList.toArray(new FileNode[fileNodeList.size()]);
    }

    private void listAll(String rootFilePath, List<FileNode> nodeList, boolean recurse) throws Exception {
        FileNode[] fns = listFile(rootFilePath);
        for (int i = 0; i < fns.length; i++) {
            if (fns[i].isFileType("cpt")) {
                nodeList.add(fns[i]);
            } else if (fns[i].isDirectory()) {
                if (recurse) {
                    listAll(rootFilePath + File.separator + fns[i].getName(), nodeList, true);
                } else {
                    nodeList.add(fns[i]);
                }
            }
        }
    }

    /**
     * ��ȡָ�����ݼ��Ĳ���
     *
     * @param tableData ���ݼ�
     * @return ���ݼ��Ĳ���
     * @throws Exception ��ȡ����ʧ�����׳����쳣
     */
    public Parameter[] getTableDataParameters(TableData tableData) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

//        ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLFileTableData(tableData, out);

        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_td_pars");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return new Parameter[0];
        }
        return DavXMLUtils.readXMLParameters(input);
    }


    /**
     * ��ȡ�洢�����еĲ���
     *
     * @param storeProcedure �洢����
     * @return ���ش洢�����е����в�����ɵ�����
     * @throws Exception �����ȡ����ʧ�����׳����쳣
     */
    public Parameter[] getStoreProcedureParameters(StoreProcedure storeProcedure) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLFileStoreProcedureAndSource(storeProcedure, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_sp_pars");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return new Parameter[0];
        }
        return DavXMLUtils.readXMLParameters(input);
    }

    /**
     * ����ָ���Ĳ�������һ��ʵ�ʿ�Ԥ�������ݼ�
     *
     * @param tableData    �����������ݼ�
     * @param parameterMap ������ֵ��
     * @param rowCount     ��Ҫ��ȡ������
     * @return ʵ�ʵĶ�ά���ݼ�
     * @throws Exception �����������ʧ�����׳����쳣
     */
    public EmbeddedTableData previewTableData(Object tableData, java.util.Map parameterMap, int rowCount) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLFileTableDataAndSource((TableData) tableData, out);

        // ��parameterMapת��JSON��ʽ���ַ���
        JSONObject jo = new JSONObject(parameterMap);
        String jsonParameter = jo.toString();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_preview_td");
        para.put("pars", jsonParameter);
        para.put("rowcount", String.valueOf(rowCount));
        InputStream input = postBytes2ServerB(out.toByteArray(), para);

        if (input == null) {
            return null;
        }

        return (EmbeddedTableData) DavXMLUtils.readXMLTableData(input);
    }

    /**
     * ����ָ���Ĳ�������һ��ʵ�ʿ�Ԥ�������ݼ�
     *
     * @param tableData    �����������ݼ�
     * @param parameterMap ������ֵ��
     * @param start        ��ʼ
     * @param end          ��β
     * @param cols         ����
     * @param colIdx       �����
     * @return ʵ�ʵĶ�λ������
     * @throws Exception �쳣
     */
    public Object previewTableData(Object tableData, java.util.Map parameterMap, int start, int end, String[] cols, int[] colIdx) throws Exception {
        return previewTableData(tableData, parameterMap, -1);
    }

    /**
     * nameValuePairs,�������Ҫ����this.path,ƴ��һ��URL,�����������req.getParameter���޷��õ���
     *
     * @param bytes ����
     * @param para  ����
     * @return �ӷ������˵õ�InputStream
     * @throws Exception �쳣
     */
    public InputStream postBytes2ServerB(byte[] bytes, HashMap<String, String> para) throws Exception {
        HttpClient client = createHttpMethod2(para);
        client.setContent(bytes);
        return execute4InputStream(client);
    }

    /**
     * Read XML.<br>
     * The method will be invoked when read data from XML file.<br>
     * May override the method to read the data that you saved.
     */
    public void readXML(XMLableReader reader) {
        if (reader.isChildNode()) {
            String tmpVal;
            if ("DIR".equals(reader.getTagName())) {
                if ((tmpVal = reader.getAttrAsString("path", null)) != null) {
                    this.path = tmpVal;
                }
                if ((tmpVal = reader.getAttrAsString("user", null)) != null) {
                    this.user = tmpVal;
                }
                if ((tmpVal = reader.getAttrAsString("password", null)) != null) {
                    this.password = tmpVal;
                }
            }
        }
    }

    /**
     * Write XML.<br>
     * The method will be invoked when save data to XML file.<br>
     * May override the method to save your own data.
     *
     * @param writer the PrintWriter.
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG("DIR").attr("path", this.path).attr("user", this.user).attr("password", this.password).end();
    }


    public static class Clock {

        private static final long CONNECT_INTERVAL = 3000L;
        private boolean connected = false;

        private RemoteEnv remoteEnv;

        public Clock(RemoteEnv remoteEnv) {
            this.remoteEnv = remoteEnv;
        }

        /**
         * ��ʼ����
         */
        public void start() {
            if (connected) {
                return;
            }
            connected = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // richie:�������γ������Ӷ�û����Ӧ���ж�Ϊ��ʧ����
                    while (connected) {
                        try {
                            attemptConnect();
                        } catch (Exception ex) {
                            try {
                                attemptConnect();
                            } catch (Exception ee) {
                                try {
                                    attemptConnect();
                                } catch (Exception exc) {
                                    stop();
                                    if (exc instanceof NoRouteToHostException) {
                                        //�������⵼�µ������ж�
                                        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Remote_Connect2Server_Again"), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION)
                                                == JOptionPane.OK_OPTION) {
                                            //�����������ӷ������ķ���
                                            connectedAgain();
                                        }
                                    } else {
                                        //�������ر�����������ж�
                                        if (JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Remote_Re_Connect_to_Server"), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION)
                                                == JOptionPane.OK_OPTION) {
                                            //�����������ӷ������ķ���
                                            connectedAgain();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }).start();
        }

        /**
         * �����������жϺ���������
         */
        private void connectedAgain() {
            try {
                if (!remoteEnv.testServerConnectionWithOutShowMessagePane()) {
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText(new String[]{"Datasource-Connection_failed", "check_communication"},
                            new String[]{",", "!"}));
                    DesignerFrameFileDealerPane.getInstance().refresh();
                    return;
                }
                String remoteVersion = remoteEnv.getDesignerVersion();
                if (StringUtils.isBlank(remoteVersion) || ComparatorUtils.compare(remoteVersion, ProductConstants.DESIGNER_VERSION) < 0) {
                    String infor = Inter.getLocText("FR-Server_Version_Tip");
                    String moreInfo = Inter.getLocText("FR-Server_Version_Tip_MoreInfo");
                    FRLogger.getLogger().log(Level.WARNING, infor);
                    new InformationWarnPane(infor, moreInfo, Inter.getLocText("FR-Designer_Tooltips")).show();
                    return;
                }
                SignIn.signIn(remoteEnv);
                LicUtils.resetBytes();
                HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshToolArea();
            } catch (Exception em) {
                FRContext.getLogger().error(em.getMessage(), em);
            }
        }

        /**
         * ֹͣ����
         */
        public void stop() {
            connected = false;
        }

        private void attemptConnect() throws Exception {
            Thread.sleep(CONNECT_INTERVAL);
            Pattern pattern = Pattern.compile("[/:]+");
            String[] strs = pattern.split(remoteEnv.path);

            String shost = strs[1];//host,�磺192.168.100.195
            int sport = Integer.parseInt(strs[2]);//�˿�,�磺8080

            Socket socket = new Socket(shost, sport);
            //OOBBINLINE���Ƿ�֧�ַ���һ���ֽڵ�TCP��������,false��ʾ���������ô����������
            socket.setOOBInline(false);
            socket.sendUrgentData(0xFF);
            socket.close();
        }
    }

    /**
     * ���������л�������������ļ�,��datasource.xml, config.xml,��Щ�ļ���������WEB-INF/resourcesĿ¼����
     *
     * @param resourceName �����ļ������֣���datasource.xml
     * @return ������
     * @throws Exception
     */
    @Override
    public InputStream readResource(String resourceName) throws Exception {
        return readBean(resourceName, ProjectConstants.RESOURCES_NAME);
    }


    /**
     * ��ȡ·���µ�svg�ļ�
     *
     * @param path �ƶ�·��,�ǻ��ڱ���Ŀ¼��resource�ļ���·��
     * @return �������ļ�
     */
    public File[] readPathSvgFiles(String path) {
        String cataloguePath = StableUtils.pathJoin(new String[]{CacheManager.getProviderInstance().getCacheDirectory().getPath(), SvgProvider.SERVER, path});

        //��黺���ļ������Ŀ¼��serversvgs�ļ����Ƿ���� ���������ݴ��������������svg�ļ�
        File catalogue = new File(cataloguePath);
        if (!catalogue.exists()) {
            catalogue.mkdirs();
        }

        ArrayList<File> fileArray = new ArrayList<>();
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "design_read_svgfile");
            para.put("resourcePath", path);
            para.put("current_uid", this.createUserID());
            para.put("currentUsername", this.getUser());

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = (JSONObject) ja.get(i);
                String svgFileName = (String) jsonObject.get("svgfileName");
                String svgfileContent = (String) jsonObject.get("svgfileContent");
                File file = new File(StableUtils.pathJoin(new String[]{cataloguePath, svgFileName}));
                InputStream in = new ByteArrayInputStream(svgfileContent.getBytes(EncodeConstants.ENCODING_UTF_8));
                FileOutputStream out = new FileOutputStream(file);
                IOUtils.copyBinaryTo(in, out);
                fileArray.add(file);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }

        return fileArray.toArray(new File[fileArray.size()]);
    }


    /**
     * дsvg�ļ�
     *
     * @param svgFile svg�ļ�
     * @return �Ƿ�д��ɹ�
     * @throws Exception �쳣
     */
    public boolean writeSvgFile(SvgProvider svgFile) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_save_svg");
        para.put("filePath", svgFile.getFilePath());
        para.put("current_uid", this.createUserID());
        para.put("currentUsername", this.getUser());

        // ͨ��ByteArrayOutputStream��svgд���ֽ���
        Bytes2ServerOutputStream out = new Bytes2ServerOutputStream(para);
        OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
        StreamResult result = new StreamResult(outWriter);

        Source source = new DOMSource(svgFile.getSvgDocument());
        try {
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            try {
                xformer.transform(source, result);
            } catch (TransformerException ex) {
                FRContext.getLogger().error(ex.getMessage());
            }

        } catch (TransformerConfigurationException ex) {
            FRContext.getLogger().error(ex.getMessage());
            return false;
        }

        try {
            HttpClient client = createHttpMethod2(out.getNameValuePairs());
            client.setContent(out.getOut().toByteArray());
            String res = stream2String(execute4InputStream(client));
            if (StringUtils.isNotEmpty(res)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Already_exist") + res);
                return false;
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * д�������л�������������ļ�
     *
     * @param mgr ���������Դ�ļ��Ĺ�����
     * @return д��xml�ɹ�����true
     * @throws Exception д��xml�������׳����쳣
     */
    @Override
    public boolean writeResource(XMLFileManagerProvider mgr) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_save_resource");
        para.put("resource", mgr.fileName());
        para.put("current_uid", this.createUserID());
        para.put("currentUsername", this.getUser());

        // alex:ͨ��ByteArrayOutputStream��mgrд���ֽ���
        Bytes2ServerOutputStream out = new Bytes2ServerOutputStream(para);
        XMLTools.writeOutputStreamXML(mgr, out);

        try {
            HttpClient client = createHttpMethod2(out.getNameValuePairs());
            client.setContent(out.getOut().toByteArray());
            String res = stream2String(execute4InputStream(client));
            if (StringUtils.isNotEmpty(res)) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Already_exist") + res);
                return false;
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * ��ȡ�ļ�
     *
     * @param beanPath �ļ���
     * @param prefix   ��ǰEnv�µù��̷��࣬��reportlets��lib��
     * @return InputStream  ������
     */
    public InputStream readBean(String beanPath, String prefix)
            throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_open");
        para.put(RemoteDeziConstants.PREFXI, prefix);
        para.put("resource", beanPath);

        HttpClient client = createHttpMethod(para);
        //        return Utils.toZipIn(execute4InputStream(method));
        //Utils.toZipIn�����bug��Զ�����ӵ�ʱ��datasource.xml���ܶ�ȡ���Ȼ�ԭ��
        return execute4InputStream(client);
    }

    /**
     * д�ļ�
     *
     * @param beanPath �ļ���
     * @param prefix   ��ǰEnv�µù��̷��࣬��reportlets��lib��
     * @return OutputStream  �����
     */
    public OutputStream writeBean(String beanPath, String prefix)
            throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fs_remote_design");
        para.put("cmd", "design_save_report");
        para.put(RemoteDeziConstants.PREFXI, prefix);
        para.put(RemoteDeziConstants.TEMPLATE_PATH, beanPath);

        return new Bytes2ServerOutputStream(para);
    }

    /**
     * �������ݿ�������
     *
     * @param selectedName ��ѡ�����ݿ���
     * @param schema       ���ݿ�ģʽ�����ڴ洢����
     * @param tableName    ��ѡ�����ݿ���
     */
    @Override
    public String[] getColumns(String selectedName, String schema, String tableName) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_columns");
        para.put("dsName", selectedName);
        para.put("schema", schema);
        para.put("tableName", tableName);

        HttpClient client = createHttpMethod2(para);
        InputStream input = execute4InputStream(client);

        if (input == null) {
            return null;
        }

        String colums = stream2String(input);
        if (StringUtils.isEmpty(colums)) {
            return null;
        }
        return colums.split("\\.");
    }

    /**
     * ����ģ���ļ�·��
     */
    @Override
    public String getWebReportPath() {
        return getPath().substring(0, getPath().lastIndexOf("/"));
    }

    @Override
    public String getProcedureText(String connectionName, String databaseName) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_procedure_text");
        para.put("procedure_name", databaseName);
        para.put("connectionName", connectionName);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return StringUtils.EMPTY;
        }
        return DavXMLUtils.readXMLProcedureText(input);
    }

    @Override
    public StoreProcedureParameter[] getStoreProcedureDeclarationParameters(String connectionName, String databaseName, String parameterDefaultValue) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_sp_parameters");
        para.put("__name__", databaseName);
        para.put("__default_value__", parameterDefaultValue);
        para.put("connectionName", connectionName);

        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return new StoreProcedureParameter[0];
        }
        return DavXMLUtils.readXMLStoreProcedureParameters(input);
    }

    /**
     * ��ȡdatasource.xml�ļ����޸ı�
     */
    public ModifiedTable getDataSourceModifiedTables(String type) {
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "get_datasource_modified_tables");
            para.put("type", type);

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            if (input == null) {
                return new ModifiedTable();
            }
            return DavXMLUtils.readXMLModifiedTables(input);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return new ModifiedTable();
    }


    /**
     * д�޸ı�
     *
     * @param modifiedTable �޸ı�
     * @param type          �������ͣ����������ӻ��Ƿ��������ݼ�
     * @return д��ɹ�����true
     */
    public boolean writeDataSourceModifiedTables(ModifiedTable modifiedTable, String type) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLModifiedTables(modifiedTable, out);
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "update_modifytable_to_server");
            para.put("type", type);

            InputStream input = postBytes2ServerB(out.toByteArray(), para);

            if (input == null) {
                return false;
            }

            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return false;
    }

    public String[] getProcedureColumns(StoreProcedure storeProcedure, java.util.Map parameterMap) throws Exception {
        String[] columns;
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp");
        HttpClient client = createHttpMethod(para);
        try {
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }

            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }

        return new String[0];
    }

    ;

    public String[] getProcedureColumns(String name) throws Exception {
        String[] columns;
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "list_sp_columns_name");
        para.put("name", name);
        HttpClient client = createHttpMethod(para);
        try {
            InputStream input = execute4InputStream(client);
            if (input == null) {
                return ArrayUtils.EMPTY_STRING_ARRAY;
            }
            columns = DavXMLUtils.readXMLSPColumns(input);
            return columns;
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new String[0];

    }

    /**
     * �����־��Ϣ
     *
     * @throws Exception
     */
    public void printLogMessage() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "get_log_message");

        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return;
        }
        LogRecordTime[] records = DavXMLUtils.readXMLLogRecords(input);
        for (LogRecordTime logRecordTime : records) {
            DesignerLogHandler.getInstance().printRemoteLog(logRecordTime);

        }
    }

    public String getUserID() {
        return userID;
    }


    //TODO:

    /**
     * Ԥ���洢����
     *
     * @param storeProcedure �洢����
     * @param parameterMap   ����map
     * @param rowCount       ����
     * @return ����ȡ���Ĵ洢����
     */
    @Override
    public ProcedureDataModel[] previewProcedureDataModel(StoreProcedure storeProcedure, Map parameterMap, int rowCount) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLFileStoreProcedureAndSource(storeProcedure, out);

        // ��parameterMapת��JSON��ʽ���ַ���
        JSONObject jo = new JSONObject(parameterMap);
        String jsonParameter = jo.toString();

        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "list_sp");
            para.put("pars", jsonParameter);

            InputStream input = postBytes2ServerB(out.toByteArray(), para);
            if (input == null) {
                return null;
            }

            TableData[] tableDatas = DavXMLUtils.readXMLTableDataArray(input);
            if (tableDatas == null || tableDatas.length == 0) {
                return new ProcedureDataModel[0];
            }
            ProcedureDataModel[] procedureDataModels = new ProcedureDataModel[tableDatas.length];
            for (int i = 0; i < tableDatas.length; i++) {
                if (tableDatas[i] instanceof EmbeddedTableData) {
                    procedureDataModels[i] = ((EmbeddedTableData) tableDatas[i]).trans2ProcedureDataModel();
                }
            }
            return procedureDataModels;


        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new ProcedureDataModel[0];
    }


    public String getAppName() {
        return "WebReport";
    }

    /**
     * �Ƿ�ΪOracle��������
     *
     * @param database ��������
     * @return �Ƿ���true
     * @throws Exception
     */
    public boolean isOracle(Connection database) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DavXMLUtils.writeXMLFileDatabaseConnection(database, out);
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_isOracle");
        InputStream input = postBytes2ServerB(out.toByteArray(), para);
        if (input == null) {
            return true;
        }
        return DavXMLUtils.readXMLBoolean(input);
    }

    public String[] getSupportedTypes() {
        return FILE_TYPE;
    }

    /**
     * ��ģ��������Ƿ�֧�����Ӵ������ļ��С���������ɾ������������ѡ��
     *
     * @return ��֧�ַ���false
     */
    public boolean isSupportLocalFileOperate() {
        return false;
    }

    /**
     * �ж��Ƿ����ļ���Ȩ��
     *
     * @param path ·��
     * @return ��Ȩ���򷵻�true
     */
    public boolean hasFileFolderAllow(String path) {
        HttpClient client = null;
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fs_remote_design");
            para.put("cmd", "design_filefolder_allow");
            para.put("current_uid", this.createUserID());
            para.put(RemoteDeziConstants.TEMPLATE_PATH, path);

            client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);

            if (input == null) {
                return false;
            }
            return Boolean.valueOf(IOUtils.inputStream2String(input, EncodeConstants.ENCODING_UTF_8));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
            return false;
        }

    }

    /**
     * �Ƿ��ǹ���Ա���
     *
     * @return ���򷵻�true
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * �Ƿ�Ϊѹ��������
     *
     * @return ���򷵻�true
     */
    @Override
    public boolean isPackDeploy() {
        return false;
    }

    @Override
    public String getDesignerVersion() throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_designer_version");
        para.put("user", user);
        para.put("password", password);

        HttpClient client = createHttpMethod(para, true);
        try {
            return stream2String(execute4InputStream(client));
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return null;
    }

    public InputStream getDataSourceInputStream(String filePath) throws Exception {
        return readBean(filePath, "datasource");
    }


    @Override
    public ArrayList getAllRole4Privilege(boolean isFS) {
        ArrayList allRoleList = new ArrayList();
        try {
            HashMap<String, String> para = new HashMap<String, String>();
            para.put("op", "fr_remote_design");
            para.put("cmd", "get_all_role");
            para.put("isFS", String.valueOf(isFS));

            HttpClient client = createHttpMethod(para);
            InputStream input = execute4InputStream(client);
            JSONArray ja = new JSONArray(stream2String(input));
            for (int i = 0; i < ja.length(); i++) {
                String roleName = (String) ((JSONObject) ja.get(i)).get("name");
                allRoleList.add(roleName);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return allRoleList;
    }

    @Override
    public String getLicName() {
        return LicUtils.FILE_NAME;
    }

    @Override
    public void setLicName(String licName) {
        //do nth
    }

    /**
     * ��ȡ��ǰenv��build�ļ�·��
     */
    public String getBuildFilePath() {
        return ResourceConstants.BUILD_PATH;
    }

    /**
     * ���õ�ǰenv��build�ļ�·��
     */
    public void setBuildFilePath(String buildFilePath) {
    }

    /**
     * ����JavaԴ���룬������ο����Ľ���
     *
     * @param sourceText Դ����
     * @return ������Ϣ���п����ǳɹ���Ϣ��Ҳ�п����ǳ�����߾�����Ϣ
     */
    public JavaCompileInfo compilerSourceCode(String sourceText) throws Exception {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_compile_source_code");
        InputStream in = postBytes2ServerB(sourceText.getBytes(EncodeConstants.ENCODING_UTF_8), para);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, EncodeConstants.ENCODING_UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jo = new JSONObject(sb.toString());
        JavaCompileInfo info = new JavaCompileInfo();
        info.parseJSON(jo);
        return info;
    }

    /**
     * ���ļ����������Ŀ¼
     *
     * @param dir    Ҫ�������ļ�
     * @param plugin ���
     */
    public void copyFilesToPluginAndLibFolder(File dir, Plugin plugin) throws Exception {

    }

    /**
     * ���ļ���ӵ�ָ��Ŀ¼����ɾ��ָ��Ŀ¼���ļ�
     *
     * @param file   ��ѹ�������ʱĿ¼
     * @param plugin ��ǰ����Ĳ��
     */
    public void movePluginEmbFile(File file, Plugin plugin) throws Exception {

    }

    /**
     * ���ļ��Ӳ��Ŀ¼ɾ��
     *
     * @param plugin Ҫɾ�����
     * @return ͬ��
     */
    public String[] deleteFileFromPluginAndLibFolder(Plugin plugin) {
        return new String[0];
    }

    /**
     * �������������ļ�
     *
     * @param plugin ���
     */
    public void writePlugin(Plugin plugin) throws Exception {

    }

    public InputStream readPluginConfig() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("op", "fr_remote_design");
        para.put("cmd", "design_get_plugin_info");

        return postBytes2ServerB(out.toByteArray(), para);
    }

    /**
     * Զ������Ȳ���Ҫ���MD5
     *
     * @return �Ƿ���ȷ
     * @throws Exception MD5�㷨�쳣
     */
    @Override
    public boolean isTruePluginMD5(Plugin plugin, File file) throws Exception {
        return true;
    }


}
