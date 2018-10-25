package com.wangbowen.modules.sys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.config.GlobalConfig;
import com.wangbowen.common.utils.AliyunOSSUtils;
import com.wangbowen.common.utils.DateUtils;
import com.wangbowen.common.utils.StringUtils;

import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "${adminPath}/sys/ueditor")
public class UeditorController extends SuperController {

    @RequestMapping("/dispatch")
    @ResponseBody
    public String config() {
        return "/* 前后端通信相关的配置,注释只允许使用多行方式 */\n" +
                "{\n" +
                "    /* 上传图片配置项 */\n" +
                "    \"imageActionName\": \"uploadimage\", /* 执行上传图片的action名称 */\n" +
                "    \"imageFieldName\": \"upfile\", /* 提交的图片表单名称 */\n" +
                "    \"imageMaxSize\": 2048000000, /* 上传大小限制，单位B */\n" +
                "    \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 上传图片格式显示 */\n" +
                "    \"imageCompressEnable\": true, /* 是否压缩图片,默认是true */\n" +
                "    \"imageCompressBorder\": 1600, /* 图片压缩最长边限制 */\n" +
                "    \"imageInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                "    \"imageUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                "    \"imagePathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "                                /* {filename} 会替换成原文件名,配置这项需要注意中文乱码问题 */\n" +
                "                                /* {rand:6} 会替换成随机数,后面的数字是随机数的位数 */\n" +
                "                                /* {time} 会替换成时间戳 */\n" +
                "                                /* {yyyy} 会替换成四位年份 */\n" +
                "                                /* {yy} 会替换成两位年份 */\n" +
                "                                /* {mm} 会替换成两位月份 */\n" +
                "                                /* {dd} 会替换成两位日期 */\n" +
                "                                /* {hh} 会替换成两位小时 */\n" +
                "                                /* {ii} 会替换成两位分钟 */\n" +
                "                                /* {ss} 会替换成两位秒 */\n" +
                "                                /* 非法字符 \\ : * ? \" < > | */\n" +
                "                                /* 具请体看线上文档: fex.baidu.com/ueditor/#use-format_upload_filename */\n" +
                "\n" +
                "    /* 涂鸦图片上传配置项 */\n" +
                "    \"scrawlActionName\": \"uploadscrawl\", /* 执行上传涂鸦的action名称 */\n" +
                "    \"scrawlFieldName\": \"upfile\", /* 提交的图片表单名称 */\n" +
                "    \"scrawlPathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "    \"scrawlMaxSize\": 2048000, /* 上传大小限制，单位B */\n" +
                "    \"scrawlUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                "    \"scrawlInsertAlign\": \"none\",\n" +
                "\n" +
                "    /* 截图工具上传 */\n" +
                "    \"snapscreenActionName\": \"uploadimage\", /* 执行上传截图的action名称 */\n" +
                "    \"snapscreenPathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "    \"snapscreenUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                "    \"snapscreenInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                "\n" +
                "    /* 抓取远程图片配置 */\n" +
                "    \"catcherLocalDomain\": [\"127.0.0.1\", \"localhost\", \"img.baidu.com\"],\n" +
                "    \"catcherActionName\": \"catchimage\", /* 执行抓取远程图片的action名称 */\n" +
                "    \"catcherFieldName\": \"source\", /* 提交的图片列表表单名称 */\n" +
                "    \"catcherPathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "    \"catcherUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                "    \"catcherMaxSize\": 2048000, /* 上传大小限制，单位B */\n" +
                "    \"catcherAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 抓取图片格式显示 */\n" +
                "\n" +
                "    /* 上传视频配置 */\n" +
                "    \"videoActionName\": \"uploadvideo\", /* 执行上传视频的action名称 */\n" +
                "    \"videoFieldName\": \"upfile\", /* 提交的视频表单名称 */\n" +
                "    \"videoPathFormat\": \"/ueditor/jsp/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "    \"videoUrlPrefix\": \"\", /* 视频访问路径前缀 */\n" +
                "    \"videoMaxSize\": 102400000, /* 上传大小限制，单位B，默认100MB */\n" +
                "    \"videoAllowFiles\": [\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\"], /* 上传视频格式显示 */\n" +
                "\n" +
                "    /* 上传文件配置 */\n" +
                "    \"fileActionName\": \"uploadfile\", /* controller里,执行上传视频的action名称 */\n" +
                "    \"fileFieldName\": \"upfile\", /* 提交的文件表单名称 */\n" +
                "    \"filePathFormat\": \"/ueditor/jsp/upload/file/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                "    \"fileUrlPrefix\": \"\", /* 文件访问路径前缀 */\n" +
                "    \"fileMaxSize\": 102400000, /* 上传大小限制，单位B，默认50MB */\n" +
                "    \"fileAllowFiles\": [\n" +
                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                "    ], /* 上传文件格式显示 */\n" +
                "\n" +
                "    /* 列出指定目录下的图片 */\n" +
                "    \"imageManagerActionName\": \"listimage\", /* 执行图片管理的action名称 */\n" +
                "    \"imageManagerListPath\": \"/ueditor/jsp/upload/image/\", /* 指定要列出图片的目录 */\n" +
                "    \"imageManagerListSize\": 20, /* 每次列出文件数量 */\n" +
                "    \"imageManagerUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                "    \"imageManagerInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                "    \"imageManagerAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 列出的文件类型 */\n" +
                "\n" +
                "    /* 列出指定目录下的文件 */\n" +
                "    \"fileManagerActionName\": \"listfile\", /* 执行文件管理的action名称 */\n" +
                "    \"fileManagerListPath\": \"/ueditor/jsp/upload/file/\", /* 指定要列出文件的目录 */\n" +
                "    \"fileManagerUrlPrefix\": \"\", /* 文件访问路径前缀 */\n" +
                "    \"fileManagerListSize\": 20, /* 每次列出文件数量 */\n" +
                "    \"fileManagerAllowFiles\": [\n" +
                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                "    ] /* 列出的文件类型 */\n" +
                "\n" +
                "}";


    }

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 图片上传
     * @param file 图片文件
     * @return
     */
    @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("upfile") MultipartFile file) {
        String output = "";
        Map<String, Object> result = new HashMap<>();
        try {
            if (file == null || file.getSize() == 0) {
                throw new Exception("没有上传图片");
            }
            String fileName = file.getOriginalFilename();
            // 获取文件扩展名
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            //如果文件不是图片，则不上传
            if (!"jpg".equalsIgnoreCase(ext) && !"jpeg".equalsIgnoreCase(ext)
                    && !"png".equalsIgnoreCase(ext) && !"gif".equalsIgnoreCase(ext)
                    && !"bmp".equalsIgnoreCase(ext)) {
                throw new Exception("图片格式错误");
            }
            String img_url = "";
            if (file.getSize() > 0) {
                img_url = uploadImageWithThumbAndCompToOSS(file, 0, 0, true, false);
            }
            result.put("result", "success");
            result.put("img_url", img_url);

            String url = Base64.encodeBase64String(img_url.getBytes("UTF-8"));
            result.put("url", GlobalConfig.getImgServer() + img_url);
            result.put("original", url);
            result.put("title", file.getOriginalFilename());
            result.put("size", file.getSize());
            result.put("type", ext);
            result.put("state", "SUCCESS");

            output = mapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("ueditor image upload error : ", e);
        }

        return output;
    }

    /**
     * 从秀米拷贝的内容带图片时，抓取该图片并上传至阿里云
     * @param source 秀米的图片地址（数组）
     * @return
     */
    @RequestMapping(value = "catchImage", method = RequestMethod.POST)
    @ResponseBody
    public String catchImage(@RequestParam("source[]") String[] source) {
        String output = "";
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        try {
            if (source != null && source.length > 0) {

                for (String sourceUrl : source) {
                    if (StringUtils.isBlank(sourceUrl) || !sourceUrl.toLowerCase().startsWith("http")) {
                        continue;
                    }
                    //将远程图片下载并上传至阿里云
                    InputStream imgIS = null;
                    ByteArrayOutputStream baos = null;
                    String newImgUrl = null;
                    try {
                        URL url = new URL(sourceUrl);
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
                        // 获取文件扩展名
                        String ext = sourceUrl.substring(sourceUrl.lastIndexOf(".") + 1);
                        String fileType = HttpURLConnection.guessContentTypeFromStream(imgIS);
                        imgIS.reset();

                        if (StringUtils.isNotBlank(fileType)) {
                            fileType = fileType.substring(fileType.indexOf("/") + 1);
                        }
                        // 获取文件扩展名
                        if (StringUtils.isNotBlank(fileType)) {
                            ext = fileType;
                        }

                        // 设置上传文件名
                        SimpleDateFormat fileFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        // 文件命名格式:yyyyMMddHHmmssSSS
                        String fileName = fileFormatter.format(new Date()) + "." + ext;
                        String dateString = DateUtils.getNoSpSysDateString();
                        String imgPath = GlobalConfig.getImagePath() + dateString + "/";
                        try {
                            AliyunOSSUtils.uploadFile(AliyunOSSUtils.BUCKET_IMAGE, imgPath + fileName, imgIS, imgIS.available());
                            newImgUrl = imgPath + fileName;
                        } catch (IOException e) {
                            logger.error("向OOS上传图片[{}]失败：{}", sourceUrl, e.getMessage());
                        }
                    } catch (Exception e) {
                        logger.error("读取网络图片[{}]失败：{}", sourceUrl, e.getMessage());
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
                        Map<String, String> map = new HashMap<>();
                        map.put("state", "SUCCESS");
                        map.put("title", "");
                        map.put("source", sourceUrl);
                        map.put("url", GlobalConfig.getImgServer() + newImgUrl);
                        map.put("size", "");
                        list.add(map);
                    }

                }
            }
            result.put("result", "success");
            result.put("list", list);

//            {"state": "SUCCESS",
//                    list: [{"state": "SUCCESS","title": "1467473307988094638.jpg","source": "http://img.xiumi.us/xmi/ua/sfLB/i/c530e9becd36d75bae0ea6b66c96df13-sz_47275.jpg@1l_640w.jpg","url": "/ueditor/jsp/upload/image/20160702/1467473307988094638.jpg","size": "47289"},
//                           {"state": "SUCCESS","title": "1467473308080038003.gif","source": "http://img.xiumi.us/stc/images/templates-assets/parts/701-other/014-hint-bottom-26.gif","url": "/ueditor/jsp/upload/image/20160702/1467473308080038003.gif","size": "6757"} ]}
//
            output = mapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("ueditor image upload error : ", e);
        }

        return output;
    }

    /**
     * 视频上传
     * @param file 视频文件
     * @return
     */
    @RequestMapping(value = "uploadvideo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadvideo(@RequestParam("upfile") MultipartFile file) {
        String output = "";
        Map<String, Object> result = new HashMap<>();
        try {
            if (file == null || file.getSize() == 0) {
                throw new Exception("没有上传文件");
            }
            String fileName = file.getOriginalFilename();
//            // 获取文件扩展名
            String video_url = "";
            if (file.getSize() > 0) {
                video_url = uploadFileToOSS(file, AliyunOSSUtils.BUCKET_VIDEO);
            }
//            result.put("result", "success");
//            result.put("video_url", video_url);

            String url = Base64.encodeBase64String(video_url.getBytes("UTF-8"));
            result.put("url", GlobalConfig.getVideoServer() + video_url);
            result.put("original", url);
            result.put("title", file.getOriginalFilename());
//            result.put("size", file.getSize());
//            result.put("type", ext);
            result.put("state", "SUCCESS");

            output = mapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("ueditor image upload error : ", e);
        }

        return output;
    }

    /**
     * 图片上传
     * @param file 图片文件
     * @return
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam("upfile") MultipartFile file) {
        String output = "";
        Map<String, Object> result = new HashMap<>();
        try {
            if (file == null || file.getSize() == 0) {
                throw new Exception("没有上传文件");
            }

            String fileName = file.getOriginalFilename();
            // 获取文件扩展名
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            String file_url = "";
            if (file.getSize() > 0) {
                file_url = uploadFileKeepNameToOSS(file, AliyunOSSUtils.BUCKET_FILE, null);
            }
//            result.put("result", "success");
//            result.put("video_url", video_url);

            String url = Base64.encodeBase64String(file_url.getBytes("UTF-8"));
            result.put("url", GlobalConfig.getFileServer() + file_url);
            result.put("original", file.getOriginalFilename());
            result.put("name", file.getOriginalFilename());
            result.put("size", file.getSize());
            result.put("type", ext);
            result.put("state", "SUCCESS");

            //{"original":"demo.jpg","name":"demo.jpg","url":"\/server\/ueditor\/upload\/image\/demo.jpg","size":"99697"
            // ,"type":".jpg","state":"SUCCESS"}

            output = mapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("ueditor image upload error : ", e);
        }

        return output;
    }

    /**
     * 广告文件列表
     */
    @RequestMapping("/listFile")
    @ResponseBody
    public String listFile(Integer start, Integer size) {
        Map<String, Object> map = new HashMap<>();
        String output = "";
        try {
//            if (start == null || start == 0) {
//                start = 1;
//            }
//            if (size == null || size == 0) {
//                size = 20;
//            }
//            AdFileExample example = new AdFileExample();
//            example.createCriteria().andOfficeIdEqualTo(UserUtils.getCurrentOfficeId());
//            example.setOrderByClause("create_date desc");
//            Page<AdFile> page = new Page<>(start, size, true);
//            List<AdFile> adFileList = adFileService.selectByExample(example);
//            page.setList(adFileList);
//
//            if (adFileList != null && adFileList.size() > 0) {
//                List<Map<String, Object>> list = new ArrayList<>();
//                for (AdFile adFile : adFileList) {
//                    Map<String, Object> fileMap = new HashMap<>();
//                    fileMap.put("url", GlobalConfig.getFileServer() + adFile.getFilePath());
//                    fileMap.put("original", adFile.getName());
//                    list.add(fileMap);
//                }
//                map.put("list", list);
//            }

            map.put("state", "SUCCESS");
            map.put("start", start * size);
//            map.put("total", page.getCount());
            output = mapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     * 改变list变成图片管理端分页
     *
     * @return
     */
    private Map<String, Object> changeListToMap(List<Map<String, Object>> list,
                                                Integer pageNo, Integer size, String filePath) {
        Map<String, Object> map = new HashMap<>();
        map.put("state", "SUCCESS");
        List<Map<String, Object>> listJieGuo = new ArrayList<>();
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (Map<String, Object> map2 : list) {
            if (map2.get("type").equals("File")) {
                list1.add(map2);
            }
        }

        for (int i = (pageNo - 1) * size; i < list1.size() && i < pageNo * size; i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("url", filePath + list1.get(i).get("name"));
            listJieGuo.add(map1);
        }
        map.put("total", list1.size());
        map.put("list", listJieGuo);
        return map;
    }
}
