package com.example.shruti.dictionary;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private static final int REQ_CODE_ADD_WORD = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void playGameClick(View view) {
        //go to Dictionary Activity
        Intent intent = new Intent(this, DictActivity.class);
        startActivity(intent);

    }

    public void addNewWordClick(View view) {
        //go to add word activity
        Intent intent = new Intent(this, addWordActivity.class);
        intent.putExtra("initialtext","Add Word");
        startActivityForResult(intent,REQ_CODE_ADD_WORD);

        //back from add word activity



    }

    //called when add word activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQ_CODE_ADD_WORD) {
            String word = intent.getStringExtra("Newword");

            TextView text = (TextView) findViewById(R.id.newWord);
            text.setText("Added word is "+ word);

        }

    }
}
