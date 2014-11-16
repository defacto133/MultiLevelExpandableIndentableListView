package com.oissela.software.sampleapp.model;

import com.oissela.software.multilevelexpindlistview.MultiLevelExpIndListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyComment implements MultiLevelExpIndListAdapter.ExpIndData {
    private int mIndentation;
    private List<MyComment> mChildren;
    private boolean mIsGroup;
    private int mGroupSize;

    public String author;
    public String comment;

    public MyComment(String author, String comment) {
        this.author = author;
        this.comment = comment;
        mChildren = new ArrayList<MyComment>();

        setIndentation(0);
    }

    @Override
    public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
        return mChildren;
    }

    @Override
    public boolean isGroup() {
        return mIsGroup;
    }

    @Override
    public void setIsGroup(boolean value) {
        mIsGroup = value;
    }

    @Override
    public void setGroupSize(int groupSize) {
        mGroupSize = groupSize;
    }

    public int getGroupSize() {
        return mGroupSize;
    }

    public void addChild(MyComment child) {
        mChildren.add(child);
        child.setIndentation(getIndentation() + 1);
    }

    public int getIndentation() {
        return mIndentation;
    }

    private void setIndentation(int indentation) {
        mIndentation = indentation;
    }
}