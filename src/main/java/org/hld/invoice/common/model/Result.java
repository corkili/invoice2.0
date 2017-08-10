package org.hld.invoice.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李浩然 On 2017/8/10.
 */
public class Result {
    private boolean successful;

    private String message;

    private Map<String, Object> map;

    public Result(boolean successful) {
        this.successful = successful;
        message = "";
        map = new HashMap<String, Object>();
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public Result add(String name, Object value) {
        map.put(name, value);
        return this;
    }

    public Object get(String name) {
        return map.get(name);
    }
}
