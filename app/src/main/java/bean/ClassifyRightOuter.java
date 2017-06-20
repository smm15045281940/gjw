package bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ClassifyRightOuter {

    private String id;
    private String name;
    private List<ClassifyRightInner> list;

    public ClassifyRightOuter() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassifyRightInner> getList() {
        return list;
    }

    public void setList(List<ClassifyRightInner> list) {
        this.list = list;
    }
}
