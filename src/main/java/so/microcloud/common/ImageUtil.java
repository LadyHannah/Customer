package so.microcloud.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	
	 /**
     * 直接指定压缩后的宽高：
     * (先保存原文件，再压缩、上传)
     * 壹拍项目中用于二维码压缩
     * @param oldFile 要进行压缩的文件全路径
     * @param width 压缩后的宽度
     * @param height 压缩后的高度
     * @param quality 压缩质量
     * @param smallIcon 文件名的小小后缀(注意，非文件后缀名称),入压缩文件名是yasuo.jpg,则压缩后文件名是yasuo(+smallIcon).jpg
     * @return 返回压缩后的文件的全路径
     */
    public static String zipImageFile(String oldFile, int width, int height,
            float quality, String smallIcon) {
        if (oldFile == null) {
            return null;
        }
        String newImage = null;
        try {
            /**对服务器上的临时文件进行处理 */
            Image srcFile = ImageIO.read(new File(oldFile));
            /** 宽,高设定 */
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(srcFile, 0, 0, width, height, null);
            String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
            /** 压缩后的文件名 */
            newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
            /** 压缩之后临时存放位置 */
            FileOutputStream out = new FileOutputStream(newImage);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
            /** 压缩质量 */
            jep.setQuality(quality, true);
            encoder.encode(tag, jep);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newImage;
    }
	/**
     * 等比例压缩算法： 
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     * @param srcURL 原图地址
     * @param deskURL 缩略图地址
     * @param comBase 压缩基数
     * @param scale 压缩限制(宽/高)比例  一般用1：
     * 当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
     * @throws Exception
     * @author shenbin
     * @createTime 2014-12-16
     * @lastModifyTime 2014-12-16
     */
    public static boolean saveMinPhoto(String srcURL, String deskURL, double comBase,
            double scale)  {
        File srcFile = new java.io.File(srcURL);
        Image src;
		try {
			src = ImageIO.read(srcFile);
			int srcHeight = src.getHeight(null);
	        int srcWidth = src.getWidth(null);
	        int deskHeight = 0;// 缩略图高
	        int deskWidth = 0;// 缩略图宽
	        double srcScale = (double) srcHeight / srcWidth;
	        /**缩略图宽高算法*/
	        if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
	            if (srcScale >= scale || 1 / srcScale > scale) {
	                if (srcScale >= scale) {
	                    deskHeight = (int) comBase;
	                    deskWidth = srcWidth * deskHeight / srcHeight;
	                } else {
	                    deskWidth = (int) comBase;
	                    deskHeight = srcHeight * deskWidth / srcWidth;
	                }
	            } else {
	                if ((double) srcHeight > comBase) {
	                    deskHeight = (int) comBase;
	                    deskWidth = srcWidth * deskHeight / srcHeight;
	                } else {
	                    deskWidth = (int) comBase;
	                    deskHeight = srcHeight * deskWidth / srcWidth;
	                }
	            }
	        } else {
	            deskHeight = srcHeight;
	            deskWidth = srcWidth;
	        }
	        BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
	        tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
	        FileOutputStream deskImage = new FileOutputStream(deskURL); //输出到文件流
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
	        encoder.encode(tag); //近JPEG编码
	        deskImage.close();
	        return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
    
    
    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param imgsrc 源图片地址
     * @param imgdist 目标图片地址
     */
    public static void reduceImg(String imgsrc, String imgdist) {
        try {
            File srcfile = new File(imgsrc);
            // 检查图片文件是否存在
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
            }
            int[] results = getImgWidthHeight(srcfile);

            int widthdist = results[0];
            int heightdist = results[1];
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(srcfile);

            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage( widthdist,  heightdist, BufferedImage.TYPE_INT_RGB);

            // 这边是压缩的模式设置
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            //创建文件输出流
            FileOutputStream out = new FileOutputStream(imgdist);
            //将图片按JPEG压缩，保存到out中
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            //关闭文件输出流
            out.close();
        } catch (Exception ef) {
            ef.printStackTrace();
        }
    }

    /**
     * 获取图片宽度和高度
     *
     * @param file 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = { 0, 0 };
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] =src.getWidth(null);
            // 得到源图片高
            result[1] =src.getHeight(null);
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }
    
    public static File thirdCompress( File file,String thumb) throws NumberFormatException, IOException {
        double size;
        String filePath = file.getAbsolutePath();

        int width = getImageSize(filePath)[0];
        int height = getImageSize(filePath)[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

      /*  width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;*/

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150) return file;

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200) return file;

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }
        File isSave=saveImage(file.getAbsolutePath(),thumb,(long)size, thumbW, thumbH);
        return isSave!=null?file:null;
    }
    
    private static File saveImage(String srcFile,String filePath, long size,int width,int highth) {
        File result = new File(filePath);
       
//        if (!result.exists() && !result.mkdirs()) return null;
        Image src;
		try {
			 if(!result.getParentFile().exists()){
		        	result.getParentFile().mkdirs();//创建父级文件路径
		        	result.createNewFile();//创建文件
		            System.out.println(result.exists());
		        }
			src = ImageIO.read(new File(srcFile));
	        BufferedImage tag = new BufferedImage(width, highth, BufferedImage.TYPE_3BYTE_BGR);
	        tag.getGraphics().drawImage(src, 0, 0, width, highth, null); //绘制缩小后的图
	        FileOutputStream deskImage = new FileOutputStream(result); //输出到文件流
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
	        encoder.encode(tag); //近JPEG编码
	        deskImage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new File(srcFile);
    }

    /**
     * obtain the image's width and height
     *
     * @param imagePath the path of image
     */
    public static int[] getImageSize(String imagePath) {
        int[] res = new int[2];

       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);*/

        
        File picture = new File(imagePath);
        BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
			 System.out.println(String.format("%.1f",picture.length()/1024.0));// 源图大小
		        System.out.println(sourceImg.getWidth()); // 源图宽度
		        System.out.println(sourceImg.getHeight()); // 源图高度

		        res[0] = sourceImg.getWidth();
		        res[1] = sourceImg.getHeight();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return res;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
//		saveMinPhoto("D:\\QQPCmgr\\Desktop\\test\\12241025.jpg", "D:\\QQPCmgr\\Desktop\\test\\09241025.jpg", 4346, 0.8);
//		zipImageFile("D:\\QQPCmgr\\Desktop\\test\\12241025.jpg", 4346, 2897, 0.5f, "_miniPic");
//    	reduceImg("D:\\QQPCmgr\\Desktop\\test\\12241025.jpg","D:\\QQPCmgr\\Desktop\\test\\08241025.jpg");
//    	thirdCompress(new File("D:\\QQPCmgr\\Desktop\\test\\xihu.jpg"));
    }
}
