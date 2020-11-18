package mydom.core;

import mydom.annotations.MyAutoWired;
import mydom.annotations.MyQualifier;
import mydom.exception.NoFindTargetBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static mydom.tool.IocUnit.getClassName;

public class DependencyInjection {
    public static Logger logger = LoggerFactory.getLogger(DependencyInjection.class);

    public static void dependencyInject(Map<String,Object> beans){
        if(beans.size() > 0){
            beans.values().forEach(obj ->{
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field f : fields)
                    if(f.isAnnotationPresent(MyAutoWired.class)){
                        Object newField = autoWiredProcess(f);
                        f.setAccessible(true);
                        try {
                            f.set(obj,newField);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
            });
        }
    }

    private static Object autoWiredProcess(Field f){
        Class<?> aClass = f.getType();
        Object newField = BeanFactory.beans.get(getClassName(aClass));
        if(aClass.isInterface()){
            Set<Object> set = new HashSet<>();
            for(Object o : BeanFactory.beans.values()){
                if(aClass.isAssignableFrom(o.getClass())){
                    set.add(o);
                }
            }
            if(set.size() == 0){
                logger.error("没有类实现了接口{}",aClass.getName());
            }
            if(set.size() == 1){
                newField = set.iterator().next();
            }
            if(set.size() > 1){
                if(f.isAnnotationPresent(MyQualifier.class)){
                    String name = f.getDeclaredAnnotation(MyQualifier.class).name();
                    newField = BeanFactory.beans.get(name);
                }else {
                    logger.error("接口{} 有太多个实现类", aClass.getName());
                }
            }
        }
        if(newField == null) {
            logger.error("{}没有找到适合的目标bean", f.getType().getName());
            throw new NoFindTargetBeanException();
        }
        return newField;
    }

}
