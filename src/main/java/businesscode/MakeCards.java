package businesscode;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;

import javax.imageio.ImageIO;

import imagequantity.ImageQuantity;

public class MakeCards {

	private static final int width = 2550;
	private static final int height = 3300;
	
	/* private constructor to prevent instantiation, because
	 this class has only static methods */
	private MakeCards() {
	}
	
	/* while the code for the swing style is uglier and more complex than the code for
	 * the FX style, it consistently results in a smaller-sized but apparently identical
	 * output image file, for reasons that are opaque to me.
	 */
	public static void makeSwingStyle(List<ImageQuantity> imageQuantities, int verticalCount, int horizontalCount, File file) throws IOException {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int boxWidth = img.getWidth() / horizontalCount;
		int boxHeight = img.getHeight() / verticalCount; 
		Graphics2D g2 = img.createGraphics();
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		drawGrid(g2, boxWidth, boxHeight);
		if (imageQuantities.size() != 0) {
			insertImages(g2, imageQuantities, horizontalCount, verticalCount, boxWidth, boxHeight);
		}
		ImageIO.write(img, "png", file);
	}

	private static Point2D getSquareCoordinates(int squareNumber, int horizontalCount, int boxWidth, int boxHeight) {
		int y_coordinate = (squareNumber / horizontalCount) * boxHeight;
		int x_coordinate = (squareNumber % horizontalCount) * boxWidth;
		Point2D point = new Point2D.Double(x_coordinate, y_coordinate);
		return point;
	}

	private static void insertImages(Graphics2D g2, List<ImageQuantity> imageQuantities, int horizontalCount, int verticalCount, int boxWidth, int boxHeight) {
		int total_quantity = horizontalCount * verticalCount;
		int current = 0;
		while (current < total_quantity) {
			for (ImageQuantity pair : imageQuantities) {
				int curr = 0;
				while (curr < pair.getQuantity()) {
					Point2D point = getSquareCoordinates(current, horizontalCount, boxWidth, boxHeight);
					point.setLocation(point.getX() + boxWidth * 1/5, point.getY() + boxHeight * 1/5);
					g2.drawImage(pair.getImg(), (int)point.getX(), (int)point.getY(),
							boxWidth * 3/5, boxHeight * 3/5, Color.white, null);
					curr += 1;
					current += 1;
				}
			}
		}
	}

	private static void drawGrid(Graphics2D g2, int boxWidth, int boxHeight) {
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(3));
		int horizontalPos = 0;
		/* the "+ 5 " is an awkward hack to prevent the drawing of an unnecessary line
		 * on the right edge of the output image, which would otherwise sometimes happen,
		 * depending on the amount of horizontal boxes
		 */
		while (horizontalPos + 5 < width) {
			g2.drawLine(horizontalPos, 0, horizontalPos, height);
			horizontalPos += boxWidth;
		}
		int verticalPos = 0;
		/* the "+ 5 " is an awkward hack to prevent the drawing of an unnecessary line
		 * on the bottom of the output image, which would otherwise sometimes happen, 
		 * depending on the amount of vertical boxes
		 */
		while (verticalPos + 5 < height) {
			g2.drawLine(0, verticalPos, width, verticalPos);
			verticalPos += boxHeight;
		}
	}	
	
	public static void makeFXStyle(List<ImageQuantity> imageQuantities, int verticalCount, int horizontalCount, File file) throws IOException {
		GridPane pane = new GridPane();
		double percentage = .6;
		double boxWidth = width / horizontalCount;
		double boxHeight = height / verticalCount;
		double xInset = (1 - percentage) * boxWidth / 2;
		double yInset = (1 - percentage) * boxHeight / 2;
		Insets inset = new Insets(xInset, yInset, xInset, yInset);
		int totalQuantity = verticalCount * horizontalCount;
		int current = 0;
		int x = 0;
		int y = 0;
		while (current < totalQuantity) {
			for (ImageQuantity pair : imageQuantities) {
				Image img = SwingFXUtils.toFXImage(pair.getImg(), null);
				int curr = 0;
				while (curr < pair.getQuantity()) {
					if (x >= horizontalCount) {
						x = 0;
						y++;
					}
					ImageView view = new ImageView(img);
					view.setFitHeight(boxHeight * percentage);
					view.setFitWidth(boxWidth * percentage);
					view.setImage(img);
					GridPane.setMargin(view, inset);
					pane.add(view, x, y);
					curr += 1;
					current += 1;
					x++;
				}
			}
		}
		pane.setGridLinesVisible(true);
		WritableImage snapshot = pane.snapshot(new SnapshotParameters(), null);
		BufferedImage img = SwingFXUtils.fromFXImage(snapshot, null);
		ImageIO.write(img, "png", file);
	} 
	
}
