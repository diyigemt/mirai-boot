package org.miraiboot.utils;

import java.io.*;

/**
 * <h2>文件管理工具类</h2>
 * 单列实例
 * <p>用于获取资源文件夹下的资源文件</p>
 * <p>具体使用请参阅方法注释</p>
 * @author diyigemt
 * @since 1.0.0
 */
public class FileUtil {
  /**
   * 资源文件根目录
   */
  private static String RESOURCE_ROOT_FOLDER_PATH;
  /**
   * 配置文件根目录
   */
  private static String CONFIG_ROOT_FOLDER_PATH;
  /**
   * Bot设备信息根目录
   */
  private static String BOT_DEVICE_FOLDER_PATH;
  // 系统目录分隔符
  /* linux: "/" windows: "\\" */
  public static String SYSTEM_PATH_DIV;

  private static final FileUtil INSTANCE = new FileUtil();

  /**
   * <h2>初始化工具类</h2>
   * 初始化和创建文件夹
   * @param mainClass 主程序入口
   */
  public static void init(Class<?> mainClass) {
    SYSTEM_PATH_DIV = File.separator;
    String base = getJARRootPath(mainClass) + SYSTEM_PATH_DIV;
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

  /**
   * <h2>获取全局实例</h2>
   * @return 全局实例
   */
  public static FileUtil getInstance() {
    return INSTANCE;
  }

  /**
   * <h2>根据文件名获取资源文件</h2>
   * @param fileName 文件名(包括文件拓展名)
   * @return 获取到的文件
   */
  public File getResourceFile(String fileName) {
    return getResourceFile(fileName, null);
  }

  /**
   * <h2>根据文件名获取音频资源文件</h2>
   * @param fileName 文件名(包括文件拓展名)
   * @return 获取到的文件
   */
  public File getVoiceResourceFile(String fileName, String fold) {
    if (fold == null) return getResourceFile(fileName, "voices");
    return getResourceFile(fileName, "voices" + SYSTEM_PATH_DIV + fold);
  }

  /**
   * <h2>根据文件名获取图片资源文件</h2>
   * @param fileName 文件名(包括文件拓展名)
   * @return 获取到的文件
   */
  public File getImageResourceFile(String fileName) {
    return getResourceFile(fileName, "images");
  }

  /**
   * <h2>根据文件名获取图片资源文件</h2>
   * @param fileName 文件名(包括文件拓展名)
   * @param fold 所在的子文件夹
   * @return 获取到的文件
   */
  public File getImageResourceFile(String fileName, String fold) {
    return getResourceFile(fileName, "images" + SYSTEM_PATH_DIV + fold);
  }

  /**
   * <h2>根据文件名获取资源文件</h2>
   * @param fileName 文件名(包括文件拓展名)
   * @param fold 所在的子文件夹
   * @return 获取到的文件
   */
  public File getResourceFile(String fileName, String fold) {
    if (fold == null) return new File(RESOURCE_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + fileName);
    return new File(RESOURCE_ROOT_FOLDER_PATH + SYSTEM_PATH_DIV + fold + SYSTEM_PATH_DIV + fileName);
  }

  /**
   * <h2>根据Bot的qq号获取设备文件</h2>
   * @param botId Bot的qq号
   * @param deviceFileName 设备文件名(包括文件拓展名)
   * @return 文件相对路径
   */
  public String getBotDeviceFilePath(long botId, String deviceFileName) {
    String s = BOT_DEVICE_FOLDER_PATH + SYSTEM_PATH_DIV + botId;
    File botDeviceFile = new File(s);
    if (!botDeviceFile.exists()) botDeviceFile.mkdir();
    return s + SYSTEM_PATH_DIV + deviceFileName;
  }

  /**
   * <h2>获取配置文件</h2>
   * @return 配置文件 没有获取到为null
   */
  public File getConfigFile() {
    File configFile = new File(CONFIG_ROOT_FOLDER_PATH + "/application.yml");
    return configFile.exists() ? configFile : null;
  }

  /**
   * <h2>创建配置文件</h2>
   * 没有找到配置文件时调用
   * @return 创建好的配置文件
   */
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
      } catch (NullPointerException ignored) {

      }
    }
    return configFile;
  }

  /**
   * <h2>获取jar包所在的全路径</h2>
   * @param target 需要获取的jar包里的class
   * @return 全路径
   */
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
