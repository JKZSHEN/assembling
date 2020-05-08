package com.jk.assembling.utli;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 非空
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(String obj){
        if( null == obj || obj.trim().length()==0){
            return false;
        }
        return true;
    }

    /**
     * 空
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj){
        if( null == obj || obj.trim().length()==0){
            return true;
        }
        return false;
    }

    /**
     * BigDecimal类型数据对比相等
     * @param va1
     * @param va2
     * @return
     */
    public static boolean compareToBig(BigDecimal va1,BigDecimal va2){
        if(va1.compareTo(va2) == 0){
            return true;
        }
        return false;
    }
    /**
     * BigDecimal类型数据对比va1大于va2
     * @param va1
     * @param va2
     * @return
     */
    public static boolean compareToBigD(BigDecimal va1,BigDecimal va2){
        if(va1.compareTo(va2) >=0){
            return true;
        }
        return false;
    }

    /**
     * Integer类型数据对比
     * @param va1
     * @param va2
     * @return
     */
    public static boolean compareToInt(Integer va1, Integer va2){
        if(va1.compareTo(va2) == 0){
            return true;
        }
        return false;
    }

    /**
     * 正则验证字符串是否是数字
     * @param va1
     * @return
     */
    public static boolean verifyNumber(String va1){
        String regex = "^[0-9]*$";
        if(StringUtils.isNotEmpty(va1)){
            char[] chars = va1.toCharArray();
            for (int i = 0; i < va1.length(); i++) {
                Pattern p = Pattern.compile(regex);
                char c = va1.charAt(i);
                String s = String.valueOf(c);
                Matcher m = p.matcher(s);
                if(!m.matches()){
                    return false;
                }
            }
            if(Integer.valueOf(va1).compareTo(28) >0 || Integer.valueOf(va1).compareTo(1)<0){
                return false;
            }
        }
        return true;
    }


    /***
     * Double类型相加
     * @param val1 1
     * @param val2 2
     * @param scale 保留scale位小数
     * @return Double
     */
    public static Double add(Double val1, Double val2, int scale) {
        if (null == val1) {
            val1 = Double.valueOf(String.valueOf(0));
        }
        if (null == val2) {
            val2 = Double.valueOf(String.valueOf(0));
        }
        return new BigDecimal(Double.toString(val1)).add(new BigDecimal(Double.toString(val2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 检查参数是否为空
     * @param array
     */
    public static boolean isEmptyArray(String array){
        if(null == array || array.length() < 1 ){
            return true;
        }
        return false;
    }

    /**
     * 没有传入图片名称就自定义文件名称
     * @param sftpFileName
     */
    public static String getFileName(String sftpFileName){
        if(StringUtils.isEmptyArray(sftpFileName)){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String day = format.format(new Date());
            sftpFileName = day;
        }
        return sftpFileName;
    }

    /**
     * 检查文件名是否有后缀
     * @param sftpFileName
     * @return
     */
    public static boolean isSerifySuffix(String sftpFileName){
        if(!sftpFileName.contains(".")){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        StringUtils.getFileName("");
    }



}
