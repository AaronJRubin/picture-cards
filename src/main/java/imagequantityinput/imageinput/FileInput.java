package main.java.imagequantityinput.imageinput;
import inputs.ImageFileInputField;

import java.awt.image.BufferedImage;
import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
This class can be "wraps" the generic ImageFileInputField,
from the inputs package in MyUtils.jar, in such a way as
to fit nicely within the layout area where I am keeping image inputs
for this particular program.
*/
public class FileInput extends ImageInput {

	private final GridPane node = new GridPane();
	private final ImageFileInputField inputField;
	
	FileInput(Stage primaryStage) {
		super();
		this.inputField = new ImageFileInputField(primaryStage);
		Button selectorButton = inputField.getSelectorButton();
		selectorButton.setMinWidth(imageInputControlWidth);
		selectorButton.setMaxWidth(imageInputControlWidth); 
		node.add(new Label("File:"), 1, 0);
		node.add(selectorButton, 0, 1);
		node.add(inputField, 1, 1);
	}

	public BufferedImage getImg() throws Exception {
		BufferedImage currentImage = inputField.getImg();
		if (currentImage == null) {
			throw new Exception();
		} else {
			return currentImage;
		}
	}
	
	public File getFile() {
		return inputField.getFile();
	}
	
	public void setFile(File f) {
		inputField.changeFile(f);
	}


	@Override
	public Node getNode() {
		return node;
	} 
}
