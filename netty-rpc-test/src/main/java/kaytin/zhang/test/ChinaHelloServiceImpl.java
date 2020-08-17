package kaytin.zhang.test;

/**
 * <p>
 * desc
 * </p>
 *
 * @author KayTin 2020/8/14
 */
public class ChinaHelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.printf("======="+name+"========");
        return name + String.valueOf(System.currentTimeMillis());
    }
}
