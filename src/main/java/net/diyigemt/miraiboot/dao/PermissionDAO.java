package net.diyigemt.miraiboot.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import net.diyigemt.miraiboot.annotation.AutoInit;
import net.diyigemt.miraiboot.constant.ConstantSQL;
import net.diyigemt.miraiboot.entity.ConfigFile;
import net.diyigemt.miraiboot.entity.PermissionItem;
import net.diyigemt.miraiboot.sql.DatabaseHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>sqlite数据库权限管理的DAO</h2>
 * 一般并不需要关心
 * @author diyigemt
 * @author Heythem723
 * @since 1.0.0
 */
@AutoInit
public class PermissionDAO {
  private static final PermissionDAO INSTANCE = new PermissionDAO();
  private static Dao<PermissionItem, Integer> dao;

  public static void init(ConfigFile config) {
    dao = DatabaseHelper.getInstance().getDAO("permission", PermissionItem.class, Integer.class);
  }

  public static PermissionDAO getInstance() { return INSTANCE; }

  public QueryBuilder<PermissionItem, Integer> getQueryBuilder() {
    return dao.queryBuilder();
  }

  public PermissionItem selectById(int id) {
    try {
      return dao.queryForId(id);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public List<PermissionItem> selectAll() {
    try {
      return dao.queryForAll();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public List<PermissionItem> selectByPrepared(PreparedQuery<PermissionItem> query) {
    try {
      return dao.query(query);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public List<PermissionItem> selectForFieldValuesArgs(Map<String, Object> args) {
    try {
      return dao.queryForFieldValuesArgs(args);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public int insert(PermissionItem item) {
    int res = ConstantSQL.SUCCESS;
    QueryBuilder<PermissionItem, Integer> builder = dao.queryBuilder();
    Map<String, Object> args = new HashMap<String, Object>();
    long senderId = item.getSenderId();
    int commandId = item.getCommandId();
    int remain = item.getRemain();
    int permits = item.getPermits();
    args.put("sender_id", senderId);
    args.put("command_id", commandId);
    List<PermissionItem> permissionItems = selectForFieldValuesArgs(args);
    if (permissionItems != null && !permissionItems.isEmpty()) {
      item = permissionItems.get(0);
      item.setSenderId(String.valueOf(senderId));
      item.setCommandId(commandId);
      item.setRemain(remain);
      item.setPermits(permits);
    }
    try {
      CreateOrUpdateStatus status = dao.createOrUpdate(item);
      if (status.isCreated()) res = ConstantSQL.INSERT_CREATE;
      if (status.isUpdated()) res = ConstantSQL.INSERT_UPDATE;
    } catch (SQLException e) {
      res = ConstantSQL.ERROR;
      e.printStackTrace();
    }
    return res;
  }

  public int update(PermissionItem item) {
    int res = ConstantSQL.SUCCESS;
    try {
      res = dao.update(item);
    } catch (SQLException e) {
      e.printStackTrace();
      res = ConstantSQL.ERROR;
    }
    return res;
  }

  public int delete(PermissionItem item) {
    int res = ConstantSQL.SUCCESS;
    DeleteBuilder<PermissionItem, Integer> builder = dao.deleteBuilder();
    Where<PermissionItem, Integer> where = builder.where();
    try {
      Where<PermissionItem, Integer> eq = where.eq("sender_id", item.getSenderId()).and().eq("command_id", item.getCommandId());
      builder.setWhere(eq);
      res = dao.delete(builder.prepare());
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      res = ConstantSQL.ERROR;
    }
    return res;
  }

  public int delete(int id) {
    int res = ConstantSQL.SUCCESS;
    try {
      res = dao.deleteById(id);
    } catch (SQLException e) {
      res = ConstantSQL.ERROR;
      e.printStackTrace();
    }
    return res;
  }
}
