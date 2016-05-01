package com.example.mona.facebookoffline;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
