package org.miraiboot.permission;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于存储@接口信息
 * 优先级 at-> permission table -> blocks -> allows -> isGroupOwnerOnly -> isAdminOnly
 */
public class Permission {

  //允许操作的用户id
  private Set<String> allows;

  //禁止操作的用户id
  private Set<String> blocks;

  //是否只允许群主操作
  private boolean isGroupOwnerOnly;

  //是否只允许管理员操作
  private boolean isAdminOnly;

  //是否接受At才操作
  private boolean isAt;

  public Permission() {
    this.allows = new HashSet<String>();
    this.blocks = new HashSet<String>();
    this.isGroupOwnerOnly = false;
    this.isAdminOnly = false;
    this.isAt = true;
  }

  public boolean isAt() {
    return isAt;
  }

  public void setAt(boolean at) {
    isAt = at;
  }

  public Set<String> getAllows() {
    return allows;
  }

  public void setAllows(Set<String> allows) {
    this.allows = allows;
  }

  public Set<String> getBlocks() {
    return blocks;
  }

  public void setBlocks(Set<String> blocks) {
    this.blocks = blocks;
  }

  public boolean isGroupOwnerOnly() {
    return isGroupOwnerOnly;
  }

  public void setGroupOwnerOnly(boolean groupOwnerOnly) {
    isGroupOwnerOnly = groupOwnerOnly;
  }

  public boolean isAdminOnly() {
    return isAdminOnly;
  }

  public void setAdminOnly(boolean adminOnly) {
    isAdminOnly = adminOnly;
  }
}
