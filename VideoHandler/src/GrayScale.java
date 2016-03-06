import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GrayScale implements Filter {

    public BufferedImage filter(BufferedImage img) {
        int length = img.getWidth() * img.getHeight();
        int[] rgbArray = new int[length];

        img.getRGB(0, 0, img.getWidth(), img.getHeight(), rgbArray, 0, img.getWidth());
        for (int i = 0; i < length; i++) {

            int medium = (int)(0.56 *(rgbArray[i] & 0x00FF0000 >> 16) +
                    0.33 *(rgbArray[i] & 0x0000FF00 >> 8 )+
                    0.11 *(rgbArray[i] & 0x000000FF));
            rgbArray[i] = rgbArray[i] + (byte)medium << 16 +
                    (byte)medium << 8 +
                    (byte)medium;
            rgbArray[i]=(0xff000000 | medium << 16 |
                    medium << 8 | medium);
        }


        img.setRGB(0, 0, img.getWidth(), img.getHeight(), rgbArray, 0, img.getWidth());
        return img;
    }
}
