package armada;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene((Parent) FXMLLoader.load(ClassLoader.getSystemResource("FXML/Main.fxml"))));
		primaryStage.setHeight(600);
		primaryStage.setWidth(1100);
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
