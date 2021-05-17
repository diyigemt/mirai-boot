package org.miraiboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.SingleMessage;
import org.miraiboot.annotation.MessageFilter;
import org.miraiboot.constant.MessageFilterMatchType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
  private boolean isAtAll;
  private boolean isAtAny;

  public MessageFilterItem() {
    this.accounts = new HashSet<String>();
    this.groups = new HashSet<String>();
    this.bots = new HashSet<String>();
  }

  public MessageFilterItem(MessageFilter filter) {
    this();
    value = filter.value();
    matchType = filter.matchType();
    addAccounts(filter.accounts());
    addGroups(filter.groups());
    addBots(filter.bots());
    isAt = filter.isAt();
    isAtAll = filter.isAtAll();
    isAtAny = filter.isAtAny();
  }

  public void addAccounts(String[] accounts) {
    this.accounts.addAll(Arrays.asList(accounts));
  }

  public void addGroups(String[] groups) {
    this.groups.addAll(Arrays.asList(groups));
  }

  public void addBots(String[] bots) {
    this.bots.addAll(Arrays.asList(bots));
  }

  public boolean check(MessageEvent event, String source) {
    if (!bots.isEmpty() && !bots.contains(String.valueOf(event.getBot().getId()))) return false;
    boolean res = false;
    switch (matchType) {
      case NULL:
        res = true;
        break;
      case EQUALS:
        res = source.equals(value);
        break;
      case EQUALS_IGNORE_CASE:
        res = source.equalsIgnoreCase(value);
        break;
      case CONTAINS:
        res = source.contains(value);
        break;
      case STARTS_WITH:
        res = source.startsWith(value);
        break;
      case ENDS_WITH:
        res = source.endsWith(value);
        break;
      case REGEX_MATCHES:
        Pattern p1 = Pattern.compile(value);
        res = p1.matcher(source).matches();
        break;
      case REGEX_FIND:
        Pattern p2 = Pattern.compile(value);
        res = p2.matcher(source).find();
        break;
    }
    if (!res) return false;
    String id = String.valueOf(event.getSender().getId());
    if (!accounts.isEmpty() && !accounts.contains(id)) return false;
    if (event instanceof GroupMessageEvent) return checkGroup((GroupMessageEvent) event);
    return true;
  }

  public boolean checkGroup(GroupMessageEvent event) {
    String group = String.valueOf(event.getSubject().getId());
    if (!groups.isEmpty() && !groups.contains(group)) return false;
    boolean at = false;
    boolean atAll = false;
    boolean atAny = false;
    for (SingleMessage message : event.getMessage()) {
      if (message instanceof At) {
        atAny = true;
        if (at) continue;
        at = ((At) message).getTarget() == event.getBot().getId();
      }
      if (message instanceof AtAll) {
        if (atAll) continue;
        atAll = true;
      }
    }
    if (isAt && !at) return false;
    if (isAtAll && !atAll) return false;
    if (isAtAny && !atAny) return false;
    return true;
  }
}
