package com.yangming.myproject.databinding;

/**
 * @author yangming on 2019/4/1
 */
public class Swordsman {
    private String name;
    private String level;

    public Swordsman(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level == null ? "" : level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
