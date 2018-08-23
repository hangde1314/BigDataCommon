package com.hzgc.seemmo.util;

import lombok.Data;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

@Data
public class CutImageUtil {


    // ===源图片路径名称如:c:\1.jpg
    private String srcpath;

    // ===剪切图片存放路径名称.如:c:\2.jpg
    private String subpath;

    // ===剪切点x坐标
    private int x;

    private int y;

    // ===剪切点宽度
    private int width;

    private int height;

    public CutImageUtil() {

    }

    public CutImageUtil(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * 对图片裁剪，并把裁剪完新图片保存 。
     */
    public byte[] cut() throws IOException {

        FileInputStream is = null;
        ImageInputStream iis = null;

        try {
            // 读取图片文件
            is = new FileInputStream(srcpath);

            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
             * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
             *（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);

            /*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索'。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
             * 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
             * ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x, y, width, height);


            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
             * 它作为一个完整的 BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);
            // 保存新图片
//            ImageIO.write(bi,"jpg",new File(subpath));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", os);
            byte[] bytes = os.toByteArray();
            return bytes;
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
    }

    //    public static void main(String[] args) {
//        CutImageUtil cutImageUtil = new CutImageUtil(2051, 676, 288, 553);
//        cutImageUtil.srcpath = "C:\\Users\\g10255\\Desktop\\100.jpg";
//        cutImageUtil.subpath = "C:\\Users\\g10255\\Desktop\\103.jpg";
//
//        try {
//            String fileSuffix = cutImageUtil.getFileSuffix("C:\\Users\\g10255\\Desktop\\100.jpg");
//            System.out.println(fileSuffix);
//            cutImageUtil.cut();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private String getFileSuffix(final String path) throws IOException {
        String result = "";
        String hex = "";
        if (path != null) {
            File image = new File(path);
            InputStream is = new FileInputStream(image);
            byte[] bt = new byte[2];
            is.read(bt);
            hex = bytesToHexString(bt);
            is.close();
            if (hex.equals("ffd8")) {
                result = "jpg";
            } else if (hex.equals("4749")) {
                result = "gif";
            } else if (hex.equals("8950")) {
                result = "png";
            }
        }

        return result;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}