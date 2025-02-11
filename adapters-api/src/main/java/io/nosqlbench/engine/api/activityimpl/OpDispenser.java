package io.nosqlbench.engine.api.activityimpl;

import java.util.function.LongFunction;

/**
 * <p>
 * <H2>Synopsis</H2>
 * An OpDispenser is responsible for mapping a cycle number into
 * an executable operation. This is where <i>Op Synthesis</i> occurs
 * in NoSQLBench.</p>
 * <hr/>
 * <p>
 * <H2>Concepts</H2>
 * Op Synthesis is the process of building a specific and executable
 * operation for some (low level driver) API by combining the
 * static and dynamic elements of the operation together.
 * In most cases, implementations of OpDispenser will be constructed
 * within the logic of an {@link OpMapper} which is responsible for
 * determining the type of OpDispenser to use as associated with a specific
 * type {@code (<T>)}. The OpMapper is called for each type of operation
 * that is active during activity initialization. It's primary responsibility
 * is figuring out what types of {@link OpDispenser}s to create based
 * on the op templates provided by users. Once the activity is initialized,
 * a set of op dispensers is held as live dispensers to use as needed
 * to synthesize new operations from generated data in real time.
 * </p>
 *
 * <hr/>
 * <h2>Implementation Strategy</h2>
 * <p>OpDispenser implementations are intended to be implemented
 * for each type of distinct operation that is supported by a
 * {@link io.nosqlbench.engine.api.activityimpl.uniform.DriverAdapter}.
 * That is not to say that an OpDispenser can't be responsible for
 * producing multiple types of operations. Operations which are similar
 * in what they need and how they are constructed make sense to be implemented
 * in the same op dispenser. Those which need different construction
 * logic or fundamentally different types of field values should be implemented
 * separately. The rule of thumb is to ensure that op construction patterns
 * are easy to understand at the mapping level ({@link OpMapper}),
 * and streamlined for fast execution at the synthesis level ({@link OpDispenser}).
 *
 * @param <T> The parameter type of the actual operation which will be used
 *            to hold all the details for executing an operation,
 *            something that implements {@link Runnable}.
 */
public interface OpDispenser<T> extends LongFunction<T> {

    /**
     * The apply method in an op dispenser should do all the work of
     * creating an operation that is executable by some other caller.
     * The value produced by the apply method should not require
     * additional processing if a caller wants to execute the operation
     * multiple times, as for retries.
     *
     * @param value The cycle number which serves as the seed for any
     *              generated op fields to be bound into an operation.
     * @return an executable operation
     */
    @Override
    T apply(long value);
}
