package net.diyigemt.miraiboot.entity;

import lombok.Data;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.constant.EventType;
import net.diyigemt.miraiboot.constant.MessageFilterMatchType;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <h2>用于保存MessageFilter的参数</h2>
 * @see MessageFilter
 * @author diyiegmt
 * @since 1.0.0
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

  public boolean check(MessageEventPack eventPack, String source) {
    if (!bots.isEmpty() && !bots.contains(String.valueOf(eventPack.getBotId()))) return false;
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
    String id = String.valueOf(eventPack.getSenderId());
    if (!accounts.isEmpty() && !accounts.contains(id)) return false;
    if (eventPack.getEventType() == EventType.GROUP_MESSAGE_EVENT) return checkGroup(eventPack);
    return true;
  }

  public boolean checkGroup(MessageEventPack pack) {
    String group = String.valueOf(pack.getSubject().getId());
    if (!groups.isEmpty() && !groups.contains(group)) return false;
    boolean at = false;
    boolean atAll = false;
    boolean atAny = false;
    for (SingleMessage message : pack.getMessage()) {
      if (message instanceof At) {
        atAny = true;
        if (at) continue;
        at = ((At) message).getTarget() == pack.getBotId();
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
