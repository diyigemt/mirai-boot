package org.miraiboot.utils;


import org.miraiboot.Main;
import org.miraiboot.annotation.AutoInit;
import org.miraiboot.interfaces.InitializeUtil;

import java.io.*;
import java.net.URL;

@AutoInit
public class FileUtil implements InitializeUtil {
  private static String RESOURCE_ROOT_FOLDER_PATH;
  /* linux: "/" windows: "\\" */
  public static String SYSTEM_PATH_DIV;

  private static final FileUtil INSTANCE = new FileUtil();

  public static void init(Class<?> target) {
//    File dataFolder = new File(getJARRootPath());
//    RESOURCE_ROOT_FOLDER_PATH = dataFolder.toPath().toString();
//    SYSTEM_PATH_DIV = System.getProperty("os.name").startsWith("win") ? "\\" : "/";
    SYSTEM_PATH_DIV = File.separator;
    RESOURCE_ROOT_FOLDER_PATH = getJARRootPath(target) + SYSTEM_PATH_DIV + "data";
  }

  public static FileUtil getInstance() {
    return INSTANCE;
  }

  public File getResourceFile(String fileName) {
    return getResourceFile(fileName, null);
  }

  public File getVoiceResourceFile(String fileName, String fold) {
    if (fold == null) return getResourceFile(fileName, "voices");
    return getResourceFile(fileName, "voices" + SYSTEM_PATH_DIV + fold);
  }

  public File getImageResourceFile(String fileName) {
    return getResourceFile(fileName, "images");
  }

  public File getImageResourceFile(String fileName, String fold) {
    return getResourceFile(fileName, "images" + SYSTEM_PATH_DIV + fold);
  }

  public File getResourceFile(String fileName, String fold) {
    if (fold == null) return new File(RESOURCE_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + fileName);
    return new File(RESOURCE_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + fold + SYSTEM_PATH_DIV + fileName);
  }

  public boolean checkConfigFileExist(Class<?> target) {
    File configFile = new File(getJARRootPath(target) + "/application.yml");
    return configFile.exists();
  }

  public boolean createConfigFile(Class<?> target) {
    File configFile = new File(getJARRootPath(target) + "/application.yml");
    if (!configFile.exists()) {
      try {
        if (!configFile.createNewFile()) return false;
        InputStream resourceAsStream = FileUtil.class.getResourceAsStream("/application-example.yml");
        FileOutputStream stream = new FileOutputStream(configFile);
        byte[] buf = new byte[4096];
        int len;
        while ((len = resourceAsStream.read(buf)) > 0) {
          stream.write(buf, 0, len);
        }
        stream.close();
        resourceAsStream.close();
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  public static String getJARRootPath(Class<?> target) {
    String path = target.getProtectionDomain().getCodeSource().getLocation().getPath();
    if (System.getProperty("os.name").contains("dows")) {
      path = path.substring(1);
    }
    if (path.contains("jar")) {
      path = path.substring(0, path.lastIndexOf("."));
      return path.substring(0, path.lastIndexOf("/"));
    }
    return path.replace("target/classes/", "");
  }
}
