package engineer.xiao.thermal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ZJ58 {
    static final byte[] init = {0x1b, 0x40};
    static final byte[] reset = {0x1b, 0x40, 0x0a};
    static final byte[] print_and_roll = {0x0a};
    static final byte[] zh_mode = {0x1c, 0x26};
    static final byte[] cut = {0x0a, 0x1d, 0x56, 0x42, 0x01, 0x0a};
    static final byte[] clean_end = {0x1b, 0x4a, 0x30, 0x1d, 0x56, 0x42, 0x01, 0x0a, 0x1b, 0x40};
    static int nPaperWidth = 384;
    static int nMode = 0;
    static int printWidth = 0;

    private static int[] p0 = new int[]{0, 128};
    private static int[] p1 = new int[]{0, 64};
    private static int[] p2 = new int[]{0, 32};
    private static int[] p3 = new int[]{0, 16};
    private static int[] p4 = new int[]{0, 8};
    private static int[] p5 = new int[]{0, 4};
    private static int[] p6 = new int[]{0, 2};

    public static byte[] processImage(BufferedImage imageToPrint){
        printWidth = ((nPaperWidth + 7) / 8) * 8;
        int height = imageToPrint.getHeight() * printWidth / imageToPrint.getWidth();
        height = ((height + 7) / 8) * 8;
        // resize image
        BufferedImage resizedImage = resize(imageToPrint, printWidth, height);

        // convert into grayScale
        BufferedImage grayScale = toGrayScale(resizedImage);

        if (grayScale.getType() != BufferedImage.TYPE_INT_ARGB) {
            BufferedImage tmp = new BufferedImage(grayScale.getWidth(), grayScale.getHeight(), BufferedImage.TYPE_INT_ARGB);
            tmp.getGraphics().drawImage(grayScale, 0, 0, null);
            grayScale = tmp;
        }

        // get all pixels as array of integers
        int[] grayPix = ((DataBufferInt)grayScale.getRaster().getDataBuffer()).getData();
        // convert grayScale to only black and white
        byte[] grayPixByte = new byte[grayScale.getWidth() * grayScale.getHeight()];
        format_K_threshold(grayPix, grayScale.getWidth(), grayScale.getHeight(), grayPixByte);

        return grayPixByte;
    }

    public static byte[] eachLinePixToCmd(byte[] src, int nWidth, int nMode) {
        int nHeight = src.length / nWidth;
        int nBytesPerLine = nWidth / 8;
        byte[] data = new byte[nHeight * (8 + nBytesPerLine)];
        int k = 0;

        for (int i = 0; i < nHeight; ++i) {
            int offset = i * (8 + nBytesPerLine);
            data[offset] = 29;
            data[offset + 1] = 118;
            data[offset + 2] = 48;
            data[offset + 3] = (byte) (nMode & 1);
            data[offset + 4] = (byte) (nBytesPerLine % 256);
            data[offset + 5] = (byte) (nBytesPerLine / 256);
            data[offset + 6] = 1;
            data[offset + 7] = 0;

            for (int j = 0; j < nBytesPerLine; ++j) {
                data[offset + 8 + j] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]] + p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]] + p6[src[k + 6]] + src[k + 7]);
                k += 8;
            }
        }

        return data;
    }

    public static BufferedImage toGrayScale(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] tmp = new int[4];
                originalImage.getRaster().getPixel(x, y, tmp);

                //calculate average
                int mean = (tmp[0]+tmp[1]+tmp[2])/3;
                tmp[0] = mean;
                tmp[1] = mean;
                tmp[2] = mean;

                //replace RGB value with avg
                originalImage.getRaster().setPixel(x, y, tmp);
            }
        }

        return originalImage;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private static void format_K_threshold(int[] orgpixels, int xsize, int ysize, byte[] despixels) {
        int graytotal = 0;
        int k = 0;

        int i;
        int j;
        int gray;
        for (i = 0; i < ysize; ++i) {
            for (j = 0; j < xsize; ++j) {
                gray = orgpixels[k] & 255;
                graytotal += gray;
                ++k;
            }
        }

        int grayave = graytotal / ysize / xsize;
        k = 0;

        for (i = 0; i < ysize; ++i) {
            for (j = 0; j < xsize; ++j) {
                gray = orgpixels[k] & 255;
                if (gray > grayave) {
                    despixels[k] = 0;
                } else {
                    despixels[k] = 1;
                }

                ++k;
            }
        }

    }
}
