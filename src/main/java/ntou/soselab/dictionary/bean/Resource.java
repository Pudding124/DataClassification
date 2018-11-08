package ntou.soselab.dictionary.bean;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;

@NodeEntity
public class Resource {

    @GraphId
    private Long id;

    private String title;

    private ArrayList<String> LDA;

    private ArrayList<String> WordNet;

    public Resource() {}

    public Resource(String title, ArrayList<String> LDA, ArrayList<String> WordNet) {
        this.title = title;
        this.LDA = LDA;
        this.WordNet = WordNet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getLDA() {
        return LDA;
    }

    public void setLDA(ArrayList<String> LDA) {
        this.LDA = LDA;
    }

    public ArrayList<String> getWordNet() {
        return WordNet;
    }

    public void setWordNet(ArrayList<String> wordNet) {
        WordNet = wordNet;
    }
}