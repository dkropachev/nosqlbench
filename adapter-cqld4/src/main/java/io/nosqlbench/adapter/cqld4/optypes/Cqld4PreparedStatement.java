package io.nosqlbench.adapter.cqld4.optypes;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import io.nosqlbench.adapter.cqld4.Cqld4Op;
import io.nosqlbench.adapter.cqld4.Cqld4OpMetrics;
import io.nosqlbench.adapter.cqld4.RSProcessors;

public class Cqld4PreparedStatement extends Cqld4Op {

    private final BoundStatement stmt;

    public Cqld4PreparedStatement(CqlSession session, BoundStatement stmt, int maxpages, boolean retryreplace, Cqld4OpMetrics metrics, RSProcessors processors) {
        super(session,maxpages,retryreplace,metrics,processors);
        this.stmt = stmt;
    }

    public BoundStatement getStmt() {
        return stmt;
    }

    @Override
    public String getQueryString() {
        return stmt.getPreparedStatement().getQuery();
    }
}
