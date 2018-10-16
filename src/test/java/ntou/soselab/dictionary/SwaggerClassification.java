package ntou.soselab.dictionary;

import ntou.soselab.dictionary.parse.CalculateLDASimilar;
import ntou.soselab.dictionary.parse.ParseSwagger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerClassification {
    Logger log = LoggerFactory.getLogger(SwaggerClassification.class);

    @Autowired
    ParseSwagger parseSwagger;

    @Autowired
    CalculateLDASimilar calculateLDASimilar;

    @Test
    public void readGuruFiles(){
        HashMap<String, Double> result = new HashMap<String, Double>();
        File sDocFolder = new File("./src/main/resources/finish");
        int category = 0;
        for (String serviceFile : sDocFolder.list()) {
            log.info("parse swagger guru file: {}", serviceFile);
            try {
                // do something
                String document = readLocalSwagger("./src/main/resources/finish/" + serviceFile);
                if(document != null){
                    String title = parseSwagger.parseSwaggerTitle(document);
                    calculateLDASimilar.CompareDocSimilarAndClassification(title, category);
                }else{
                    log.error("error read swagger local file: {}", serviceFile);
                }
            } catch (Exception e) {
                log.error("error parsing on {}", serviceFile);
                log.info(e.toString());
            }
            category++;
        }
    }

    // For testing
    public String readLocalSwagger(String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, "UTF-8");
        } catch (IOException e) {
            System.err.println("read swagger error");
            return null;
        }

    }
}
