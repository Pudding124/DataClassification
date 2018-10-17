package ntou.soselab.dictionary.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;

public class Word2vec {
    Logger log = LoggerFactory.getLogger(Word2vec.class);

    // windowns 10 connect python
    public void useWord2vecWithPython(String str, String str2) throws IOException, InterruptedException {
        // String pythonPath = "E://word2vec/main.py";
        ProcessBuilder pb = new ProcessBuilder("python", "E:/word2vec/main.py", "weather");
        pb.redirectErrorStream(true);
        Process p = pb.start();

        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String response = br.readLine();
        String res[] = response.replace(" ","").split(",");
        for(String str3 : res){
            String str4 = str3.replaceAll("\\p{P}","");
            log.info(str4);
        }
        log.info("response :{}", response);
//        while (true) {
//            int n = br.read(readBuffer);
//            if (n <= 0)
//                break;
//            System.out.print(new String(readBuffer, 0, n));
//        }
    }
}
