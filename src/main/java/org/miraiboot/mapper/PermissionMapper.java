package org.miraiboot.mapper;

import org.miraiboot.entity.PermissionItem;

public interface PermissionMapper extends BaseMapper {

  PermissionItem getPermission(PermissionItem item);

  void updatePermission(PermissionItem item);

  void removePermission(PermissionItem item);
}
