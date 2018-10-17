package ntou.soselab.dictionary.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;

public class Word2vec {
    Logger log = LoggerFactory.getLogger(Word2vec.class);

    public void useWord2vecWithPython(String str, String str2) throws IOException, InterruptedException {
        // String pythonPath = "E://word2vec/main.py";
        ProcessBuilder pb = new ProcessBuilder("python", "E:/word2vec/main.py", "weather");
        pb.redirectErrorStream(true);
        Process p = pb.start();

        char[] readBuffer = new char[1000];
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        while (true) {
            int n = br.read(readBuffer);
            if (n <= 0)
                break;
            System.out.print(new String(readBuffer, 0, n));
        }
    }
}
