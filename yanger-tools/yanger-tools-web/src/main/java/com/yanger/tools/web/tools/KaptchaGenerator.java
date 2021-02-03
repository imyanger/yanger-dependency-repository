package com.yanger.tools.web.tools;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 验证码生成器  </p>
 *
 * @author yanghao
 * @version 1.0.0
 * @email "mailto:yanghao@fkhwl.com"
 * @date 2020.12.24 16:31
 * @since 1.0.0
 */
@Slf4j
public class KaptchaGenerator {

    /**
     * Props
     */
    private final Properties props = new Properties();

    /**
     * Config
     */
    private final Config config = new Config(props);

    /**
     * Kaptcha producer
     */
    private Producer producer = null;

    /**
     * 获取验证码信息
     *
     * @return the optional
     * @since 1.0.0
     */
    public Optional<KaptchaData> create() {
        // create the text for the image
        String capText = getProducer().createText();
        // create the image with the text
        BufferedImage bi = this.producer.createImage(capText);
        // bufferImage->base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", outputStream);
        } catch (IOException e) {
            log.error("验证码图片转base64异常", e);
            return Optional.empty();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Img = encoder.encodeToString(outputStream.toByteArray());
        return Optional.of(KaptchaData.builder().capText(capText).capVal(capText).bufferedImage(bi).base64Img(base64Img).build());
    }

    /**
     * 获取计算型验证码信息
     *
     * @return the optional
     * @since 1.0.0
     */
    public Optional<KaptchaData> createCal() {

        String[] calcShows = new String[] {"+", "-", "x"};
        int now = ThreadLocalRandom.current().nextInt(0, 3);
        int one = ThreadLocalRandom.current().nextInt(0, 10);
        int two = ThreadLocalRandom.current().nextInt(0, 10);
        int val = 0;
        switch (now) {
            case 0:
                val = one + two;
                break;
            case 1:
                if (one < two) {
                    val = two;
                    two = one;
                    one = val;
                }
                val = one - two;
                break;
            case 2:
                val = one * two;
                break;
            default:
        }

        String capText = one + " " + calcShows[now] + " " + two + " = ";

        getProducer();
        // create the image with the text
        BufferedImage bi = this.producer.createImage(capText);
        // bufferImage->base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", outputStream);
        } catch (IOException e) {
            log.error("验证码图片转base64异常", e);
            return Optional.empty();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Img = encoder.encodeToString(outputStream.toByteArray());
        return Optional.of(KaptchaData.builder().capText(capText).capVal(String.valueOf(val)).bufferedImage(bi).base64Img(base64Img).build());
    }


    /**
     * Gets producer *
     *
     * @return Producer producer
     * @since 1.0.0
     */
    private Producer getProducer() {
        props.put("kaptcha.image.width", "100");
        props.put("kaptcha.image.height", "40");
        props.put("kaptcha.textproducer.char.length", "4");
        props.put("kaptcha.textproducer.font.size", "30");
        props.put("kaptcha.textproducer.char.space", "3");
        props.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        props.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        props.put("kaptcha.textproducer.char.string", "0123456789");
        if (producer == null) {
            producer = config.getProducerImpl();
        }
        return producer;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @ToString(callSuper = true)
    public static class KaptchaData implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 验证码字符 */
        private String capText;

        /** 验证码的值 */
        private String capVal;

        /** 验证码图片 base64 字符串 */
        private String base64Img;

        /** 验证码 BufferedImage 信息 */
        private BufferedImage bufferedImage;

    }

}
