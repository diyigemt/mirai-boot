package org.miraiboot.sql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper {
  private static final DatabaseHelper INSTANCE = new DatabaseHelper();
  private static final String SQL_URL_BASE = "jdbc:sqlite:./config/dbs/";


  public static DatabaseHelper getInstance() { return INSTANCE; }

  public JdbcConnectionSource getDataSourceURL(String dBName) throws SQLException {
    if (dBName.contains(".db")) dBName = dBName.replace(".db", "");
    String url = SQL_URL_BASE + dBName + ".db";
    return new JdbcConnectionSource(url);
  }

  public <T, TD> Dao<T, TD> getDAO(String dBName, Class<T> entity, Class<TD> key) {
    Dao<T, TD> dao = null;
    try {
      dao = DaoManager.createDao(getDataSourceURL(dBName), entity);
      if (!dao.isTableExists()) TableUtils.createTable(dao);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return dao;
  }
}
