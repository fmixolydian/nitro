package eu.sqrt5.nitro.core;
import java.util.HashMap;
import java.util.Map;

public class Namespace {
    private Map<String, Object> vars = new HashMap<>();

    public void set(String name, Object value) {
        vars.put(name, value);
    }

    public Object get(String name) {
        return vars.get(name);
    }
}
