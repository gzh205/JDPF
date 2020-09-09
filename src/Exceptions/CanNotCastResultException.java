package Exceptions;

public class CanNotCastResultException extends RuntimeException{
    public CanNotCastResultException(){
        super("SqlConn.getResult无法识别对应的数据类型");
    }
}
