package ubublik.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ubublik.network.models.dao.ImageDao;

/**
 * Created by Bublik on 10-Jun-17.
 */

@SpringBootApplication
public class Application {



    // TODO: 14-Jun-17 add session.close() to all dao classes
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);

    }
}
