package kaytin.zhang.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * desc
 * </p>
 *
 * @author KayTin 2020/8/14
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = "kaytin.zhang.test.ChinaHelloServiceImpl";
        Object helloService = Class.forName(className).newInstance();
        Method[] methods = helloService.getClass().getMethods();
        Method method = null;
        for (Method m:methods) {
            if(m.getName().contains("sayHello")) {
                method = m;
            }
        }
        method.invoke(helloService,"lk:");
    }
}
