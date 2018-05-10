package com.tiansu.eam.modules.sys.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author wangjl
 * @description 图片处理及压缩工具类,压缩采用google开源Thumbnailator包
 * @create 2017-09-04 8:49
 **/

/*常用接口:    按指定大小把图片进行缩放（会遵循原图高宽比例）
            //按指定大小把图片进行缩和放（会遵循原图高宽比例）
            //此处把图片压成400×500的缩略图
            Thumbnails.of(fromPic).size(400,500).toFile(toPic);//变为400*300,遵循原图比例缩或放到400*某个高度
    按照指定比例进行缩小和放大
    //按照比例进行缩小和放大
        Thumbnails.of(fromPic).scale(0.2f).toFile(toPic);//按比例缩小
        Thumbnails.of(fromPic).scale(2f);//按比例放大
    图片尺寸不变，压缩图片文件大小
    //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
        Thumbnails.of(fromPic).scale(1f).outputQuality(0.25f).toFile(toPic);*/


public class ImagineUtil {

    private static Logger logger=Logger.getLogger(ImagineUtil.class);



//    Thumbnails.of(fromPic).size(400,500).toFile(toPic);






    public static boolean ThumbnailsCompressPic(String inputFile, String outputFile, int size, float quality) {
        File input = new File(inputFile);
        if (input.getParentFile() != null || !input.getParentFile().isDirectory()) {
            //创建文件
            input.getParentFile().mkdirs();
        }
        try {
            Thumbnails.Builder<File> fileBuilder = Thumbnails.of(input).scale(1.0).outputQuality(1.0);
            BufferedImage src = fileBuilder.asBufferedImage();
            if(src.getHeight(null) > size || src.getWidth(null) > size) {
                Thumbnails.Builder<File> builder = Thumbnails.of(input);
                builder.size(size, size); //取最大的尺寸变成size，然后等比缩放
                builder.outputQuality(quality).outputFormat("jpg").toFile(outputFile);
            } else {
                Thumbnails.of(input).scale(1.0).outputQuality(quality).outputFormat("jpg").toFile(outputFile);
            }
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

}