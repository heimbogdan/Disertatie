package ro.helator.dia.factory;

import org.apache.log4j.Logger;

import com.sun.javafx.tk.Toolkit;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ro.helator.dia.util.ScreenUtils;

/**
 *
 * @author bogdan.heim
 *
 */
@SuppressWarnings("restriction")
public class Toast {

	public static final int SHORT = 2000;
	public static final int NORMAL = 3000;
	public static final int LONG = 5000;
	
	public static final String SYSTEM_ERROR = "An error occoured! Please contact an administrator!";
	
	private Toast() {
	};

	public static void showWarning(Pane pane, String message, Logger log, int time){
		show(pane, getWarningAlert(message), message, log, time);
	}
	
	public static void showError(Pane pane, String message, Logger log, int time){
		show(pane, getErrorAlert(message), message, log, time);
	}
	
	public static void showInfo(Pane pane, String message, Logger log, int time){
		show(pane, getInfoAlert(message), message, log, time);
	}
	
	private static void show(Pane pane, Pane toast, String message, Logger log, int time){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					toast.setLayoutX((pane.getPrefWidth()/2) - (toast.getPrefWidth()/2));
					toast.setLayoutY(pane.getPrefHeight() * 0.03);
					pane.getChildren().add(toast);
					toast.setVisible(true);
				});
				try{
					Thread.sleep(time);
				} catch (Exception e){
					log.error(e);
					System.err.println(e);
				}
				Platform.runLater(() -> pane.getChildren().remove(toast));						
			}
		}).start();
	}
	
	public static Pane getWarningAlert(String warningMessage) {
		return getAlert(warningMessage, Color.YELLOW);
	}
	
	public static Pane getErrorAlert(String errorMessage){
		return getAlert(errorMessage, Color.RED);
	}
	
	public static Pane getInfoAlert(String infoMessage){
		return getAlert(infoMessage, Color.LIGHTBLUE);
	}

	public static Pane getAlert(String message, Color color) {
		Pane toast = new Pane();
		Font font = Font.font("System", FontWeight.BOLD, 24);
		Label label = LabelFactory.getCenteredLabel(message, color, font);
		
		toast.getChildren().add(label);
		String[] messages = message.split("\n");
		String longestMessage = "";
		for(String msg : messages){
			if(msg.length() > longestMessage.length()){
				longestMessage = msg;
			}
		}
		double lineSpacing = label.getLineSpacing();
		double w = Math.floor(Toolkit.getToolkit().getFontLoader().computeStringWidth(longestMessage, font)) + 40;
		double h = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getLineHeight();
		double finalHeight = (4 * lineSpacing) + ((messages.length + 0.5 ) * h) +
				(lineSpacing * (messages.length - 1));
		ScreenUtils.setRegionSize(w, finalHeight, toast);
		setBackground(toast, 0.4, 22);
		return toast;
	}

	private static void setBackground(Pane pane, double opacity, double radii) {
		BackgroundFill fill = new BackgroundFill(Color.color(0, 0, 0, opacity),
				new CornerRadii(radii), null);
		Background bkg = new Background(fill);
		pane.setBackground(bkg);
		double h = pane.getPrefHeight();
		double arc = h / 2;
		ScreenUtils.setNodeClipAsRectangle(pane, pane.getPrefWidth(), h, arc);
	}

}
