package egs.task.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Date;
import java.util.Random;

public class FileUtilForTask {

    public static String saveFile(String fileDirectory, MultipartFile file) {
        try {
            final String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                String filename = ((new Date()).getTime()) + RandomStringUtils.random(20, true, false) +
                        Math.abs(new Random().nextInt()) + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                try {
                    InputStream input = file.getInputStream();
                    ImageIO.read(input);
                    IOUtils.toByteArray(input);
                } catch (IOException e) {
                    throw new IOException("Couldn't upload Image");
                }
                File files = new File(fileDirectory);
                if (!files.exists()) {
                    if (!files.mkdirs()) {
                        new File(filename + filename).mkdirs();
                    }
                }
                FileOutputStream output = new FileOutputStream(fileDirectory + filename);
                output.write(file.getBytes());
                return filename;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] loadFile(String filename, String dirName, int width, int height) throws IOException {
        InputStream in = new FileInputStream(dirName + filename);
        return IOUtils.toByteArray(in);
    }

}
