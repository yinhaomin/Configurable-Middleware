package com.baidu.config.middleware;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrderedProperties extends Properties {
    private static final long serialVersionUID = -4627607243846121965L;

    private final LinkedHashSet keys = new LinkedHashSet();

    public Enumeration keys() {
        return Collections.enumeration(keys);
    }

    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }

    public synchronized Object remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    public Set keySet() {
        return keys;
    }

    public Set stringPropertyNames() {
        Set set = new LinkedHashSet();
        for (Object key : this.keys) {
            set.add((String) key);
        }
        return set;
    }
}
