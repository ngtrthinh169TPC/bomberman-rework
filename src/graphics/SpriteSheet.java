package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/** A class contains 1 image that included all sprites, and separate them for use. **/
public class SpriteSheet {

    private final String path;
    public final int SIZE;
    private final int[] pixels;

    public int getPixels(int x, int y) {
        return pixels[x + y * SIZE];
    }

    public static SpriteSheet tiles = new SpriteSheet("/textures/classic.png", 256);

    /** Constructor. **/
    public SpriteSheet(String path, int size) {
        this.path = path;
        this.SIZE = size;
        this.pixels = new int[SIZE * SIZE];
        load();
    }

    /** Load the SpriteSheet from file(s). **/
    private void load() {
        try {
            URL a = SpriteSheet.class.getResource(path);
            BufferedImage image = ImageIO.read(a);
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
