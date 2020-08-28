package engineer.xiao.thermal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class t2G {
    public static BufferedImage convert(String text) {
        Font font = new Font("System", Font.PLAIN, 50);
        return convert(text, font);
    }

    public static BufferedImage convert(String text, Font font) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();

        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        return img;
    }
}