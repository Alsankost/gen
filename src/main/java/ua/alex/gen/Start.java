package ua.alex.gen;

import java.io.File;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ua.alex.gen.com.World;
import ua.alex.gen.model.XmlWorld;
import ua.alex.gen.model.components.Bot;
import ua.alex.gen.view.ComponentView;

public class Start extends Application {
	private static World world;
	
	public static Label energy;
	public static Parent p = null;
	//private static TextField sunInput = new TextField();
	//private static Button sunOk = new Button("Ok");
	
	public static Stage primaryStage = null;
	
	public static Random random = new Random();
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static World getWorld() {
		return world;
	}
	
	public static void main(String[] args) {
		launch();
		world.stop();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		world = new World(100, 100);
		Bot bot = new Bot();
		for (int  i = 0; i < 3; i++) {
			bot.setGen((byte) i, (byte) 17);
		}
		
		bot.setGen((byte) 2, (byte)  2);
		bot.setGen((byte) 3, (byte) 12);
		
		world.set(bot, 50, 20);
		
		FXMLLoader loader = new FXMLLoader(Start.class.getResource("/ua/alex/gen/view/MainView.fxml"));
		loader.load();
		BorderPane root = loader.getRoot();

		Scene scene = new Scene(root, 900, 900);
		primaryStage.setScene(scene);
		primaryStage.show();
		//isRun = false;
	}
}
