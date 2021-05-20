package org.miraiboot.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * <h2>用于权限操作的数据类</h2>
 * @author diyigemt
 * @since 1.0.0
 */
@DatabaseTable(tableName = "permission")
public class PermissionItem {
  // 主键id
  @DatabaseField(generatedId = true, columnName = "id", unique = true)
  private int id;
  // EventHandler注解中的permissionIndex
  @DatabaseField(columnName = "command_id")
  private int commandId;
  // 发送者qq号
  @DatabaseField(columnName = "sender_id")
  private String senderId;
//  @DatabaseField(columnName = "permit", defaultValue = "false")
//  private boolean permit;
  // 权限等级
  @DatabaseField(columnName = "permit", defaultValue = "-1")
  private int permits = 0;
  // 剩余使用次数 默认-1代表无限次 用于临时权限
  @DatabaseField(columnName = "remain")
  private int remain = -1;

  public PermissionItem() {}

  public PermissionItem(long senderId, String commandId) {
    this.senderId = String.valueOf(senderId);
    this.commandId = Integer.parseInt(commandId);
  }

  public PermissionItem(String senderId, String commandId) {
    this.senderId = senderId;
    this.commandId = Integer.parseInt(commandId);
  }


  public PermissionItem(long senderId, int commandId, int permits) {
    this.senderId = String.valueOf(senderId);
    this.commandId = commandId;
    this.permits = permits;
  }

  public PermissionItem(long senderId, int commandId, int permits, int remain) {
    this.senderId = String.valueOf(senderId);
    this.commandId = commandId;
    this.permits = permits;
    this.remain = remain;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCommandId() {
    return commandId;
  }

  public void setCommandId(int commandId) {
    this.commandId = commandId;
  }

  public long getSenderId() {
    return Long.parseLong(senderId);
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

//  public String isPermit() {
//    return this.permit ? "true" : "false";
//  }
//
//  public void setPermit(boolean permit) {
//    this.permit = permit;
//  }

  public int getRemain() {
    return remain;
  }

  public void setRemain(int remain) {
    this.remain = remain;
  }

  public int getPermits() {
    return permits;
  }

  public void setPermits(int permits) {
    this.permits = permits;
  }
}

