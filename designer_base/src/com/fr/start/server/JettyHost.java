package com.fr.start.server;

import java.awt.SystemTray;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fr.general.GeneralContext;
import com.fr.stable.ProductConstants;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.jetty.servlet.XMLConfiguration;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.start.StartServer;

public class JettyHost {

	private Server server;
	private MultiOutputStream multiOutputStream = null;
	private File outLogFile = null;
	private int currentPort = -1;

	// ���÷�����һ���˿���������ж��Ӧ��,����content��������
	private Map<String, WebApplicationContext> webAppsMap = new HashMap<String, WebApplicationContext>();

	private List<JettyServerListener> listenerList = new ArrayList<JettyServerListener>();

	private boolean isDemoAppLoaded = false;

	public JettyHost(int port) {
		this.currentPort = port;
		initServer();

		initLogFileAndOutputStream();

		// TODO: ��HostJettyServer�ŵ�ServerTray��ȥ
		tryStartServerTray();
	}

	private void initServer() {
		// alex:������仰�Ļ�,jetty�޷����ճ���200k�Ĳ���
		System.setProperty("org.mortbay.http.HttpRequest.maxFormContentSize", "-1");
		try {
			// jetty server�������ļ�
			this.server = new Server("jetty.xml");
		} catch (IOException e) {
			// ���û�������ļ�����ô����Ĭ�ϵİ�
			this.server = new Server();
			SocketListener listener = new SocketListener();
			listener.setPort(this.currentPort);
			this.server.addListener(listener);
			this.server.setWebApplicationConfigurationClassNames(new String[] { XMLConfiguration.class.getName() });
		}
	}

	private void initLogFileAndOutputStream() {
		// log�ļ����õ�λ��
		File logDir = null;
		String installHome = StableUtils.getInstallHome();
		if (installHome == null) {// û��installHome��ʱ�򣬾ͷŵ�user.home�����
			logDir = new File(ProductConstants.getEnvHome() + File.separator + ProjectConstants.LOGS_NAME);
		} else {
			// james��logs���ڰ�װĿ¼����
			logDir = new File(installHome + File.separator + ProjectConstants.LOGS_NAME + File.separator + "jetty");
		}
		StableUtils.mkdirs(logDir);
		DateFormat fateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar curCalendar = Calendar.getInstance();
		outLogFile = new File(logDir, "jetty_" + fateFormat.format(curCalendar.getTime()) + ".log");

		try {
			multiOutputStream = new MultiOutputStream();
			multiOutputStream.addOutputStream(new FileOutputStream(outLogFile, true));
			multiOutputStream.addOutputStream(System.out);
			System.setErr(new PrintStream(multiOutputStream));
			System.setOut(new PrintStream(multiOutputStream));
		} catch (IOException ioe) {
			FRContext.getLogger().error(ioe.getMessage(), ioe);
		}
	}

	private synchronized void addWebApplication(String context, String webappsPath) {
		try {
			FRContext.getLogger().info("The new  Application Path is: \n" + webappsPath + ", it will be added.");
			if (webAppsMap.get(context) != null) {
				WebApplicationContext webapp = webAppsMap.remove(context);
				try {
					webapp.stop();
					webapp.destroy();
				} catch (Exception e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
			}
			WebApplicationContext webapp = this.getServer().addWebApplication(context, webappsPath);
			webAppsMap.put(context, webapp);
		} catch (IOException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
	}

	private void addAndStartWebApplication(String context, String webAppPath) {
		addWebApplication(context, webAppPath);

		WebApplicationContext webapp = webAppsMap.get(context);
		try {
			if (!webapp.isStarted()) {
				webapp.start();
			}
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
	}

	/**
	 * Get MultiOutputStream.
	 */
	public MultiOutputStream getMultiOutputStream() {
		return this.multiOutputStream;
	}

	/**
	 * Get out log file
	 */
	public File getOutLogFile() {
		return this.outLogFile;
	}

	private Server getServer() {
		if (server == null) {
			initServer();
		}

		return server;
	}

	/**
	 * Start
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		getServer().start();

		for (int i = 0; i < listenerList.size(); i++) {
			JettyServerListener listener = this.getLinstener(i);
			listener.started(this);
		}

		FRContext.getLogger().info(Inter.getLocText("LOG-Report_Server_IS_Started"));
	}

	/**
	 * Stop
	 * 
	 * @throws Exception
	 */
	public void stop() {
		try {
			getServer().stop();
		} catch (InterruptedException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		for (int i = 0; i < listenerList.size(); i++) {
			JettyServerListener listener = this.getLinstener(i);
			listener.stopped(this);
		}

		getServer().destroy();
		StartServer.currentEnvChanged();
		server = null;//����server
	}

	/**
	 * Is started
	 * 
	 * @throws Exception
	 */
	public boolean isStarted() throws Exception {
		return getServer().isStarted();
	}

	public void addListener(JettyServerListener listener) {
		this.listenerList.add(listener);
	}

	public int getLinstenerCount() {
		return this.listenerList.size();
	}

	public JettyServerListener getLinstener(int index) {
		if (index < 0 || index >= this.getLinstenerCount()) {
			return null;
		}

		return this.listenerList.get(index);
	}

	public void clearLinsteners() {
		this.listenerList.clear();
	}

	/**
	 * ��������ϵͳ����
	 */
	private void tryStartServerTray() {
		if (SystemTray.isSupported()) {
			new ServerTray(this);
		} else {
			FRContext.getLogger().error("Do not support the SystemTray!");
		}
	}

	public void exit() {
		try {
			getServer().stop();
		} catch (InterruptedException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		for (int i = 0; i < listenerList.size(); i++) {
			JettyServerListener listener = this.getLinstener(i);
			listener.exited(this);
		}

		getServer().destroy();
		StartServer.currentEnvChanged();

	}

	public int getCurrentPort() {
		return currentPort;
	}

	/**
	 * ��װĿ¼�µ�Ĭ�ϵ�WebReport�����ִֻ��һ��,����Ԥ��demo�������Ĳ�Ҫ�����������
	 */
	public void addAndStartInstallHomeWebApp() {
		if (!isDemoAppLoaded) {
			String installHome = StableUtils.getInstallHome();
			String webApplication = StableUtils.pathJoin(new String[]{installHome, ProjectConstants.WEBAPP_NAME});

			if (new File(webApplication).isDirectory()) {
				addAndStartWebApplication("/" + ProjectConstants.WEBAPP_NAME, webApplication);
			}
		}
		isDemoAppLoaded = true;
	}

	/**
	 * ����Env�µı������л���
	 */
	public void addAndStartLocalEnvHomeWebApp() {
		String name = DesignerEnvManager.getEnvManager().getCurEnvName();
		if (name.equals(Inter.getLocText("Default"))) {
			isDemoAppLoaded = true;
		}
		Env env = FRContext.getCurrentEnv();
		if (env instanceof LocalEnv) {
			String webApplication = new File(env.getPath()).getParent();
			FRContext.getLogger().info(Inter.getLocText("INFO-Reset_Webapp") + ":" + webApplication);
			addAndStartWebApplication("/" + GeneralContext.getCurrentAppNameOfEnv(), webApplication);
		}
	}

	public boolean isDemoAppLoaded() {
		return isDemoAppLoaded;
	}

}