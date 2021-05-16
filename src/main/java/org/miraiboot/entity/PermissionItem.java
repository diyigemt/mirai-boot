package org.miraiboot.entity;

public class PermissionItem {
  private int id;
  private int commandId;
  private String senderId;
  private boolean permit;
  private int remain;

  public PermissionItem() {}

  public PermissionItem(long senderId, String commandId) {
    this.senderId = String.valueOf(senderId);
    this.commandId = Integer.parseInt(commandId);
  }

  public PermissionItem(String senderId, String commandId) {
    this.senderId = senderId;
    this.commandId = Integer.parseInt(commandId);
  }

  public PermissionItem(long senderId, int commandId, boolean permit) {
    this.senderId = String.valueOf(senderId);
    this.commandId = commandId;
    this.permit = permit;
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

  public String isPermit() {
    return this.permit ? "true" : "false";
  }

  public void setPermit(boolean permit) {
    this.permit = permit;
  }

  public int getRemain() {
    return remain;
  }

  public void setRemain(int remain) {
    this.remain = remain;
  }
}
