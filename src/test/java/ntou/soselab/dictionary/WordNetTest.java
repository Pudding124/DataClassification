package ntou.soselab.dictionary;

import edu.mit.jwi.item.POS;
import ntou.soselab.dictionary.algo.WordNetExpansion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Hashtable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordNetTest {
    Logger log = LoggerFactory.getLogger(WordNetTest.class);

    @Autowired
    WordNetExpansion wordNetExpansion;

    @Test
    public void wordnetTest() {
        ArrayList<String> wordSum = new ArrayList<>();
        // 查詢上義詞
        Hashtable<String, Double> testWord1 = wordNetExpansion.getHypernymsByNounOrVerb("weather", POS.NOUN);
        Hashtable<String, Double> testWord2 = wordNetExpansion.getHyponymsByNounOrVerb("weather", POS.NOUN);

        for(String str : testWord1.keySet()) {
            log.info("testWord1 --> Word :{} Score :{}", str, testWord1.get(str));
            if(!wordSum.contains(str)) {
                wordSum.add(str);
            }
        }

        for(String str : testWord2.keySet()) {
            log.info("testWord2 --> Word :{} Score :{}", str, testWord2.get(str));
            if(!wordSum.contains(str)) {
                wordSum.add(str);
            }
        }

        for(String str : wordSum) {
            log.info("wordSum --> Word :{} ", str);
        }
    }
}
