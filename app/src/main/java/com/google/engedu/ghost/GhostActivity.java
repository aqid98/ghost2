/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.DuplicateFormatFlagsException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView GameStatus,GhostText,label;
    private Button Challenge, Restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        GameStatus = (TextView) findViewById(R.id.gameStatus);
        GhostText = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        Challenge = (Button) findViewById(R.id.Challenge);
        Restart = (Button) findViewById(R.id.Restart);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        try {
            dictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Cannot Load Dictionary ", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
        Challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenge();
            }
        });
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });
    }
        private void challenge()
        {
            String word = GhostText.getText().toString();
            if(!word.equals("")) {
                if (word.length() >= 4 && dictionary.isWord(word)) {
                    GameStatus.setText("Congratulations, You Win! " + word + " is a real word");
                    return;
                }
                String longWord = dictionary.getGoodWordStartingWith(word);
                if (longWord != null) {
                    GameStatus.setText("You Lose! A possible word is " + longWord);
                } else {
                    GameStatus.setText("Congratulations! You Win");
                }

                Challenge.setEnabled(false);

            }
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        Challenge.setEnabled(true);
        TextView text =(TextView)findViewById(R.id.ghostText);
        text.setText("");

        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {

        GameStatus.setText(COMPUTER_TURN);
        String word = GhostText.getText().toString();
        String next;
        if (dictionary.isWord(word) && word.length() >= 4) {
            Log.d("Computer Tag", "Computerturn:true");
            GhostText.setText(word);
            GameStatus.setText(word);
            label.setText("Computer Won");
        } else {
            next = dictionary.getAnyWordStartingWith(word);
            if(next!=null)
            {
                String letter=next.substring(word.length(),word.length()+1);
                word=word+letter;
                GhostText.setText(word);
                userTurn=true;
             label.setText(USER_TURN);

            }
            else{

                label.setText("Computer Won");
                Challenge.setEnabled(false);
            }
        }

    }
    private void addTextToGame(char c){
       GhostText.setText(GhostText.getText().toString() + c);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char c = (char) event.getUnicodeChar();


        Log.d("onKeyUp", "char displayed is " + c);
        if (Character.isLetter(c)) {
            addTextToGame(c);
            label.setText(COMPUTER_TURN);
            userTurn=false;
            computerTurn();
            }
            else (Toast.makeText(this, "Entered an invalid Char", Toast.LENGTH_LONG)).show();

        return super.onKeyUp(keyCode, event);

    }



}
