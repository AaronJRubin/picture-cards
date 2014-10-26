package main.java.imagequantityinput.imageinput;

import java.awt.image.BufferedImage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TogglableImageInput extends ImageInput {

	private final HBox node = new HBox();
	private final FileInput fileInput;
	private final TextInput textInput;
	
	private final ObjectProperty<ImageInputMode> mode;
	
	public TogglableImageInput(Stage primaryStage, ObjectProperty<ImageInputMode> mode) {
		this.fileInput = new FileInput(primaryStage);
		this.textInput = new TextInput();
		this.mode = mode;
		mode.addListener(new ChangeListener<ImageInputMode>() {
			@Override
			public void changed(ObservableValue<? extends ImageInputMode> arg0,
					ImageInputMode arg1, ImageInputMode arg2) {
				refreshDisplay();
			}
		});
		refreshDisplay();
	}
	
	public FileInput getFileInput() {
		return fileInput;
	}
	
	public TextInput getTextInput() {
		return textInput;
	}
	
	private void refreshDisplay() {
		node.getChildren().clear();
		switch(mode.getValue()) {
		case FILE:
			node.getChildren().add(fileInput.getNode());
			break;
		case TEXT:
			node.getChildren().add(textInput.getNode());
			break;
		}
		return;
	}
	
	@Override
	public BufferedImage getImg() throws Exception {
		switch(mode.getValue()) {
		case FILE:
			return fileInput.getImg();
		case TEXT:
			return textInput.getImg();
		default:
			return null;
		}
	}

	@Override
	public Node getNode() {
		return node;
	}

	
	
}
