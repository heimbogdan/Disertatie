package ro.helator.dia.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 * 
 * @author bogdan.heim
 *
 */
public class ScreenUtils {

	private static final Logger log = Logger.getLogger(ScreenUtils.class);
	
	public static void setPaneSize(Pane pane, Double width, Double height){
		if(log.isTraceEnabled()){
			log.trace("--setPaneSize(): Width [" + width + "] - Height [" + height + "]");
		}
		setRegionSize(width, height, pane);
	}
	
	public static void setPaneFullSize(Pane pane){
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		Double width = primaryScreenBounds.getWidth();
		Double height = primaryScreenBounds.getHeight();
		if(log.isTraceEnabled()){
			log.trace("--setPaneFullSize(): Width [" + width + "] - Height [" + height + "]");
		}
		setRegionSize(width, height, pane);
	}
	
	public static void setNodeClipAsRectangle(Node node, double w, double h, double arc){
		Rectangle rect = new Rectangle(w, h);
		rect.setArcWidth(arc);
		rect.setArcHeight(arc);
		node.setClip(rect);
	}
	
	
	public static void setNodeLayout(Node node, double x, double y){
		node.setLayoutX(x);
		node.setLayoutY(y);
	}
	
	public static void setNodeLayout(Node node, double xy){
		setNodeLayout(node, xy, xy);
	}
	
	public static void setRegionSize(double w,double h, Region region){
		region.setMinSize(w, h);
		region.setPrefSize(w, h);
		region.setMaxSize(w, h);
	}
	
	public static void setRegionSize(double maxw,double maxh, double minw,double minh, 
			double prefw,double prefh, Region region){
		region.setMinSize(minw, minh);
		region.setPrefSize(prefw, prefh);
		region.setMaxSize(maxw, maxh);
	}
	
	public static void setRegionSize(double w,double h, Region... regions){
		for(Region region : regions){
			setRegionSize(w, h, region);
		}
	}
	
	public static void setRegionSize(double maxw,double maxh, double minw,double minh, 
			double prefw,double prefh, Region... regions){
		for(Region region : regions){
			setRegionSize(maxw, maxh, minw, minh, prefw, prefh, region);
		}
	}
	
	public static void setRegionBackground(Region region, String img) throws FileNotFoundException{
		Image background = new Image(new FileInputStream(img), region.getWidth(),
				region.getHeight(), false, true);
		setRegionBackground(region, background);
	}
	
	public static void setRegionBackground(Region region, Image img){
		region.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));
	}
	
	
	public static void resizeRegion(Region region1, Region region2){
		region1.setMaxSize(region2.getMaxWidth(), region2.getMaxHeight());
		region1.setMinSize(region2.getMinWidth(), region2.getMinHeight());
		region1.setPrefSize(region2.getPrefWidth(), region2.getPrefHeight());
	}
	
	public static void bindRegionsSizeProperty(Region region1, Region region2){
		region1.minHeightProperty().bind(region2.minHeightProperty());
		region1.maxHeightProperty().bind(region2.maxHeightProperty());
		region1.prefHeightProperty().bind(region2.prefHeightProperty());
	}
	
	public static void resizeButton(Button button, double w, double h){
		setRegionSize(w, h, button);
	}
	
	public static void resizeButtons(double w, double h, Button... buttons ){
		for(Button button : buttons){
			setRegionSize(w, h, button);
		}
	}
	
	public static void resizeLabeledFont(double size, Labeled labeled){
		Font font = labeled.getFont();
		labeled.setFont(new Font(font.getName(), size));
	}
	
	public static void resizeLabeledFont(double size, Labeled... labeleds){
		for(Labeled labeled : labeleds){
			resizeLabeledFont(size, labeled);
		}
	}
	
	public static void resizeTextInputControlFont(double size, TextInputControl textInputControl){
		Font font = textInputControl.getFont();
		textInputControl.setFont(new Font(font.getName(), size));
	}
	
	public static void resizeTextInputControlFont(double size, TextInputControl... textInputControls){
		for(TextInputControl textInputControl : textInputControls){
			resizeTextInputControlFont(size, textInputControl);
		}
	}
	
	public static void resizeTooltipFont(double size, Tooltip tooltip){
		if(tooltip == null){
			tooltip = new Tooltip();
		}
		Font font = tooltip.getFont();
		tooltip.setFont(new Font(font.getName(), size));
	}
	
	public static void resizeTooltipFont(double size, Tooltip... tooltips){
		for(Tooltip tooltip : tooltips){
			resizeTooltipFont(size, tooltip);
		}
	}
}
