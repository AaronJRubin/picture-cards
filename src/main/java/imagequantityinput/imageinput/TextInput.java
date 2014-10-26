package main.java.imagequantityinput.imageinput;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextInput extends ImageInput {
	
	private final GridPane node = new GridPane();
	private final ColorPicker picker = new ColorPicker(Color.BLACK);
	private final TextField text = new TextField();
	
	public TextInput() {
		picker.setMinWidth(imageInputControlWidth);
		picker.setMaxWidth(imageInputControlWidth);
		Label pickerLabel = new Label("Color:");
		GridPane.setValignment(text, VPos.TOP);
		Label textLabel = new Label("Text:");
		node.add(pickerLabel, 0, 0);
		node.add(picker, 0, 1);
		node.add(textLabel, 1, 0);
		node.add(text, 1, 1);
	}
	
	public String getText() {
		return text.getText();
	}
	
	public Color getColor() {
		return picker.getValue();
	}
	
	public void setColor(Color c) {
		picker.setValue(c);
	}
	
	public void setText(String freshText) {
		text.setText(freshText);
	}
	
	public BufferedImage getImg() throws Exception {
		Text text = new Text(getText());
		text.setFont(Font.font("Sans Serif", 200));
		text.setFill(getColor());
		WritableImage snapshot = text.snapshot(new SnapshotParameters(), null);
		BufferedImage img = SwingFXUtils.fromFXImage(snapshot, null);
		return img;
	}

	@Override
	public Node getNode() {
		return node;
	}

}
