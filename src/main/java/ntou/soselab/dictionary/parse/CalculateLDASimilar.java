package ntou.soselab.dictionary.parse;

import ntou.soselab.dictionary.algo.CosineSimilarity;
import ntou.soselab.dictionary.bean.Resource;
import ntou.soselab.dictionary.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CalculateLDASimilar {

    Logger log = LoggerFactory.getLogger(CalculateLDASimilar.class);

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ClassificationFile classificationFile;

    CosineSimilarity cosineSimilarity = new CosineSimilarity();

    public void CompareScoreBetweenDoc(String title, HashMap<String, Double> result) {
        Resource resource = resourceRepository.findByTitle(title);
        double score = 0.0;
        for(Resource resource1 : resourceRepository.findAll()){
            ArrayList<String> allWord = new ArrayList<>();
            for(String str : resource.getLDA()){
                if(!allWord.contains(str)) {
                    allWord.add(str);
                }
            }
            for(String str : resource1.getLDA()){
                if(!allWord.contains(str)) {
                    allWord.add(str);
                }
            }
            double[] target = new double[allWord.size()];
            double[] compare = new double[allWord.size()];
            for(int i = 0;i < allWord.size();i++){
                boolean flag = true;
                for(String str : resource.getLDA()){
                    if(allWord.get(i).equals(str)) {
                        target[i] = 1.0;
                        flag = false;
                        break;
                    }
                }
                if(flag) target[i] = 0.0;
            }

            for(int i = 0;i < allWord.size();i++){
                boolean flag = true;
                for(String str : resource1.getLDA()){
                    if(allWord.get(i).equals(str)) {
                        compare[i] = 1.0;
                        flag = false;
                        break;
                    }
                }
                if(flag) compare[i] = 0.0;
            }
            score += cosineSimilarity.cosineSimilarity(target, compare);
        }
        log.info("Score :{}", score);
        result.put(title, score);
    }

    public void CompareDocSimilarAndClassification(String title, int categoryNum) {
        Resource resource = resourceRepository.findByTitle(title);
        double score = 0.0;
        for(Resource resource1 : resourceRepository.findAll()){
            ArrayList<String> allWord = new ArrayList<>();
            for(String str : resource.getLDA()){
                if(!allWord.contains(str)) {
                    allWord.add(str);
                }
            }
            for(String str : resource1.getLDA()){
                if(!allWord.contains(str)) {
                    allWord.add(str);
                }
            }
            double[] target = new double[allWord.size()];
            double[] compare = new double[allWord.size()];
            for(int i = 0;i < allWord.size();i++){
                boolean flag = true;
                for(String str : resource.getLDA()){
                    if(allWord.get(i).equals(str)) {
                        target[i] = 1.0;
                        flag = false;
                        break;
                    }
                }
                if(flag) target[i] = 0.0;
            }

            for(int i = 0;i < allWord.size();i++){
                boolean flag = true;
                for(String str : resource1.getLDA()){
                    if(allWord.get(i).equals(str)) {
                        compare[i] = 1.0;
                        flag = false;
                        break;
                    }
                }
                if(flag) compare[i] = 0.0;
            }
            score = cosineSimilarity.cosineSimilarity(target, compare);
            // 判斷是否有相關進行分類
            if(score >= 0.5) {
                classificationFile.FileCreate(resource1.getTitle(), categoryNum);
            }
            log.info("Score :{}", score);
        }
    }

}
