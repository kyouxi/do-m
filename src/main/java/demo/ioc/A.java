package demo.ioc;

import mydom.annotations.MyAutoWired;
import mydom.annotations.MyComponent;

@MyComponent
public class A {
    @MyAutoWired
    C c;

    @MyAutoWired
    B b;
}
