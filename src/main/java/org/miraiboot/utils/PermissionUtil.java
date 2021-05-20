package org.miraiboot.utils;

import org.miraiboot.dao.PermissionDAO;
import org.miraiboot.entity.PermissionItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>权限管理的工具类</h2>
 * 单例 一般用不到
 * @author diyigemt
 * @since 1.0.0
 */
public class PermissionUtil {
  private static final PermissionUtil INSTANCE = new PermissionUtil();

  public PermissionUtil() {}

  public static PermissionUtil getInstance() { return INSTANCE; }

  /**
   * <h2>获取权限数据库中的记录</h2>
   * 根据发送者qq号和EventHandler的permissionIndex获取记录
   * @param senderId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @return 对应的记录 没有记录时返回null
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public PermissionItem getPermissionItem(long senderId, String commandId) {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("sender_id", senderId);
    args.put("command_id", commandId);
    List<PermissionItem> permissionItems = PermissionDAO.getInstance().selectForFieldValuesArgs(args);
    if (permissionItems != null && !permissionItems.isEmpty()) return permissionItems.get(0);
    return null;
  }

  /**
   * <h2>向数据库中添加一天记录</h2>
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void addPermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  /**
   * <h2>向数据库中添加一条记录</h2>
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @param permits 权限等级 防止下克上
   * @param remain 剩余次数
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void addPermissionItem(long targetId, int commandId, int permits, int remain) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits, remain);
    updatePermissionItem(item);
  }

  /**
   * <h2>从数据库中移除一条记录</h2>
   * 根据发送者qq号和permissionIndex从数据库中移除一条记录
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void removePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    PermissionDAO.getInstance().delete(item);
  }

  /**
   * <h2>启用权限</h2>
   * 根据发送者qq号和permissionIndex从数据库中启用一条记录
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void enablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  /**
   * <h2>启用权限</h2>
   * 根据发送者qq号和permissionIndex从数据库中启用一条记录
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @param permits 权限等级
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void enablePermissionItem(long targetId, int commandId, int permits) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits);
    updatePermissionItem(item);
  }

  /**
   * <h2>关闭权限而不删除记录</h2>
   * 根据发送者qq号和permissionIndex从数据库中关闭一条记录
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void disablePermissionItem(long targetId, int commandId) {
    PermissionItem item = new PermissionItem(targetId, String.valueOf(commandId));
    updatePermissionItem(item);
  }

  /**
   * <h2>关闭权限而不删除记录</h2>
   * 根据发送者qq号和permissionIndex从数据库中关闭一条记录
   * @param targetId 发送者qq号
   * @param commandId EventHandler注解中的permissionIndex
   * @param permits 权限等级 防止下克上
   * @see org.miraiboot.permission.CheckPermission
   * @see PermissionItem
   */
  public void disablePermissionItem(long targetId, int commandId, int permits) {
    PermissionItem item = new PermissionItem(targetId, commandId, permits);
    updatePermissionItem(item);
  }

  /**
   * <h2>更新数据库中的权限信息</h2>
   * @param item 权限存储类
   */
  public void updatePermissionItem(PermissionItem item) {
    PermissionDAO.getInstance().insert(item);
  }
}
