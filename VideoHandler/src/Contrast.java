import java.awt.image.BufferedImage;

class Contrast {

    public BufferedImage ContrastImage(BufferedImage grayScale,double mul,double offset){

        int length = grayScale.getWidth() * grayScale.getHeight();
        int[] gArray = new int[length];
        offset = offset * 0xFF;

        gArray = grayScale.getRGB(0, 0, grayScale.getWidth(), grayScale.getHeight(), gArray, 0, grayScale.getWidth());
        for (int i = 0; i < length; i++) {
            int g = gArray[i] & 0x00000000FF;
            g = (int) (g*mul+offset);
            if(g > 0xFF) {g=0xFF;}
            if(g < 0x00) {g=0x00;}
            gArray[i]=(0xFF000000 | g << 16 |
                    g << 8 | g);
        }

        grayScale.setRGB(0, 0, grayScale.getWidth(), grayScale.getHeight(), gArray, 0, grayScale.getWidth());
        return grayScale;

    }
}

