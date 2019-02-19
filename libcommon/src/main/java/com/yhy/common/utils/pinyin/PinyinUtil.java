package com.yhy.common.utils.pinyin;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

	/**
	 * 将一个包含汉字的字符串转换成拼音字符串
	 * @param string 包含汉字的字符串
	 * @return 拼音
	 */
	public static String getPinyin(String string) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 去除发音
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出大写字母
		
		
		char[] charArray = string.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// 判断空格, 跳过循环
			if(Character.isWhitespace(c)){
				continue;
			}
			
			// DLHJ5345(^(&(*&   -128 -> 127
			
			if(c >= -128 && c <= 127){
				// 不需要转换
				sb.append(c);
			}else {
				// 可能是汉字
				try {
					// 把汉字字符转换成拼音. 黑 -> HEI, 单 -> DAN, SHAN
					String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// 汉字转换失败
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}

}
