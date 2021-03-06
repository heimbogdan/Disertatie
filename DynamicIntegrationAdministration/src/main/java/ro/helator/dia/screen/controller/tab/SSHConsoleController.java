package ro.helator.dia.screen.controller.tab;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.wittams.gritty.jsch.JSchTty;
import com.wittams.gritty.swing.GrittyTerminal;
import com.wittams.gritty.swing.TermPanel;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.server.Server;

@Component
public class SSHConsoleController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(SSHConsoleController.class);

	private GrittyTerminal terminal;
	private SwingNode node = new SwingNode();

	private Server server;

	@FXML
	private AnchorPane sshConsolePane;

	@FXML
	private ToolBar sshToolbar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.terminal = new GrittyTerminal();
		TermPanel termPanel = this.terminal.getTermPanel();
		termPanel.setVisible(true);
		this.terminal.setVisible(true);
		node.setContent(terminal);
		setfullAnchor(node);

		sshConsolePane.getChildren().add(node);
		Button item = new Button("Connect");
		item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				SwingUtilities.invokeLater(() -> {
					if (!(terminal.isSessionRunning())) {
						terminal
								.setTty(new JSchTty(server.getFullHostname(), server.getUser(), server.getPassword()));
						terminal.start();
					}
				});
			}
		});
		
		sshToolbar.getItems().add(item);
	}

	public void openSession(Server server) {
		this.server = server;
	}

	private void setfullAnchor(Node node) {
		AnchorPane.setTopAnchor(node, 30.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
	}
	
	
}
