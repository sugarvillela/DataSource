package rule_identifier.impl;

import rule_identifier.iface.IIdentifierRule;
import err.ERR_TYPE;
import readnode.iface.IReadNode;
import runstate.Glob;

public abstract class IdentifierRuleImplGroup {
    public static final IIdentifierRule ID_IGNORE =     new IdentifierIgnore();
    public static final IIdentifierRule ID_REQUIRE =    new IdentifierRequire();
    public static final IIdentifierRule ID_ALLOW =      new IdentifierAllow();
    public static final IIdentifierRule ID_DISALLOW =   new IdentifierDisallow();

    public static abstract class IdentifierRuleBase implements IIdentifierRule {
        private String pushedIdentifier;

        protected abstract boolean shouldContinue(boolean hasIdentifier);

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
                pushedIdentifier = pushReadNode.textEvent().substring();// grab before it is deleted by setIdentifier()
                Glob.DATA_SINK.setIdentifier(pushReadNode);
                return true;
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
                Glob.DATA_SINK.getIdentifier(pushedIdentifier).setListening(false);
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