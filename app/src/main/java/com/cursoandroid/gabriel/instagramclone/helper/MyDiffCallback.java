package com.cursoandroid.gabriel.instagramclone.helper;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.cursoandroid.gabriel.instagramclone.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MyDiffCallback extends DiffUtil.Callback {

    private List<Post> oldList = new ArrayList<>();
    private List<Post> newList = new ArrayList<>();

    public MyDiffCallback(List<Post> oldList, List<Post> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return newList;
    }
}
