package ntou.soselab.dictionary;

import ntou.soselab.dictionary.parse.JavaCodeParse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaParseTest {
    Logger log = LoggerFactory.getLogger(JavaParseTest.class);

    @Test
    public void Test() {
        JavaCodeParse javaCodeParse = new JavaCodeParse();
        try {
            // /home/mingjen/IdeaProjects/DataClassification/src/main/resources/javaDoc/LocationAdapter.java
            // E:\STS_Test\Dictionary\src\main\resources\javaDoc
            javaCodeParse.getJavaMethodUse("https://api.gettyimages.com/v3/search/images", "E:\\STS_Test\\Dictionary\\src\\main\\resources\\javaDoc\\LocationAdapter.java");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
