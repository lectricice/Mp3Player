
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JSlider;

import com.sun.javafx.tk.Toolkit.Task;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FirstFrameApplication extends Application implements EventHandler {

	Button playButton;
	Button stopButton;
	Button skipButton;
	Button skipBackButton;
	ToggleButton pauseButton;
	Thread thread;

	MP3Player mp3player = new MP3Player();

	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();

		// ImageViewer
		Image play = new Image(getClass().getResourceAsStream("ressources/56-512.png"), 30, 30, true, true);
		Image volumne = new Image(getClass().getResourceAsStream("ressources/volumnePic.png"), 30, 20, true, true);
		Image pause = new Image(getClass().getResourceAsStream("ressources/pause.png"), 30, 30, true, true);
		Image stop = new Image(getClass().getResourceAsStream("ressources/stop-button.jpg"), 30, 30, true, true);
		Image skip = new Image(getClass().getResourceAsStream("ressources/skip_forward-512.png"), 30, 30, true, true);
		Image skipBack = new Image(getClass().getResourceAsStream("ressources/skip_back-512.png"), 30, 30, true, true);
		Image cover = new Image(getClass().getResourceAsStream("ressources/cover2.jpg"), 200, 200, true, true);
		Image cover2 = new Image(getClass().getResourceAsStream("ressources/metallica.jpg"), 150, 150, true, true);
		Image cover3 = new Image(getClass().getResourceAsStream("ressources/windmill.jpg"), 150, 150, true, true);
		Image cover4 = new Image(getClass().getResourceAsStream("ressources/human.jpg"), 150, 150, true, true);
		Image cover5 = new Image(getClass().getResourceAsStream("ressources/ks.jpg"), 150, 150, true, true);
		Image cover6 = new Image(getClass().getResourceAsStream("ressources/TOA.jpg"), 150, 150, true, true);

		// centerTopPane
		BorderPane centerTopPane = new BorderPane();
		Label coverPic = new Label();
		coverPic.setGraphic(new ImageView(cover));
		BorderPane songInfo = new BorderPane();
		Label title = new Label(mp3player.getAktiveTrack().getName());
		Label artist = new Label(mp3player.getAktiveTrack().getInterpret());
		Label album = new Label(mp3player.getAktiveTrack().getAlbum());
		ProgressBar progressSlider = new ProgressBar(0);
		progressSlider.progressProperty().set(0);
		mp3player.getSOP().addListener(new ChangeListener<Track>() {
			public void changed(ObservableValue<? extends Track> observable, Track oldValue, Track newValue) {
				title.setText(newValue.getName());
				artist.setText(newValue.getInterpret());
				album.setText(newValue.getAlbum());
			}
		});
		  final ChangeListener progressListener = new ChangeListener<Double>() {

			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
			      System.out.println("oldValue:"+ oldValue + ", newValue = " + newValue);
     		      progressSlider.setProgress(newValue);		
			}
	    };
		mp3player.getTime().addListener(progressListener);
		VBox artistAlbum = new VBox();
		artistAlbum.getChildren().addAll(artist, album);
		BorderPane volumnePane = new BorderPane();
		Label volumnePic = new Label();
		volumnePic.setGraphic(new ImageView(volumne));
		Slider volumneSlider = new Slider();
		volumneSlider.setMin(0);
		volumneSlider.setMax(100);
		volumneSlider.setValue(50);
		volumneSlider.setShowTickMarks(true);
		volumneSlider.setShowTickLabels(true);
		volumneSlider.setMajorTickUnit(25);
		volumneSlider.setBlockIncrement(10);
		volumneSlider.setOrientation(javafx.geometry.Orientation.VERTICAL);
		volumnePane.setCenter(volumneSlider);
		volumnePane.setBottom(volumnePic);
		volumneSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println(newValue);
				mp3player.gain(newValue.floatValue() - 80);
			}
		});

		centerTopPane.setLeft(coverPic);
		centerTopPane.setCenter(songInfo);
		centerTopPane.setRight(volumnePane);
		songInfo.setTop(title);
		songInfo.setBottom(artistAlbum);

		VBox.setVgrow(volumneSlider, Priority.ALWAYS);

		// CenterBottomPane
		BorderPane centerBottomPane = new BorderPane();
		Label playListView = new Label();
		ListView<String> playList = new ListView<String>();
		Track temp = mp3player.getAktiveTrack();
		Playlist tempList = mp3player.getPlaylist();
		ObservableList<String> testList = FXCollections.observableArrayList();
		do {
			testList.add(temp.getName());
			temp = tempList.nextTrack(temp);
		} while (temp != mp3player.getAktiveTrack());
		playList.setItems(testList);
		playList.setOrientation(javafx.geometry.Orientation.VERTICAL);
		playList.setMaxHeight(180);

		HBox playPane = new HBox();
		VBox progressPane = new VBox();
		HBox rightButtons = new HBox();
		ToggleButton playGroup = new ToggleButton();
		ImageView playGroupImage = new ImageView();
		playGroupImage.imageProperty().bind(Bindings.when(playGroup.selectedProperty()).then(pause).otherwise(play));
		playGroup.setGraphic(playGroupImage);
		playButton = new Button();
		pauseButton = new ToggleButton();
		skipBackButton = new Button();
		stopButton = new Button();
		skipButton = new Button();
		Label timerLabel = new Label("00:00");
		TimerTask tT = new TimerTask();
		tT.setSeconds((int)mp3player.getAktiveTrack().getLength());
		tT.setI(0);

		stopButton.setOnAction((event) -> {
			if (playGroup.selectedProperty().getValue()) {
				playGroup.fire();
			}
			mp3player.stop();
			tT.cancel();
			progressSlider.progressProperty().unbind();
			progressSlider.setProgress(0);
			timerLabel.textProperty().unbind();
			timerLabel.setText("00:00");
			
		});
		playGroup.setOnAction((event) -> {
			if (playGroup.selectedProperty().getValue()) {
						mp3player.play();
						tT.restart();
						timerLabel.textProperty().bind(tT.messageProperty());
						progressSlider.progressProperty().bind(tT.progressProperty());
			} else {
				mp3player.pause();
				tT.cancel();
			}
		});
		skipButton.setOnAction((event) -> {
			mp3player.skip();
			tT.restart();
		});
		skipBackButton.setOnAction((event) -> {
			mp3player.skipBack();
			tT.restart();
		});

		rightButtons.getChildren().addAll(skipBackButton, stopButton, skipButton);

		playButton.setGraphic(new ImageView(play));
		skipBackButton.setGraphic(new ImageView(skipBack));
		stopButton.setGraphic(new ImageView(stop));
		skipButton.setGraphic(new ImageView(skip));
		pauseButton.setGraphic(new ImageView(pause));

		progressPane.getChildren().addAll(progressSlider, timerLabel);
		playPane.getChildren().addAll(playGroup, progressPane, rightButtons);

		centerBottomPane.setTop(playList);
		centerBottomPane.setBottom(playPane);
		playPane.setAlignment(Pos.CENTER);

		BorderPane.setAlignment(playList, Pos.TOP_CENTER);

		// CenterPane
		BorderPane centerPane = new BorderPane();
		centerPane.setTop(centerTopPane);
		centerPane.setBottom(centerBottomPane);

		// Skinning
		centerTopPane.setPadding(new Insets(20));
		title.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
		VBox.setMargin(centerTopPane, new Insets(20));
		centerTopPane.setStyle("-fx-border-style:hidden hidden solid hidden; -fx-border-color: black;");
		songInfo.setPadding(new Insets(0, 0, 0, 20));
		playPane.setPadding(new Insets(20));
		playPane.setSpacing(5);
		progressSlider.setPadding(new Insets(10, 0, 0, 0));
		progressSlider.setMinSize(400, 0);
		playList.setStyle("-fx-control-inner-background: rgb(255,255,240)");
		skipButton.setStyle("-fx-background-color:transparent");
		skipBackButton.setStyle("-fx-background-color:transparent");
		centerPane.setStyle("-fx-border-color: black");
		root.setCenter(centerPane);
		BorderPane.setMargin(centerPane, new Insets(30));

		// ColorsRGB:
		// aliceBlue(240,248,255)
		// ghostWhite(248,248,255)
		// Ivory(255,255,240)
		root.setStyle("-fx-background-color:rgb(248,248,255)");

		Scene scene = new Scene(root, 700, 600);
		//scene.getStylesheets().add(getClass().getResource("ressources/GUIStyle.css").toExternalForm());
		scene.setFill(null);

		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.setTitle("MP3Player");
		primaryStage.show();
	}

	public void init() {

	}

	public void stop() {

	}
	public void fTime(Label text, String s){
		text.setText(s);
	}

	@Override
	public void handle(Event event) {
		// TODO Auto-generated method stub
		
	}

}