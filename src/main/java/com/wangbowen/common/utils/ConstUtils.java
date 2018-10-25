package com.wangbowen.common.utils;

/**
 * 常量定义
 */
public class ConstUtils {
    public static final Integer DEL_FLAG_NOT_DELETE = 0;
    public static final Integer DEL_FLAG_DELETE = 1;

    public static final String YES = "1";
    public static final String NO = "0";

    public static final String adminPath = "/admin";

    public static final String MENU_TYPE_MENU = "1";
    public static final String MENU_TYPE_PERMISSION = "2";

    /**
     * 水印常量
     */
    public static final class WATERMARK_CONST {
        /**
         * 水印图片地址
         */
        public static final String WATERMARK_IMAGE_PATH = "/sy.png";

        /**
         * 二维码LOGO图片地址
         */
        public static final String QRCODE_LOGO_PATH = "/tx.png";

        /**
         * 用户个人的唯一二维码
         */
        public static final String QRCODE_CUSTOMER_IMAGE = "/custQRimage.png";

        /**
         * 图片水印开关 -- 开启
         */
        public static final String WATERMARK_SWITCH_ON = "1";
        /**
         * 图片水印开关 -- 关闭
         */
        public static final String WATERMARK_SWITCH_OFF = "0";

        /**
         * 图片水印位置 -- 左上
         */
        public static final int WATERMARK_POSITION_TOP_LEFT = 1;
        /**
         * 图片水印位置 -- 右上
         */
        public static final int WATERMARK_POSITION_TOP_RIGHT = 2;
        /**
         * 图片水印位置 -- 左下
         */
        public static final int WATERMARK_POSITION_BOTTOM_LEFT = 3;
        /**
         * 图片水印位置 -- 右下(默认)
         */
        public static final int WATERMARK_POSITION_BOTTOM_RIGHT = 4;
        /**
         * 图片水印位置 -- 居中
         */
        public static final int WATERMARK_POSITION_CENTER = 5;

    }

}
