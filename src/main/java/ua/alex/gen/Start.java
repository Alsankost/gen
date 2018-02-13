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
	private static AnchorPane frame;
	public static World world;
	public static TextArea codeTA = new TextArea();
	
	public static Label energy;
	public static Parent p = null;
	private static TextField sunInput = new TextField();
	private static Button sunOk = new Button("Ok");
	
	public static Random random = new Random();
	
	public static AnchorPane getRoot() {
		return frame;
	}
	
	public static void main(String[] args) {
		launch();
		world.stop();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		
		AnchorPane tools = new AnchorPane();
		root.setRight(tools);
		
		codeTA.setPrefHeight(100);
		root.setBottom(codeTA);
		
		Button defViewMode = new Button("Defailt");
		defViewMode.setOnAction((e) -> {
			Bot.viewMode = 0;
		});
		defViewMode.setLayoutX(10);
		defViewMode.setLayoutY(100);
		defViewMode.setPrefWidth(80);
		tools.getChildren().add(defViewMode);
		
		Button toxicViewMode = new Button("Toxic");
		toxicViewMode.setOnAction((e) -> {
			Bot.viewMode = 1;
		});
		toxicViewMode.setLayoutX(10);
		toxicViewMode.setLayoutY(130);
		toxicViewMode.setPrefWidth(80);
		tools.getChildren().add(toxicViewMode);
		
		Button energyViewMode = new Button("Energy");
		energyViewMode.setOnAction((e) -> {
			Bot.viewMode = 2;
		});
		energyViewMode.setLayoutX(10);
		energyViewMode.setLayoutY(160);
		energyViewMode.setPrefWidth(80);
		tools.getChildren().add(energyViewMode);
		
		Button pause = new Button("Pause");
		pause.setOnAction((e) -> {
			if (!world.isPause()) {
				world.pause();
				pause.setText("Resume");
			}
			else {
				world.resume();
				pause.setText("Pause");
			}
		});
		pause.setLayoutX(10);
		pause.setLayoutY(190);
		pause.setPrefWidth(80);
		tools.getChildren().add(pause);
		
		FileChooser fileChooser = new FileChooser();
		
		Button save = new Button("Save");
		save.setOnAction((e) -> {
			XmlWorld xmlWorld = world.save();
			world.pause();
			
			fileChooser.setTitle("Save world");
			
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file == null) {
				return;
			}
			
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(XmlWorld.class.getPackage().getName());
				Marshaller m = jc.createMarshaller();
				m.marshal(xmlWorld, file);
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		save.setLayoutX(10);
		save.setLayoutY(310);
		save.setPrefWidth(80);
		tools.getChildren().add(save);
		
		Button open = new Button("Open");
		open.setOnAction((e) -> {
			fileChooser.setTitle("Open world");
			
			world.pause();
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file == null) {
				return;
			}
			
			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(XmlWorld.class.getPackage().getName());
				Unmarshaller m = jc.createUnmarshaller();
				XmlWorld xw = (XmlWorld) m.unmarshal(file);
				world.load(xw);
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		open.setLayoutX(10);
		open.setLayoutY(330);
		open.setPrefWidth(80);
		tools.getChildren().add(open);
		
		
		energy = new Label();
		energy.setLayoutX(10);
		energy.setLayoutY(10);
		tools.getChildren().add(energy);
		
		sunInput.setLayoutX(1);
		sunInput.setLayoutY(20);
		sunInput.setPrefWidth(90);
		//tools.getChildren().add(sunInput);
		
		sunOk.setLayoutX(1);
		sunOk.setLayoutY(80);
		sunOk.setOnAction((e) -> {
			double tmp = Bot.SUN_DE;
			try {
				tmp = Double.parseDouble(sunInput.getText());
			}
			catch (Exception ex) {
				tmp = Bot.SUN_DE;
			}
			Bot.SUN_DE = tmp;
		});
		//tools.getChildren().add(sunOk);
		
		frame = new AnchorPane();
		frame.setLayoutX(800);
		frame.setLayoutY(800);
		root.setCenter(frame);
		
		world = new World(100, 100);
		Bot bot = new Bot();
		for (int  i = 0; i < 3; i++) {
			bot.setGen((byte) i, (byte) 17);
		}
		
		bot.setGen((byte) 2, (byte)  2);
		bot.setGen((byte) 3, (byte) 12);
		
		world.set(bot, 50, 20);
		
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				frame.getChildren().add(new ComponentView(x, y));
			}
		}
		
		Timeline timeline = new Timeline (new KeyFrame (Duration.millis(5), //1000 мс * 60 сек = 1 мин
			ae -> {
				for (int i = 0; i < frame.getChildren().size(); i++) {
					((ComponentView) frame.getChildren().get(i)).upadte(world);
				}
			}
		));
		timeline.setCycleCount(-1);
		timeline.play();
		
		world.start();

		Scene scene = new Scene(root, 900, 900);
		primaryStage.setScene(scene);
		primaryStage.show();
		//isRun = false;
	}
}
