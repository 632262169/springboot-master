package com.wangbowen.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.wangbowen.common.config.GlobalConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云OSS服务工具类
 * Created by Administrator on 2015/7/23.
 */
public class AliyunOSSUtils {
    private static final String ACCESS_ID = GlobalConfig.getConfig("aliyun.oss.accessId");
    private static final String ACCESS_KEY = GlobalConfig.getConfig("aliyun.oss.accessKey");
    private static final String OSS_ENDPOINT = GlobalConfig.getConfig("aliyun.oss.endpoint_internal");
    public static final String BUCKET_IMAGE = GlobalConfig.getConfig("aliyun.oss.bucket.image");
    public static final String BUCKET_FILE = GlobalConfig.getConfig("aliyun.oss.bucket.file");
    public static final String BUCKET_VIDEO = GlobalConfig.getConfig("aliyun.oss.bucket.video");
    public static final String BUCKET_AUDIO = GlobalConfig.getConfig("aliyun.oss.bucket.audio");

    private static OSSClient client = null;

    static{
        client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param key 私钥
     * @param filePath 文件名称
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static String uploadFile(String bucketName, String key, String filePath)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filePath);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
//        objectMeta.setContentType("image/jpeg");

        InputStream input = new FileInputStream(file);
        PutObjectResult putObjectResult = client.putObject(bucketName, key, input, objectMeta);

//        try {
//            input.close();
//        } catch (Exception e) {
//
//        }
        if (putObjectResult != null) {
            return putObjectResult.getETag();
        }
        return null;
    }

    /**
     * 获取对象
     */
    public static OSSObject getObject(String bucketName, String key) {
        return client.getObject(bucketName, key);
    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param key 私钥
     * @param input 文件流
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static String uploadFile(String bucketName, String key, InputStream input, long fileLength)
            throws OSSException, ClientException, FileNotFoundException {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(fileLength);
        // 可以在metadata中标记文件类型
//        objectMeta.setContentType("image/jpeg");
        PutObjectResult putObjectResult = client.putObject(bucketName, key, input, objectMeta);

        if (putObjectResult != null) {
            return putObjectResult.getETag();
        }
        return null;
    }

    /**
     * 删除文件
     * @param bucketName bucket名称
     * @param key 私钥
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static void deleteFile(String bucketName, String key)
            throws OSSException, ClientException, FileNotFoundException {
        client.deleteObject(bucketName, key);
    }

    public static void downloadAndUpload(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            for (String sourceUrl : urls) {
                System.out.println("原始URL" + sourceUrl);
                if (StringUtils.isBlank(sourceUrl) || !sourceUrl.toLowerCase().startsWith("http")) {
                    continue;
                }
                //将远程图片下载并上传至阿里云
                InputStream imgIS = null;
                ByteArrayOutputStream baos = null;
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

                    String filePath = sourceUrl.replaceAll("http://1do-image.oss-cn-hangzhou.aliyuncs.com/", "");
                    try {
                        System.out.println("上传" + filePath);
                        AliyunOSSUtils.uploadFile(AliyunOSSUtils.BUCKET_IMAGE, filePath, imgIS, imgIS.available());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        uploadFile(BUCKET_IMAGE, "testDir/201507/test33.jsp", "D:/QQ图片20150721160504.png");

        List<String> pathList = new ArrayList<>();

        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008143326239.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008143929808.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009093756057.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008150912933.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008152536167.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008152857411.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008153515634.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008154345893.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009093421870.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009095515861.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009100022772.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009102552759.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009135055412.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008141147334.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008140952608.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008142748735.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161008/20161008145230840.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009105624643.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009112904151.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009151548249.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009132318845.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009151641015.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161009/20161009141844748.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161013/20161013093434890.JPG");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161015/20161015191206982.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161015/20161015185833884.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161015/20161015190502232.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161016/20161016114756500.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161016/20161016115903732.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161017/20161017172043739.png");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161018/20161018174509062.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161019/20161019183222299.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161021/20161021101603105.png");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161026/20161026181242638.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161030/20161030035418978.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161101/20161101174351996.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161102/20161102173920422.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161103/20161103170803891.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161103/20161103173352812.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161114/20161114090415571.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161116/20161116175049782.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161120/20161120231247327.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161129/2016112917361425778.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20161201/20161201173217549306.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302114938534619.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302115327459287.JPG");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302140029996317.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302150200776902.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302152428840350.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302154409751174.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170302/20170302154834127814.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170309/20170309100244000241.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/onedoo/20170309/20170309102013272427.jpg");



        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008143326239.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008143929808.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009093756057.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008150912933.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008152536167.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008152857411.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008153515634.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008154345893.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009093421870.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009095515861.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009100022772.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009102552759.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009135055412.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008141147334.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008140952608.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008142748735.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161008/20161008145230840.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009105624643.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009112904151.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009151548249.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009132318845.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009151641015.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161009/20161009141844748.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161013/20161013093434890.JPG");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161015/20161015191206982.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161015/20161015185833884.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161015/20161015190502232.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161016/20161016114756500.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161016/20161016115903732.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161017/20161017172043739.png");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161018/20161018174509062.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161019/20161019183222299.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161021/20161021101603105.png");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161026/20161026181242638.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161030/20161030035418978.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161101/20161101174351996.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161102/20161102173920422.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161103/20161103170803891.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161103/20161103173352812.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161114/20161114090415571.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161116/20161116175049782.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161120/20161120231247327.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161129/2016112917361425778.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20161201/20161201173217549306.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302114938534619.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302115327459287.JPG");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302140029996317.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302150200776902.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302152428840350.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302154409751174.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170302/20170302154834127814.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170309/20170309100244000241.jpg");
        pathList.add("http://1do-image.oss-cn-hangzhou.aliyuncs.com/thumbs/onedoo/20170309/20170309102013272427.jpg");

        downloadAndUpload(pathList);
    }
}
