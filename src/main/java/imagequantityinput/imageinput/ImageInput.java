package main.java.imagequantityinput.imageinput;

import java.awt.image.BufferedImage;
import javafx.scene.Node;

abstract class ImageInput {
	
	protected static int imageInputControlWidth = 150;
	public abstract Node getNode();
	public abstract BufferedImage getImg() throws Exception;

}