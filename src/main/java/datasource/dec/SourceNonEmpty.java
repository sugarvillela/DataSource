package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Output is one iteration behind internal state, or more if blank lines are skipped. */
public class SourceNonEmpty extends InterceptorBase {
    public SourceNonEmpty(IDataSource dataSource) {
        super(dataSource);

    }

    @Override
    protected boolean shouldSkip() {
        return nextNode.text().trim().isEmpty();
    }
}
