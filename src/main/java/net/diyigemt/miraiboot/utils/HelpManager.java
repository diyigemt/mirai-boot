package net.diyigemt.miraiboot.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>管理所有帮助信息</h2>
 * @since 1.0.4
 * @author diyigemt
 */
public class HelpManager {
	private static final HelpManager INSTANCE = new HelpManager();

	private static final Map<String, String> DESC_STORE = new HashMap<String , String>();

	private static final Map<String, String> DETAIL_STORE = new HashMap<String , String>();

	public static HelpManager getInstance() { return INSTANCE; }
}
