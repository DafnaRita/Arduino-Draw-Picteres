import java.awt.*;
import java.awt.image.BufferedImage;


class Scale implements Filter{
    public  BufferedImage filter(BufferedImage img) {
        BufferedImage dimg = new BufferedImage(84, 48, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, 84, 48, null);
        g.dispose();
        return dimg;
    }
}