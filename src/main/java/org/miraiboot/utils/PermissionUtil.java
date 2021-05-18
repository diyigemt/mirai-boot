package org.miraiboot.utils;

import org.miraiboot.dao.PermissionDAO;
import org.miraiboot.entity.PermissionItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtil {
  private static final PermissionUtil INSTANCE = new PermissionUtil();

  public PermissionUtil() {}

  public static PermissionUtil getInstance() { return INSTANCE; }

  public PermissionItem getPermissionItem(long senderId, String commandId) {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("sender_id", senderId);
    args.put("command_id", commandId);
    List<PermissionItem> permissionItems = PermissionDAO.getInstance().selectForFieldValuesArgs(args);
    if (permissionItems != null && !permissionItems.isEmpty()) return permissionItems.get(0);
    return null;
  }

  public void addPermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  public void addPermissionItem(long targetId, int commandId, int permits, int remain) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits, remain);
    updatePermissionItem(item);
  }

  public void removePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    PermissionDAO.getInstance().delete(item);
  }

  public void enablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  public void enablePermissionItem(long targetId, int commandId, int permits) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits);
    updatePermissionItem(item);
  }

  public void disablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  public void disablePermissionItem(long targetId, int commandId, int permits) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits);
    updatePermissionItem(item);
  }

  public void updatePermissionItem(PermissionItem item) {
    PermissionDAO.getInstance().insert(item);
  }
}
