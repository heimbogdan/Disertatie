package ro.helator.dia.screen.controller.tab;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import ro.helator.dia.entity.Route;
import ro.helator.dia.entity.Template;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.util.BrokerConnector;
import ro.helator.dia.util.RequestType;

public class KarafAdminController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(KarafAdminController.class);

	private static final String ROUTE_TEMP_LIST_REQ = "<routeTemplateRequest><option>3</option></routeTemplateRequest>";

	@FXML
	private AnchorPane karafAdminPane;

	@FXML
	private ListView<Route> routesListView;

	@FXML
	private ToolBar tempToolBar;

	@FXML
	private TableView<Template> tempTable;
	@FXML
	private TableColumn<Template, String> tempTypeCol;
	@FXML
	private TableColumn<Template, String> tempSubtypeCol;
	@FXML
	private TableColumn<Template, Integer> tempTokensCol;
	@FXML
	private TableColumn<Template, Integer> tempDeployedCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		routesListView.setCellFactory(list -> {
			ListCell<Route> cell = new ListCell<Route>() {
				@Override
				protected void updateItem(Route r, boolean bln) {
					super.updateItem(r, bln);
					if (r != null) {
						setText(r.getId());
					}
				}
			};
			return cell;
		});
		// routesListView.getItems().add(new Route("Test"));
		// routesListView.getItems().add(new Route("Test2"));
		routesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		routesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			if (newVal != null) {
				System.out.println(newVal.getId());
			} else {
				System.out.println("null");
			}
		});

		tempTypeCol.setCellValueFactory(new PropertyValueFactory<Template, String>("type"));
		tempSubtypeCol.setCellValueFactory(new PropertyValueFactory<Template, String>("subtype"));
		tempTokensCol.setCellValueFactory(new PropertyValueFactory<Template, Integer>("tokensNo"));
		tempDeployedCol.setCellValueFactory(new PropertyValueFactory<Template, Integer>("deployedNo"));

		Properties tokens = new Properties();
		try {
			tokens.load(new StringBufferInputStream("test=123\ntest1=as"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tempTable.getItems().add(new Template("JMS", "Simple", 5, tokens));

		Button refresh = new Button("Refresh");
		refresh.setOnAction(event -> {
			try {
				String response = BrokerConnector.getInstance().request(ROUTE_TEMP_LIST_REQ, RequestType.ROUTE_TEMPLATE_LIST);
				if(response != null && !response.isEmpty()){
					System.out.println(response);
				}
			} catch (JMSException e) {
				log.error(e.getMessage(), e);
			}
		});

		tempToolBar.getItems().add(refresh);
	}

}
