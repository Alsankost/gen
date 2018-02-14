package ua.alex.gen.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import ua.alex.gen.Start;
import ua.alex.gen.model.components.Bot;

public class MainController implements Initializable {

	@FXML
	private AnchorPane frame;
	
	@FXML
	private static TextField code;
	
	public static void setCode(String text) {
		code.setText(text);
	}

	//Menu "View":
	@FXML
	private void action_setDefaultView() {
		Bot.setViewMode(0);
		update();
	}
	
	@FXML
	private void action_setToxicView() {
		Bot.setViewMode(1);
		update();
	}
	
	@FXML
	private void action_setEnergyView() {
		Bot.setViewMode(2);
		update();
	}
	
	public void update() {
		for (int i = 0; i < frame.getChildren().size(); i++) {
			((ComponentView) frame.getChildren().get(i)).upadte(Start.getWorld());
		}
	}
	
	private Timeline timeline = new Timeline(new KeyFrame (Duration.millis(5), //1000 мс * 60 сек = 1 мин
			ae -> { update(); }
		)
	);
	
	//Menu "File":
	private static FileChooser fileChooser = new FileChooser();

	@FXML
	private void action_new() {
		Start.getWorld().clear();
	}
	
	@FXML
	private void action_save() {
		fileChooser.setTitle("Save world");
		
		File file = fileChooser.showSaveDialog(Start.getPrimaryStage());
		if (file == null) {
			return;
		}
		
		Start.getWorld().save(file);
	}
	
	@FXML
	private void action_open() {
		Start.getWorld().pause();
		
		fileChooser.setTitle("Open world");
		
		File file = fileChooser.showOpenDialog(Start.getPrimaryStage());
		if (file == null) {
			return;
		}
		
		Start.getWorld().load(file);
	}
	
	@FXML
	private void action_close() {
		Start.getPrimaryStage().close();
	}
	
	//Menu "Play":
	@FXML
	private void action_play() {
		timeline.play();
		if (!Start.getWorld().isRun()) Start.getWorld().start();
		if (Start.getWorld().isPause()) Start.getWorld().resume();
	}
	
	@FXML
	private void action_pause() {
		if (!Start.getWorld().isPause()) Start.getWorld().pause();
		timeline.stop();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timeline.setCycleCount(-1);
		
		for (int x = 0; x < Start.getWorld().getWidth(); x++) {
			for (int y = 0; y < Start.getWorld().getHeight(); y++) {
				frame.getChildren().add(new ComponentView(x, y));
			}
		}
	}

}
