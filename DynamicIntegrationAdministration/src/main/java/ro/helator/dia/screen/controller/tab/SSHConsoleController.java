package ro.helator.dia.screen.controller.tab;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import ro.helator.dia.screen.BaseScreenController;


@Component
public class SSHConsoleController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(SSHConsoleController.class);
	
	@FXML
	private AnchorPane sshConsolePane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}