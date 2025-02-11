package io.nosqlbench.adapters.stdout;

import io.nosqlbench.engine.api.activityconfig.yaml.OpTemplate;
import io.nosqlbench.engine.api.activityimpl.OpMapper;
import io.nosqlbench.engine.api.activityimpl.uniform.BaseDriverAdapter;
import io.nosqlbench.engine.api.activityimpl.uniform.DriverSpaceCache;
import io.nosqlbench.engine.api.templating.OpTemplateSupplier;
import io.nosqlbench.nb.api.config.standard.NBConfigModel;
import io.nosqlbench.nb.api.config.standard.NBConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class StdoutDriverAdapter extends BaseDriverAdapter<StdoutOp, StdoutSpace> implements OpTemplateSupplier {

    @Override
    public OpMapper<StdoutOp> getOpMapper() {
        DriverSpaceCache<? extends StdoutSpace> ctxCache = getSpaceCache();
        return new StdoutOpMapper(ctxCache);
    }

    @Override
    public Function<String, ? extends StdoutSpace> getSpaceInitializer(NBConfiguration cfg) {
        return (s) -> new StdoutSpace(cfg);
    }

    @Override
    public NBConfigModel getConfigModel() {
        return StdoutSpace.getConfigModel();
    }

    @Override
    public Optional<List<OpTemplate>> loadOpTemplates(NBConfiguration cfg) {
        throw new RuntimeException("implement me");
    }
}
