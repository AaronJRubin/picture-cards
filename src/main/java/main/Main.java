package main;
import java.io.File;
import java.io.IOException;
import java.util.List;

import businesscode.MakeCards;
import gui.imagequantityinput.ImageQuantityInputs;
import gui.view.View;
import imagequantity.ImageQuantity;
import dialogs.ErrorBox;
import dialogs.IntegerInputDialog;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	private StringProperty title = new SimpleStringProperty();
	private View view;
	private ImageQuantityInputs imageInputs;
	
	private final static String fileExtension = "crd";

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(final Stage primaryStage) {
		this.imageInputs = new ImageQuantityInputs(primaryStage, 5);
		this.view = new View(primaryStage, imageInputs);
		title.addListener(e -> {
			primaryStage.setTitle(new File(title.getValue()).getName());
			view.enableSaveWorkButton();
		});
		view.setAddImageButtonAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				imageInputs.addImage();
			}
		});
		view.setMakeCardsAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				List<ImageQuantity> imageQuantities = imageInputs.getImageQuantities();
				int vertical = view.getVertical();
				int horizontal = view.getHorizontal();
				FileChooser chooser = new FileChooser();
				chooser.getExtensionFilters().add(
						new FileChooser.ExtensionFilter("Image File", "*.png"));
				File f = chooser.showSaveDialog(primaryStage);
				if (f != null) {
					try {
						MakeCards.makeSwingStyle(imageQuantities, vertical, horizontal, f);
				//		MakeCards.makeFXStyle(imageQuantities, vertical, horizontal, f);
					} catch (IOException q) {
						ErrorBox box = new ErrorBox(primaryStage, "I just failed to write the output file. " +
								"Maybe you're out of space on your disk or something?");
						box.show();
					}
				}
			}
		});
		view.setEqualizeQuantitiesButtonAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String message = "What number would you like to set all quantities to?";
				final IntegerInputDialog dialog = new IntegerInputDialog(primaryStage, message, 0);
				dialog.setConfirmButtonAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						int input = dialog.getInput();
						imageInputs.equalizeQuantities(input);
					}
				});
				dialog.show();
			}
		});
		view.setSaveWorkAsButtonAction(e -> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("Card Maker File", "*." + fileExtension));
			File f = chooser.showSaveDialog(primaryStage);
			if (f != null) {
				imageInputs.toFile(f);
				title.setValue(f.toString());
			}
		});
		view.setSaveWorkButtonAction(e -> {
			imageInputs.toFile(new File(title.getValue()));
			view.temporarilyDisableSaveWorkButton();
		});
		view.setOpenWorkButtonAction(e -> {
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("Card Maker File", "*." + fileExtension));
			File f = chooser.showOpenDialog(primaryStage);
			if (f != null) {
				imageInputs = ImageQuantityInputs.fromFile(primaryStage, f);
				view.setContent(imageInputs.getNode());
				title.setValue(f.toString());
			}
		});
		primaryStage.show();
	}
}
