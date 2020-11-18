package mydom.tool;

import mydom.annotations.MyComponent;

public class IocUnit {

    public static String getClassName(Class<?> tclass){
        String name = tclass.getName();
        if(tclass.isAnnotationPresent(MyComponent.class)){
            String tname = tclass.getAnnotation(MyComponent.class).name();
            if(!"".equals(tname)){
                return tname;
            }
        }
        return name;
    }

    public static Object getObjectByClass(Class<?> tclass){
        try {
            return tclass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
