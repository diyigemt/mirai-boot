package org.miraiboot.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.miraiboot.annotation.AutoInit;
import org.miraiboot.mapper.BaseMapper;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author diyigemt
 * mybatis工具类
 */
@AutoInit
public class MybatisUtil implements InitializeUtil {
	// 全局mybatis工厂 官方文档推荐只有一个
	private static SqlSessionFactory factory;

	private static final MybatisUtil INSTANCE = new MybatisUtil();

	/**
	 * 根据配置文件 初始化工厂
	 */
	public static void init(Class<?> target) {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		factory = new SqlSessionFactoryBuilder().build(reader);
	}

	public static MybatisUtil getInstance() {
		return INSTANCE;
	}

	/**
	 * 获取一个sqlSession
	 * 官方推荐执行完事务后需要释放
	 * @return 一个session
	 */
	public SqlSession getSqlSession() {
		if (factory == null) {
			init(null);
		}
		return factory.openSession();
	}

	public <T extends BaseMapper, K> K getSingleData(Class<T> mapperClass, Class<K> resClass, String methodName, Object arg1) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		K res = null;
		try {
			Method method = mapperClass.getMethod(methodName, arg1.getClass());
			res = (K) method.invoke(mapper, arg1);
		} catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> List<K> getListData(Class<T> mapperClass, Class<K> resClass, String methodName) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		List<K> res = null;
		try {
			Method method = mapperClass.getMethod(methodName);
			res = (List<K>) method.invoke(mapper);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> List<K> getListData(Class<T> mapperClass, Class<K> resClass, String methodName, String arg1) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		List<K> res = null;
		try {
			Method method = mapperClass.getMethod(methodName, arg1.getClass());
			res = (List<K>) method.invoke(mapper, arg1);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> int insetData(Class<T> mapperClass, Class<K> resClass, String methodName, Object arg1) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		int res = -1;
		Method[] methods = mapperClass.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		try {
			if (method == null) throw new NoSuchMethodException("没有与名字" + methodName + "对应的方法!");
			Object o = method.invoke(mapper, arg1);
			if (o != null) res = Integer.parseInt(o.toString());
			sqlSession.commit();
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> int updateData(Class<T> mapperClass, Class<K> resClass, String methodName, Object arg1) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		int res = -1;
		Method[] methods = mapperClass.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		try {
			if (method == null) throw new NoSuchMethodException("没有与名字" + methodName + "对应的方法!");
			method.invoke(mapper, arg1);
			sqlSession.commit();
			res = 0;
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> int updateData(Class<T> mapperClass, Class<K> resClass, String methodName, Object arg1, Object arg2) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		int res = -1;
		Method[] methods = mapperClass.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		try {
			if (method == null) throw new NoSuchMethodException("没有与名字" + methodName + "对应的方法!");
			method.invoke(mapper, arg1, arg2);
			sqlSession.commit();
			res = 0;
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper> void removeData(Class<T> mapperClass, String methodName, Object arg1) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		Method[] methods = mapperClass.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		try {
			if (method == null) throw new NoSuchMethodException("没有与名字" + methodName + "对应的方法!");
			method.invoke(mapper, arg1);
			sqlSession.commit();
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}
}