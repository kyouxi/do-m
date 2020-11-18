package mydom.core;

import mydom.annotations.MyComponent;
import mydom.tool.IocUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static mydom.tool.IocUnit.getClassName;

public class BeanFactory {
    public static Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    public final static Map<String,Object> beans = new ConcurrentHashMap<>();

    public Object getBean(String className) {
        return beans.get(className);
    }

    public <T> T getBean(Class<T> tclass){
        String className = getClassName(tclass);
        if(!beans.containsKey(className)){
            logger.error("{} 不在容器中",className);
            return null;
        }
        return (T)beans.get(className);
    }

    public void loadBeans(String[] packageName){
        for(String name : packageName){
            loadBeans(name , MyComponent.class);
        }
    }

    public void loadBeans(String packageName , Class<? extends Annotation> annotation){
        String packageDirName = packageName.replace(".", File.separator);
        logger.info("packageDirName = {}",packageDirName);
        Enumeration<URL> dirs;
        try{
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if(protocol.equals("file")){
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesInPackage(packageName,filePath,true,annotation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findClassesInPackage(String packageName,
                                     String packagePath,
                                     final boolean recursive,
                                     Class<? extends Annotation> annotation){
        File dir = new File(packagePath);
        if(!dir.exists() || !dir.isDirectory()){
            logger.warn("{} 下没有定义任何文件",packageName);
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (recursive && pathname.isDirectory()) || (pathname.getName().endsWith(".class"));
            }
        });
        for (File file:dirfiles) {
            if(file.isDirectory()){
                findClassesInPackage(packageName+"."+file.getName(),file.getAbsolutePath(),recursive,annotation);
            }else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try{
                    Class<?> tclass = Thread.currentThread().getContextClassLoader().loadClass(packageName+'.'+className);
                    if(tclass.isAnnotationPresent(annotation)) {
                        logger.info("添加 {} 至容器中",getClassName(tclass));
                        beans.put(getClassName(tclass),IocUnit.getObjectByClass(tclass));
                    }
                }catch (ClassNotFoundException e){
                    logger.error("找不到类{}的.class文件",packageName+'.'+className);
                    e.printStackTrace();
                }
            }
        }
    }

}
