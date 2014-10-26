package main.java.gui.imagequantityinput;

import dialogs.ErrorBox;
import io.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.imagequantity.ImageQuantity;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;

public class ImageQuantityInputs {

	private final Stage primaryStage;
	private List<ImageQuantityInput> imageQuantityInputs = new ArrayList<ImageQuantityInput>();
	private final VBox node = new VBox();

	private final static String serializedSeparator = "\nITEM\n";

	public ImageQuantityInputs(Stage primaryStage, int startingCount) {
		super();
		this.primaryStage = primaryStage;
		node.setSpacing(10);
		node.setAlignment(Pos.CENTER);
		while (startingCount > 0) {
			startingCount--;
			addImage();
		}
		updateDisplay();
	}

	/* 
	toFile writes to a directory and then zips it up to give it to the appearance of a single file.
	The directory contains a manifest file, called MANIFEST, which contains three lines of text for each image. 
	The first line is the quantity.
	The second line is the name of the image file (which is saved to the zipped directory)
	if the image is in fact a file. Otherwise, it is blank.
	If the image is text, the third line consists of the name of the color, the magic string COLOR_END, and the text itself.
	Otherwise, the third line is blank. */
	public void toFile(File file) {
		//	System.err.println("Calling toFile...");
		try {
			Path temporaryDirectory = Files.createTempDirectory(null);
			//	System.out.println(temporaryDirectory);
			StringBuilder manifest = new StringBuilder();
			for (ImageQuantityInput i : imageQuantityInputs) {
				manifest.append(i.serialize(temporaryDirectory));
				manifest.append(serializedSeparator);
			}
			File manifestFile = new File(temporaryDirectory + "/MANIFEST");
			FileWriter writer = new FileWriter(manifestFile);
			writer.write(manifest.toString());
			writer.close();
			ZipFile zipFile = new ZipFile(file);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			for (File s : temporaryDirectory.toFile().listFiles()) {
				zipFile.addFile(s, parameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Path extractZipToTemporaryDirectory(File file) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(file);
		Path tempDirectory = Files.createTempDirectory(null);
		zipFile.extractAll(tempDirectory.toString());
		return tempDirectory;
	}
	/*
	private static String fileToString(File f) {
		BufferedReader reader = new BufferedReader(new FileReader(manifestFile));
		int fileLength = (int) manifestFile.length();
		char[] cbuf = new char[fileLength]; 
		reader.read(cbuf, 0, fileLength);
		reader.close();
		String manifest = new String(cbuf);
	} */

	public static ImageQuantityInputs fromFile(Stage primaryStage, File file) {
		try {
			Path tempDirectory = extractZipToTemporaryDirectory(file);
			File manifestFile = new File(tempDirectory + "/MANIFEST");
			/*BufferedReader reader = new BufferedReader(new FileReader(manifestFile));
			int fileLength = (int) manifestFile.length();
			char[] cbuf = new char[fileLength]; 
			reader.read(cbuf, 0, fileLength);
			reader.close();
			String manifest = new String(cbuf); */
			//String manifest = IO.readFile(manifestFile);
			String manifest = IO.readFile(manifestFile);
			String[] split = manifest.split(serializedSeparator);
			ImageQuantityInputs toReturn = new ImageQuantityInputs(primaryStage, 0);
			for (String s : split) {
				ImageQuantityInput toAdd = ImageQuantityInput.deserialize(primaryStage, s, tempDirectory);
				toReturn.addImage(toAdd);
			}
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void equalizeQuantities(int newQuantity) {
		for (ImageQuantityInput i : imageQuantityInputs) {
			i.setQuantity(newQuantity);
		}
	}

	private EventHandler<ActionEvent> upHandler(final ImageQuantityInput i) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				moveUp(i);
			}
		};
	}

	private void moveUp(ImageQuantityInput i) {
		int loc = imageQuantityInputs.indexOf(i);
		if (loc != 0) {
			Collections.swap(imageQuantityInputs, loc, loc - 1);
			updateDisplay();
		}
	}

	private EventHandler<ActionEvent> downHandler(final ImageQuantityInput i) {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				moveDown(i);
			}
		};
	}

	private void moveDown(ImageQuantityInput i) {
		int loc = imageQuantityInputs.indexOf(i);
		if (loc != imageQuantityInputs.size() - 1) {
			Collections.swap(imageQuantityInputs, loc, loc + 1);
			updateDisplay();
		}
	}

	private void updateDisplay() {
		node.getChildren().clear();
		updateLabels();
		updateButtons();
		for (ImageQuantityInput i : imageQuantityInputs) {
			node.getChildren().add(i.getNode());
		}
	}

	private void updateLabels() {
		int index = 1;
		for (ImageQuantityInput i : imageQuantityInputs) {
			i.updateNumber(index);
			index++;
		}
	}

	private void updateButtons() {
		int index = 0;
		for (ImageQuantityInput i : imageQuantityInputs) {
			if (index == 0) {
				i.setUpButtonDisable(true);
			} else {
				i.setUpButtonDisable(false);
			}
			if (index == imageQuantityInputs.size() - 1) {
				i.setDownButtonDisable(true);
			} else {
				i.setDownButtonDisable(false);
			}
			index++;
		}
	}

	public void addImage(ImageQuantityInput toAdd) {
		toAdd.setDeleteButtonAction(deleteHandler(toAdd));
		toAdd.setUpButtonAction(upHandler(toAdd));
		toAdd.setDownButtonAction(downHandler(toAdd));
		imageQuantityInputs.add(toAdd);
		updateDisplay();
	}

	public void addImage() {
		ImageQuantityInput toAdd = new ImageQuantityInput(primaryStage, imageQuantityInputs.size() + 1);
		toAdd.setDeleteButtonAction(deleteHandler(toAdd));
		toAdd.setUpButtonAction(upHandler(toAdd));
		toAdd.setDownButtonAction(downHandler(toAdd));
		imageQuantityInputs.add(toAdd);
		updateDisplay();
	}

	private EventHandler<ActionEvent> deleteHandler(final ImageQuantityInput toDelete) {
		return new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent e) {
				if (imageQuantityInputs.size() > 1) {
					imageQuantityInputs.remove(toDelete);
					updateDisplay();
				} else {
					ErrorBox error = new ErrorBox(primaryStage, "You need at least one image!");
					error.show();
				}
			}
		};
	}

	public List<ImageQuantity> getImageQuantities() {
		List<ImageQuantity> toReturn = new ArrayList<ImageQuantity>();
		for (ImageQuantityInput i : imageQuantityInputs) {
			try {
				ImageQuantity toAdd = i.getImageQuantity();
				toReturn.add(toAdd);
			} catch (Exception e) {
			}
		}
		return toReturn;
	}

	public Node getNode() {
		return node;
	}
}