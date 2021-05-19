package org.miraiboot.utils;

import java.io.*;

public class FileUtil {
  private static String RESOURCE_ROOT_FOLDER_PATH;
  private static String CONFIG_ROOT_FOLDER_PATH;
  private static String BOT_DEVICE_FOLDER_PATH;
  /* linux: "/" windows: "\\" */
  public static String SYSTEM_PATH_DIV;

  private static final FileUtil INSTANCE = new FileUtil();

  public static void init(Class<?> target) {
    SYSTEM_PATH_DIV = File.separator;
    String base = getJARRootPath(target) + SYSTEM_PATH_DIV;
    RESOURCE_ROOT_FOLDER_PATH = base + "data";
    CONFIG_ROOT_FOLDER_PATH = base + "config";
    String dBRootFolderPath = CONFIG_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + "dbs";
    // 初始化资源文件夹
    File resourceFileDir = new File(RESOURCE_ROOT_FOLDER_PATH);
    if (!resourceFileDir.exists()) resourceFileDir.mkdirs();
    // 初始化配置文件夹
    File configFileDir = new File(CONFIG_ROOT_FOLDER_PATH);
    if (!configFileDir.exists()) configFileDir.mkdirs();
    // 初始化机器人device文件位置文件夹
    BOT_DEVICE_FOLDER_PATH = CONFIG_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + "bots";
    File botDeviceFileDir = new File(BOT_DEVICE_FOLDER_PATH);
    if (!botDeviceFileDir.exists()) botDeviceFileDir.mkdirs();
    //初始化db文件夹
    File dBFileDir = new File(dBRootFolderPath);
    if (!dBFileDir.exists()) dBFileDir.mkdirs();
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

  public String getBotDeviceFilePath(long botId, String deviceFileName) {
    String s = BOT_DEVICE_FOLDER_PATH + SYSTEM_PATH_DIV + botId;
    File botDeviceFile = new File(s);
    if (!botDeviceFile.exists()) botDeviceFile.mkdir();
    return s + SYSTEM_PATH_DIV + deviceFileName;
  }

  public File getConfigFile() {
    File configFile = new File(CONFIG_ROOT_FOLDER_PATH + "/application.yml");
    return configFile.exists() ? configFile : null;
  }

  public File createConfigFile() {
    File configFile = new File(CONFIG_ROOT_FOLDER_PATH + "/application.yml");
    if (!configFile.exists()) {
      try {
        if (!configFile.createNewFile()) return null;
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
        return null;
      }
    }
    return configFile;
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
