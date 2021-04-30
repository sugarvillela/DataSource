package langdefsub;

public enum FUN_TYPE {
    // back end, unless add feature
    LIT,
    PATH,
    // front end
    LEN,
    RANGE
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
