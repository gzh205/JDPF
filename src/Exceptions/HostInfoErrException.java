package Exceptions;

public class HostInfoErrException extends RuntimeException{
    public HostInfoErrException(){
        super("HostInfo格式错误");
    }
}
