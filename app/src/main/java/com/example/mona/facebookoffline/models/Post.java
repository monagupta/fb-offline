package com.example.mona.facebookoffline.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by whunt on 5/7/16.
 */
@Table
public class Post extends SugarRecord {
    private Long id;

    public String text;
    public String title;

    public Post() { }
    public Post(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public Long getId() {
        return id;
    }
}
