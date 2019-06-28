package com.yangming.myproject.databinding;

import android.databinding.ObservableField;

/**
 * @author yangming on 2019/4/2
 * 使用ObservableField 实现动态更新
 */
public class ObFSwordsman {
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> level = new ObservableField<>();

    public ObFSwordsman(String name, String level) {
        this.name.set(name);
        this.level.set(level);
    }

    public ObservableField<String> getName() {
        return name;
    }

    public void setName(ObservableField<String> name) {
        this.name = name;
    }

    public ObservableField<String> getLevel() {
        return level;
    }

    public void setLevel(ObservableField<String> level) {
        this.level = level;
    }
}
