package ntou.soselab.dictionary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogBackTest {

    Logger log = LoggerFactory.getLogger(LogBackTest.class);

    @Test
    public void logBack() {
        log.info("It is INFO");
        log.debug("It is DEBUG");
        log.warn("It is WARN");
        log.error("IT is ERROR");
    }
}
