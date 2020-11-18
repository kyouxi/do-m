package demo;

import mydom.annotations.MyPackageScan;
import mydom.core.MyApplication;

@MyPackageScan("demo")
public class DomApplication {
    public static void main(String[] args) {
        MyApplication.run(DomApplication.class);
    }
}
