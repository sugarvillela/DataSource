package rule_identifier.impl;

import rule_identifier.iface.IIdentifierRule;
import err.ERR_TYPE;
import readnode.iface.IReadNode;
import runstate.Glob;

public abstract class IdentifierRuleImplGroup {
    public static final IIdentifierRule ID_IGNORE =         new IdentifierIgnore();         // no action, no error
    public static final IIdentifierRule ID_REQUIRE =        new IdentifierRequire();        // save id, err if absent
    public static final IIdentifierRule ID_REQUIRE_SILENT = new IdentifierRequireSilent();  // don't save, err if absent
    public static final IIdentifierRule ID_ALLOW =          new IdentifierAllow();          // save id, no error if absent
    public static final IIdentifierRule ID_DISALLOW =       new IdentifierDisallow();       // don't save, err if id

    public static abstract class IdentifierRuleBase implements IIdentifierRule {
        protected String pushedIdentifier;

        protected abstract boolean shouldContinue(boolean hasIdentifier);

        protected boolean putInDataSink(IReadNode pushReadNode){
            pushedIdentifier = pushReadNode.textEvent().substring();// grab before it is deleted by setIdentifier()
            Glob.DATA_SINK.setIdentifier(pushReadNode);
            return true;
        }

        @Override
        public boolean ignore() {
            return false;
        }

        /**Specifies ignore, error or setIdentifier: by class and by text event
         * @param pushReadNode the current read node at push time
         * @return true if identifier passed to DataSearch setIdentifier() method. */
        @Override
        public boolean onPush(IReadNode pushReadNode) {
            if(
                !ignore() &&
                shouldContinue(
                pushReadNode != null && pushReadNode.hasTextEvent() && pushReadNode.textEvent().hasSubstring()
                )
            ){
                //System.out.println("newSinkOnIdentifier");
                return putInDataSink(pushReadNode);
            }
            pushedIdentifier = null;
            return false;
        }

        @Override
        public void onPop(String pushedIdentifier) {
            if(
                !ignore() &&
                shouldContinue(
                        pushedIdentifier != null
                )
            ){
                //System.out.println("newSinkOnIdentifier");
                Glob.DATA_SINK.getIdentifierOrErr(pushedIdentifier).setListening(false);
            }
        }

        @Override
        public String getPushedIdentifier() {
            return pushedIdentifier;
        }
    }

    public static class IdentifierIgnore extends IdentifierRuleBase {
        private IdentifierIgnore(){}

        @Override
        protected boolean shouldContinue(boolean hasIdentifier) {
            return false;
        }

        @Override
        public boolean ignore() {
            return true;
        }
    }

    public static class IdentifierRequire extends IdentifierRuleBase {
        private IdentifierRequire(){}

        @Override
        protected boolean shouldContinue(boolean hasIdentifier) {
            if(!hasIdentifier){
                Glob.ERR.kill(ERR_TYPE.MISSING_ID);
            }
            return hasIdentifier;
        }
    }

    public static class IdentifierRequireSilent extends IdentifierRequire {
        private IdentifierRequireSilent(){}

        @Override
        protected boolean putInDataSink(IReadNode pushReadNode) {
            pushedIdentifier = null;
            return false;
        }
    }

    public static class IdentifierAllow extends IdentifierRuleBase {
        private IdentifierAllow(){}

        @Override
        protected boolean shouldContinue(boolean hasIdentifier) {
            return hasIdentifier;
        }
    }

    public static class IdentifierDisallow extends IdentifierRuleBase {
        private IdentifierDisallow(){}

        @Override
        protected boolean shouldContinue(boolean hasIdentifier) {
            if(hasIdentifier){
                Glob.ERR.kill(ERR_TYPE.MISPLACED_ID);
            }
            return false;
        }
    }
}
