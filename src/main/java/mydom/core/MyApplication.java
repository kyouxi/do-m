package mydom.core;


import mydom.annotations.MyPackageScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public class MyApplication {

    public static Logger logger = LoggerFactory.getLogger(MyApplication.class);
    public final static BeanFactory beanFactory = new BeanFactory();

    public static void run(Class<?> tClass){
        String[] packageName = getPackageName(tClass);
        beanFactory.loadBeans(packageName);
        DependencyInjection.dependencyInject(BeanFactory.beans);
    }

    private static String[] getPackageName(Class<?> tClass){
        MyPackageScan myPackageScan = tClass.getAnnotation(MyPackageScan.class);
        String[] packageName = myPackageScan.value();
        logger.info(Arrays.toString(packageName));
        return packageName;
    }

}
