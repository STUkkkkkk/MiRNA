package mirna.stukk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StukkApplication {

    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
    public static void main(String[] args) {
        SpringApplication.run(StukkApplication.class, args);
    }

}
