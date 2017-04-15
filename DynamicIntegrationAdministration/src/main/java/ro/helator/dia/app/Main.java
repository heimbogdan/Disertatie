package ro.helator.dia.app;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.util.component.ApiMethodHelper.MatchType;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.beans.factory.config.YamlProcessor.MatchStatus;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.DisconnectReason;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.TransportException;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.echo.EchoAdapters;
import net.sf.expectit.echo.EchoOutput;
import ro.helator.dia.config.AppContextConfig;
import ro.helator.dia.screen.ScreensContoller;
import ro.helator.dia.server.Server;

import static net.sf.expectit.matcher.Matchers.regexp;
/**
 * 
 * @author bogdan.heim
 *
 */
public class Main extends Application {

	private static final Logger log = Logger.getLogger(Main.class);

	@SuppressWarnings("resource")
	@Override
	public void start(Stage stage) {
		try {
			PropertyConfigurator.configure("./config/log.properties");
			log.info("Application started!");
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppContextConfig.class);
			ScreensContoller bean = context.getBean(ScreensContoller.class);
			bean.init(stage);
			bean.loadScreen("/fxml/Main.fxml");
		} catch (Exception e) {
			log.error(e);

		}
	}

	public static void main(String[] args) {
		// try {
		// File servers = new File("config/servers/servers.bin");
		// servers.createNewFile();
		// FileOutputStream fos = new FileOutputStream(servers);
		// ObjectOutputStream oos = new ObjectOutputStream(fos);
		// List<Server> list = new ArrayList<Server>();
		// list.add(new Server("server1", "172.168.23.11", "22", "root", "pass",
		// "/var/karaf/"));
		// list.add(new Server("server2", "172.168.23.12", "22", "root", "pass",
		// "/var/karaf/"));
		// oos.writeObject(list);
		// oos.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

//		SSHClient ssh = new SSHClient();
//		try {
//			try {
//				ssh.connect("gbib.hq.edata.ro");
//			} catch (TransportException e) {
//				if (e.getDisconnectReason() == DisconnectReason.HOST_KEY_NOT_VERIFIABLE) {
//					String msg = e.getMessage();
//					String[] split = msg.split("`");
//					String vc = split[3];
//					ssh = new SSHClient();
//					ssh.addHostKeyVerifier(vc);
//					ssh.connect("gbib.hq.edata.ro");
//				} else {
//					throw e;
//				}
//			}
//
//			AnsiConsole.systemInstall();
//			// client.authPublickey(server.getUser());
//			ssh.authPassword("root", "password");
//			Session session = ssh.startSession();
//			session.allocateDefaultPTY();
//			Shell shell=session.startShell();
//			EchoOutput a = new EchoOutput() {
//				
//				@Override
//				public void onSend(String paramString) throws IOException {
////					System.out.println(paramString);
//					
//				}
//				
//				@Override
//				public void onReceive(int paramInt, String paramString) throws IOException {
//					System.out.println(paramString);
//					
//				}
//			};
//			Expect expect = new ExpectBuilder().withEchoOutput(a)
//			        .withOutput(shell.getOutputStream())
//			        .withInputs(shell.getInputStream(), shell.getErrorStream())
//			        .build();
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			String s = null;
//			expect.expect(regexp("(#|\\$|>)\\s?$"));
//			while((s = br.readLine()) != null){
//				if(s.equals("exit")){
//					break;
//				}
//				expect.sendLine(s);
//				expect.expect(regexp("(#|\\$|>)\\s?$"));
//			}
			
//			System.out.println(expect.expect(regexp("#\\s$")).getBefore());
			// Command cmd = session.exec("cd /var/mqsi/");
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			// cmd.join(5, TimeUnit.SECONDS);
			// System.out.println("\n** exit status: " + cmd.getExitStatus());
			// cmd.close();
			// session = ssh.startSession();
			// cmd = session.exec("ls");
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			AnsiConsole.systemUninstall();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 launch(args);
	}

	@Override
	public void stop() throws Exception {
		log.info("exit");
		System.out.println("exit");
		Platform.exit();
		System.exit(0);
	}
}