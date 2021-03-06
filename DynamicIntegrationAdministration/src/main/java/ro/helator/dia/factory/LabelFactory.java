package ro.helator.dia.factory;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * 
 * @author bogdan.heim
 *
 */
public class LabelFactory {

	private LabelFactory(){};
	
	public static Label getCenteredLabel(String message){
		Label label = new Label(message);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		label.setLayoutX(20);
		label.setLayoutY(5);
		return label;
	}
	
	public static Label getCenteredLabel(String message, Color textColor, Font font){
		Label label = getCenteredLabel(message);
		label.setTextFill(textColor);
		label.setFont(font);
		return label;
	}
}
