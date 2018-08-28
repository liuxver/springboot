/**
 * created by liuxv on 2018/8/14
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@SpringBootApplication
@RestController
public class Application {
    @RequestMapping("/")
    String home(){
        return "hello!";
    }

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
