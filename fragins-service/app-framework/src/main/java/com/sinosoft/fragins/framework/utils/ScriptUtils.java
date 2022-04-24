package com.sinosoft.fragins.framework.utils;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * 执行JavaScript小脚本用的工具类，调用Java8中的nashorn引擎。可以用于动态的计算公式执行等功能。
 */
public class ScriptUtils {

	private static ScriptEngine scriptEngine;

	static {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		scriptEngine = scriptEngineManager.getEngineByName("nashorn");
	}

	/**
	 * 执行小脚本
	 * 
	 * @param script js脚本表达式内容，比如"A1+A2+A3"
	 * @param params 脚本中需要的参数，比如{"A1":1, "A2":2, "A3":3}
	 * @return 脚本表达式执行后的结果
	 */
	public static Object eval(String script, Map<String, Object> params) {
		Bindings bindings = scriptEngine.createBindings();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			bindings.put(entry.getKey(), entry.getValue());
		}
		try {
			return scriptEngine.eval(script, bindings);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ScriptUtils() {
	}

}
