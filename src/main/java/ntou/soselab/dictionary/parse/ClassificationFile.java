package ntou.soselab.dictionary.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ClassificationFile {

    Logger log = LoggerFactory.getLogger(ClassificationFile.class);

    @Autowired
    ParseSwagger parseSwagger;

    public void FileCreate(String title, int categoryNum) {
        File file = new File("./src/main/resources/classification/"+categoryNum);
        if(!file.exists()) {
            if(file.mkdir()) {
                log.info("Directory is created !");
            }else {
                log.info("Failed to create directory");
            }
        }
        File sDocFolder = new File("./src/main/resources/store");
        for (String serviceFile : sDocFolder.list()) {
            log.info("parse swagger guru file: {}", serviceFile);
            try {
                // do something
                String document = readLocalSwagger("./src/main/resources/store/" + serviceFile);
                if(document != null){
                    if(title.equals(parseSwagger.parseSwaggerTitle(document))) {
                        Files.move(Paths.get("./src/main/resources/store/" + serviceFile), Paths.get("./src/main/resources/classification/"+ categoryNum + "/" + serviceFile));
                    }
                }
                log.info("finish move file {} to finish folder.", serviceFile);
            } catch (Exception e) {
                log.error("error parsing on {}", serviceFile);
                log.info(e.toString());
            }
        }
    }

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
