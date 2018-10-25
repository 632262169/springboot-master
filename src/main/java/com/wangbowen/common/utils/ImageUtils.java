package com.wangbowen.common.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 图片处理工具
 * Created by Administrator
 */
public class ImageUtils {
    private File file = null; // 文件对象
    private String inputDir; // 输入图路径
    private String outputDir; // 输出图路径
    private String inputFileName; // 输入图文件名
    private String outputFileName; // 输出图文件名
    private int outputWidth = 100; // 默认输出图片宽
    private int outputHeight = 100; // 默认输出图片高
    private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)
    private boolean enlarge = false;//是否放大图片（默认否）


    /* 水印边距 */
    private static final int WATERMARK_MARGIN = 10;
    private static final String FONT_NAME = "Simsun";
    private static final Color FONT_COLOR = Color.WHITE;
    private static final int FONT_SIZE = 20;
    private static final int FONT_STYLE = Font.BOLD;
    private static final Color BOUNDS_COLOR = Color.black;


    // ===剪切点x y坐标
    private int x;
    private int y;

    public ImageUtils() { // 初始化变量
        inputDir = "";
        outputDir = "";
        inputFileName = "";
        outputFileName = "";
        outputWidth = 100;
        outputHeight = 100;
    }

    /**
     * 获得图片大小
     * @param path ：图片路径
     */
    public long getPicSize(String path) {
        file = new File(path);
        return file.length();
    }

    /**
     *
     * 缩略图生成，能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。
     *
     * @return ok-处理完成 no-处理失败
     */
    public boolean compressPic() {
        if (outputHeight == 0 && outputWidth == 0) {
            return false;
        }
        try {
            //获得源文件
            file = new File(inputDir + inputFileName);
            if (!file.exists()) {
                return false;
            }
            Image img = ImageIO.read(file);
            // 判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                return false;
            } else {
                //如果图片尺寸小于要压缩的尺寸，则原图直接作为压缩后的图片
                if(img.getWidth(null) <= outputWidth && img.getHeight(null) <= outputHeight && !enlarge) {
                    FileUtils.copyFileCover(inputDir + inputFileName, outputDir + outputFileName, true);
                } else {
                    int newWidth; int newHeight;
                    // 判断是否是等比缩放
                    if (this.proportion) {
                        // 为等比缩放计算输出的图片宽度及高度
                        double rate1 = 0d;
                        if (outputWidth > 0) {
                            rate1 = ((double) img.getWidth(null)) / (double) outputWidth;
                        }
                        double rate2 = 0d;
                        if (outputHeight > 0) {
                            rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
                        }
                        // 根据缩放比率大的进行缩放控制
                        double rate = rate1 > rate2 ? rate1 : rate2;
                        newWidth = (int) (((double) img.getWidth(null)) / rate);
                        newHeight = (int) (((double) img.getHeight(null)) / rate);
                    } else {
                        newWidth = outputWidth; // 输出的图片宽度
                        newHeight = outputHeight; // 输出的图片高度
                    }
                    BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

                /*
                 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
                 * 优先级比速度高 生成的图片质量比较好 但速度慢
                 */
                    tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_AREA_AVERAGING), 0, 0, null);
                    FileOutputStream out = new FileOutputStream(outputDir + outputFileName);
                    // JPEGImageEncoder可适用于其他图片类型的转换
                    //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    //encoder.encode(tag);
                    ImageIO.write(tag, "JPG", out);
                    out.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据参数压缩图片
     * @param inputDir 输入图路径
     * @param outputDir 输出图路径
     * @param inputFileName 输入图文件名
     * @param outputFileName 输出图文件名
     * @return ok-处理完成 no-处理失败
     */
    public boolean compressPic (String inputDir, String outputDir, String inputFileName, String outputFileName) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        return compressPic();
    }

    /**
     * 根据参数压缩图片
     * @param inputDir 输入图路径
     * @param outputDir 输出图路径
     * @param inputFileName 输入图文件名
     * @param outputFileName 输出图文件名
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @param gp 是否是等比缩放 标记
     * @return ok-处理完成 no-处理失败
     */
    public boolean compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName, int width, int height, boolean gp) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPic();
    }

    /**
     * 根据参数压缩图片
     * @param inputDir 输入图路径
     * @param outputDir 输出图路径
     * @param inputFileName 输入图文件名
     * @param outputFileName 输出图文件名
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @param gp 是否是等比缩放 标记
     * @param enlarge 是否放大图片
     * @return ok-处理完成 no-处理失败
     */
    public boolean compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName, int width, int height, boolean gp, boolean enlarge) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        this.enlarge = enlarge;
        return compressPic();
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }
    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }
    public void setWidthAndHeight(int width, int height) {
        this.outputWidth = width;
        this.outputHeight = height;
    }
    public void setXAndY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** 对图片裁剪，并把裁剪完的新图片保存 */
    public boolean cutImage(String inputDir, String outputDir, String inputFileName, String outputFileName, int x, int y, int width, int height) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        setXAndY(x, y);
        return cut();
    }

    /** 对图片裁剪，并把裁剪完的新图片保存 */
    private boolean cut(){
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            File file = new File(inputDir + inputFileName);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            //如果裁剪尺寸大于原图尺寸，则不裁剪
            if (width <= outputWidth && height <= outputHeight) {
                FileUtils.copyFileCover(inputDir + inputFileName, outputDir + outputFileName, true);
                return true;
            }

            // 读取图片文件
            is = new FileInputStream(inputDir + inputFileName);
            String ext = inputFileName.substring(inputFileName.lastIndexOf(".") + 1);
            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
             * 参数：formatName - 包含非正式格式名称 . （例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext.toLowerCase());
//            if (it.next() == null) {
//                return false;
//            }
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);
            /*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);
            /*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
             * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();
            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x, y, outputWidth, outputHeight);
            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);
            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
             * BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);
            // 保存新图片
            ImageIO.write(bi, ext.toLowerCase(), new File(outputDir + outputFileName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (iis != null) {
                try {
                    iis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**图片格式：JPG*/
    private static final String PICTRUE_FORMATE_JPG = "jpg";

    /**
     * 添加图片水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param position 水印位置
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param sizeFixed 水印图片的尺寸是否固定 true-固定 false-不固定 ，当不固定时默认按照原图的20%设置水印大小
     */
    public static void pressImage(String targetImg, String waterImg, int position, float alpha, boolean sizeFixed) {
        try {
            File file = new File(targetImg);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            InputStream watermark = ImageUtils.class.getResourceAsStream(waterImg);
            if (watermark == null) {
                throw new Exception("properties file [" + waterImg + "] load fail!");
            }

			/* 水印文件 */
            Image waterImage = ImageIO.read(watermark);    // 水印文件
            int logoWidth = waterImage.getWidth(null);
            int logoHeight = waterImage.getHeight(null);
            /* 如果原图长宽小于水印的两倍，则不加水印 */
            if (sizeFixed && (width < (logoWidth * 2) || height < (logoHeight * 2))) {
                return;
            }
            if (!sizeFixed) {
                double rate = 0.2;//水印图片占原图大小的比例 0.1即10%
                //水印图片的宽和高要根据原图进行计算，需考虑水印图片和原图比例不一致的情况
                // 为等比缩放计算输出的水印图片宽度及高度
                int compWith = (int) (width * rate);
                int compHeight = (int) (height * rate);
                double rate1 = 0d;
                if (compWith > 0) {
                    rate1 = ((double) logoWidth) / (double) compWith;
                }
                double rate2 = 0d;
                if (compHeight > 0) {
                    rate2 = ((double) logoHeight) / (double) compHeight;
                }
                // 根据缩放比率大的进行缩放控制
                rate = rate1 > rate2 ? rate1 : rate2;
                logoWidth = (int) (((double) logoWidth) / rate);
                logoHeight = (int) (((double) logoHeight) / rate);
            }

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            /* 根据水印位置常量计算左上角水印坐标 */
            int left;
            int top;
            switch (position) {
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_TOP_LEFT:
                    left = WATERMARK_MARGIN;
                    top = WATERMARK_MARGIN;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_TOP_RIGHT:
                    left = width - WATERMARK_MARGIN - logoWidth;
                    top = WATERMARK_MARGIN;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_BOTTOM_LEFT:
                    left = WATERMARK_MARGIN;
                    top = height - WATERMARK_MARGIN - logoHeight;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_BOTTOM_RIGHT:
                    left = width - WATERMARK_MARGIN - logoWidth;
                    top = height - WATERMARK_MARGIN - logoHeight;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_CENTER:
                    left = (width - logoWidth) / 2;
                    top = (height - logoHeight) / 2;
                    break;
                default:
                    left = width - WATERMARK_MARGIN - logoWidth;
                    top = height - WATERMARK_MARGIN - logoHeight;
                    break;
            }

            g.drawImage(waterImage, left, top, logoWidth, logoHeight, null); // 水印文件结束
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文字水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param position 水印位置
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public static void pressText(String targetImg, String pressText, int position, float alpha) {
        try {
            File file = new File(targetImg);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            int logoWidth = FONT_SIZE * getLength(pressText);
            int logoHeight = FONT_SIZE;
            /* 如果原图长宽小于水印的两倍，则不加水印 */
            if ((width < (logoWidth * 2)) || (height < (logoHeight * 2))) {
                return;
            }

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //显示文字
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout tl = new TextLayout(pressText, new Font(FONT_NAME, FONT_STYLE, FONT_SIZE), frc);

            //线形
            Rectangle2D bounds = tl.getBounds();
            /* 根据水印位置常量计算左上角水印坐标 */
            int left;
            int top;
            switch (position) {
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_TOP_LEFT:
                    left = WATERMARK_MARGIN;
                    top = WATERMARK_MARGIN;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_TOP_RIGHT:
                    left = width - WATERMARK_MARGIN - (int)bounds.getWidth();
                    top = WATERMARK_MARGIN;
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_BOTTOM_LEFT:
                    left = WATERMARK_MARGIN;
                    top = height - WATERMARK_MARGIN - (int)bounds.getHeight();
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_BOTTOM_RIGHT:
                    left = width - WATERMARK_MARGIN - (int)bounds.getWidth();
                    top = height - WATERMARK_MARGIN - (int)bounds.getHeight();
                    break;
                case ConstUtils.WATERMARK_CONST.WATERMARK_POSITION_CENTER:
                    left = (width - (int)bounds.getWidth()) / 2;
                    top = (height - (int)bounds.getHeight()) / 2;
                    break;
                default:
                    left = width - WATERMARK_MARGIN - (int)bounds.getWidth();
                    top = height - WATERMARK_MARGIN - (int)bounds.getHeight();
                    break;
            }
            Shape sha = tl
                    .getOutline(AffineTransform.getTranslateInstance(-bounds.getX() + left, -bounds.getY() + top));
            g.setColor(BOUNDS_COLOR);   //边框颜色
            g.setStroke(new BasicStroke(1.0f)); //粗细
            g.draw(sha);
            g.setColor(FONT_COLOR);//字体颜色
            g.fill(sha);
            g.dispose();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(pressText, left, top + logoHeight);
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text 文字内容
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    // main测试
    // compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
    public static void main(String[] arg) {
//        ImageUtils mypic = new ImageUtils();
//        System.out.println("输入的图片大小：" + mypic.getPicSize("d:\\14.jpg")/1024 + "KB");
//        int count = 0; // 记录全部图片压缩所用时间
//        for (int i = 0; i < 100; i++) {
//            int start = (int) System.currentTimeMillis();   // 开始时间
//            mypic.compressPic("d:\\", "d:\\test\\", "14.jpg", "r1"+i+".jpg", 120, 120, true);
//            int end = (int) System.currentTimeMillis(); // 结束时间
//            int re = end-start; // 但图片生成处理时间
//            count += re; System.out.println("第" + (i+1) + "张图片压缩处理使用了: " + re + "毫秒");
//            System.out.println("输出的图片大小：" + mypic.getPicSize("e:\\test\\r1"+i+".jpg")/1024 + "KB");
//        }
//        System.out.println("总共用了：" + count + "毫秒");

//        pressImage("D://24.PNG", "D://watermark.png", 5000, 5000, 1f);
        pressText("D://24 - 副本.PNG", "少林寺", 1, 0.1f);
//        resize("C://pic//4.jpg", 1000, 500, true);
    }
}
