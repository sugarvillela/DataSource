package stackpayload.iface;

import langdefalgo.iface.LANG_STRUCT;
import stack.iface.IStackLog;
import stack.iface.IStackLogIterationItem;

import java.util.ArrayList;

// constructor sets algo

/** Vertical chain of references:
 * IStack has many IStackPayload            (stateful,  many; holds state for LANG_STRUCT->Algo)
 * IStackPayload has one LANG_STRUCT->Algo  (stateless, one per LANG_STRUCT enum; many stack payloads share a stateless algo)
 * Algo holds:
 *      NestingRule                         (immutable list of LANG_STRUCT enums, one per LANG_STRUCT enum
 *      PushPop                             (immutable list of stateless push-pop strategy objects, one per algo)
 *      Reference to parent enum            (LANG_STRUCT enum)
 */
public interface IStackPayload {
    void onPush();
    void onPop();

    void addToStackLog(IStackLog stackLog, ArrayList<IStackLogIterationItem> newIteration);

    void setBelow(IStackPayload below);
    IStackPayload getBelow();

    void go();

    LANG_STRUCT getLangStructEnum();
    IPayloadState getState();
}
