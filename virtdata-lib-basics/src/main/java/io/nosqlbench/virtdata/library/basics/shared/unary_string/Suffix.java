package io.nosqlbench.virtdata.library.basics.shared.unary_string;

import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.Example;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Add the specified prefix String to the input value and return the result.
 */
@ThreadSafeMapper@Categories({Category.general})
public class Suffix implements Function<String,String> {
    private final String suffix;

    @Example({"Suffix('--Fin')", "Append '--Fin' to every input value"})
    public Suffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String apply(String s) {
        return s + suffix;
    }
}
