package parser;

import validate.DuplicateNameException;

import java.util.HashMap;

public class SchedulerMap<K, V> extends HashMap<K, V> {

    @Override
    public V put(K key, V value) {
        if (this.containsKey(key)) {
            throw new DuplicateNameException("2 or more of the same " + value.getClass().getSimpleName() + " share the name " + key);
        } else {
            return super.put(key, value);
        }
    }
}
