package ro.helator.dia.screen.controller.tab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import ro.helator.dia.factory.PopupFactory;
import ro.helator.dia.screen.BaseScreenController;
import ro.helator.dia.server.Server;

@Component
public class ServersController extends BaseScreenController {

	@FXML
	private AnchorPane serversPane;

	@FXML
	private TableView<Server> serversTable;
	@FXML
	private TableColumn<Server, String> nameTCol;
	@FXML
	private TableColumn<Server, String> ipTCol;
	@FXML
	private TableColumn<Server, String> portTCol;
	@FXML
	private TableColumn<Server, String> userTCol;
	@FXML
	private TableColumn<Server, String> passwordTCol;
	@FXML
	private TableColumn<Server, String> karafPathTCol;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		File serversFile = new File("config/servers/servers.bin");
		List<Server> list = null;
		if (serversFile.exists()) {
			try {
				FileInputStream fin = new FileInputStream(serversFile);
				ObjectInputStream ois = new ObjectInputStream(fin);
				list = (List<Server>) ois.readObject();
				ois.close();
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		initTable(list);
	}

	public void initTable(List<Server> list) {
		initTableColumn(nameTCol, "name");
		initTableColumn(ipTCol, "ip");
		initTableColumn(portTCol, "port");
		initTableColumn(userTCol, "user");
		initTableColumn(passwordTCol, "password");
		initTableColumn(karafPathTCol, "karafPath");
		populateTable(list);
		initContextMenu();
	}

	private void initContextMenu() {
		final ContextMenu menu = new ContextMenu();

		final MenuItem connect = new MenuItem("Connect");
		connect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		connect.visibleProperty().bind(Bindings.isNotEmpty(serversTable.getSelectionModel().getSelectedItems()));

		final MenuItem fileTransfer = new MenuItem("File Transfer");
		fileTransfer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});
		fileTransfer.visibleProperty().bind(Bindings.isNotEmpty(serversTable.getSelectionModel().getSelectedItems()));

		final MenuItem add = new MenuItem("Add...");
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Dialog<Server> dialog = PopupFactory.newOKCancelFormDialog(new Server(), "Add new server");
				dialog.initOwner(serversPane.getScene().getWindow());
				dialog.initModality(Modality.APPLICATION_MODAL);
				Optional<Server> result = dialog.showAndWait();
				Server s = result.isPresent() ? result.get() : null;
				if (s != null) {
					ObservableList<Server> data = serversTable.getItems();
					data.add(s);
					List<Server> servers = new ArrayList<Server>();
					for (Server server : data) {
						servers.add(server);
					}
					saveList(servers);
				}
			}
		});

		final MenuItem delete = new MenuItem("Delete");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				serversTable.getItems().removeAll(serversTable.getSelectionModel().getSelectedItems());
				saveList();
			}
		});
		delete.visibleProperty().bind(Bindings.isNotEmpty(serversTable.getSelectionModel().getSelectedItems()));

		menu.getItems().addAll(connect, fileTransfer, add, delete);

		menu.setOnHiding(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				serversTable.getSelectionModel().clearSelection();

			}
		});
		serversTable.setContextMenu(menu);
	}

	private <S, T> void initTableColumn(TableColumn<S, T> column, String propertyName) {
		column.setCellValueFactory(new PropertyValueFactory<S, T>(propertyName));
		column.setEditable(false);
	}

	private void populateTable(List<Server> list) {
		ObservableList<Server> data = serversTable.getItems();
		data.clear();
		for (Server server : list) {
			data.add(server);
		}
	}

	private void saveList(){
		ObservableList<Server> data = serversTable.getItems();
		List<Server> servers = new ArrayList<Server>();
		for (Server server : data) {
			servers.add(server);
		}
		saveList(servers);
	}
	private void saveList(List<Server> list) {
		try {
			File servers = new File("config/servers/servers.bin");
			servers.createNewFile();
			FileOutputStream fos = new FileOutputStream(servers);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}