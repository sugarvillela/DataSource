package langdefsub;

public enum FUN_TYPE {
    // back end, unless add feature
    LIT,
    GET_PATH,
    GET_ACCESS,
    // front end rx
    LEN,
    RANGE,
    //IS_DUP,
    // fx flags
    VOTE,
    SET,
    DROP,
    // fx structure
    MOVE,
    COPY,
    CON,
    //PUT,
    SWAP
    ;

    public static FUN_TYPE fromString(String text){
        //System.out.println("RX_FUN_TYPE fromString: "+ text);
        try{
            //System.out.println("try fromString: "+ text);
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            System.out.println("fail fromString: "+ text);
            return null;
        }
    }
}
