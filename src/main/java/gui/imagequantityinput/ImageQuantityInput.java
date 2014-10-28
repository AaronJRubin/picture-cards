package gui.imagequantityinput;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import imagequantity.ImageQuantity;
import imagequantityinput.imageinput.FileInput;
import imagequantityinput.imageinput.ImageInputMode;
import imagequantityinput.imageinput.TextInput;
import imagequantityinput.imageinput.TogglableImageInput;
import inputs.IntegerInput;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import buttons.*;

class ImageQuantityInput {
	
	private final GridPane node = new GridPane();
	private final IntegerInput count = new IntegerInput(0);
	private final TogglableImageInput imageInput;
	
	private final Button deleteButton;
	private final ToggleButton textModeButton;
	private final ToggleButton fileModeButton;
	private final Button upButton = new UpButton(33, 33);
	private final Button downButton = new DownButton(33, 33);
	
	private final IntegerProperty numberProperty = new SimpleIntegerProperty();
	private final ObjectProperty<ImageInputMode> mode = new SimpleObjectProperty<ImageInputMode>();
	
	private final static String serializedColorTextSeparator = "COLOR_END";
	private final static String serializedElementSeparator = "\nELEMENT\n";

	String serialize(Path directory) {
		StringBuilder builder = new StringBuilder();
		builder.append(mode.getValue().toString() + serializedElementSeparator);
		builder.append(count.getInt() + serializedElementSeparator);
		FileInput fileInput = imageInput.getFileInput();
		try {
			String fileName = fileInput.getFile().getName();
			String convertedFileName = fileName.substring(0, fileName.length() - 4) + ".png"; 
			BufferedImage img = fileInput.getImg();
			File copiedImage = new File(directory.toString() + "/" + convertedFileName);
			ImageIO.write(img, "png", copiedImage);
			builder.append(convertedFileName + serializedElementSeparator);
		} catch (Exception e) {
			builder.append(serializedElementSeparator);
		}
		TextInput textInput = imageInput.getTextInput();
		String text = textInput.getText();
		Color color = textInput.getColor();
		builder.append(color.toString() + serializedColorTextSeparator + text + serializedElementSeparator);
		return builder.toString();
	}
	
	private void setMode(ImageInputMode m) {
		mode.set(m);
		if (m.equals(ImageInputMode.FILE)) {
			fileModeButton.setDisable(true);
			textModeButton.setDisable(false);
			return;
		}
		if (m.equals(ImageInputMode.TEXT)) {
			fileModeButton.setDisable(false);
			textModeButton.setDisable(true);
			return;
		}
	}

	static ImageQuantityInput deserialize(Stage primaryStage, String serializedForm, Path directory) {
		//System.out.println("About to deserialize the following: " + serializedForm);
		String[] split = serializedForm.split(serializedElementSeparator);
		int count = Integer.parseInt(split[1]);
		ImageQuantityInput toReturn = new ImageQuantityInput(primaryStage, 0);
		toReturn.setQuantity(count);
		toReturn.setMode(ImageInputMode.valueOf(split[0]));
		if (!split[2].equals("")) {
			File file = new File(directory.toString() + "/" + split[2]);
		//	System.out.println("About to read the following file: " + file);
			toReturn.setFile(file);
		}
		String[] text = split[3].split(serializedColorTextSeparator);
		Color color = Color.valueOf(text[0]);
		toReturn.setColor(color);
		if (text.length > 1) {
			toReturn.setText(text[1]);
		}
		return toReturn;
	}
	
	private void setFile(File file) {
		imageInput.getFileInput().setFile(file);
	}
	
	private void setColor(Color color) {
		imageInput.getTextInput().setColor(color);
	}
	
	private void setText(String text) {
		imageInput.getTextInput().setText(text);
	}

	ImageQuantityInput(Stage primaryStage, int number) {
		node.setHgap(10);
		node.setStyle("-fx-border-color: black;");
		node.setAlignment(Pos.CENTER);
		
		final Label numberLabel = new Label();
		numberLabel.setAlignment(Pos.BOTTOM_CENTER);
		numberProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				numberLabel.setText("Image " + arg2.intValue());
			}
		});
		numberProperty.setValue(number);
		
		this.fileModeButton = new ToggleButton("File Mode");
		fileModeButton.getStyleClass().add("button");

		this.textModeButton = new ToggleButton("Text Mode");
		textModeButton.getStyleClass().add("button");
		
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(fileModeButton, textModeButton);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,
					Toggle arg1, Toggle arg2) {
				if (arg2.equals(fileModeButton)) {
					setMode(ImageInputMode.FILE);
					return;
				}
				if (arg2.equals(textModeButton)) {
					setMode(ImageInputMode.TEXT);
					return;
				}
			}
		});
		
		toggleGroup.selectToggle(fileModeButton);
		
		this.deleteButton = new Button("Delete");
		
		HBox topRow = new HBox();
		topRow.setSpacing(10);
		topRow.getChildren().addAll(numberLabel, fileModeButton, textModeButton, deleteButton);
		node.add(topRow, 0, 0);

		VBox countBox = new VBox();
		countBox.getChildren().addAll(new Label("Quantity:"), count);
		
		this.imageInput = new TogglableImageInput(primaryStage, mode);
		HBox bottomRow = new HBox();
		bottomRow.setSpacing(10);
		bottomRow.getChildren().add(imageInput.getNode());
		bottomRow.getChildren().add(countBox);
		node.add(bottomRow, 0, 1);
		
		VBox upDownBox = new VBox();
		upDownBox.getChildren().addAll(upButton, downButton);
		node.add(upDownBox, 1, 0, 1, 2);
	}
	
	public void setDeleteButtonAction(EventHandler<ActionEvent> e) {
		deleteButton.setOnAction(e);
	}
	
	public void updateNumber(int newNumber) {
		numberProperty.setValue(newNumber);
	}

	public ImageQuantity getImageQuantity() throws Exception {
		int quantity = count.getInt();
		if (quantity == 0) {
			throw new Exception();
		}
		return new ImageQuantity(imageInput.getImg(), quantity);
	}
	
	public void setQuantity(int quantity) {
		count.setInt(quantity);
	}
	
	public void setUpButtonAction(EventHandler<ActionEvent> e) {
		upButton.setOnAction(e);
	}
	
	public void setDownButtonAction(EventHandler<ActionEvent> e) {
		downButton.setOnAction(e);
	}
	
	public void setUpButtonDisable(boolean newValue) {
		upButton.setDisable(newValue);
	}
	
	public void setDownButtonDisable(boolean newValue) {
		downButton.setDisable(newValue);
	}
	
	public Node getNode() {
		return node;
	}
}
