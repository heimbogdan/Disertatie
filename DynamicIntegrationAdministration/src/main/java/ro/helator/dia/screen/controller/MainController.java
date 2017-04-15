package ro.helator.dia.screen.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ro.helator.dia.factory.Toast;
import ro.helator.dia.screen.BaseScreenController;
import static ro.helator.dia.util.Constants.*;

@Component
public class MainController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(MainController.class);
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane tabPane;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
	
	private void loadTabPane(String name){
		if(log.isTraceEnabled()){
			log.trace(">>loadTabPane()");
		}
		try {
			Pane newLoadedPane = FXMLLoader.load(getClass().getResource(
					FXML_TAB_FOLDER + name + FXML_EXTENSION));
			Tab newTab = new Tab(name, newLoadedPane);
			tabPane.getTabs().add(newTab);
		} catch (IOException e) {
			Toast.getErrorAlert("Could not load [" + name + "]");
			log.error("--loadTabPane(): " + e.getMessage(), e);
		}
		if(log.isTraceEnabled()){
			log.trace("<<loadTabPane()");
		}
	}
	
	@FXML
	private void openPreferences(ActionEvent event){
		loadTabPane(FXML_PREFERENCES);
	}
	
	@FXML
	private void openServers(ActionEvent event){
		loadTabPane(FXML_SERVERS);
	}
	
	@FXML
	private void close(ActionEvent event){
		this.sc.getStage().close();
	}

}