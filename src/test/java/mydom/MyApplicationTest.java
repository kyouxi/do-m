package mydom;

import demo.DomApplication;
import mydom.annotations.MyComponent;
import mydom.core.MyApplication;
import org.junit.Test;

import java.io.File;

public class MyApplicationTest {

    @Test
    public void run(){
        MyApplication.run(DomApplication.class);
    }

}
