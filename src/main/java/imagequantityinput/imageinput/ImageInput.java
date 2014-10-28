package imagequantityinput.imageinput;

import java.awt.image.BufferedImage;
import javafx.scene.Node;

abstract class ImageInput {
	
	/** 
	imageInputControlWidth is a constant pixel width for a button-like
	element that I expect an implementer of this glass to put on
	the left side within its Node. In the case of the TextInput implementer,
	this is a ColorInput, and in the case of the FileInput
	implementer, this is a button for selecting a file.
	I found it to be useful to define this constant so that
	implementers of this class have a convention for making
	their Nodes look uniform.
	*/
	protected static int imageInputControlWidth = 150;
	public abstract Node getNode();
	public abstract BufferedImage getImg() throws Exception;

}