package com.huyu.sdk.util;

import java.util.HashMap;
import java.util.Map;


public class SDKParams {
    private Map<String, String> configs = new HashMap();


    public SDKParams(Map<String, String> configs) {
        this();

        if (configs != null) {
            for (String key : configs.keySet()) {
                put(key, (String) configs.get(key));
            }
        }
    }

    public SDKParams() {
    }

    public boolean contains(String key) {
        return this.configs.containsKey(key);
    }


    public void put(String key, String value) {
        this.configs.put(key, value);
    }


    public String getString(String key) {
        if (this.configs.containsKey(key)) {
            return (String) this.configs.get(key);
        }
        return null;
    }

    public int getInt(String key) {
        String val = getString(key);

        return ((val == null) ? null : Integer.valueOf(Integer.parseInt(val))).intValue();
    }

    public Float getFloat(String key) {
        String val = getString(key);
        return (val == null) ? null : Float.valueOf(Float.parseFloat(val));
    }

    public Double getDouble(String key) {
        String val = getString(key);
        return (val == null) ? null : Double.valueOf(Double.parseDouble(val));
    }

    public Long getLong(String key) {
        String val = getString(key);
        return (val == null) ? null : Long.valueOf(Long.parseLong(val));
    }

    public Boolean getBoolean(String key) {
        String val = getString(key);
        return (val == null) ? null : Boolean.valueOf(Boolean.parseBoolean(val));
    }

    public Short getShort(String key) {
        String val = getString(key);
        return (val == null) ? null : Short.valueOf(Short.parseShort(val));
    }

    public Byte getByte(String key) {
        String val = getString(key);
        return (val == null) ? null : Byte.valueOf(Byte.parseByte(val));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : this.configs.keySet()) {
            sb.append(key).append("=").append((String) this.configs.get(key)).append("\n");
        }

        return sb.toString();
    }
}


