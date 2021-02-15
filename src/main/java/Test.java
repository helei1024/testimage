import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.*;
import com.drew.metadata.photoshop.PhotoshopDirectory;

import java.awt.*;
import java.io.File;
import java.util.Collection;

/**
 * @author HeLei
 * @create 2020/8/15 3:19
 **/
public class Test {
    public static void main(String[] args) throws Exception {

        System.out.println("分支dev commit1");
        System.out.println("主线master 添加 commit1");
        
        System.out.println("github add commmit1");

        System.out.println("master commit2");
        File jpegFile = new File("D:/cap_images/IMG_00000650.jpg");
        Metadata meta = JpegMetadataReader.readMetadata(jpegFile);
        GpsDirectory gps = meta.getFirstDirectoryOfType(GpsDirectory.class);

        if (gps != null) {
            Collection<Tag> tags = gps.getTags();
            for (Tag tag : tags) {
                String kv =  String.format("%s = %s",tag.getTagName(),tag.getDescription());
                System.out.println(kv);
            }
        }


        File file = new File("D:/cap_images/IMG_00000650.jpg");
        //File file = new File("C:/Users/Administrator/Desktop/ttt.jpg");

        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            System.out.println("属性组：" + directory.getName());
            for (Tag tag : directory.getTags()) {
                String kv =  String.format("%s = %s",tag.getTagName(),tag.getDescription());
                System.out.println(kv);
            }
        }


        System.out.println("++++++++++++++++++ EXIF ++++++++++++++++++++");
        ExifIFD0Directory exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if(null != exif){
            //循环输出
            exif.getTags().forEach(System.out::println);
            if(exif.containsTag(ExifIFD0Directory.TAG_MAKE)){
                System.out.println("Make:" + exif.getDescription(ExifIFD0Directory.TAG_MAKE));
            }
        }
        System.out.println("ExifSubIFDDirectory：");
        Directory exifSub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if(null != exifSub){
            //循环输出
            exifSub.getTags().forEach(System.out::println);
        }
        System.out.println("ExifInteropDirectory：");
        Directory exifInterop = metadata.getFirstDirectoryOfType(ExifInteropDirectory.class);
        if(null != exifInterop){
            //循环输出
            exifInterop.getTags().forEach(System.out::println);
        }
        System.out.println("ExifThumbnailDirectory：");
        Directory exifThumbnail = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
        if(null != exifThumbnail){
            //循环输出
            exifThumbnail.getTags().forEach(System.out::println);
        }
    }
}
