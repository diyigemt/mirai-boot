package org.miraiboot.permission;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于存储@接口信息
 * 优先级 at-> permission table -> blocks -> allows -> isStrictRestricted -> isGroupOwnerOnly -> isAdminOnly
 */
public class Permission {

  //允许操作的用户id
  private Set<String> allows;

  //禁止操作的用户id
  private Set<String> blocks;

  //是否严格检查身份（群主和管理员之间的权限将严格划分）
  boolean isStrictRestricted;

  //是否只允许群主操作
  private boolean isGroupOwnerOnly;

  //是否只允许管理员操作
  private boolean isAdminOnly;

  public Permission() {
    this.allows = new HashSet<String>();
    this.blocks = new HashSet<String>();
    this.isGroupOwnerOnly = false;
    this.isAdminOnly = false;
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

  public boolean isStrictRestricted() {
    return isStrictRestricted;
  }

  public void setStrictRestricted(boolean strictRestricted) {
    isStrictRestricted = strictRestricted;
  }
}
