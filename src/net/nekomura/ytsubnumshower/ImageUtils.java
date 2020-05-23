package net.nekomura.ytsubnumshower;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static Image getFromURL(String imageURL) throws IOException {
		URL url = new URL(imageURL);
		Image image = ImageIO.read(url);
		return image;
	}
	
	public static Image getFromBASE64(String imageBASE64) throws IOException {
		BufferedImage image = null;
        byte[] imageByte;
        try {
        	Base64.Decoder decoder = Base64.getDecoder();
            imageByte = decoder.decode(imageBASE64);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
	}
}