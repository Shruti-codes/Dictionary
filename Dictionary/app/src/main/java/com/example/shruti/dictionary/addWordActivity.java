package com.example.shruti.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class addWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
    }

    public void addThisWordClick(View view) throws FileNotFoundException {
        //add given word and def to dictionary
        EditText text = (EditText) findViewById(R.id.theWord);
        String newWord = text.getText().toString();
        EditText text1 = (EditText) findViewById(R.id.newDef);
        String newDef = text1.getText().toString();

        //creating a new file for user's words
        PrintStream output = new PrintStream(openFileOutput("newwords.txt", MODE_PRIVATE | MODE_APPEND));
            output.println(newWord+"->"+newDef);
            output.close();

        TextView t = (TextView) findViewById(R.id.newWord);
        t.setText(getResources().getString(R.string.successful));

        //go back to start activity
        Intent back = new Intent();
        back.putExtra("Newword",newWord);
        setResult(RESULT_OK,back);
        finish();       //calls onDestroy()

    }
}
