package Exceptions;

public class TuplesNotFoundException extends RuntimeException{
    public TuplesNotFoundException(){
        super("alter函数找不到对应的行");
    }
}
