package io.nosqlbench.virtdata.library.basics.shared.from_long.to_uuid;

import io.nosqlbench.virtdata.api.annotations.Categories;
import io.nosqlbench.virtdata.api.annotations.Category;
import io.nosqlbench.virtdata.api.annotations.ThreadSafeMapper;

import java.util.UUID;
import java.util.function.LongFunction;

/**
 * This function creates a non-random UUID in the type 4 version (Random).
 * It always puts the same value in the MSB position of the UUID format.
 * The input value is put in the LSB position.
 *
 * This function is suitable for deterministic testing of scenarios which depend
 * on type 4 UUIDs, but without the mandated randomness that makes testing difficult.
 * Just be aware that the MSB will always contain value 0x0123456789ABCDEFL unless you
 * specify a different long value to pre-fill it with.
 */
@ThreadSafeMapper
@Categories({Category.conversion})
public class ToUUID implements LongFunction<UUID> {

    private final long msbs;

    public ToUUID() {
        // Something memorable, but the correct version
        this.msbs = (0x0123456789ABCDEFL & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000004000L;
    }

    public ToUUID(long msbs) {
        this.msbs = (msbs & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000004000L;
    }

    @Override
    public UUID apply(long value) {
        long lsbs = (value & 0x1FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
        UUID uuid= new UUID(msbs,lsbs);
        return uuid;
    }
}
