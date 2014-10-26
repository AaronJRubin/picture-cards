package main.java.imagequantity;
import java.awt.image.BufferedImage;


public class ImageQuantity {

	private final BufferedImage img;
	private final int quantity;

	public ImageQuantity(BufferedImage img, int quantity) {
		this.img = img;
		this.quantity = quantity;
	}

	public BufferedImage getImg() {
		return img;
	}

	public int getQuantity() {
		return quantity;
	}

}
