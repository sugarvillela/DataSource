package err.iface;

import err.ERR_TYPE;
import readnode.iface.IReadNode;

/** Implement two error logging systems:
 *    ErrDev:   for transpiler development: quit on bad program construction; disable errDev for release
 *    Err:      for transpiler user: quit on bad program input; always on
 */
public interface IErr {
    boolean check(ERR_TYPE errType);

    void kill(ERR_TYPE errType);

    void kill(IReadNode statusNode, String message);

    void kill(String message, String text);

    void kill(String message);
}
