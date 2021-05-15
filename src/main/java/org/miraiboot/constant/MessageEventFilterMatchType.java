package org.miraiboot.constant;

public enum MessageEventFilterMatchType {
  /**
   * 忽略
   */
  NULL,
  /**
   * 相同匹配
   */
  EQUALS,
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
