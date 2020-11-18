package demo.classmodify;

import java.lang.reflect.Method;

public class JavaclassExecuter {

    //外部传入一个代表java类的Byte数组
    public static String execute(byte[] classByte) {
        HackSystem.clearBuffer();
        ClassModifier cm = new ClassModifier(classByte);
        //将字节码常量池中的UTF8常量中表示的"java/lang/System"字符串替换成 "demo/remoteHandler/HackSystem"
        byte[] modiBytes = cm.modifyUTF8Constant("java/lang/System", "demo/classmodify/HackSystem");
        HotSwapClassLoader loader = new HotSwapClassLoader();
        //通过loadByte调用 defineClass方法，加载字节码变成 class对象
        Class clazz = loader.loadByte(modiBytes);
        try{
            Method method = clazz.getMethod("main", new Class[]{String[].class});
            method.invoke(null,new String[]{null});
        }catch (Throwable e){
            e.printStackTrace(HackSystem.out);
        }
        return HackSystem.getBufferString();
    }
}
