package com.example.mona.facebookoffline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mona.facebookoffline.models.Post;

import java.util.List;

/**
 * Created by whunt on 5/17/16.
 */
public class ChoosePostActivity extends Activity {

    private PostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_post);

        ListView listView = (ListView) findViewById(R.id.choose_post_list);
        List<Post> posts = Post.listAll(Post.class);

        mAdapter = new PostAdapter(this, R.layout.choose_post_item, posts);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(getClickListener());
    }

    private AdapterView.OnItemClickListener getClickListener() {
        return new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Launch EditActivity with the id of the post
                Long postId = mAdapter.getItem(position).getId();
                Intent intent = new Intent(ChoosePostActivity.this, EditActivity.class);
                intent.putExtra("id", postId);
                startActivity(intent);
            }
        };
    }

    private class PostAdapter extends ArrayAdapter<Post> {

        private List<Post> mPosts;

        public PostAdapter(Context context, int resource, List<Post> posts) {
            super(context, resource, posts);
            mPosts = posts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.choose_post_item, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(mPosts.get(position).title);

            return convertView;
        }
    }
}
