package org.miraiboot.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "permission")
public class PermissionItem {
  @DatabaseField(generatedId = true, columnName = "id", unique = true)
  private int id;
  @DatabaseField(columnName = "command_id")
  private int commandId;
  @DatabaseField(columnName = "sender_id")
  private String senderId;
//  @DatabaseField(columnName = "permit", defaultValue = "false")
//  private boolean permit;
  @DatabaseField(columnName = "permit", defaultValue = "-1")
  private int permits = 0;
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

//  public PermissionItem(long senderId, int commandId, boolean permit) {
//    this.senderId = String.valueOf(senderId);
//    this.commandId = commandId;
//    this.permit = permit;
//  }

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

