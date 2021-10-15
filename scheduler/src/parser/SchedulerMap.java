package parser;

import validate.DuplicateNameException;

import java.util.HashMap;

public class SchedulerMap extends HashMap {

    @Override
    public Object put(Object key, Object value) {
        if (this.containsKey(key)) {
            throw new DuplicateNameException("2 or more of the same non-terminal share the name " + key);
        } else {
            return super.put(key, value);
        }
    }
}
