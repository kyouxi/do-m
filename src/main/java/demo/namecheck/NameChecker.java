package demo.namecheck;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;
import java.util.EnumSet;


public class NameChecker {
    private final Messager messager;

    NameCheckScanner nameCheckScanner = new NameCheckScanner();

    public NameChecker(ProcessingEnvironment processingEnv){
        this.messager = processingEnv.getMessager();
    }

    public void checkNames(Element element){
        nameCheckScanner.scan(element);
    }

    private class NameCheckScanner extends ElementScanner8<Void,Void> {
        @Override
        public Void visitType(TypeElement e, Void p) {
            scan(e.getTypeParameters(),p);
            checkCamelCase(e,true);
            super.visitType(e,p);
            return null;
        }

        @Override
        public Void visitVariable(VariableElement e, Void p) {
            //常量名检查
            if(e.getKind() == ElementKind.ENUM_CONSTANT || e.getConstantValue() != null
                    || heuristicallyConstant(e)){
                checkAllCaps(e);
            }else{
                // 变量名检查
                checkCamelCase(e,false);
            }
            return null;
        }

        @Override
        public Void visitExecutable(ExecutableElement e, Void p) {
            //方法名检查
            if(e.getKind() == ElementKind.METHOD){
                Name name = e.getSimpleName();
                if(name.contentEquals(e.getEnclosingElement().getSimpleName())){
                    messager.printMessage(Diagnostic.Kind.WARNING,"一个普通方法 “" + name +
                            "” 不应当与类名重复，避免与构造函数产生混淆",e);
                    checkCamelCase(e,false);
                }
            }
            super.visitExecutable(e,p);
            return null;
        }

        private boolean heuristicallyConstant(VariableElement e){
            if(e.getEnclosingElement().getKind() == ElementKind.INTERFACE){
                return true;
            }else if(e.getKind() == ElementKind.FIELD && e.getModifiers().containsAll(EnumSet.of(Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL))){
                return true;
            }else{
                return false;
            }
        }

        //驼峰命名检查
        private void checkCamelCase(Element e ,boolean initialCaps){
            String name = e.getSimpleName().toString();
            int firstCodePoint = name.codePointAt(0);
            boolean previousUpper = false;
            boolean conventional = true;
            if(Character.isUpperCase(firstCodePoint)){
                previousUpper = true;
                if(!initialCaps){
                    messager.printMessage(Diagnostic.Kind.WARNING,name + " 应小写字母开头",e);
                    return;
                }
            }else if(Character.isLowerCase(firstCodePoint)){
                if(initialCaps){
                    messager.printMessage(Diagnostic.Kind.WARNING,name +" 应大写字母开头",e);
                    return;
                }
            }else {
                conventional = false;
            }
            //遍历name字符串，如果出现连续的大写字母，则判断不符合命名规则
            if(conventional){
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i<name.length(); i+= Character.charCount(cp)){
                    cp = name.codePointAt(i);
                    if(Character.isUpperCase(cp)){
                        if(previousUpper){
                            conventional = false;
                            break;
                        }
                        previousUpper =true;
                    }else{
                        previousUpper = false;
                    }
                }
            }
            if(!conventional){
                messager.printMessage(Diagnostic.Kind.WARNING,name + "应符合驼峰命名", e);
            }
        }

        //常量值的命名检查：检查首字母必须是大写，其余部分可以是大写或者_
        private void checkAllCaps(Element e){
            String name = e.getSimpleName().toString();
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if(!Character.isUpperCase(firstCodePoint)){
                conventional  =false;
            }else {
                boolean previousUnderscore = false;
                int cp = firstCodePoint;
                //遍历字符串name，
                for (int i = Character.charCount(cp);i<name.length();i+=Character.charCount(cp)){
                    cp = name.codePointAt(i);
                    if(cp == (int) '_'){
                        //如果出现连续的两个 _ ，则判定不符合
                        if(previousUnderscore){
                            conventional = false;
                            break;
                        }
                        previousUnderscore = true;
                    }else{
                        previousUnderscore = false;
                        //如果不是大写 或者 不是 数字 则不符合
                        if(!Character.isUpperCase(cp) && !Character.isDigit(cp)){
                            conventional = false;
                            break;
                        }
                    }
                }
            }
            if(!conventional){
                messager.printMessage(Diagnostic.Kind.WARNING,"常量 ”" + name+ "“ " +
                        "应当全部以大写字母或下划线命名，并且以字母开头", e);
            }
        }

    }
}
