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

    public Resource() {}

    public Resource(String title, ArrayList<String> LDA) {
        this.title = title;
        this.LDA = LDA;
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
}