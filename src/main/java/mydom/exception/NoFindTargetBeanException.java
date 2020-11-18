package mydom.exception;

public class NoFindTargetBeanException extends RuntimeException{

    public NoFindTargetBeanException(){}

    public NoFindTargetBeanException(String message){
        super(message);
    }
}
