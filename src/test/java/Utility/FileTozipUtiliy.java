package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileTozipUtiliy {
    public void FiletoZip(String SourceFileTozip,String destnationOfZipFile) throws IOException {

        File f1 = new File(SourceFileTozip);
        FileInputStream fis = new FileInputStream(f1);
        File f2 = new File(destnationOfZipFile);
        FileOutputStream fos = new FileOutputStream(f2);
        ZipOutputStream zipout = new ZipOutputStream(fos);
        ZipEntry zipentry = new ZipEntry(f1.getName());
        zipout.putNextEntry(zipentry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes ))>=0) {
            zipout.write(bytes,0,length);
        }
        zipout.close();
        fis.close();
        fos.close();
        System.out.println("zip complete");
    }
    public void ZipToFile(String ZipFilepath,String UnzipDirectory) throws IOException {


        FileInputStream fis = new FileInputStream(ZipFilepath);
        ZipInputStream zis = new ZipInputStream(fis);
        byte[] buffer = new byte[1024];
        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {
            String entryname = entry.getName();
            String entrypath = UnzipDirectory + File.separator + entryname;
            File entryfile = new File(entrypath);

            if (entry.isDirectory()) {
                entryfile.mkdirs();
            } else {
                File parent = entryfile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(entryfile);

                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead); // Write the actual bytes read
                }
                fos.close();
            }
        }
        zis.close();
    }
}

