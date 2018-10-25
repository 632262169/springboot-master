package com.wangbowen.common.base;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageInfo;
import com.wangbowen.common.config.GlobalConfig;
import com.wangbowen.common.utils.AliyunOSSUtils;
import com.wangbowen.common.utils.DateUtils;
import com.wangbowen.common.utils.ImageUtils;
import com.wangbowen.common.utils.StringUtils;

import org.apache.oro.text.regex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Controller父类
 */
@Controller
public class SuperController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 管理基础路径
     */
    @Value("${adminPath}")
    protected String adminPath;

    /**
     * 图片上传至阿里云OSS服务器，上传后文件名重命名，按照指定的尺寸进行压缩，并按参数的尺寸生成缩略图
     * 1、例如源文件保存在/userfile/bus100/20141024/20141024155312804.JPG，那么缩略图地址为/thumbs/userfile/bus100/20141024/20141024155312804.JPG
     * 2、若compWidth和compHeight都为0表示不压缩
     * 3、若thumbWidth和thumbHeight都为0表示不生成缩略图
     * @param imgFile     上传的文件
     * @param compWidth   原图压缩后的宽度，为0表示只按高度压缩
     * @param compHeight  原图压缩后的高度，为0表示只按宽度压缩
     * @param proportion  是否按等比例压缩
     * @param enlarge     是否强制把图片压缩（或放大）到固定的尺寸（不按比例，可能会导致图片失真），一般设置为false；设置为true时需要把proportion设为false
     * @return 文件所在服务器的完整目录，如/userfile/bus100/20141024/20141024155312804.JPG
     */
    protected String uploadImageWithThumbAndCompToOSS(MultipartFile imgFile, int compWidth, int compHeight, boolean proportion, boolean enlarge) {
        if (imgFile == null) {
            return null;
        }

        String fileName = imgFile.getOriginalFilename();
        // 获取文件扩展名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 设置上传文件名
        SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 源文件重命名,文件命名格式:yyyyMMddHHmmssSSS
        fileName = fileFormatter.format(new Date()) + new Random().nextInt(1000) + "." + ext;

        //如果文件不是图片，则不上传
        if (!"jpg".equalsIgnoreCase(ext) && !"jpeg".equalsIgnoreCase(ext)
                && !"png".equalsIgnoreCase(ext) && !"gif".equalsIgnoreCase(ext)
                && !"bmp".equalsIgnoreCase(ext)) {
            return null;
        }
        try {
            //将上传的图片文件临时保存到服务器的临时目录中
            String outputDirectory = System.getProperty("java.io.tmpdir") + "uploadImageFile/";
            File file = new File(outputDirectory);
            if (!file.exists()) {
                if (!file.mkdir()) {//生成临时目录
                    return null;
                }
            }
            File tempFile = new File(outputDirectory + fileName);
            if (!tempFile.createNewFile()) {//生成临时文件
                return null;
            }
            imgFile.transferTo(tempFile);

            //压缩后的图片临时存放目录
            String outputCompDirectory = System.getProperty("java.io.tmpdir") + "uploadImageCompFile/";
            File fileComp = new File(outputCompDirectory);
            if (!fileComp.exists()) {
                if (!fileComp.mkdir()) {//生成临时目录
                    return null;
                }
            }
            ImageUtils imageUtils = new ImageUtils();
            boolean isCompressed = imageUtils.compressPic(outputDirectory, outputCompDirectory, fileName, fileName, compWidth, compHeight, proportion, enlarge);

            String imgPath = GlobalConfig.getImagePath() + DateUtils.getNoSpSysDateString() + "/";

            //如果图片被压缩，则上传压缩后的图片；没有被压缩则上传原图
            if (isCompressed) {
                AliyunOSSUtils.uploadFile(AliyunOSSUtils.BUCKET_IMAGE, imgPath + fileName , outputCompDirectory + fileName);
            } else {
                AliyunOSSUtils.uploadFile(AliyunOSSUtils.BUCKET_IMAGE, imgPath + fileName , outputDirectory + fileName);
            }

            //最后删除临时文件 包括原图、压缩图和缩略图
            try {
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                new File(outputCompDirectory + fileName).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return imgPath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 文件上传至阿里云OSS服务器，上传后文件名重命名
     *
     * @param file 上传的文件
     * @return 文件所在服务器的完整目录，如/userfile/bus100/20141024/20141024155312804.JPG
     */
    protected String uploadFileToOSS(MultipartFile file, String bucketName) {
        String fileName = file.getOriginalFilename();
        // 获取文件扩展名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 设置上传文件名
        SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 文件命名格式:yyyyMMddHHmmssSSS
        fileName = fileFormatter.format(new Date()) + new Random().nextInt(1000) + "." + ext;
        String filePath = GlobalConfig.getImagePath() + DateUtils.getNoSpSysDateString() + "/";
        try {
            AliyunOSSUtils.uploadFile(bucketName, filePath + fileName, file.getInputStream(), file.getSize());
            return filePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 并将HTML图文内容中的远程图片地址替换成阿里云的地址
     * @param content 需要修改的HTML字符串
     *
     */
    public String replaceRemoteImgToAliyunOSSImg(String content) {
		if (StringUtils.isNotBlank(content) || StringUtils.isBlank(content) ) {//此处暂不做处理
			return content;
		}

        //替换background-image里的图片地址为腾讯的图片素材地址
        Pattern pattern = null;
        try {
            pattern = new Perl5Compiler().compile("(background-image:\\s*url\\()([^\\)]+)(\\))", Perl5Compiler.CASE_INSENSITIVE_MASK);
            PatternMatcher matcher = new Perl5Matcher();
            PatternMatcherInput matcherInput = new PatternMatcherInput(content);
            while (matcher.contains(matcherInput, pattern)) {
                try {
                    MatchResult matchResult = matcher.getMatch();
                    String matchUrl = matchResult.group(2);
                    if (StringUtils.isNotBlank(matchUrl)) {
                        String sourImageUrl = StringUtils.trim(matchUrl).replaceAll("\"", "").replaceAll("'", "").replaceAll("&quot;", "");
                        String newImgUrl = uploadImageOSSFromUrl(sourImageUrl);
                        if (newImgUrl != null) {
                            content = content.replace(sourImageUrl, newImgUrl);
                        }

                    }
                } catch (Exception e) {
                    logger.error("替换background-image失败，" + e.getMessage());
                }

            }
        } catch (MalformedPatternException e) {
            logger.error("替换background-image失败，" + e.getMessage());
        }

        return content;
    }

    public String uploadImageOSSFromUrl(String imgUrl) {
        if (StringUtils.isNotBlank(imgUrl) && imgUrl.startsWith("http") && !imgUrl.startsWith(GlobalConfig.getImgServer())) {
            //将远程图片下载并上传至阿里云
            InputStream imgIS = null;
            ByteArrayOutputStream baos = null;
            String newImgUrl = null;
            try {
                URL url = new URL(imgUrl);
                //打开链接
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                //设置请求方式为"GET"
                conn.setRequestMethod("GET");
                //超时响应时间为5秒
                conn.setConnectTimeout(5 * 1000);
                InputStream input = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();

                imgIS = new ByteArrayInputStream(baos.toByteArray());
                String fileType = HttpURLConnection.guessContentTypeFromStream(imgIS);
                imgIS.reset();
                if (StringUtils.isNotBlank(fileType)) {
                    fileType = fileType.substring(fileType.indexOf("/") + 1);
                }
                // 设置上传文件名
                SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                // 文件命名格式:yyyyMMddHHmmssSSS
                String fileName = fileFormatter.format(new Date()) + "." + fileType;
                String dateString = DateUtils.getNoSpSysDateString();
                String imgPath = GlobalConfig.getImagePath() + dateString + "/";
                try {
                    AliyunOSSUtils.uploadFile(AliyunOSSUtils.BUCKET_IMAGE, imgPath + fileName, imgIS, imgIS.available());
                    newImgUrl = imgPath + fileName;
                } catch (IOException e) {
                    logger.error("向OOS上传图片[{}]失败：{}", imgUrl, e.getMessage());
                }
            } catch (Exception e) {
                logger.error("读取网络图片[{}]失败：{}", imgUrl, e.getMessage());
            } finally {
                if (imgIS != null) {
                    try {
                        imgIS.close();
                    } catch (Exception e) {}
                }
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {}
                }
            }
            if (StringUtils.isNotBlank(newImgUrl)) {
                return GlobalConfig.getImgServer() + newImgUrl;
            }
        }
        return imgUrl;
    }

    /**
     * 文件上传至阿里云OSS服务器，上传后文件名保持不变
     *
     * @param img_article 上传的文件
     * @param bucketName 桶名称
     * @return 文件所在服务器的完整目录，如/userfile/bus100/20141024/test.JPG
     */
    protected String uploadFileKeepNameToOSS(MultipartFile img_article, String bucketName, String filePath) {
        String fileName = img_article.getOriginalFilename();
        if (StringUtils.isBlank(filePath)) {
            filePath = GlobalConfig.getFilePath() + DateUtils.getNoSpSysDateString() + "/";
        }
        try {
            AliyunOSSUtils.uploadFile(bucketName, filePath + fileName, img_article.getInputStream(), img_article.getSize());
            return filePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Page对象转成html代码
     * @param page Page分页对象
     * @return 用于页面分页展示的html代码
     */
    protected String pageToFrontHTML(Page page) {
        if (page == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        int pageNo = page.getCurrent();
        int pageSize = page.getSize();
        int first = 1;
        int total = page.getTotal();
        int prev, next;
        int length = 8;
        int slider = 1;
        int last = (total / pageSize + first - 1);
        if (total % pageSize != 0 || last == 0) {
            last++;
        }

        if (last < first) {
            last = first;
        }

        if (pageNo <= 1) {
            pageNo = first;
        }

        if (pageNo >= last) {
            pageNo = last;
        }

        if (pageNo < last - 1) {
            next = pageNo + 1;
        } else {
            next = last;
        }

        if (pageNo > 1) {
            prev = pageNo - 1;
        } else {
            prev = first;
        }

        if (pageNo > last) {// 如果当前页大于尾页
            pageNo = last;
        }

        if (pageNo == first) {// 如果是首页
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
        } else {
            sb.append("<li><a href=\"javascript:page(" + prev + "," + pageSize + ");\">&#171; 上一页</a></li>\n");
        }

        int begin = pageNo - (length / 2);

        if (begin < first) {
            begin = first;
        }

        int end = begin + length - 1;

        if (end >= last) {
            end = last;
            begin = end - length + 1;
            if (begin < first) {
                begin = first;
            }
        }

        if (begin > first) {
            int i = 0;
            for (i = first; i < first + slider && i < begin; i++) {
                sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                        + (i + 1 - first) + "</a></li>\n");
            }
            if (i < begin) {
                sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
            }
        }

        for (int i = begin; i <= end; i++) {
            if (i == pageNo) {
                sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
                        + "</a></li>\n");
            } else {
                sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                        + (i + 1 - first) + "</a></li>\n");
            }
        }

        if (last - end > slider) {
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
            end = last - slider;
        }

        for (int i = end + 1; i <= last; i++) {
            sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                    + (i + 1 - first) + "</a></li>\n");
        }

        if (pageNo == last) {
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
        } else {
            sb.append("<li><a href=\"javascript:page(" + next + "," + pageSize + ");\">"
                    + "下一页 &#187;</a></li>\n");
        }

        sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
        sb.append("<input disabled=\"disabled\" type=\"text\"  value=\"" + pageNo + "\" onkeypress=\"__pageNoChange(event,this)\"");
        sb.append(" onclick=\"this.select();\"/> / ");
        float pageNumber = total / pageSize;
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        sb.append("<input type=\"text\" disabled=\"disabled\" value=\"" + (int) Math.ceil(pageNumber) + "\" onkeypress=\"__pageSizeChange(event,this)\"");
        sb.append(" onclick=\"this.select();\"/> 页，");
        sb.append("共 " + total + " 条" + "</a><li>\n");

        sb.insert(0, "<ul>\n").append("</ul>\n");

        sb.append("<script>\n");
        sb.append("function __pageNoChange(e, __this){var evt=e||window.event||__this;var c=e.keyCode||e.which;");
        sb.append("if(c==13) page(__this.value," + pageSize + ");}\n");
        sb.append("function __pageSizeChange(e, __this){var evt=e||window.event||__this;var c=e.keyCode||e.which;");
        sb.append("if(c==13) page(" + pageNo + ",__this.value);}\n");
        sb.append("</script>\n");

        sb.append("<div style=\"clear:both;\"></div>");

//		sb.insert(0,"<div class=\"page\">\n").append("</div>\n");

        return sb.toString();
    }

    /**
     * Page对象转成html代码
     * @param page Page分页对象
     * @return 用于页面分页展示的html代码
     */
    protected String pageToFrontHTML(PageInfo page) {
        if (page == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        int pageNo = page.getPageNum();
        int pageSize = page.getPageSize();
        int first = 1;
        long total = page.getTotal();
        int prev, next;
        int length = 8;
        int slider = 1;
        long last = (total / pageSize + first - 1);
        if (total % pageSize != 0 || last == 0) {
            last++;
        }

        if (last < first) {
            last = first;
        }

        if (pageNo <= 1) {
            pageNo = first;
        }

        if (pageNo >= last) {
            pageNo = Integer.valueOf(last + "");
        }

        if (pageNo < last - 1) {
            next = pageNo + 1;
        } else {
            next = Integer.valueOf(last + "");
        }

        if (pageNo > 1) {
            prev = pageNo - 1;
        } else {
            prev = first;
        }

        if (pageNo > last) {// 如果当前页大于尾页
            pageNo = Integer.valueOf(last + "");
        }

        if (pageNo == first) {// 如果是首页
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
        } else {
            sb.append("<li><a href=\"javascript:page(" + prev + "," + pageSize + ");\">&#171; 上一页</a></li>\n");
        }

        int begin = pageNo - (length / 2);

        if (begin < first) {
            begin = first;
        }

        int end = begin + length - 1;

        if (end >= last) {
            end = Integer.valueOf(last + "");
            begin = end - length + 1;
            if (begin < first) {
                begin = first;
            }
        }

        if (begin > first) {
            int i = 0;
            for (i = first; i < first + slider && i < begin; i++) {
                sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                        + (i + 1 - first) + "</a></li>\n");
            }
            if (i < begin) {
                sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
            }
        }

        for (int i = begin; i <= end; i++) {
            if (i == pageNo) {
                sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
                        + "</a></li>\n");
            } else {
                sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                        + (i + 1 - first) + "</a></li>\n");
            }
        }

        if (last - end > slider) {
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
            end = Integer.valueOf(last + "") - slider;
        }

        for (int i = end + 1; i <= last; i++) {
            sb.append("<li><a href=\"javascript:page(" + i + "," + pageSize + ");\">"
                    + (i + 1 - first) + "</a></li>\n");
        }

        if (pageNo == last) {
            sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
        } else {
            sb.append("<li><a href=\"javascript:page(" + next + "," + pageSize + ");\">"
                    + "下一页 &#187;</a></li>\n");
        }

        sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
        sb.append("<input disabled=\"disabled\" type=\"text\"  value=\"" + pageNo + "\" onkeypress=\"__pageNoChange(event,this)\"");
        sb.append(" onclick=\"this.select();\"/> / ");
        float pageNumber = total / pageSize;
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        sb.append("<input type=\"text\" disabled=\"disabled\" value=\"" + (int) Math.ceil(pageNumber) + "\" onkeypress=\"__pageSizeChange(event,this)\"");
        sb.append(" onclick=\"this.select();\"/> 页，");
        sb.append("共 " + total + " 条" + "</a><li>\n");

        sb.insert(0, "<ul>\n").append("</ul>\n");

        sb.append("<script>\n");
        sb.append("function __pageNoChange(e, __this){var evt=e||window.event||__this;var c=e.keyCode||e.which;");
        sb.append("if(c==13) page(__this.value," + pageSize + ");}\n");
        sb.append("function __pageSizeChange(e, __this){var evt=e||window.event||__this;var c=e.keyCode||e.which;");
        sb.append("if(c==13) page(" + pageNo + ",__this.value);}\n");
        sb.append("</script>\n");

        sb.append("<div style=\"clear:both;\"></div>");

//		sb.insert(0,"<div class=\"page\">\n").append("</div>\n");

        return sb.toString();
    }
}
