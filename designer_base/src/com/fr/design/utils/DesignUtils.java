package com.fr.design.utils;

import com.fr.base.*;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.dav.DavXMLUtils;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.UILookAndFeel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.env.RemoteEnv;
import com.fr.file.FileFILE;
import com.fr.general.*;
import com.fr.general.http.HttpClient;
import com.fr.stable.*;
import com.fr.start.StartServer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.Level;


/**
 * Some util method of Designer
 */
public class DesignUtils {
    private static final int MESSAGEPORT = 51462;
    private static final int TIME_OUT = 20 * 1000;

    public synchronized static void setPort(int port) {
        DesignUtils.port = port;
    }

    private static int port = MESSAGEPORT;

    private DesignUtils() {

    }


    /**
     * ͨ���˿��Ƿ�ռ���ж��������û������
     *            s
     * @return �����˷���true
     */
    public static boolean isStarted() {
        try {
            new Socket("localhost", port);
            return true;
        } catch (Exception exp) {

        }
        return false;
    }

    /**
     * ����������������У����������˴���
     *
     * @param lines ������
     */
    public static void clientSend(String[] lines) {
        if (lines != null && lines.length <= 0) {
            return;
        }
        Socket socket = null;
        PrintWriter writer = null;
        try {
            socket = new Socket("localhost", port);

            writer = new PrintWriter(socket.getOutputStream());
            for (int i = 0; i < lines.length; i++) {
                writer.println(lines[i]);
            }

            writer.flush();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        } finally {
            try {
                writer.close();
                socket.close();
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     *���������˿�
     * @param startPort  �˿�
     * @param suffixs �ļ���׺
     */
    public static void creatListeningServer(final int startPort,final String[] suffixs) {
        Thread serverSocketThread = new Thread() {
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(startPort);
                } catch (IOException e1) {
                    FRLogger.getLogger().log(Level.WARNING, "Cannot create server socket on" + port);
                }
                while (true) {
                    try {
                        Socket socket = serverSocket.accept(); // ���տͻ�����
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("demo")) {
                                StartServer.browerDemoURL();
                            } else if (!StringUtils.isEmpty(line)) {
                                File f = new File(line);
                                String path = f.getAbsolutePath();

                                boolean isMatch = false;
                                for(int i= 0; i<suffixs.length;i++){
                                    isMatch = isMatch || path.endsWith(suffixs[i]);
                                }
                                if (isMatch) {
                                    DesignerContext.getDesignerFrame().openTemplate(new FileFILE(f));
                                }
                            }
                        }

                        reader.close();
                        socket.close();
                    } catch (IOException e) {

                    }
                }
            }
        };
        serverSocketThread.start();
    }

    /**
     * �����Ի���,��ʾ����
     *
     * @param message ������Ϣ
     */
    public static void errorMessage(String message) {
        final String final_msg = message;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), final_msg);
            }
        });
    }


    /**
     * ��ǰ�ı������л����л���env
     *
     * @param env ��Ҫ�л�ȥ�Ļ���
     */
    public static void switchToEnv(Env env) {
        if (env == null) {
            return;
        }

        Env oldEnv = FRContext.getCurrentEnv();
        String oldEnvPath = oldEnv == null ? null : oldEnv.getPath();

        // ��һ�����env��DesignerEnvManager������û�ж�Ӧ��,�еĻ���setCurrentEnvName
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        java.util.Iterator<String> nameIt = envManager.getEnvNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            if (ComparatorUtils.equals(envManager.getEnv(name), env)) {
                envManager.setCurEnvName(name);
                break;
            }
        }

        // ����CurrentEnv��FRContext & DesignerEnvManager
        FRContext.setCurrentEnv(env);

        final Env run_env = env;

        // ˢ��DesignerFrame��������
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (DesignerContext.getDesignerFrame() == null) {
                    return;
                }
                DesignerContext.getDesignerFrame().refreshEnv(run_env);
                DesignerContext.getDesignerFrame().repaint();// kunsnat: �л������� ˢ���� ����. ����ͼ��ĳЩ���ı�.
            }
        });
        // ���������л���,���÷������������´�Ԥ��ʱ����
        if (env instanceof LocalEnv && !ComparatorUtils.equals(env.getPath(), oldEnvPath)) {
            StartServer.currentEnvChanged();
        }
    }

    /**
     * p:��ʼ��look and feel, ��һ�зŵ��������.�����ö���ط�����.
     */
    public static void initLookAndFeel() {
        // p:���ضԻ����ϵͳ��������look and feel����ı�����.
        try {
            UIManager.setLookAndFeel(UILookAndFeel.class.getName());
        } catch (Exception e) {
            FRLogger.getLogger().log(Level.WARNING, "Substance Raven Graphite failed to initialize");
        }
        //��ȡ��ǰϵͳ������������õ�Ĭ������
        FRFont guiFRFont = getCurrentLocaleFont();
        //ָ��UIManager������
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();

            if (key.endsWith(".font")) {
                UIManager.put(key, isTextField(key) ? getNamedFont("Dialog") : guiFRFont);
            }
        }
    }
    
    private static boolean isTextField(String key){
    	return key.startsWith("TextField.") || key.startsWith("PasswordField.");
    }
    
    private static FRFont getCurrentLocaleFont(){
        FRFont guiFRFont;
        Locale defaultLocale = Locale.getDefault();
        
        if (isDisplaySimSun(defaultLocale)) {
            guiFRFont = getNamedFont("SimSun");
        } else if(isDisplayDialog(defaultLocale)) {
            guiFRFont = getNamedFont("Dialog");
        } else {
            guiFRFont = getNamedFont("Tahoma");
        }
        
        //�ȳ�ʼ���������locale, ���ʼ��lookandfeel.���˳�����, ���ҲҪ����.
        Locale designerLocale = FRContext.getLocale();
        String file = Inter.getLocText("FR-Designer_File");
        char displayChar = file.charAt(0);
        if (!guiFRFont.canDisplay(displayChar)) {
			//���������Ĭ�ϵ�������ʾ����, ��������Ӣ��ϵͳ�������������
        	//Ĭ������(����:����, Ӣ��:Tahoma, ����:Dialog)
        	guiFRFont = getNamedFont("SimSun");
        	if (!guiFRFont.canDisplay(displayChar)) {
				FRContext.getLogger().error(Inter.getLocText("FR-Base_SimSun_Not_Found"));
			}
		}
        
        return guiFRFont;
    }
    
    private static FRFont getNamedFont(String name){
    	return FRFont.getInstance(name, Font.PLAIN, 12);
    }
    
    private static boolean isDisplaySimSun(Locale defaultLocale){
    	return ComparatorUtils.equals(defaultLocale, Locale.SIMPLIFIED_CHINESE);
    }
    
    private static boolean isDisplayDialog(Locale defaultLocale){
    	return ComparatorUtils.equals(defaultLocale, Locale.TRADITIONAL_CHINESE) 
    			|| ComparatorUtils.equals(defaultLocale, Locale.JAPANESE)
                || ComparatorUtils.equals(defaultLocale, Locale.JAPAN) 
                || ComparatorUtils.equals(defaultLocale, Locale.KOREAN);
    }

    /**
     * ���ʷ���������
     *
     * @param names  ��������
     * @param values ����ֵ
     */
    public static void visitEnvServerByParameters(String[] names, String[] values) {
        int len = Math.min(ArrayUtils.getLength(names), ArrayUtils.getLength(values));
        String[] segs = new String[len];
        for (int i = 0; i < len; i++) {
            try {
                //����������˵Ϊ�˸�ʲô����ͳһ, �ѷָ���ͳһ��File.separator, ��ζ����windows���汨��·�������\
                //��ǰ�ĳ���, �Լ�Ԥ��urlʲô�Ķ���/, ��Ʒ�����˼�����õ��ĵط��滻��, �����.
                String value = values[i].replaceAll("\\\\", "/");
                segs[i] = URLEncoder.encode(CodeUtils.cjkEncode(names[i]), EncodeConstants.ENCODING_UTF_8) + "=" + URLEncoder.encode(CodeUtils.cjkEncode(value), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        String postfixOfUri = (segs.length > 0 ? "?" + StableUtils.join(segs, "&") : StringUtils.EMPTY);

        if (FRContext.getCurrentEnv() instanceof RemoteEnv) {
            try {
                if (Utils.isEmbeddedParameter(postfixOfUri)) {
                    String time = Calendar.getInstance().getTime().toString().replaceAll(" ", "");
                    boolean isUserPrivilege = ((RemoteEnv) FRContext.getCurrentEnv()).writePrivilegeMap(time, postfixOfUri);
                    postfixOfUri = isUserPrivilege ? postfixOfUri + "&fr_check_url=" + time + "&id=" + FRContext.getCurrentEnv().getUserID(): postfixOfUri ;
                }

                Desktop.getDesktop().browse(new URI(FRContext.getCurrentEnv().getPath() + postfixOfUri));
            } catch (Exception e) {
                FRContext.getLogger().error("cannot open the url Successful", e);
            }
        } else {
            try {
                String web = GeneralContext.getCurrentAppNameOfEnv();
                String url = "http://localhost:" + DesignerEnvManager.getEnvManager().getJettyServerPort() + "/" + web + "/" + ConfigManager.getProviderInstance().getServletMapping()
                        + postfixOfUri;
                StartServer.browerURLWithLocalEnv(url);
            } catch (Throwable e) {
                //
            }
        }
    }

    //TODO:august:�¸��汾��Ҫ�������ͼƬ������һ��preload�ļ����£���ʾ����Ԥ�ȼ��ء�Ȼ�����һ�¾Ϳ����ˣ�������ôһ��һ����д��

    /**
     * Ԥ����
     */
    public static void preLoadingImages() {
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/baobiaozhuti_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/canshujiemian_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/setting_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/page_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/form_click.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit_hover.png");
        BaseUtils.readIcon("com/fr/design/images/custombtn/edit_click.png");
        BaseUtils.readIcon("com/fr/base/images/oem/addworksheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/addpolysheet.png");
        BaseUtils.readIcon("com/fr/base/images/oem/worksheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
        BaseUtils.readIcon("com/fr/design/images/sheet/left_right_btn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/cellPop.png");
        BaseUtils.readIcon("/com/fr/design/images/docking/right.gif");
        BaseUtils.readIcon("/com/fr/design/images/docking/left.gif");
        BaseUtils.readIcon("/com/fr/design/images/m_file/save.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/word.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/svg.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/csv.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/text.png");
        BaseUtils.readIcon("/com/fr/design/images/m_web/datasource.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/webreportattribute.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/pageSetup.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/header.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/footer.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/saveAs.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/background.png");
        loadOtherImages();
    }


    private static void loadOtherImages() {
        BaseUtils.readIcon("/com/fr/design/images/m_report/reportWriteAttr.png");
        BaseUtils.readIcon("/com/fr/design/images/m_report/linearAttr.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/bindColumn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/text.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/bias.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/subReport.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/insertRow.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/insertColumn.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/highlight.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/hyperLink.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/merge.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/unmerge.png");
        BaseUtils.readIcon("/com/fr/design/images/m_file/export.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/cell.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/float.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/undo.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/redo.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png");
        BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/leftAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/centerAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/rightAlignment.png");
        BaseUtils.readIcon("/com/fr/design/images/m_format/noboder.png");
        BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png");
        BaseUtils.readIcon("/com/fr/design/images/m_insert/floatPop.png");
    }


    /**
     * ���û�����������������
     *
     * @param feedBackInfo �û�����
     * @return ���ͳɹ�����true
     * @throws Exception �쳣
     */
    public static boolean sendFeedBack(FeedBackInfo feedBackInfo) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ��tableDataд��xml�ļ���out
        DavXMLUtils.writeXMLFeedBackInfo(feedBackInfo, out);
        InputStream input = postBytes2ServerB(out.toByteArray());
        return input != null;

    }


    private static InputStream postBytes2ServerB(byte[] bytes) throws Exception {
    	HttpClient client = new HttpClient("http://114.215.175.35:8080/WebReport/product_advice.jsp");
        client.asGet();
    	client.setContent(bytes);
    	return execute4InputStream(client);
    }


    /**
     * execute method֮��,ȡ���ص�inputstream
     */
    private static ByteArrayInputStream execute4InputStream(HttpClient client) throws Exception {
        int statusCode = client.getResponseCode();
        if(statusCode != HttpURLConnection.HTTP_OK){
        	throw new EnvException("Method failed: " + statusCode);
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
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer_file-is-locked"));
                return null;
            } else if (message.startsWith(RemoteDeziConstants.RUNTIME_ERROR_PREFIX)) {
            }
            return new ByteArrayInputStream(bytes);
        } finally {
            in.close();
            out.close();
            client.release();
        }
    }


}
