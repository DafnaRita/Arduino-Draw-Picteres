import jssc.SerialPort;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Send {

    public void sending(BufferedImage image, SerialPort out) throws IOException {
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
                    //0
                }
                else{
                    compressedRgbArray[0] = (byte) (compressedRgbArray[0] | (1 << (6-(i % 7))));
                }
                if (i%7==6) {
                    compressedRgbArray[0] = (byte) (compressedRgbArray[0] & (0b01111111));

                    out.writeByte(compressedRgbArray[0]);
                    buffer = out.readBytes(1);

                    compressedRgbArray[0] = 0b0;
                }
            }
            //byte of end buffer
            compressedRgbArray[0]= -32;
            out.writeByte(compressedRgbArray[0]);
        }

        catch (Exception e){
            System.out.print("Error sending:"+e.getMessage());
        }
    }
}


