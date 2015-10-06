package me.kingingo.kcore.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UtilFile
{
	
	public static void listDir(File dir) {

		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				System.out.print(files[i].getAbsolutePath());
				if (files[i].isDirectory()) {
					System.out.print(" (Ordner)\n");
					listDir(files[i]); // ruft sich selbst mit dem 
						// Unterverzeichnis als Parameter auf
					}
				else {
					System.out.print(" (Datei)\n");
				}
			}
		}
	}
	
	public static void copyDir(File quelle, File ziel) throws FileNotFoundException, IOException {
		
		File[] files = quelle.listFiles();
		File newFile = null; // in diesem Objekt wird für jedes File der Zielpfad gespeichert.
				     // 1. Der alte Zielpfad
				     // 2. Das systemspezifische Pfadtrennungszeichen
				     // 3. Der Name des aktuellen Ordners/der aktuellen Datei
		ziel.mkdirs();	     // erstellt alle benötigten Ordner
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
					newFile = new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + files[i].getName());
				if (files[i].isDirectory()) {
					copyDir(files[i], newFile);
				}
				else {
					copyFile(files[i], newFile);
				}
			}
		}
	}
	
	public long getDirSize(File dir) {
		
		long size = 0;
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					size += getDirSize(files[i]); // Gesamtgröße des Verzeichnisses aufaddieren
				}
				else {
					size += files[i].length(); // Größe der Datei aufaddieren
				}
			}
		}
		return size;
	}
	
	public static void copyFile(File file, File ziel) throws FileNotFoundException, IOException {
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(ziel, true));
		int bytes = 0;
		while ((bytes = in.read()) != -1) { // Datei einlesen
			out.write(bytes); // Datei schreiben
		}
		in.close();
		out.close();
	}
	
	public static boolean existPath(File path){
		return path.exists();
	}
	
	public static void createFile(File pfad,String name,String[] list){
		pfad.mkdirs();
		createFile(new File(pfad.getAbsolutePath()+File.separator+name), list);
	}
	
	public static void createFile(File pfad,String name,String list){
		pfad.mkdirs();
		createFile(new File(pfad,name), list);
	}
	
	public static void createFile(File pfad,String s){
		try {
			PrintStream fileTimings = new PrintStream( pfad );
			String[] list = s.split("-/-");
			for(String l : list){
				fileTimings.println(l);
			}
			fileTimings.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFile(File pfad,String[] list){
		try {
			FileWriter fstream = new FileWriter(pfad);
			BufferedWriter out = new BufferedWriter(fstream); 
			
			for(String l : list){
				out.write(l);
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(File pfad,String name){
		deleteFile(new File(pfad.getAbsolutePath()+File.separator+name));
	}
	
	public static void deleteFile(File pfad){
		if(pfad.exists()){
			pfad.delete();
		}
	}
	
	public static String[] loadFile(File pfad,String name){
		return loadFile(new File(pfad.getAbsolutePath()+File.separator+name));
	}
	
	public static String[] loadFile(File pfad){
		try {
			FileInputStream fstream = new FileInputStream(pfad);
			DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    Object[] objects=br.lines().toArray().clone();
		    
		    fstream.close();
		    in.close();
		    br.close();
		    
		    String[] list = new String[objects.length];
		    
		    int a = 0;
		    for(Object i : objects){
		    	if(i instanceof String){	
		    		list[a]=((String)i);
		    		a++;
			    }
		    }
		    
		    objects=null;
		    a=0;
		    fstream=null;
		    in=null;
		    br=null;
		    
		    return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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