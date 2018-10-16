package ntou.soselab.dictionary;

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
public class GuruLocalFiles {
    Logger log = LoggerFactory.getLogger(GuruLocalFiles.class);

    @Autowired
    ParseSwagger parseSwagger;

    @Test
    public void readGuruFiles(){
        HashMap<String, Double> result = new HashMap<String, Double>();
        File sDocFolder = new File("./src/main/resources/swagger");
        for (String serviceFile : sDocFolder.list()) {
            log.info("parse swagger guru file: {}", serviceFile);
            try {
                // do something
                String document = readLocalSwagger("./src/main/resources/swagger/" + serviceFile);
                if(document != null){
                    parseSwagger.parseSwaggerDescription(document, result);
                }else{
                    log.error("error read swagger local file: {}", serviceFile);
                }
                Files.move(Paths.get("./src/main/resources/swagger/" + serviceFile), Paths.get("./src/main/resources/finish/" + serviceFile));
                log.info("finish move file {} to finish folder.", serviceFile);
            } catch (Exception e) {
                log.error("error parsing on {}", serviceFile);
                log.info(e.toString());
                try {
                    Files.move(Paths.get("./src/main/resources/swagger/" + serviceFile), Paths.get("./src/main/resources/parseError/" + serviceFile));
                } catch (IOException e1) {
                    log.info("error on move file to error folder", e);
                }
            }
        }
        for(String key : result.keySet()){
            log.info("------------------------------------------------------");
            log.info("Title :{}", key);
            log.info("Score :{}", result.get(key));
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
