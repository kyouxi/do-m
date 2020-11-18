package demo.namecheck;


import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;

public class test {
    private cache hash = new cache(null,null);

    public static void main(String[] args) {
        test test = new test();
        for(int i=0;i<100;i=i+2){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer y = test.service( finalI);
                    System.out.println(finalI + ":" + y);
                }
            }).run();
        }
    }

    public Integer service(Integer x){
        Integer y = hash.get(x);
        if(y == null){
            y = x +1;
            hash = new cache(x,y);
        }
        return y;
    }


    class cache{
        private Integer x;
        private Integer y;
        public cache(Integer x ,Integer y){
            this.x = x;
            this.y = y;
        }

        public Integer get(Integer x){
            if(x==null || !x.equals(this.x))
                return null;
            else
                return this.y;
        }
    }
}
