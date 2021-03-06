package ro.helator.dia.screen.controller;

import static ro.helator.dia.util.Constants.FXML_EXTENSION;
import static ro.helator.dia.util.Constants.FXML_SERVERS;
import static ro.helator.dia.util.Constants.FXML_TAB_FOLDER;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.wittams.gritty.swing.GrittyTerminal;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ro.helator.dia.factory.Toast;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.util.Console;

@Component
public class MainController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(MainController.class);

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane tabPane;

	@FXML
	private TextArea console;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Console.getInstance().setOutput(console);
		loadTabPane(FXML_SERVERS, FXML_SERVERS);
		Tab srvTab = getTabById(FXML_SERVERS);
		if (srvTab != null) {
			srvTab.setClosable(false);
		}
	}

	private void loadTabPane(String name, String id) {
		if (log.isTraceEnabled()) {
			log.trace(">>loadTabPane()");
		}
		try {
			Pane newLoadedPane = FXMLLoader.load(getClass().getResource(FXML_TAB_FOLDER + name + FXML_EXTENSION));
			Tab newTab = new Tab(name, newLoadedPane);
			newTab.setId(id);
			tabPane.getTabs().add(newTab);
		} catch (IOException e) {
			Toast.getErrorAlert("Could not load [" + name + "]");
			log.error("--loadTabPane(): " + e.getMessage(), e);
		}
		if (log.isTraceEnabled()) {
			log.trace("<<loadTabPane()");
		}
	}

	private Tab getTabById(String id) {
		ObservableList<Tab> tabs = tabPane.getTabs();
		for (Tab tab : tabs) {
			if (tab.getId() != null && tab.getId().equals(id)) {
				return tab;
			}
		}
		return null;
	}

	@FXML
	private void openPreferences(ActionEvent event) {
		// loadTabPane(FXML_PREFERENCES);
	}

	@FXML
	private void openServers(ActionEvent event) {
		// loadTabPane(FXML_SERVERS);
		Tab tab = getTabById(FXML_SERVERS);
		if (tab != null) {
			tabPane.getSelectionModel().select(tab);
		}
	}

	@FXML
	private void close(ActionEvent event) {
		this.sc.getStage().close();
	}

}
