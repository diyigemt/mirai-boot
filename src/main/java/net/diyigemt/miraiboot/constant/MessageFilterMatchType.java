package net.diyigemt.miraiboot.constant;

import net.diyigemt.miraiboot.annotation.MessageFilter;

/**
 * <h2>消息过滤类型</h2>
 * @author diyigemt
 * @since 1.0.0
 * @see MessageFilter
 */
public enum MessageFilterMatchType {
  /**
   * 忽略
   */
  NULL,
  /**
   * 相同匹配
   */
  EQUALS,
  /**
   * 忽略大小写的相同匹配
   */
  EQUALS_IGNORE_CASE,
  /**
   * 包含匹配
   */
  CONTAINS,
  /**
   * 开头匹配
   */
  STARTS_WITH,
  /**
   * 结尾匹配
   */
  ENDS_WITH,
  /**
   * 正则全文匹配
   */
  REGEX_MATCHES,
  /**
   * 正则查找匹配
   */
  REGEX_FIND
}
