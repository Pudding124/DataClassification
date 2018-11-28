package ntou.soselab.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DictionaryApplication {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DictionaryApplication.class, args);
    }
}
