package ro.helator.dia.screen.controller.tab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ro.helator.dia.screen.BaseScreenController;

import static ro.helator.dia.util.Constants.*;

@Component
public class PreferencesController extends BaseScreenController {

	@FXML
	private TreeView<String> preferencesTree;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("test");

		File preferences = new File(PREFERENCES_FOLDER);
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		if (preferences.isDirectory()) {
			File[] list = preferences.listFiles();
			Gson gson = new Gson();
			for (File f : list) {
//				File f = new File(file);
				if (f.isFile()) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(f));
						String line = null;
						StringBuilder sb = new StringBuilder();
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}
						br.close();

						String json = sb.toString();
						Map<String, Object> map = new HashMap<String, Object>();
						map = (Map<String, Object>) gson.fromJson(json, map.getClass());
						prefList.add(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			TreeItem<String> pref = new TreeItem<String>("Preferences");
			pref.setExpanded(true);

			for (Map<String, Object> map : prefList) {
				pref.getChildren().add(buildTreeItem(map, null));
			}
			preferencesTree.setRoot(pref);
		}

	}

	private TreeItem<String> buildTreeItem(Map<String, Object> map, TreeItem<String> item) {
		String id = (String) map.get("id");
		String desc = (String) map.get("description");
		TreeItem<String> newItem = new TreeItem<String>(id);
		if (item != null) {
			item.getChildren().add(newItem);
		} else {
			item = newItem;
		}

		return item;
	}

	private AnchorPane buildAnchorPane(String desc, List<Object> children) {
		AnchorPane pane = new AnchorPane();
		TextField descTF = new TextField(desc);
		pane.getChildren().add(descTF);
		setAnchorLayout(descTF, 30.0, null, 30.0, null);
		return pane;
	}

	private void setAnchorLayout(Node node, Double top, Double bottom, Double left, Double right) {
		if (top != null) {
			AnchorPane.setTopAnchor(node, top);
		}
		if (bottom != null) {
			AnchorPane.setBottomAnchor(node, bottom);
		}
		if (left != null) {
			AnchorPane.setLeftAnchor(node, left);
		}
		if (right != null) {
			AnchorPane.setRightAnchor(node, right);
		}
	}
}
