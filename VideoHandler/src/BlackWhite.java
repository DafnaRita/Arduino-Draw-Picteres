import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class BlackWhite {

    public BufferedImage toBlackWhiteArray(BufferedImage image) throws IOException {
        int length = image.getWidth() * image.getHeight();
        int[] rgbArray = new int[length];
        byte[] compressedRgbArray = new byte[1];
        byte[] buffer;
        try {
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
            Random random = new Random();

            for (int i = 0; i < length; i++) {

                int medium = (rgbArray[i] & 0x00FF0000 >> 16 +
                        rgbArray[i] & 0x0000FF00 >> 8 +
                        rgbArray[i] & 0x000000FF) / 3;

                rgbArray[i]=0xFF000000;
                if (medium >= random.nextInt(20)) {
                    //white pixel
                    rgbArray[i]=0xFFFFFFFF;
                }
                else{
                    compressedRgbArray[0] = (byte) (compressedRgbArray[0] | (1 << (6-(i % 7))));
                    //black pixel
                    }
                if (i%7==6) {
                    compressedRgbArray[0] = (byte) (compressedRgbArray[0] & (0b01111111));
                    compressedRgbArray[0] = 0b0;
                }
            }
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgbArray, 0, image.getWidth());
        }

        catch (Exception e){
            System.out.print("Error sending:"+e.getMessage());
        }

        return image;
    }
}
