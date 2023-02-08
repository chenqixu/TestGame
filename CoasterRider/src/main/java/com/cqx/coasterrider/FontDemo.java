package com.cqx.coasterrider;

import com.cqx.coasterrider.util.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字库解析<br>
 * win7系统路径：C:\Windows\Fonts
 *
 * @author chenqixu
 */
public class FontDemo {
    private static final Logger logger = LoggerFactory.getLogger(FontDemo.class);
    /**
     * 预编译的正则表达式
     */
    private static final Pattern PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
    private static final Pattern PATTERN_16 = Pattern.compile("^[0-9A-F]+$");

    public static void main(String[] args) {
        // 本地字体的路径
        String fontFileUrl = "d:\\tmp\\data\\font\\ttf\\msyh.ttf";
        // 生成的图片存放路径
        String dirUrl = "d:\\tmp\\data\\font\\";
        // 像素点的宽和高
        int width = 16;
        int height = 16;
        // 读取本地文件
        try (
                FileInputStream fis = new FileInputStream(fontFileUrl);
                // 指定字符编码读取文件
                InputStreamReader isr = new InputStreamReader(fis, "gb2312");
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String str = sb.toString();
            logger.info(str);
            String[] split = str.split("uni");
            List<String> tempList = Arrays.asList(split);
            List<String> arrList = new ArrayList<>(tempList);
            arrList.remove(0);
            logger.info("{}", arrList.size());
            Font font = loadFont(fontFileUrl, 340);

            for (String character : arrList) {
                if (null != character && character.length() > 4) {
                    character = character.substring(0, 4);
                    // 字符串处理转为汉字
                    character = convertToCn(character);
                    String characterUrl = dirUrl + "/" + character + ".png";

                    if (isChineseChar(character)) {
                        try {
                            FontUtil.createImage(character, font, new File(characterUrl), width, height, 0, 0);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                } else {
                    character = convertToCn(character);
                    String characterUrl = dirUrl + "/" + character + ".png";
                    if (isChineseChar(character)) {
                        try {
                            FontUtil.createImage(character, font, new File(characterUrl), width, height, 0, 0);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 将字符串转换为汉字输出
     *
     * @param s unicode编码的字符串
     * @return 转换后的汉字
     */
    private static String convertToCn(String s) {
        // 进行unicode 编码转换
        s = strToUnicode(s);
        // 进行unicode 编码转换成汉字
        s = unicodeToCn(s);
        return s;
    }

    /**
     * 将16进制的字符串转换为unicode编码
     *
     * @param str 16进制字符串
     * @return unicode编码
     */
    public static String strToUnicode(String str) {
        // 判断字符串是否属于16进制
        if (isHexStrValid(str)) {
            BigInteger saint = new BigInteger(str, 16);
            int strNum = saint.intValue();
            int num = 255;
            if (strNum > num) {
                str = "\\u" + str;
            } else {
                str = "\\u00" + str;
            }
            return str;
        } else {
            return "一";
        }
    }

    /**
     * 对字符串进行判断，是否属于16进制
     *
     * @param str
     * @return
     */
    public static boolean isHexStrValid(String str) {
        return PATTERN_16.matcher(str).matches();
    }

    /**
     * unicode编码转汉字
     *
     * @param str 字符串
     * @return 转换的汉字
     */
    public static String unicodeToCn(String str) {
        Matcher matcher = PATTERN.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 读取本地字体文件供系统使用
     *
     * @param fontFileName 本地字体所在路径
     * @param fontSize     字体字号大小
     * @return font
     */
    public static Font loadFont(String fontFileName, float fontSize) {
        try {
            return getFont(fontFileName, fontSize);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //异常处理，如果出现异常久返回宋体类型的字体
            return new java.awt.Font("宋体", Font.PLAIN, 100);
        }
    }

    private static Font getFont(String fontFileName, float fontSize) throws FontFormatException, IOException {
        File file = new File(fontFileName);
        FileInputStream fis = new FileInputStream(file);
        Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, fis);
        Font dynamicFontPt = dynamicFont.deriveFont(fontSize);
        fis.close();
        return dynamicFontPt;
    }

    /**
     * 判断字符是否为汉字
     *
     * @param c 字符串
     * @return 判断结果
     */
    public static boolean isChineseChar(String c) {
        return c.matches("[\u4e00-\u9fa5]");
    }
}
