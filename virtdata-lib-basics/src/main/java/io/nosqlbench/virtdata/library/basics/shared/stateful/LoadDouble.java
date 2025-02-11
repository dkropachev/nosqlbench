package io.nosqlbench.virtdata.library.basics.shared.stateful;

import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.Example;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;
import io.nosqlbench.virtdata.library.basics.core.threadstate.SharedState;

import java.util.HashMap;
import java.util.function.Function;

@Categories(Category.state)
@ThreadSafeMapper
public class LoadDouble implements Function<Object,Double> {

    private final String name;
    private final Function<Object,Object> nameFunc;
    private final double defaultValue;

    @Example({"LoadDouble('foo')","for the current thread, load a double value from the named variable."})
    public LoadDouble(String name) {
        this.name = name;
        this.nameFunc=null;
        this.defaultValue=0.0D;
    }

    @Example({"LoadDouble('foo',23D)","for the current thread, load a double value from the named variable," +
            "or the default value if the named variable is not defined."})
    public LoadDouble(String name, double defaultValue) {
        this.name = name;
        this.nameFunc=null;
        this.defaultValue=defaultValue;
    }

    @Example({"LoadDouble(NumberNameToString())","for the current thread, load a double value from the named variable, " +
            "where the variable name is provided by a function."})
    public LoadDouble(Function<Object,Object> nameFunc) {
        this.name=null;
        this.nameFunc = nameFunc;
        this.defaultValue=0.0D;
    }

    @Example({"LoadDouble(NumberNameToString(),23D)","for the current thread, load a double value from the named variable," +
            "where the variable name is provided by a function, or the default value if the named" +
            "variable is not defined."})
    public LoadDouble(Function<Object,Object> nameFunc, double defaultValue) {
        this.name=null;
        this.nameFunc = nameFunc;
        this.defaultValue=defaultValue;
    }

    @Override
    public Double apply(Object o) {
        HashMap<String, Object> map = SharedState.tl_ObjectMap.get();
        String varname=(nameFunc!=null) ? String.valueOf(nameFunc.apply(o)) : name;
        Object value = map.getOrDefault(varname, defaultValue);
        return (Double) value;
    }
}
