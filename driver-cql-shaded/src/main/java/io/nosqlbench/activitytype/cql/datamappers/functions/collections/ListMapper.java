package io.nosqlbench.activitytype.cql.datamappers.functions.collections;

import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.Example;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;
import io.nosqlbench.virtdata.core.bindings.DataMapper;
import io.nosqlbench.virtdata.core.bindings.VirtData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

/**
 * This is an example of a mapping function that can create a list of objects
 * from another internal mapping function.
 *
 * The input value for each function is incremented by one from the initial input value
 * this this overall function.
 *
 */
@ThreadSafeMapper
@Categories({Category.collections})
public class ListMapper implements LongFunction<List<?>> {

    private final int size;
    private final DataMapper<String> elementMapper;

    @Example({"ListMapper(5,NumberNameToString())","creates a list of number names"})
    public ListMapper(int size, String genSpec) {
        this.size = size;
        elementMapper = VirtData.getMapper(genSpec,String.class);
    }

    @Override
    public List<?> apply(long value) {
        List<Object> list = new ArrayList<>(size);
        for (int listpos = 0; listpos < size; listpos++) {
            Object o = elementMapper.get(value + listpos);
            list.add(o);
        }
        return list;
    }
}
