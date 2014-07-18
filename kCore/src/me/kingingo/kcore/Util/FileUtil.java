package me.kingingo.kcore.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil
{
	
	public static boolean existPath(File path){
		return path.exists();
	}
	
	 public static void download(URL source, File destination) throws IOException {

	        InputStream inputStream = null;
	        OutputStream outputStream = null;
	        try {
	            inputStream = source.openStream();
	            outputStream = new FileOutputStream(destination);
	            outputStream.flush();

	            byte[] tempBuffer = new byte[4096];
	            int counter;
	            while ( (counter = inputStream.read(tempBuffer)) > 0) {
	                outputStream.write(tempBuffer, 0, counter);
	                outputStream.flush();
	            }
	        } catch (IOException e) {
	            throw e;
	        } finally {
	            if (inputStream != null) {
	                try {
	                    inputStream.close();
	                } catch (IOException e) {
	                    // Ignore
	                }
	            }
	            if (outputStream != null) {
	                try {
	                    outputStream.close();
	                } catch (IOException e) {
	                    // Ignore
	                }
	            }
	        }
	    }
	
	public static void unzip(File zip, File destination) throws IOException {

        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(zip);

            for (ZipEntry zipEntry : Collections.list(zipFile.entries())) {
                File file = new File(destination, zipEntry.getName());

                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    new File(file.getParent()).mkdirs();

                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        inputStream = zipFile.getInputStream(zipEntry);
                        outputStream = new FileOutputStream(file);

                        byte[] buffer = new byte[0xFFFF];
                        for (int lenght; (lenght = inputStream.read(buffer)) != -1;) {
                            outputStream.write(buffer, 0, lenght);
                        }
                    } catch (IOException e) {
                        throw e;
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
	
	private static boolean deleteDirectory(File path)
	{
	  if (path.exists()) {
	    File[] files = path.listFiles();
	    for (int i = 0; i < files.length; i++) {
	      if (files[i].isDirectory())
	        deleteDirectory(files[i]);
	      else {
	        files[i].delete();
	      }
	    }
	  }
	  return path.delete();
	}
	
  public static void DeleteFolder(File folder)
  {
    if (!folder.exists()) {
      return;
    }
    File[] files = folder.listFiles();

    if (files != null)
    {
      for (File f : files)
      {
        if (f.isDirectory())
          DeleteFolder(f);
        else {
          f.delete();
        }
      }
    }
    folder.delete();
  }

  public static void CopyToDirectory(File file, String outputDirectory)
  {
    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    BufferedInputStream bufferedInputStream = null;
    try
    {
      fileInputStream = new FileInputStream(file);
      bufferedInputStream = new BufferedInputStream(fileInputStream);

      byte[] buffer = new byte[2048];

      fileOutputStream = new FileOutputStream(outputDirectory + "\\" + file.getName());
      bufferedOutputStream = new BufferedOutputStream(fileOutputStream, buffer.length);
      int size;
      while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) != -1)
      {
        bufferedOutputStream.write(buffer, 0, size);
      }

      bufferedOutputStream.flush();
      bufferedOutputStream.close();
      fileOutputStream.flush();
      fileOutputStream.close();

      bufferedInputStream.close();
      fileInputStream.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();

      if (fileInputStream != null)
      {
        try
        {
          fileInputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (bufferedInputStream != null)
      {
        try
        {
          bufferedInputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (fileOutputStream != null)
      {
        try
        {
          fileOutputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }

      if (bufferedOutputStream != null)
      {
        try
        {
          bufferedOutputStream.close();
        }
        catch (IOException e1)
        {
          e1.printStackTrace();
        }
      }
    }
  }
}