package demo.classmodify;


public class HotSwapClassLoader extends ClassLoader {
    public HotSwapClassLoader(){
        super(HotSwapClassLoader.class.getClassLoader());
    }

    // defineClass方法是一个 protect 方法，所以需要一个子类通过public方法内部调用 该protect方法
    public Class loadByte(byte[] classByte){
        return defineClass(null,classByte,0,classByte.length);
    }
}
