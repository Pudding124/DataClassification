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

    //@Test
    public void collection_LDA_CSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("/home/mingjen/Desktop/TSNETestData/LDA.csv"));
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
        double sumScore = 0.0;
        for(Resource currentResource : resourceRepository.findAll()) {
            sb.append(currentResource.getTitle().replaceAll(","," ") + ",");

            for(Long id : allResourceId) {
                Resource compareResource = resourceRepository.findById(id);
                ArrayList<String> allWord = new ArrayList<>();
                double ldaScore = 0.0;

                // avoid get null
                if(!currentResource.getLDA().isEmpty() && !compareResource.getLDA().isEmpty()) {
                    // record all LDA appear word
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
                                target[i]++;
                                flag = false;
                            }
                        }
                        if(flag) target[i] = 0.0;
                    }

                    for(int i = 0;i < allWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getLDA()){
                            if(allWord.get(i).equals(str)) {
                                compare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) compare[i] = 0.0;
                    }
                    ldaScore = cosineSimilarity.cosineSimilarity(target, compare);
                }

                if(currentResource.getId().equals(compareResource.getId())) {
                    sumScore = 1;
                }else {
                    sumScore = ldaScore;
                }
                log.info("Score :{}", sumScore);
                sb.append(sumScore + ",");
            }
            sb.append("\n");
            pw.write(sb.toString());
            sb.delete(0, sb.length());
        }
        pw.close();
    }

    //@Test
    public void collection_LDA_WordNet_CSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("/home/mingjen/Desktop/TSNETestData/LDA-WordNet-588-Threshold-0.01.csv"));
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
        double sumScore = 0.0;
        for(Resource currentResource : resourceRepository.findAll()) {
            sb.append(currentResource.getTitle().replaceAll(","," ") + ",");

            for(Long id : allResourceId) {
                Resource compareResource = resourceRepository.findById(id);

                // record LDA all word
                ArrayList<String> ldaAllWord = new ArrayList<>();
                // record WordNet all word
                ArrayList<String> wordnetAllWord = new ArrayList<>();

                double ldaScore = 0.0;
                double wordnetScore = 0.0;

                // avoid get null
                if(!currentResource.getLDA().isEmpty() && !compareResource.getLDA().isEmpty()) {
                    // record all LDA appear word
                    for(String str : currentResource.getLDA()){
                        if(!ldaAllWord.contains(str)) {
                            ldaAllWord.add(str);
                        }
                    }
                    for(String str : compareResource.getLDA()){
                        if(!ldaAllWord.contains(str)) {
                            ldaAllWord.add(str);
                        }
                    }

                    double[] target = new double[ldaAllWord.size()];
                    double[] compare = new double[ldaAllWord.size()];
                    for(int i = 0;i < ldaAllWord.size();i++){
                        boolean flag = true;
                        for(String str : currentResource.getLDA()){
                            if(ldaAllWord.get(i).equals(str)) {
                                target[i]++;
                                flag = false;
                            }
                        }
                        if(flag) target[i] = 0.0;
                    }

                    for(int i = 0;i < ldaAllWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getLDA()){
                            if(ldaAllWord.get(i).equals(str)) {
                                compare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) compare[i] = 0.0;
                    }
                    ldaScore = cosineSimilarity.cosineSimilarity(target, compare);
                }

                // avoid get null
                if(!currentResource.getWordNet().isEmpty() && !compareResource.getWordNet().isEmpty()) {
                    // record all LDA appear word
                    for(String str : currentResource.getWordNet()){
                        if(!wordnetAllWord.contains(str)) {
                            wordnetAllWord.add(str);
                        }
                    }
                    for(String str : compareResource.getWordNet()){
                        if(!wordnetAllWord.contains(str)) {
                            wordnetAllWord.add(str);
                        }
                    }

                    double[] target = new double[wordnetAllWord.size()];
                    double[] compare = new double[wordnetAllWord.size()];
                    for(int i = 0;i < wordnetAllWord.size();i++){
                        boolean flag = true;
                        for(String str : currentResource.getWordNet()){
                            if(wordnetAllWord.get(i).equals(str)) {
                                target[i]++;
                                flag = false;
                            }
                        }
                        if(flag) target[i] = 0.0;
                    }

                    for(int i = 0;i < wordnetAllWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getWordNet()){
                            if(wordnetAllWord.get(i).equals(str)) {
                                compare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) compare[i] = 0.0;
                    }
                    wordnetScore = cosineSimilarity.cosineSimilarity(target, compare);
                }

                if(currentResource.getId().equals(compareResource.getId())) {
                    sumScore = 1;
                }else if(currentResource.getWordNet().isEmpty() && compareResource.getWordNet().isEmpty()) {
                    sumScore = ldaScore;
                }else {
                    sumScore = ldaScore*0.7 + wordnetScore*0.3;
                }

                if(sumScore < 0.01) sumScore = 0;
                log.info("Score :{}", sumScore);
                sb.append(sumScore + ",");
            }
            sb.append("\n");
            pw.write(sb.toString());
            sb.delete(0, sb.length());
        }
        pw.close();
    }

    //@Test
    public void collection_Full_Text_CSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("/home/mingjen/Desktop/TSNETestData/FullTextScore-588.csv"));
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

        // 將所有出現過的 node id 寫入到 csv (不重複)
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
            for(Long id : allResourceId) {
                Resource compareResource = resourceRepository.findById(id);
                ArrayList<String> allWord = new ArrayList<>();

                for(String str : currentResource.getFullText()){
                    if(!allWord.contains(str)) {
                        allWord.add(str);
                    }
                }
                for(String str : compareResource.getFullText()){
                    if(!allWord.contains(str)) {
                        allWord.add(str);
                    }
                }


                double[] target = new double[allWord.size()];
                double[] compare = new double[allWord.size()];
                for(int i = 0;i < allWord.size();i++){
                    boolean flag = true;
                    for(String str : currentResource.getFullText()){
                        if(allWord.get(i).equals(str)) {
                            target[i]++;
                            flag = false;
                        }
                    }
                    if(flag) target[i] = 0.0;
                }

                for(int i = 0;i < allWord.size();i++){
                    boolean flag = true;
                    for(String str : compareResource.getFullText()){
                        if(allWord.get(i).equals(str)) {
                            compare[i]++;
                            flag = false;
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

    // LDA 0.6 WordNet 0.2 FullText 0.2
    @Test
    public void collection_LDA_WordNet_FullText_CSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("/home/mingjen/Desktop/TSNETestData/LDA-WordNet-FullText-588-712-Threshold-321.csv"));
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
        double sumScore = 0.0;
        for(Resource currentResource : resourceRepository.findAll()) {
            sb.append(currentResource.getTitle().replaceAll(","," ") + ",");

            for(Long id : allResourceId) {
                Resource compareResource = resourceRepository.findById(id);

                ArrayList<String> ldaAllWord = new ArrayList<>();
                ArrayList<String> wordnetAllWord = new ArrayList<>();
                ArrayList<String> fullTextAllWord = new ArrayList<>();

                double ldaScore = 0.0;
                double wordnetScore = 0.0;
                double fullTextScore = 0.0;

                if(!currentResource.getLDA().isEmpty() && !compareResource.getLDA().isEmpty()) {
                    // collection all LDA appear word
                    for(String str : currentResource.getLDA()){
                        if(!ldaAllWord.contains(str)) {
                            ldaAllWord.add(str);
                        }
                    }
                    for(String str : compareResource.getLDA()){
                        if(!ldaAllWord.contains(str)) {
                            ldaAllWord.add(str);
                        }
                    }

                    double[] ldaTarget = new double[ldaAllWord.size()];
                    double[] ldaCompare = new double[ldaAllWord.size()];

                    for(int i = 0;i < ldaAllWord.size();i++){
                        boolean flag = true;
                        for(String str : currentResource.getLDA()){
                            if(ldaAllWord.get(i).equals(str)) {
                                ldaTarget[i]++;
                                flag = false;
                            }
                        }
                        if(flag) ldaTarget[i] = 0.0;
                    }
                    for(int i = 0;i < ldaAllWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getLDA()){
                            if(ldaAllWord.get(i).equals(str)) {
                                ldaCompare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) ldaCompare[i] = 0.0;
                    }

                    ldaScore = cosineSimilarity.cosineSimilarity(ldaTarget, ldaCompare);
                }

                if(!currentResource.getWordNet().isEmpty() && !compareResource.getWordNet().isEmpty()) {
                    // collection all WordNet appear word
                    for(String str : currentResource.getWordNet()){
                        if(!wordnetAllWord.contains(str)) {
                            wordnetAllWord.add(str);
                        }
                    }
                    for(String str : compareResource.getWordNet()){
                        if(!wordnetAllWord.contains(str)) {
                            wordnetAllWord.add(str);
                        }
                    }

                    double[] wordnetTarget = new double[wordnetAllWord.size()];
                    double[] wordnetCompare = new double[wordnetAllWord.size()];

                    for(int i = 0;i < wordnetAllWord.size();i++){
                        boolean flag = true;
                        for(String str : currentResource.getWordNet()){
                            if(wordnetAllWord.get(i).equals(str)) {
                                wordnetTarget[i]++;
                                flag = false;
                            }
                        }
                        if(flag) wordnetTarget[i] = 0.0;
                    }
                    for(int i = 0;i < wordnetAllWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getWordNet()){
                            if(wordnetAllWord.get(i).equals(str)) {
                                wordnetCompare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) wordnetCompare[i] = 0.0;
                    }

                    wordnetScore = cosineSimilarity.cosineSimilarity(wordnetTarget, wordnetCompare);
                }

                if(!currentResource.getFullText().isEmpty() && !compareResource.getFullText().isEmpty()) {
                    // collection all FullText appear word
                    for(String str : currentResource.getFullText()){
                        if(!fullTextAllWord.contains(str)) {
                            fullTextAllWord.add(str);
                        }
                    }
                    for(String str : compareResource.getFullText()){
                        if(!fullTextAllWord.contains(str)) {
                            fullTextAllWord.add(str);
                        }
                    }

                    double[] fullTextTarget = new double[fullTextAllWord.size()];
                    double[] fullTextCompare = new double[fullTextAllWord.size()];

                    for(int i = 0;i < fullTextAllWord.size();i++){
                        boolean flag = true;
                        for(String str : currentResource.getFullText()){
                            if(fullTextAllWord.get(i).equals(str)) {
                                fullTextTarget[i]++;
                                flag = false;
                            }
                        }
                        if(flag) fullTextTarget[i] = 0.0;
                    }
                    for(int i = 0;i < fullTextAllWord.size();i++){
                        boolean flag = true;
                        for(String str : compareResource.getFullText()){
                            if(fullTextAllWord.get(i).equals(str)) {
                                fullTextCompare[i]++;
                                flag = false;
                            }
                        }
                        if(flag) fullTextCompare[i] = 0.0;
                    }
                    fullTextScore = cosineSimilarity.cosineSimilarity(fullTextTarget, fullTextCompare);
                }

                if(currentResource.getId().equals(compareResource.getId())) {
                    sumScore = 1;
                }else if(currentResource.getLDA().isEmpty() && compareResource.getLDA().isEmpty() && currentResource.getWordNet().isEmpty() && compareResource.getWordNet().isEmpty()) {
                    sumScore = fullTextScore;
                }else if(currentResource.getWordNet().isEmpty() && compareResource.getWordNet().isEmpty()) {
                    sumScore = ldaScore*0.8 + fullTextScore*0.2;
                }else {
                    sumScore = ldaScore*0.7 + wordnetScore*0.1 + fullTextScore*0.2;
                }

                if(sumScore >= 1) {
                    sumScore = 3;
                }else if(sumScore >= 0.5) {
                    sumScore = 2;
                }else if(sumScore >= 0.1) {
                    sumScore = 1;
                }else {
                    sumScore = 0;
                }
                log.info("Score :{}", sumScore);
                sb.append(sumScore + ",");
            }
            sb.append("\n");
            pw.write(sb.toString());
            sb.delete(0, sb.length());
        }
        pw.close();
    }
}
