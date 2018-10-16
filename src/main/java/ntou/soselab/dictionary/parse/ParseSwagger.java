package ntou.soselab.dictionary.parse;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import ntou.soselab.dictionary.bean.Resource;
import ntou.soselab.dictionary.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ParseSwagger {
    Logger log = LoggerFactory.getLogger(ParseSwagger.class);

    SwaggerToLDA swaggerToLDA = new SwaggerToLDA();

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    CalculateLDASimilar calculateLDASimilar;

    public void parseSwaggerDescription(String swaggerDoc,  HashMap<String, Double> result) throws IOException {
        Swagger swagger = new SwaggerParser().parse(swaggerDoc);

        ArrayList<String> swaggerInfo = new ArrayList<>(); // store swagger parse information

        String title = swagger.getInfo().getTitle();
        log.info("title :{}", title);
        String description = swagger.getInfo().getDescription();
        log.info("description :{}", description);

        if(title != null) swaggerInfo.add(title);
        if(description != null) swaggerInfo.add(description);

        // parse LDA
        // ArrayList<String> LDAWord = swaggerToLDA.swaggerParseLDA(swaggerInfo.toArray(new String[0]));

        // store neo4j information
        // Resource resource = new Resource(title, LDAWord);
        // resourceRepository.save(resource);

        // 計算 Swagger Doc 與所有 Swagger Doc 做餘弦相似度，並相加
        calculateLDASimilar.CompareScoreBetweenDoc(title, result);
    }

    public String parseSwaggerTitle(String swaggerDoc) {
        Swagger swagger = new SwaggerParser().parse(swaggerDoc);
        String title = swagger.getInfo().getTitle();
        return title;
    }

}
