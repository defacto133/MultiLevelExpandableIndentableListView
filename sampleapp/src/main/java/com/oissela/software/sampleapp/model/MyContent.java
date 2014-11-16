package com.oissela.software.sampleapp.model;

import com.oissela.software.multilevelexpindlistview.MultiLevelExpIndListAdapter;

import java.util.List;

public class MyContent implements MultiLevelExpIndListAdapter.ExpIndData {
    public String content;

    public MyContent(String content) {
        this.content = content;
    }

    @Override
    public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
        return null;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean value) {
    }

    @Override
    public void setGroupSize(int groupSize) {

    }
}
