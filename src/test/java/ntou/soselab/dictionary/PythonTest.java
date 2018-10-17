package ntou.soselab.dictionary;

import ntou.soselab.dictionary.algo.Word2vec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PythonTest {
    Logger log = LoggerFactory.getLogger(PythonTest.class);

    Word2vec word2vec = new Word2vec();

    @Test
    public void pythonTest() {
        try {
            word2vec.useWord2vecWithPython("2", "3");
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}
