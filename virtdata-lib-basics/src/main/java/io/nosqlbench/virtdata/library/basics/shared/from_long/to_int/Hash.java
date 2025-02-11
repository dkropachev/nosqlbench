package io.nosqlbench.virtdata.library.basics.shared.from_long.to_int;

import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;
import io.nosqlbench.virtdata.murmur.Murmur3F;

import java.nio.ByteBuffer;
import java.util.function.LongToIntFunction;

@ThreadSafeMapper
@Categories({Category.general})
public class Hash implements LongToIntFunction {

    ThreadLocal<ByteBuffer> bb_TL = ThreadLocal.withInitial(() -> ByteBuffer.allocate(Long.BYTES));
    ThreadLocal<Murmur3F> murmur3f_TL = ThreadLocal.withInitial(Murmur3F::new);

    @Override
    public int applyAsInt(long value) {
        Murmur3F murmur3F = murmur3f_TL.get();
        ByteBuffer bb = bb_TL.get();
        murmur3F.reset();
        bb.putLong(0,value);
        murmur3F.update(bb.array(),0,Long.BYTES);
        long result= Math.abs(murmur3F.getValue());
        return (int) (result & Integer.MAX_VALUE);
    }
}
