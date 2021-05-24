package net.diyigemt.miraiboot.constant;

/**
 * <h2>全局变量默认值以及变量名</h2>
 * @author diyigemt
 * @since 1.0.0
 */
public class ConstantGlobal {
  /**
   * 配置文件中配置指令默认开头的字段名
   */
  public static final String DEFAULT_COMMAND_START = "DEFAULT_COMMAND_START";
  /**
   * 配置文件中配置上下文监听超时时间的字段名
   */
  public static final String DEFAULT_EVENT_NET_TIMEOUT = "DEFAULT_EVENT_NET_TIMEOUT";
  /**
   * 默认上下文监听超时时间 可在配置文件中更改
   */
  public static final long DEFAULT_EVENT_NET_TIMEOUT_TIME = 5 * 60 * 1000L;
}
