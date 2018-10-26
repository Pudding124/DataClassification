package ntou.soselab.dictionary;

import ntou.soselab.dictionary.algo.CosineSimilarity;
import ntou.soselab.dictionary.bean.Resource;
import ntou.soselab.dictionary.repository.ResourceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CosineSimilarityCSV {
    Logger log = LoggerFactory.getLogger(CosineSimilarityCSV.class);

    @Autowired
    ResourceRepository resourceRepository;

    CosineSimilarity cosineSimilarity = new CosineSimilarity();

    @Test
    public void collectionCosineSimilarityCSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("E:\\ScoreTest.csv"));
        StringBuilder sb = new StringBuilder();

        // 收集所有 Resource 的 id
        ArrayList<Long> allResourceId = new ArrayList<>();
        try {
            for(Resource resource : resourceRepository.findAll()) {
                allResourceId.add(resource.getId());
            }
        } catch (Exception e) {
            log.info("error1 info :{}", e.toString());
        }

        // 將所有出現過的文字寫入到 csv (不重複)
        sb.append(",");
        for(int i = 0;i < allResourceId.size();i++) {
            if(i < allResourceId.size()-1) {
                sb.append(allResourceId.get(i) + ",");
            }else {
                sb.append(allResourceId.get(i));
            }
        }
        sb.append("\n");
        pw.write(sb.toString());
        sb.delete(0, sb.length());

        // 紀錄每個餘弦相似的結果
        double score = 0.0;
        for(Resource currentResource : resourceRepository.findAll()) {
            sb.append(currentResource.getTitle().replaceAll(","," ") + ",");
            ArrayList<String> allWord = new ArrayList<>();
            for(Long id : allResourceId) {
                Resource compareResource = resourceRepository.findById(id);
                for(String str : currentResource.getLDA()){
                    if(!allWord.contains(str)) {
                        allWord.add(str);
                    }
                }
                for(String str : compareResource.getLDA()){
                    if(!allWord.contains(str)) {
                        allWord.add(str);
                    }
                }
                double[] target = new double[allWord.size()];
                double[] compare = new double[allWord.size()];
                for(int i = 0;i < allWord.size();i++){
                    boolean flag = true;
                    for(String str : currentResource.getLDA()){
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
                    for(String str : compareResource.getLDA()){
                        if(allWord.get(i).equals(str)) {
                            compare[i] = 1.0;
                            flag = false;
                            break;
                        }
                    }
                    if(flag) compare[i] = 0.0;
                }
                score = cosineSimilarity.cosineSimilarity(target, compare);
                log.info("Score :{}", score);
                sb.append(score + ",");
            }
            sb.append("\n");
            pw.write(sb.toString());
            sb.delete(0, sb.length());
        }
        pw.close();
    }
}
