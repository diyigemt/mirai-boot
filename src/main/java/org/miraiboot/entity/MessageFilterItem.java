package org.miraiboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.miraiboot.constant.MessageFilterMatchType;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于保存MessageFilter的参数
 * @see org.miraiboot.annotation.MessageFilter
 */
@Data
public class MessageFilterItem {
  private String value;
  private MessageFilterMatchType matchType;
  private Set<String> accounts;
  private Set<String> groups;
  private Set<String> bots;
  private boolean isAt;
  private boolean isAtAny;

  public MessageFilterItem() {
    this.accounts = new HashSet<String>();
    this.groups = new HashSet<String>();
    this.bots = new HashSet<String>();
  }
}
