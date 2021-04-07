package err.iface;

import err.ERR_TYPE;
import readnode.iface.IReadNode;

/** Implement two error logging systems:
 *    ErrDev:   for transpiler development: quit on bad program construction; disable errDev for release
 *    Err:      for transpiler user: quit on bad program input; always on
 */
public interface IErr {
    boolean check(ERR_TYPE errType);                    // use if RunState.currNode() gives correct position

    void kill(ERR_TYPE errType);                        // use if RunState.currNode() gives correct position

    void kill(ERR_TYPE errType, IReadNode statusNode);  // use if RunState.currNode() not correct position

    void kill(ERR_TYPE errType, String message);        // use for file errors etc.

    void kill(IReadNode statusNode, String message);    // use for a more helpful err message

    void kill(String message, String text);             // use for a more helpful err message

    void kill(String message);                          // use for a more helpful err message
}
