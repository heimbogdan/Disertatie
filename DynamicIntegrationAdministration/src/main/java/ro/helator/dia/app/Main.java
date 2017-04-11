package ro.helator.dia.app;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ro.helator.dia.config.AppContextConfig;
import ro.helator.dia.screen.ScreensContoller;

/**
 * 
 * @author bogdan.heim
 *
 */
public class Main extends Application {

	private static final Logger log = Logger.getLogger(Main.class);

	@SuppressWarnings("resource")
	@Override
	public void start(Stage stage) {
		try {
			PropertyConfigurator.configure("./config/log.properties");
			log.info("Application started!");
			AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppContextConfig.class);
			ScreensContoller bean = context.getBean(ScreensContoller.class);
			bean.init(stage);
			bean.loadScreen("/fxml/LoaderScreen.fxml");
		} catch (Exception e) {
			log.error(e);

		}
	}

	public static void main(String[] args) {
			launch(args);
	}

	@Override
	public void stop() throws Exception {
		log.info("exit");
		System.out.println("exit");
		Platform.exit();
		System.exit(0);
	}
}
