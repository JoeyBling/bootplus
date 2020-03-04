package io.github.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * 汉语拼音转换帮助类
 *
 * <dependency>
 * <groupId>net.sourceforge</groupId>
 * <artifactId>pinyin4j</artifactId>
 * <version>2.5.0</version>
 * </dependency>
 *
 * @author Created by 思伟 on 2019/7/16
 */
public class HanyuPinyinHelper {

    /**
     * 将文字转为汉语拼音
     *
     * @param chineseLanguage 要转成拼音的中文
     * @return 转换后的汉语拼音
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String toHanyuPinyin(String chineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        if (StringUtils.isBlank(chineseLanguage)) {
            return chineseLanguage;
        }
        char[] clChars = chineseLanguage.trim().toCharArray();
        String hanyupinyin = StringUtils.EMPTY;
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < clChars.length; i++) {
                // 如果字符是中文,则将中文转为汉语拼音
                if (String.valueOf(clChars[i]).matches("[\u4e00-\u9fa5]+")) {
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(clChars[i], defaultFormat)[0];
                } else {
                    // 如果字符不是中文,则不转换
                    hanyupinyin += clChars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw e;
        }
        return hanyupinyin;
    }

    public static String getFirstLettersUp(String chineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        return getFirstLetters(chineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String chineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        return getFirstLetters(chineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    /**
     * 将文字转为汉语拼音(首拼)
     *
     * @param chineseLanguage 要转成拼音的中文
     * @param caseType        HanyuPinyinCaseType
     * @return 转换后的汉语拼音
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getFirstLetters(String chineseLanguage, HanyuPinyinCaseType caseType) throws BadHanyuPinyinOutputFormatCombination {
        if (StringUtils.isBlank(chineseLanguage)) {
            return chineseLanguage;
        }
        char[] clChars = chineseLanguage.trim().toCharArray();
        String hanYuPinYin = StringUtils.EMPTY;
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 指定输出大小写拼音
        defaultFormat.setCaseType(caseType);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            for (int i = 0; i < clChars.length; i++) {
                String str = String.valueOf(clChars[i]);
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                if (str.matches("[\u4e00-\u9fa5]+")) {
                    hanYuPinYin += PinyinHelper.toHanyuPinyinStringArray(clChars[i], defaultFormat)[0].substring(0, 1);
                } else if (str.matches("[0-9]+")) {
                    // 如果字符是数字,取数字
                    hanYuPinYin += clChars[i];
                } else if (str.matches("[a-zA-Z]+")) {
                    // 如果字符是字母,取字母
                    hanYuPinYin += clChars[i];
                } else {
                    //如果是标点符号的话，带着
                    hanYuPinYin += clChars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw e;
        }
        if (HanyuPinyinCaseType.UPPERCASE.equals(caseType)) {
            hanYuPinYin = hanYuPinYin.toUpperCase(Locale.ENGLISH);
        } else {
            hanYuPinYin = hanYuPinYin.toLowerCase(Locale.ENGLISH);
        }
        return hanYuPinYin;
    }

    /**
     * 取第一个汉字的第一个字符
     *
     * @param chineseLanguage
     * @return
     */
    public static String getFirstLetter(String chineseLanguage) throws BadHanyuPinyinOutputFormatCombination {
        char[] clChars = chineseLanguage.trim().toCharArray();
        String hanYuPinYin = StringUtils.EMPTY;
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String chineseRegex = "[\u4e00-\u9fa5]+";
            String numberRegex = "[0-9]+";
            String letterRegex = "[a-zA-Z]+";
            String str = String.valueOf(clChars[0]);
            if (str.matches(chineseRegex)) {
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                hanYuPinYin = PinyinHelper.toHanyuPinyinStringArray(
                        clChars[0], defaultFormat)[0].substring(0, 1);
            } else if (str.matches(numberRegex)) {
                // 如果字符是数字,取数字
                hanYuPinYin += clChars[0];
            } else if (str.matches(letterRegex)) {
                // 如果字符是字母,取字母
                hanYuPinYin += clChars[0];
            } else {
                // 否则不转换
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw e;
        }
        return hanYuPinYin;
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        String pyWord = "aafdatrgh我的姓名叫周思伟，来自浙江杭州啊实打实的1231aasda";
        System.out.println(HanyuPinyinHelper.toHanyuPinyin(pyWord));
        System.out.println(HanyuPinyinHelper.getFirstLettersUp(pyWord));
        System.out.println(HanyuPinyinHelper.getFirstLettersLo(pyWord));
    }
}
