package ro.helator.dia.screen.controller.tab;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.terminalfx.config.TerminalConfig;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import ro.helator.dia.screen.BaseScreenController;


@Component
public class SSHConsoleController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(SSHConsoleController.class);
	
	private TerminalConfig terminalConfig = new TerminalConfig();
	private LinkedBlockingQueue<String> commandQueue = new LinkedBlockingQueue<String>();
	private WebView webView = new WebView();
	
	@FXML
	private AnchorPane sshConsolePane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.terminalConfig.setBackgroundColor(Color.rgb(16, 16, 16));
		this.terminalConfig.setForegroundColor(Color.rgb(240, 240, 240));
		this.terminalConfig.setCursorColor(Color.rgb(255, 0, 0, 0.5D));
//		this.webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			sshConsolePane.getParent().getScene().getWindow().setMember("app", this);
//		});
	}
	
	

}
