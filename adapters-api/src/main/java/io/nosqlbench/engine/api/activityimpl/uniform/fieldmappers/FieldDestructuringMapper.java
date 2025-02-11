package io.nosqlbench.engine.api.activityimpl.uniform.fieldmappers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FieldDestructuringMapper implements Function<Map<String, Object>, Map<String, Object>> {

    private final String fieldname;
    private final Function<String, Optional<Map<String, Object>>> thenfunc;

    public FieldDestructuringMapper(String fieldName, Function<String, Optional<Map<String, Object>>> thenfunc) {
        this.fieldname = fieldName;
        this.thenfunc = thenfunc;
    }

    @Override
    public Map<String, Object> apply(Map<String, Object> stringObjectMap) {
        if (stringObjectMap.containsKey(fieldname)) {
            Object o = stringObjectMap.get(fieldname);
            if (o instanceof CharSequence) {
                String rawfield = o.toString();
                Optional<Map<String, Object>> optionalResult = thenfunc.apply(rawfield);
                if (optionalResult.isPresent()) {
                    Map<String, Object> resultmap = optionalResult.get();
                    LinkedHashMap<String, Object> returnmap = new LinkedHashMap<>(stringObjectMap);
                    returnmap.remove(fieldname);
                    resultmap.forEach((k, v) -> {
                        if (returnmap.containsKey(k)) {
                            throw new RuntimeException("element '" + k + "' already exist during field remapping.");
                        }
                        returnmap.put(k, v);
                    });
                    return returnmap;
                } else {
                    return stringObjectMap;
                }
            } else {
                throw new RuntimeException("During op mapping, can't parse something that is not a CharSequence: '" + fieldname + "' (type is " + o.getClass().getCanonicalName() + ")");
            }
        } else {
            return stringObjectMap;
        }
    }
}
