package ro.helator.dia.screen.controller.tab;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.server.Server;

@Component
public class ConsoleController extends BaseScreenController {

	@FXML
	private AnchorPane consolePane;
	
	@FXML
	private TextArea consoleTA;
	
	private SSHClient client;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

	}
	
	public void initSSH(Server server){
		try {
			client.connect(server.getIp());
//			client.authPublickey(server.getUser());
			client.authPassword(server.getUser(), server.getPassword());
			final Session session = client.startSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
