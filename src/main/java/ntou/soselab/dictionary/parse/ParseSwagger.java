package ntou.soselab.dictionary.parse;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import ntou.soselab.dictionary.algo.TokenizationAndStemming;
import ntou.soselab.dictionary.algo.WordNetExpansion;
import ntou.soselab.dictionary.bean.Resource;
import ntou.soselab.dictionary.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParseSwagger {
    Logger log = LoggerFactory.getLogger(ParseSwagger.class);

    SwaggerToLDA swaggerToLDA = new SwaggerToLDA();

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    WordNetExpansion wordNetExpansion;

    TokenizationAndStemming tokenizationAndStemming = new TokenizationAndStemming();

    public void parseSwaggerDescription(String swaggerDoc,  HashMap<String, Double> result) throws IOException {
        Swagger swagger = new SwaggerParser().parse(swaggerDoc);

        // store swagger parse information
        ArrayList<String> swaggerInfo = new ArrayList<>();

        // For saving key: stemming term --> value: original term
        HashMap<String, String> stemmingAndTermsTable = new HashMap<String, String>();

        String title = swagger.getInfo().getTitle();
        log.info("title :{}", title);
        String description = swagger.getInfo().getDescription();
        log.info("description :{}", description);

        if(title != null) swaggerInfo.add(title);
        if(description != null) swaggerInfo.add(description);

        // parse Full Text
        ArrayList<String> FullText = parseSwaggerAllDescriptionForTFIDFAndStemming(swaggerDoc);
        // parse LDA
        ArrayList<String> LDAWord = swaggerToLDA.swaggerParseLDA(swaggerInfo.toArray(new String[0]), stemmingAndTermsTable);
        // parse WordNet
        ArrayList<String> WordNetWord = wordNetExpansion.getWordNetExpansion(LDAWord, stemmingAndTermsTable);

        // store neo4j information
        Resource resource = new Resource(title, FullText, LDAWord, WordNetWord);
        resourceRepository.save(resource);
    }

    public ArrayList<String> parseSwaggerAllDescriptionForTFIDFAndStemming(String swaggerDoc) throws IOException {
        Swagger swagger = new SwaggerParser().parse(swaggerDoc);
        // this doc all description word
        ArrayList<String> word = new ArrayList<>();
        ArrayList<String> allTokenWord = new ArrayList<>();
        HashMap<String, String> stemmingAndTermsTable = new HashMap<String, String>();

        String title = swagger.getInfo().getTitle();
        log.info("title :{}", title);
        if(title != null) word.add(title);
        String description = swagger.getInfo().getDescription();
        log.info("description :{}", description);
        if(description != null) word.add(description);

        for(String path : swagger.getPaths().keySet()) {
            if (swagger.getPaths().get(path).getDelete() != null) {
                io.swagger.models.Operation swaggerOperation = swagger.getPaths().get(path).getDelete();
                String pathDescription = swaggerOperation.getDescription();
                String pathSummary = swaggerOperation.getSummary();
                log.info("pathDescription :{}", pathDescription);
                log.info("pathSummary :{}", pathSummary);
                if(pathDescription != null) word.add(pathDescription);
                if(pathSummary != null) word.add(pathSummary);

                List<String> pathTags = swaggerOperation.getTags();
                if(pathTags != null) {
                    for(String pathTag : pathTags) {
                        log.info("pathTag :{}", pathTag);
                        if(pathTag != null) word.add(pathTag);
                    }
                }

                List<Parameter> pathParameters = swaggerOperation.getParameters();
                if(pathParameters != null) {
                    for(Parameter parameter : pathParameters) {
                        String parameterDescription = parameter.getDescription();
                        log.info("parameterDescription");
                        if(parameterDescription != null) word.add(parameterDescription);
                    }
                }

                for(String statusCode : swaggerOperation.getResponses().keySet()) {
                    String responseDescription = swaggerOperation.getResponses().get(statusCode).getDescription();
                    log.info("responseDescription :{}", responseDescription);
                    if(responseDescription != null) word.add(responseDescription);
                }
            }
            if (swagger.getPaths().get(path).getGet() != null) {
                io.swagger.models.Operation swaggerOperation = swagger.getPaths().get(path).getGet();
                String pathDescription = swaggerOperation.getDescription();
                String pathSummary = swaggerOperation.getSummary();
                log.info("pathDescription :{}", pathDescription);
                log.info("pathSummary :{}", pathSummary);
                if(pathDescription != null) word.add(pathDescription);
                if(pathSummary != null) word.add(pathSummary);

                List<String> pathTags = swaggerOperation.getTags();
                if(pathTags != null) {
                    for(String pathTag : pathTags) {
                        log.info("pathTag :{}", pathTag);
                        if(pathTag != null) word.add(pathTag);
                    }
                }

                List<Parameter> pathParameters = swaggerOperation.getParameters();
                if(pathParameters != null) {
                    for(Parameter parameter : pathParameters) {
                        String parameterDescription = parameter.getDescription();
                        log.info("parameterDescription");
                        if(parameterDescription != null) word.add(parameterDescription);
                    }
                }

                for(String statusCode : swaggerOperation.getResponses().keySet()) {
                    String responseDescription = swaggerOperation.getResponses().get(statusCode).getDescription();
                    log.info("responseDescription :{}", responseDescription);
                    if(responseDescription != null) word.add(responseDescription);
                }
            }
            if (swagger.getPaths().get(path).getPatch() != null) {
                io.swagger.models.Operation swaggerOperation = swagger.getPaths().get(path).getPatch();
                String pathDescription = swaggerOperation.getDescription();
                String pathSummary = swaggerOperation.getSummary();
                log.info("pathDescription :{}", pathDescription);
                log.info("pathSummary :{}", pathSummary);
                if(pathDescription != null) word.add(pathDescription);
                if(pathSummary != null) word.add(pathSummary);

                List<String> pathTags = swaggerOperation.getTags();
                if(pathTags != null) {
                    for(String pathTag : pathTags) {
                        log.info("pathTag :{}", pathTag);
                        if(pathTag != null) word.add(pathTag);
                    }
                }

                List<Parameter> pathParameters = swaggerOperation.getParameters();
                if(pathParameters != null) {
                    for(Parameter parameter : pathParameters) {
                        String parameterDescription = parameter.getDescription();
                        log.info("parameterDescription");
                        if(parameterDescription != null) word.add(parameterDescription);
                    }
                }

                for(String statusCode : swaggerOperation.getResponses().keySet()) {
                    String responseDescription = swaggerOperation.getResponses().get(statusCode).getDescription();
                    log.info("responseDescription :{}", responseDescription);
                    if(responseDescription != null) word.add(responseDescription);
                }
            }
            if (swagger.getPaths().get(path).getPost() != null) {
                io.swagger.models.Operation swaggerOperation = swagger.getPaths().get(path).getPost();
                String pathDescription = swaggerOperation.getDescription();
                String pathSummary = swaggerOperation.getSummary();
                log.info("pathDescription :{}", pathDescription);
                log.info("pathSummary :{}", pathSummary);
                if(pathDescription != null) word.add(pathDescription);
                if(pathSummary != null) word.add(pathSummary);

                List<String> pathTags = swaggerOperation.getTags();
                if(pathTags != null) {
                    for(String pathTag : pathTags) {
                        log.info("pathTag :{}", pathTag);
                        if(pathTag != null) word.add(pathTag);
                    }
                }

                List<Parameter> pathParameters = swaggerOperation.getParameters();
                if(pathParameters != null) {
                    for(Parameter parameter : pathParameters) {
                        String parameterDescription = parameter.getDescription();
                        log.info("parameterDescription");
                        if(parameterDescription != null) word.add(parameterDescription);
                    }
                }

                for(String statusCode : swaggerOperation.getResponses().keySet()) {
                    String responseDescription = swaggerOperation.getResponses().get(statusCode).getDescription();
                    log.info("responseDescription :{}", responseDescription);
                    if(responseDescription != null) word.add(responseDescription);
                }
            }
            if (swagger.getPaths().get(path).getPut() != null) {
                io.swagger.models.Operation swaggerOperation = swagger.getPaths().get(path).getPut();
                String pathDescription = swaggerOperation.getDescription();
                String pathSummary = swaggerOperation.getSummary();
                log.info("pathDescription :{}", pathDescription);
                log.info("pathSummary :{}", pathSummary);
                if(pathDescription != null) word.add(pathDescription);
                if(pathSummary != null) word.add(pathSummary);

                List<String> pathTags = swaggerOperation.getTags();
                if(pathTags != null) {
                    for(String pathTag : pathTags) {
                        log.info("pathTag :{}", pathTag);
                        if(pathTag != null) word.add(pathTag);
                    }
                }

                List<Parameter> pathParameters = swaggerOperation.getParameters();
                if(pathParameters != null) {
                    for(Parameter parameter : pathParameters) {
                        String parameterDescription = parameter.getDescription();
                        log.info("parameterDescription");
                        if(parameterDescription != null) word.add(parameterDescription);
                    }
                }

                for(String statusCode : swaggerOperation.getResponses().keySet()) {
                    String responseDescription = swaggerOperation.getResponses().get(statusCode).getDescription();
                    log.info("responseDescription :{}", responseDescription);
                    if(responseDescription != null) word.add(responseDescription);
                }
            }
        }
        // 收集好所有資訊，進行 stemming 完後，回傳
        String[] swaggerInfoWord = word.toArray(new String[0]);
        for (int i = 0; i < swaggerInfoWord.length; i++) {
            String terms = change_ToSeperateTerms(
                    changeDotsToSeperateTerms(changeCamelWordsToSeperateTerms(replaceTagsToNone(swaggerInfoWord[i]))));
            swaggerInfoWord[i] = tokenizationAndStemming.stemTermsAndSaveOriginalTerm(terms, stemmingAndTermsTable);
            //writeTxt.inputTxt("['"+ResourceConcept[i]+"'],");
            log.info(" -- {}", swaggerInfoWord[i]);
            String str[] = swaggerInfoWord[i].split(" ");
            for(String token : str) {
                allTokenWord.add(token);
            }
        }
        return allTokenWord;
    }

    private String replaceTagsToNone(String input) {
        return input.replaceAll("<.*?>", " ").trim();
    }

    private String changeCamelWordsToSeperateTerms(String input) {
        String[] data = input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        StringBuilder builder = new StringBuilder();
        for (String w : data) {
            builder.append(w.toLowerCase());
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private String changeDotsToSeperateTerms(String input) {
        return input.replaceAll("\\.", " ").trim();
    }

    private String change_ToSeperateTerms(String input) {
        return input.replaceAll("_", " ").trim();
    }
}
