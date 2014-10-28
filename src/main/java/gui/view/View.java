package gui.view;
import gui.imagequantityinput.ImageQuantityInputs;
import buttons.HelpButton;
import inputs.IntegerInput;
import io.IO;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import styles.Styles;


public class View {
	
	private final static String helpMessage = IO.stringFromStream(View.class.getResourceAsStream("/HelpMessage.txt"), 3000);
	private final Button equalizeQuantitiesButton;
	private final Button addImageButton;
	private final Button makeCardsButton;
	private final Button saveWorkAsButton;
	private final Button saveWorkButton;
	private final Button openWorkButton;
	private final IntegerInput horizontal = new IntegerInput(4);
	private final IntegerInput vertical = new IntegerInput(6);
	private final ScrollPane scrollPane;
	
	public void setContent(Node n) {
		scrollPane.setContent(n);
	}
	
	public View(Stage primaryStage, ImageQuantityInputs imageInputs) {
		StackPane root = new StackPane();
		
		Button helpButton = new HelpButton(primaryStage, helpMessage);
		StackPane.setAlignment(helpButton, Pos.BOTTOM_LEFT);
		StackPane.setMargin(helpButton,  new Insets(10, 10, 10, 0));
		
		BorderPane pane = new BorderPane();
		
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setSpacing(10);
		BorderPane.setMargin(box, new Insets(20, 20, 20, 20));
		BorderPane.setAlignment(box, Pos.CENTER);
		addImageButton = new Button("Add Image");
		equalizeQuantitiesButton = new Button("Set Quantities To...");
		saveWorkAsButton = new Button("Save As...");
		saveWorkButton = new Button("Save");
		saveWorkButton.setDisable(true);
		openWorkButton = new Button("Open");
		box.getChildren().addAll(addImageButton, equalizeQuantitiesButton, saveWorkAsButton, saveWorkButton, openWorkButton);
		pane.setTop(box);
		
		scrollPane = new ScrollPane();
		scrollPane.setStyle("-fx-background-color:transparent;");
		BorderPane.setMargin(scrollPane, new Insets(0, 0, 0, 20));
		scrollPane.setContent(imageInputs.getNode());
		pane.setCenter(scrollPane);
		
		makeCardsButton = new Button("Make Cards");
		VBox bottom = new VBox();
		bottom.setAlignment(Pos.CENTER);
		bottom.setSpacing(10);
		BorderPane.setMargin(bottom, new Insets(20, 20, 20, 20));
		bottom.getChildren().addAll(generateDimensionsInputBox(), makeCardsButton);
		pane.setBottom(bottom);
		
		root.getChildren().addAll(pane, helpButton);
		Scene scene = new Scene(root, 580, 720);
		scene.getStylesheets().add(Styles.getStylesheet());
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
	}
	
	public Node centeredButton(Button b) {
		HBox toReturn = new HBox();
		toReturn.getChildren().add(b);
		toReturn.setAlignment(Pos.CENTER);
		return toReturn;
	}
	
	public void setSaveWorkAsButtonAction(EventHandler<ActionEvent> e) {
		saveWorkAsButton.setOnAction(e);
	}
	
	public void setSaveWorkButtonAction(EventHandler<ActionEvent> e) {
		saveWorkButton.setOnAction(e);
	}
	
	public void setOpenWorkButtonAction(EventHandler<ActionEvent> e) {
		openWorkButton.setOnAction(e);
	}
	
	public void setAddImageButtonAction(EventHandler<ActionEvent> e) {
		addImageButton.setOnAction(e);
	}
	
	public void setEqualizeQuantitiesButtonAction(EventHandler<ActionEvent> e) {
		equalizeQuantitiesButton.setOnAction(e);
	}
	
	public void setMakeCardsAction(EventHandler<ActionEvent> e) {
		makeCardsButton.setOnAction(e);
	}
	
	private Node generateDimensionsInputBox() {
		Label horizontalLabel = new Label("Horizontal");
		Label verticalLabel = new Label("Vertical");
		GridPane toReturn = new GridPane();
		toReturn.setHgap(10);
		toReturn.setAlignment(Pos.CENTER);
		toReturn.add(horizontalLabel, 0, 0);
		toReturn.add(horizontal, 0, 1);
		toReturn.add(verticalLabel, 1, 0);
		toReturn.add(vertical, 1, 1);
		return toReturn;
	}
	
	public void enableSaveWorkButton() {
		saveWorkButton.setDisable(false);
	}
	
	public void temporarilyDisableSaveWorkButton() {
		saveWorkButton.setDisable(true);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000), e -> {
			saveWorkButton.setDisable(false);
		}));
		timeline.setCycleCount(1);
		timeline.play();
	}
	
	public int getHorizontal() {
		return horizontal.getInt();
	}
	
	public int getVertical() {
		return vertical.getInt();
	}
 
}
