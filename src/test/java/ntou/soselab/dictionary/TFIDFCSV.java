package ntou.soselab.dictionary;

import ntou.soselab.dictionary.bean.Resource;
import ntou.soselab.dictionary.parse.ParseSwagger;
import ntou.soselab.dictionary.parse.SwaggerToLDA;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFIDFCSV {
    Logger log = LoggerFactory.getLogger(TFIDFCSV.class);

    @Autowired
    ParseSwagger parseSwagger;

    @Autowired
    ResourceRepository resourceRepository;

    @Test
    public void CollectionCSV() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("E:\\test.csv"));
        StringBuilder sb = new StringBuilder();
        ArrayList<String> allWord = new ArrayList<>();
        try {
            for(Resource resource : resourceRepository.findAll()) {
                for(String str : resource.getLDA()) {
                    if(!allWord.contains(str)) {
                        allWord.add(str);
                    }
                }
            }
        } catch (Exception e) {
            log.info("error1 info :{}", e.toString());
        }
        // 將所有出現過的文字寫入到 csv (不重複)
        for(int i = 0;i < allWord.size();i++) {
            if(i < allWord.size()-1) {
                sb.append(allWord.get(i) + ",");
            }else {
                sb.append(allWord.get(i));
            }
        }
        sb.append("\n");
        pw.write(sb.toString());
        sb.delete(0, sb.length());
        // 比對文本在所有文字中有出現的文字
        try {
            for(Resource resource : resourceRepository.findAll()) {
                sb.append(resource.getTitle().replaceAll(","," ") + ",");
                for(int i = 0;i < allWord.size();i++) {
                    boolean flag = false;
                    for(String str : resource.getLDA()) {
                        if(allWord.get(i).equals(str)) {
                            sb.append("1");
                            flag = true;
                            if(i < allWord.size()-1) {
                                sb.append(",");
                            }
                            break;
                        }
                    }
                    if(!flag) {
                        sb.append("0");
                        if(i < allWord.size()-1) {
                            sb.append(",");
                        }
                    }
                }
                sb.append("\n");
                pw.write(sb.toString());
                sb.delete(0, sb.length());
            }
        } catch (Exception e) {
            log.info("error2 info :{}", e.toString());
        }
        pw.close();
    }
}
