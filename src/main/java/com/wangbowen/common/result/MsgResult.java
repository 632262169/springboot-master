package com.wangbowen.common.result;

import java.util.HashMap;
import java.util.Map;

public class MsgResult extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public MsgResult() {
		put("code", 0);
		put("msg", "操作成功");
	}
	
	public static MsgResult error() {
		return error(1, "操作失败");
	}

	public static MsgResult error(String msg) {
		return error(500, msg);
	}

	public static MsgResult error(int code, String msg) {
		MsgResult r = new MsgResult();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static MsgResult ok(String msg) {
		MsgResult r = new MsgResult();
		r.put("msg", msg);
		return r;
	}

	public static MsgResult ok(Map<String, Object> map) {
		MsgResult r = new MsgResult();
		r.putAll(map);
		return r;
	}

	public static MsgResult ok() {
		return new MsgResult();
	}

	public MsgResult put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
