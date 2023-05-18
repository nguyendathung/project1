package com.example.mybook.Interface;

import com.example.mybook.Model.Comic;

import java.util.List;

public interface IComicLoadDone {
    void onComicLoadDoneListener(List<Comic> comicList);
}
