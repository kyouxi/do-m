package demo.ioc;

import mydom.annotations.MyAutoWired;
import mydom.annotations.MyComponent;

@MyComponent
public class B {

    @MyAutoWired
    A a;
}
