/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.helator.dia.screen;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

/**
 * 
 * @author bogdan.heim
 *
 */
public abstract class BaseScreenController implements Initializable, BeanNameAware {
	protected String screenId;
	@Autowired
	protected ScreensContoller sc;
	protected Parent root;

	@FXML
	protected Label screenName;

	public Parent getRoot() {
		return root;
	}

	public void setRoot(Parent root) {
		this.root = root;
	}

	@Override
	public void setBeanName(String name) {
		this.screenId = name;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public ScreensContoller getScreen() {
		return this.sc;
	}

	public void loadStyle(String styleFileName) {
		this.sc.getStage().getScene().getStylesheets()
				.add(getClass().getClassLoader().getResource("styles/" + styleFileName + ".css").toExternalForm());
	}

	public void clearStyles() {
		this.sc.getStage().getScene().getStylesheets().clear();
	}

	public void clearData() {

	}
}
