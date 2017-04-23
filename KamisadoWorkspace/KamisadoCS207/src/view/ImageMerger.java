package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class ImageMerger implements Serializable{

	private int width = 31;
	private int height = 31;

	public ImageMerger() {
	}

	public BufferedImage mergeRegularStyle(Color backgroundColor, String team, String level) {
		BufferedImage colourImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = colourImage.createGraphics();
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, width, height);
		g2d.dispose();

		return combinedImage(team, "pieceStyleOne", level, colourImage);
	}

	public BufferedImage mergeAlternateStyle(Color backgroundColor, String team, String level) {
		BufferedImage colourImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = colourImage.createGraphics();
		int R = backgroundColor.getRed();
		int G = backgroundColor.getGreen();
		int B = backgroundColor.getBlue();
		g2d.setColor(backgroundColor);
		g2d.fillRect(0, 0, width, 14);
		g2d.setColor(colourChanger(R, G, B, true));
		g2d.fillRect(0, 14, 15, 18);
		g2d.setColor(colourChanger(R, G, B, false));
		g2d.fillRect(15, 14, 15, 18);
		g2d.dispose();

		return combinedImage(team, "pieceStyleTwo", level, colourImage);
	}
	
	private Color colourChanger(Integer R, Integer G, Integer B, boolean brighter){
		int increment = 10;
		if(brighter){
			increment *= -1;
		}
		if (R + increment < 255 && R + increment > 0) {
			R += increment;
		} else if (increment > 0) {
			R = 254;
		} else {
			R = 1;
		}
		if (G + increment < 255 && G + increment > 0) {
			G += increment;
		} else if (increment > 0) {
			G = 254;
		} else {
			G = 1;
		}
		if (B + increment < 255 && B + increment > 0) {
			B += increment;
		} else if (increment > 0) {
			B = 254;
		} else {
			B = 1;
		}
		return new Color(R.intValue(), G.intValue(), B.intValue());
	}

	private BufferedImage combinedImage(String team, String style, String level, BufferedImage colourImage) {
		BufferedImage returnImage = null;
		try {
			returnImage = ImageIO.read(getClass().getResource("/" + style + "/" + team + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage combinedImage = new BufferedImage(returnImage.getWidth(), returnImage.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = combinedImage.createGraphics();
		g.drawImage(colourImage, 10, 7, null);
		g.drawImage(returnImage, 0, 0, null);
		if (!level.equals("Standard")) {
			try {
				BufferedImage pieceLevel = ImageIO.read(getClass().getResource("/images/" + team + level + ".png"));
				g.drawImage(pieceLevel, 0, 0, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		g.dispose();
		return combinedImage;
	}
}
