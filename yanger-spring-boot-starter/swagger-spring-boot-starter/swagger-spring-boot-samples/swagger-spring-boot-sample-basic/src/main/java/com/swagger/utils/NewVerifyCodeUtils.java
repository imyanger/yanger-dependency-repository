package com.swagger.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * 图片验证码生成
 *
 * @author Levin
 */
public class NewVerifyCodeUtils {

    /**
     * 使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
     */
    private static final String DEFAULT_STRING_VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String DEFAULT_NUMBER_VERIFY_CODES = "0123456789";
    private static final String FORMAT_NAME = "png";
    private static final String FONT_NAME = "Courier";
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 80;
    private static final int DEFAULT_VERIFY_LENGTH = 4;
    private static Random random = new Random();


    /**
     * 使用系统默认字符源生成验证码
     *
     * @param verifySize 验证码长度
     * @return 字符验证码
     */
    public static String generateStringCode(int verifySize) {
        return generateVerifyCode(verifySize, DEFAULT_STRING_VERIFY_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param verifySize 验证码长度
     * @param sources    验证码字符源
     * @return 字符验证码
     */
    public static String generateVerifyCode(int verifySize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = DEFAULT_STRING_VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 生成随机验证码文件,并返回验证码值
     *
     * @param width      宽度
     * @param height     高度
     * @param outputFile 输出路径
     * @param verifySize 字符长度
     * @return 验证码
     * @throws IOException IOException
     */
    public static String outputVerifyImage(int width, int height, File outputFile, int verifySize) throws IOException {
        String verifyCode = generateStringCode(verifySize);
        outputImage(width, height, outputFile, verifyCode);
        return verifyCode;
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     *
     * @param width      宽度
     * @param height     高度
     * @param os         输出流
     * @param verifySize 字符长度
     * @return 验证码
     * @throws IOException IOException
     */
    public static String outputStringVerifyImage(int width, int height, OutputStream os, int verifySize) throws IOException {
        String verifyCode = generateStringCode(verifySize);
        outputImage(width, height, os, verifyCode);
        return verifyCode;
    }

    public static String outputNumberVerifyImage(OutputStream os) throws IOException {
        String verifyCode = generateVerifyCode(DEFAULT_VERIFY_LENGTH, DEFAULT_NUMBER_VERIFY_CODES);
        outputImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, os, verifyCode);
        return verifyCode;
    }

    /**
     * 生成指定验证码图像文件
     *
     * @param width      宽度
     * @param height     高度
     * @param outputFile 输出文件
     * @param code       指定验证码
     * @throws IOException IOException
     */
    public static void outputImage(int width, int height, File outputFile, String code) throws IOException {
        if (outputFile == null) {
            return;
        }
        File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);
            outputImage(width, height, fos, code);
            fos.close();
        } catch (IOException e) {
            throw e;
        }
    }

    private static final int LINE_SIZE = 20;

    /**
     * 输出指定验证码图片流
     *
     * @param width  宽度
     * @param height 高度
     * @param os     输出流
     * @param code   指定验证码
     * @throws IOException IOException
     */
    public static void outputImage(int width, int height, OutputStream os, String code) throws IOException {
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA,
                Color.ORANGE, Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);
        // 设置边框色
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, width, height);

        Color c = getRandColor(200, 250);
        // 设置背景色
        g2.setColor(c);
        g2.fillRect(0, 2, width, height - 4);

        // 设置背景色
        g2.setBackground(Color.WHITE);
        g2.setColor(new Color(0, 191, 255));
        int fontSize = height - 5;
        //LAYOUT_NO_LIMIT_CONTEXT
        Font font = new Font(FONT_NAME, Font.PLAIN, fontSize);
        g2.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < verifySize; i++) {
            g2.drawChars(chars, i, 1, ((width - 10) / verifySize) * i + 5, height / 2 + fontSize / 2 - 5);
        }
        // 绘制干扰线
        Random random = new Random();
        // 设置线条的颜色
        g2.setColor(getRandColor(160, 200));
        for (int i = 0; i < LINE_SIZE; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }

        // 添加噪点 // 噪声率
        float yawpRate = 0.15f;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }
        g2.dispose();
        ImageIO.write(image, FORMAT_NAME, os);
    }

    private static final int TRICOLOR_MAX_SIZE = 255;

    private static Color getRandColor(int fc, int bc) {
        if (fc > TRICOLOR_MAX_SIZE) {
            fc = TRICOLOR_MAX_SIZE;
        }
        if (bc > TRICOLOR_MAX_SIZE) {
            bc = TRICOLOR_MAX_SIZE;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static final int RANDOM_SIZE = 3;

    private static int[] getRandomRgb() {
        int[] rgb = new int[RANDOM_SIZE];
        for (int i = 0; i < RANDOM_SIZE; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        int frames = 1;
        int phase = random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }

    private static void shearY(Graphics graphics, int width, int height, Color color) {
        // 50;
        int period = random.nextInt(40) + 10;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < width; i++) {
            double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            graphics.copyArea(i, 0, 1, height, 0, (int) d);
            graphics.setColor(color);
            graphics.drawLine(i, (int) d, i, 0);
            graphics.drawLine(i, (int) d + height, i, height);
        }
    }

    public static void main(String[] args) throws IOException {
        File dir = new File("D:/img_data");
        int width = 200, height = 80;
        String verifyCode = generateVerifyCode(4, "0123456789");
        File file = new File(dir, verifyCode + ".png");
        outputImage(width, height, file, verifyCode);
        //NewVerifyCodeUtils.outputImage(116, 43, os, verifyCode);
    }
}
