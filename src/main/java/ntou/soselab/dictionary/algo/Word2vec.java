package ntou.soselab.dictionary.algo;

import java.io.IOException;

public class Word2vec {

    public void useWord2vecWithPython() throws IOException {
        String pythonPath = "E://word2vec/main.py";
        String[] cmd = new String[2];
        cmd[0] = "python"; // check version of installed python: python -V
        cmd[1] = pythonPath;
        // create runtime to execute external command
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(cmd);
    }
}
