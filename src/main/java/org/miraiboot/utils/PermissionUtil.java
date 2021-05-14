package org.miraiboot.utils;

import org.miraiboot.entity.PermissionItem;
import org.miraiboot.mapper.PermissionMapper;

public class PermissionUtil {
  private static final PermissionUtil INSTANCE = new PermissionUtil();

  public PermissionUtil() {}

  public static PermissionUtil getInstance() { return INSTANCE; }

  public PermissionItem getPermissionItem(long senderId, String commandId) {
    PermissionItem item = new PermissionItem(senderId, commandId);
    item = MybatisUtil.getInstance().getSingleData(PermissionMapper.class, PermissionItem.class, "getPermission", item);
    return item;
  }

  public void addPermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, commandId, true);
    updatePermissionItem(item);
  }

  public void removePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, commandId, false);
    MybatisUtil.getInstance().removeData(PermissionMapper.class, "removePermission", item);
  }

  public void enablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, commandId, true);
    updatePermissionItem(item);
  }

  public void disablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, commandId, false);
    updatePermissionItem(item);
  }

  public void updatePermissionItem(PermissionItem item) {
    MybatisUtil.getInstance().insetData(PermissionMapper.class, Integer.class, "updatePermission", item);
  }

}
