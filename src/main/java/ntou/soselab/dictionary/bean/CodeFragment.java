package ntou.soselab.dictionary.bean;

public class CodeFragment {

    private String uri;

    private String fragment;

    public CodeFragment() {

    }

    public CodeFragment(String uri, String fragment) {
        this.uri = uri;
        this.fragment = fragment;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
