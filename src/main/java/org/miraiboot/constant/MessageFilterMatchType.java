package org.miraiboot.constant;

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