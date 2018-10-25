package com.wangbowen.common.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * description
 *
 * @usage
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    private static final BigDecimal BIGDEC_ZERO = new BigDecimal(0);

    private static final BigDecimal BIGDEC_HUNDRED = new BigDecimal(100);
    
    private static final BigDecimal BIGDEC_CORNER = new BigDecimal(10);

    /**
     * 相加
     *
     * @param number1
     * @param number2
     *
     * @return
     */
    public static String add(String number1, String number2) {
        BigDecimal bd = new BigDecimal(number1);
        return bd.add(new BigDecimal(number2)).toString();
    }
    
    /**
     * 相减
     *
     * @param number1
     * @param number2
     *
     * @return
     */
    public static String minus(String number1, String number2) {
        BigDecimal bd = new BigDecimal(number1);
        
        BigDecimal result = bd.subtract(new BigDecimal(number2));
        DecimalFormat df =new DecimalFormat("#0.00");
        
        return df.format(result.doubleValue());
    }

    /**
     * 乘以100
     * @param num
     * @return
     */
    public static BigDecimal multiplyHundred(BigDecimal num) {
        if (num == null) {
            return null;
        }
        return num.multiply(BIGDEC_HUNDRED);
    }
    
    /**
     * 乘以10
     * @param num
     * @return
     */
    public static BigDecimal multiplyCorner(BigDecimal num) {
        if (num == null) {
            return null;
        }
        return num.multiply(BIGDEC_CORNER);
    }

    /**
     * 乘以
     * @param num1
     * @return
     */
    public static BigDecimal multiplyHundred(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) {
            return null;
        }
        if (num2 == null) {
            return null;
        }
        return num2.multiply(num1);
    }

    /**
     * 除以100
     * @param num
     * @return
     */
    public static BigDecimal divideHundred(BigDecimal num) {
        if (num == null) {
            return null;
        }
        return num.divide(BIGDEC_HUNDRED);
    }
    
    /**
     * 除以10
     * @param num
     * @return
     */
    public static BigDecimal divideCorner(BigDecimal num) {
        if (num == null) {
            return null;
        }
        return num.divide(BIGDEC_CORNER);
    }
    
    

    /**金额为分的格式 */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";


    /**
     * 标题:    changeF2Y
     * 描述:    将分为单位的转换为元 （除100） 保留2位小数
     * 参数:    @param amount 以分为单位的金额
     * 返回类型: String<br/> 以元单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeF2Y(String amount) throws Exception {
        if(StringUtils.isBlank(amount)){
            return "";
        }
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        DecimalFormat df =new DecimalFormat("#0.00");
        return df.format(divideHundred(new BigDecimal(amount)));
    }

    /**
     * 标题:    changeY2F
     * 描述:    将元为单位的转换为分 （乘100）
     * 参数:    @param amount 以元为单位的金额
     * 返回类型: String<br/>  以分为单位的金额
     * 创建者: xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeY2F(String amount) throws Exception {
        return String.valueOf(multiplyHundred(new BigDecimal(amount)).longValue());
    }
    
    /**
     * 标题:    changeF2J
     * 描述:    将分为单位的转换为角(毛) （除10） 保留2位小数
     * 参数:    @param amount 以分为单位的金额
     * 返回类型: String<br/> 以角单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeF2J(String amount) throws Exception {
        if(StringUtils.isBlank(amount)){
            return "";
        }
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        DecimalFormat df =new DecimalFormat("#0.00");
        return df.format(divideCorner(new BigDecimal(amount)));
    }
    
    /**
     * 标题:    changeJ2F
     * 描述:    将角(毛)为单位的转换为分 （乘以10） 保留2位小数
     * 参数:    @param amount 以分为单位的金额
     * 返回类型: String<br/> 以角单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeJ2F(String amount) throws Exception {
        
        if(!StringUtils.isNumeric(amount)){
            return "";
        }
        
        return String.valueOf(multiplyCorner(new BigDecimal(amount)).longValue());
    }
    
    /**
     * 标题:    changeJ2Y
     * 描述:    将角为单位的转换为元 （除10） 保留2位小数
     * 参数:    @param amount 以角为单位的金额
     * 返回类型: String<br/> 以元单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeJ2Y(String amount) throws Exception {
        if(StringUtils.isBlank(amount)){
            return "";
        }
        
        DecimalFormat df =new DecimalFormat("#0.00");
        return df.format(divideCorner(new BigDecimal(amount)));
    }
    
    /**
     * 标题:    changeY2J
     * 描述:    将元为单位的转换为角(毛) （乘以10） 保留2位小数
     * 参数:    @param amount 以元为单位的金额
     * 返回类型: String<br/> 以角单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeY2J(String amount) throws Exception {
        
        if(!StringUtils.isNumericDot(amount)){
            return "";
        }
        
        return String.valueOf(multiplyCorner(new BigDecimal(amount)).longValue());
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if (StringUtils.isBlank(s)) {
            return s;
        }
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 标题:    changeF2YIntegerPart，只获取整数部分
     * 描述:    将分为单位的转换为元 （除100）
     * 参数:    @param amount 以分为单位的金额
     * 返回类型: String<br/> 以元单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeF2YIntegerPart(String amount) throws Exception {
        if(StringUtils.isBlank(amount)){
            return "";
        }
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        DecimalFormat df =new DecimalFormat("#0.00");
        String str = df.format(divideHundred(new BigDecimal(amount)));
        return str.substring(0, str.indexOf("."));
    }

    /**
     * 标题:    changeF2YDotPart，只获取小数部分
     * 描述:    将分为单位的转换为元 （除100）
     * 参数:    @param amount 以分为单位的金额
     * 返回类型: String<br/> 以元单位的金额
     * 异常:    金额格式不对
     * 创建者:  xu_qing
     * 修改者:
     * 修改日期    :
     * 修改备注    :
     */
    public static String changeF2YDotPart(String amount) throws Exception {
        if(StringUtils.isBlank(amount)){
            return "";
        }
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        DecimalFormat df =new DecimalFormat("#0.00");
        String str = df.format(divideHundred(new BigDecimal(amount)));
        return str.substring(str.indexOf(".") + 1);
    }

    /**
     * 获取增加的排序序号
     * @return 序号
     */
    public static int getIncreaseSortNum() {
        try {
            long minusSec = System.currentTimeMillis() - 1509601302141L;
            if (minusSec < 0) {
                minusSec = -minusSec;
            }
            minusSec = minusSec/1000;
            String secStr = String.valueOf(minusSec);
            return Integer.parseInt(secStr);
        } catch (Exception e) {
        }
        return 0;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(getIncreaseSortNum());
        System.out.println(getIncreaseSortNum());
        System.out.println(getIncreaseSortNum());
        System.out.println(getIncreaseSortNum());

        System.out.println(subZeroAndDot("123.0"));
    }
}
