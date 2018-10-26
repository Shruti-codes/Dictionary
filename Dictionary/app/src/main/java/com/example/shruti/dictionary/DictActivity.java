package com.example.shruti.dictionary;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class DictActivity extends AppCompatActivity {

    private Map<String, String> dictionary;
    private List<String> words; //like dictionary but is indexed so easy to pick up in random
    private int points =0;
    private int high_score;
    private MediaPlayer mp;

    private static final String[] OPTIONS = {
            "definiton", "synonyms" , "antonyms"
    };

    private void readFile()
    {
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.words));
        readFileHelper(scan);

        try {
          Scanner scan2 = new Scanner(openFileInput("newwords.txt"));
            readFileHelper(scan2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFileHelper(Scanner scan) throws NullPointerException {
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            String parts[] = line.split("->");
            if(parts.length<2) continue;
            dictionary.put(parts[0],parts[1]);
            words.add(parts[0]);
        }
        scan.close();
    }

    //pick a word, show 4 definitions of which 1 is correct, choose it
    private void chooseWord(){
        Random rand = new Random();
        int randI = rand.nextInt(words.size());
        String theWord = words.get(randI);
        String theDef = dictionary.get(theWord);

        List<String> defs = new ArrayList<String>(dictionary.values());
        defs.remove(theDef);
        Collections.shuffle(defs); //to shuffle a list
        defs = defs.subList(0,3);
        defs.add(theDef);
        Collections.shuffle(defs);

        TextView text=findViewById(R.id.theWord);
        text.setText(theWord);

        ListView list = (ListView) findViewById(R.id.word_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, //activity
                android.R.layout.simple_list_item_1, //layout
                defs //array
        );
        list.setAdapter(adapter);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //always call super
        setContentView(R.layout.activity_dict); //set the layout

        dictionary = new HashMap<>();
        words = new ArrayList<>();
        readFile();
        chooseWord();

        Spinner spin = (Spinner)findViewById(R.id.spin_list);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String word = parent.getItemAtPosition(position).toString();
                }
                @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        ListView list = (ListView) findViewById(R.id.word_list);
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, //activity
                android.R.layout.simple_list_item_1, //layout
                new ArrayList<String>(dictionary.keySet()) //array
        );
        list.setAdapter(adapter); */

       list.setOnItemClickListener((parent, view, position, id) -> {
                String defClicked = parent.getItemAtPosition(position).toString();
                //text.setText(dictionary.get(word));
                TextView text1 = (TextView) findViewById(R.id.theWord);
                String theWord =  text1.getText().toString();
                String correctDef = dictionary.get(theWord);
           TextView text = (TextView) findViewById(R.id.mytextView);
               if (defClicked.equals(correctDef)) {
                   points++;
                   if(points > high_score) {
                       high_score=points;

                       SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
                       SharedPreferences.Editor prefsEditor = prefs.edit();
                       prefsEditor.putInt("high_score",high_score);
                       prefsEditor.apply(); //or commit()

                   }
                   text.setText(getResources().getString(R.string.correct)+" "+points+", High Score = "+high_score);

               }
               else {
               points--;
               text.setText(getResources().getString(R.string.incorrect)+" "+points+", High Score = "+high_score);

                }
           chooseWord();
            });

       //add music to a activity
       mp = MediaPlayer.create(this, R.raw.sound);
       mp.start();

       //to put an app's description etc file in internal storage of phone
        File outDir = getExternalFilesDir(null);
        File outFile = new File(outDir,"words.txt");
        PrintStream output = null;
        try {
            output = new PrintStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        output.print("this is a dictionary");
        output.close();

        SharedPreferences prefs = getSharedPreferences("myprefs",MODE_PRIVATE);
        high_score=prefs.getInt("high_score",0);

    }

    @Override
    protected void onPause() {
        super.onPause();        //just to print the log messages when this func is called
        mp.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView text1 = (TextView) findViewById(R.id.theWord);
        String theWord =  text1.getText().toString();
        outState.putInt("points",points);
        outState.putString("theWord", theWord);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        points=savedInstanceState.getInt("points",0);
    }

    //if external storage available for writing
    public boolean isExternalStorageWritable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());

    }

    public boolean isExternalStorageReadable(){
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());

    }

    public void addWordClick(View view) {
        //go to add word activity

        Intent intent = new Intent(this, addWordActivity.class);
        startActivity(intent);
    }
}
