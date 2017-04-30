package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    //private TextView txtSpeechInput;
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private EditText input4;
    private EditText input5;
    private EditText input6;
    private EditText input7;
    private EditText input8;
    private EditText input9;
    private ImageButton btnSpeak;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int numberOfLines = 0;
    private List<String> validWords = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero", "0","1","2","3","4","5","6","7","8","9","none");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        input1 = (EditText) findViewById(R.id.input1);
        input2 = (EditText) findViewById(R.id.input2);
        input3 = (EditText) findViewById(R.id.input3);
        input4 = (EditText) findViewById(R.id.input4);
        input5 = (EditText) findViewById(R.id.input5);
        input6 = (EditText) findViewById(R.id.input6);
        input7 = (EditText) findViewById(R.id.input7);
        input8 = (EditText) findViewById(R.id.input8);
        input9 = (EditText) findViewById(R.id.input9);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void promptSpeechInputInvalid() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt_invalid));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.append("\n" + result.get(0));
                    numberOfLines++;
                    boolean validate = validWords.contains(result.get(0));
                    if(!validate) {
                        numberOfLines--;
                        promptSpeechInputInvalid();
                        return;
                    }
                    if(numberOfLines==1) {
                        input1.setText(result.get(0));
                        input2.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==2) {
                        input2.setText(result.get(0));
                        input3.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==3) {
                        input3.setText(result.get(0));
                        input4.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==4) {
                        input4.setText(result.get(0));
                        input5.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==5) {
                        input5.setText(result.get(0));
                        input6.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==6) {
                        input6.setText(result.get(0));
                        input7.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==7) {
                        input7.setText(result.get(0));
                        input8.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==8) {
                        input8.setText(result.get(0));
                        input9.requestFocus();
                        promptSpeechInput();
                    }
                    if(numberOfLines==9) {
                        input9.setText(result.get(0));
                        //input7.requestFocus();
                    }

                    if(numberOfLines==9) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.sudoku_board_received),
                                Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}