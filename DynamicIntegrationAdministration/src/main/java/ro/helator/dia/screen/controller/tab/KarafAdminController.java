package ro.helator.dia.screen.controller.tab;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ro.helator.dia.entity.Route;
import ro.helator.dia.entity.Template;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.server.Server;
import ro.helator.dia.util.BrokerConnector;
import ro.helator.dia.util.RequestType;
import ro.helator.dia.util.RouteTemplateParser;

public class KarafAdminController extends BaseScreenController {

	private static final Logger log = Logger.getLogger(KarafAdminController.class);

	private static final String ROUTE_TEMP_LIST_REQ = "<routeTemplateRequest><option>3</option></routeTemplateRequest>";

	private BrokerConnector broker;

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

		initListView();
		initTable();
		initTableContextMenu();
		initToolBar();
	}

	public void initBroker(Server server) {
		broker = new BrokerConnector(server);
		try {
			this.broker.initQueues();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void initListView() {
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
		routesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		routesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			if (newVal != null) {
				System.out.println(newVal.getId());
			} else {
				System.out.println("null");
			}
		});
	}

	private void initTable() {
		tempTypeCol.setCellValueFactory(new PropertyValueFactory<Template, String>("type"));
		tempSubtypeCol.setCellValueFactory(new PropertyValueFactory<Template, String>("subtype"));
		tempTokensCol.setCellValueFactory(new PropertyValueFactory<Template, Integer>("tokensNo"));
		tempDeployedCol.setCellValueFactory(new PropertyValueFactory<Template, Integer>("deployedNo"));
	}

	private void initTableContextMenu() {
		final ContextMenu menu = new ContextMenu();

		final MenuItem create = new MenuItem("Create route...");
		create.setOnAction(event -> {

		});
		create.visibleProperty().bind(Bindings.isNotEmpty(tempTable.getSelectionModel().getSelectedItems()));

		menu.getItems().addAll(create);
		menu.setOnHiding(event -> {
			tempTable.getSelectionModel().clearSelection();
		});
		tempTable.setContextMenu(menu);
	}

	private void initToolBar() {
		Button refresh = new Button("Refresh");
		refresh.setOnAction(event -> {
			try {
				String response = broker.request(ROUTE_TEMP_LIST_REQ, RequestType.ROUTE_TEMPLATE_LIST);
				if (response != null && !response.isEmpty()) {
					List<Template> list = RouteTemplateParser.parseResponse(response);
					if (list != null && !list.isEmpty()) {
						tempTable.getItems().clear();
						tempTable.getItems().addAll(list);
					}
				}
			} catch (JMSException e) {
				log.error(e.getMessage(), e);
			}
		});

		tempToolBar.getItems().add(refresh);
	}
}
