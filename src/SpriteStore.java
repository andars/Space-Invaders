import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class SpriteStore {
	private static SpriteStore single = new SpriteStore();
	private HashMap sprites = new HashMap();
	private Image image;
	
	public static SpriteStore get(){
		return single;
	}
	
	public Sprite getSprite(String ref){
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		}
		
		BufferedImage sourceImage = null;
		try{
			URL url = this.getClass().getClassLoader().getResource(ref);
			
			if (url == null) {
				fail("Can't find ref: "+ref);
			}
			
			// use ImageIO to read the image in

			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: "+ref);
		}
		// create an accelerated image of the right size to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		image.getGraphics().drawImage(sourceImage,0,0,null);
		//image= sourceImage;
		
		
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);
		return sprite;
	}

	private void fail(String string) {
		System.err.println(string);
		System.exit(0);
		
	}
}
