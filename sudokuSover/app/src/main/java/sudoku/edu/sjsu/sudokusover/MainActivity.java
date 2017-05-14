package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {


    //private TextView txtSpeechInput;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int numberOfLines = 0;
    private List<String> validWords = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine","1", "2", "3", "4", "5", "6", "7", "8", "9");
    private TextToSpeech ttobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextToSpeech tts = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
        tts.setLanguage(Locale.US);
        tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);

        findViews();
        //txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        ttobj = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    ttobj.setLanguage(Locale.US);
                }
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
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

    public void setText(String input, EditText inputEdit) {
        HashMap<String,String> map = new HashMap<>();
        map.put("one","1");
        map.put("two","2");
        map.put("three","3");
        map.put("four","4");
        map.put("five","5");
        map.put("six","6");
        map.put("seven","7");
        map.put("eight","8");
        map.put("nine","9");
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        map.put("4","4");
        map.put("5","5");
        map.put("6","6");
        map.put("7","7");
        map.put("8","8");
        map.put("9","9");
        inputEdit.setText(map.get(input));
    }

    /**
     * Receiving speech input
     */
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
                    if (!validate) {
                        numberOfLines--;
                        promptSpeechInputInvalid();
                        return;
                    }
                    if (numberOfLines == 1) {
                        setText(result.get(0), input13);
                        input21.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 2) {
                        setText(result.get(0), input21);
                        input36.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 3) {
                        setText(result.get(0), input36);
                        input47.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 4) {
                        setText(result.get(0), input47);
                        input74.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 5) {
                        setText(result.get(0), input74);
                        input89.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 6) {
                        setText(result.get(0), input89);
                        input94.requestFocus();
                        promptSpeechInput();
                    }
                    if (numberOfLines == 7) {
                        setText(result.get(0), input94);
                    }

                    if (numberOfLines == 7) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.sudoku_board_received),
                                Toast.LENGTH_SHORT).show();
                        fillSudokuBoardAndApplyAlgorithm();
                        break;

                    }
                }
                break;
            }

        }
    }

    private void fillSudokuBoardAndApplyAlgorithm() {
        char[][] input = new char[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                input[i][j] = '.';
        input[0][2] = input13.getText().charAt(0);
        input[7][8] = input89.getText().charAt(0);
        input[1][0] = input21.getText().charAt(0);
        input[2][5] = input36.getText().charAt(0);
        input[3][6] = input47.getText().charAt(0);
        input[6][3] = input74.getText().charAt(0);
        input[8][3] = input94.getText().charAt(0);
        SudokuSolver ss = new SudokuSolver();
        ss.solveSudoku(input);

        String toSpeak = "Sudoku algorithm is applied on the input board";
        ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        ttobj.speak(Character.toString(input[0][1]), TextToSpeech.QUEUE_ADD, null);
        input11.setText(Character.toString(input[0][0]));
        input12.setText(Character.toString(input[0][1]));
        input13.setText(Character.toString(input[0][2]));
        input14.setText(Character.toString(input[0][3]));
        input15.setText(Character.toString(input[0][4]));
        input16.setText(Character.toString(input[0][5]));
        input17.setText(Character.toString(input[0][6]));
        input18.setText(Character.toString(input[0][7]));
        input19.setText(Character.toString(input[0][8]));
        input21.setText(Character.toString(input[1][0]));
        input22.setText(Character.toString(input[1][1]));
        input23.setText(Character.toString(input[1][2]));
        input24.setText(Character.toString(input[1][3]));
        input25.setText(Character.toString(input[1][4]));
        input26.setText(Character.toString(input[1][5]));
        input27.setText(Character.toString(input[1][6]));
        input28.setText(Character.toString(input[1][7]));
        input29.setText(Character.toString(input[1][8]));
        input31.setText(Character.toString(input[2][0]));
        input32.setText(Character.toString(input[2][1]));
        input33.setText(Character.toString(input[2][2]));
        input34.setText(Character.toString(input[2][3]));
        input35.setText(Character.toString(input[2][4]));
        input36.setText(Character.toString(input[2][5]));
        input37.setText(Character.toString(input[2][6]));
        input38.setText(Character.toString(input[2][7]));
        input39.setText(Character.toString(input[2][8]));
        input41.setText(Character.toString(input[3][0]));
        input42.setText(Character.toString(input[3][1]));
        input43.setText(Character.toString(input[3][2]));
        input44.setText(Character.toString(input[3][3]));
        input45.setText(Character.toString(input[3][4]));
        input46.setText(Character.toString(input[3][5]));
        input47.setText(Character.toString(input[3][6]));
        input48.setText(Character.toString(input[3][7]));
        input49.setText(Character.toString(input[3][8]));
        input51.setText(Character.toString(input[4][0]));
        input52.setText(Character.toString(input[4][1]));
        input53.setText(Character.toString(input[4][2]));
        input54.setText(Character.toString(input[4][3]));
        input55.setText(Character.toString(input[4][4]));
        input56.setText(Character.toString(input[4][5]));
        input57.setText(Character.toString(input[4][6]));
        input58.setText(Character.toString(input[4][7]));
        input59.setText(Character.toString(input[4][8]));
        input61.setText(Character.toString(input[5][0]));
        input62.setText(Character.toString(input[5][1]));
        input63.setText(Character.toString(input[5][2]));
        input64.setText(Character.toString(input[5][3]));
        input65.setText(Character.toString(input[5][4]));
        input66.setText(Character.toString(input[5][5]));
        input67.setText(Character.toString(input[5][6]));
        input68.setText(Character.toString(input[5][7]));
        input69.setText(Character.toString(input[5][8]));
        input71.setText(Character.toString(input[6][0]));
        input72.setText(Character.toString(input[6][1]));
        input73.setText(Character.toString(input[6][2]));
        input74.setText(Character.toString(input[6][3]));
        input75.setText(Character.toString(input[6][4]));
        input76.setText(Character.toString(input[6][5]));
        input77.setText(Character.toString(input[6][6]));
        input78.setText(Character.toString(input[6][7]));
        input79.setText(Character.toString(input[6][8]));
        input81.setText(Character.toString(input[7][0]));
        input82.setText(Character.toString(input[7][1]));
        input83.setText(Character.toString(input[7][2]));
        input84.setText(Character.toString(input[7][3]));
        input85.setText(Character.toString(input[7][4]));
        input86.setText(Character.toString(input[7][5]));
        input87.setText(Character.toString(input[7][6]));
        input88.setText(Character.toString(input[7][7]));
        input89.setText(Character.toString(input[7][8]));
        input91.setText(Character.toString(input[8][0]));
        input92.setText(Character.toString(input[8][1]));
        input93.setText(Character.toString(input[8][2]));
        input94.setText(Character.toString(input[8][3]));
        input95.setText(Character.toString(input[8][4]));
        input96.setText(Character.toString(input[8][5]));
        input97.setText(Character.toString(input[8][6]));
        input98.setText(Character.toString(input[8][7]));
        input99.setText(Character.toString(input[8][8]));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onPause() {
        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }

    private EditText input11;
    private EditText input12;
    private EditText input13;
    private EditText input14;
    private EditText input15;
    private EditText input16;
    private EditText input17;
    private EditText input18;
    private EditText input19;
    private EditText input21;
    private EditText input22;
    private EditText input23;
    private EditText input24;
    private EditText input25;
    private EditText input26;
    private EditText input27;
    private EditText input28;
    private EditText input29;
    private EditText input31;
    private EditText input32;
    private EditText input33;
    private EditText input34;
    private EditText input35;
    private EditText input36;
    private EditText input37;
    private EditText input38;
    private EditText input39;
    private EditText input41;
    private EditText input42;
    private EditText input43;
    private EditText input44;
    private EditText input45;
    private EditText input46;
    private EditText input47;
    private EditText input48;
    private EditText input49;
    private EditText input51;
    private EditText input52;
    private EditText input53;
    private EditText input54;
    private EditText input55;
    private EditText input56;
    private EditText input57;
    private EditText input58;
    private EditText input59;
    private EditText input61;
    private EditText input62;
    private EditText input63;
    private EditText input64;
    private EditText input65;
    private EditText input66;
    private EditText input67;
    private EditText input68;
    private EditText input69;
    private EditText input71;
    private EditText input72;
    private EditText input73;
    private EditText input74;
    private EditText input75;
    private EditText input76;
    private EditText input77;
    private EditText input78;
    private EditText input79;
    private EditText input81;
    private EditText input82;
    private EditText input83;
    private EditText input84;
    private EditText input85;
    private EditText input86;
    private EditText input87;
    private EditText input88;
    private EditText input89;
    private EditText input91;
    private EditText input92;
    private EditText input93;
    private EditText input94;
    private EditText input95;
    private EditText input96;
    private EditText input97;
    private EditText input98;
    private EditText input99;
    private Button btnSpeak;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-13 20:14:14 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        input11 = (EditText) findViewById(R.id.input11);
        input12 = (EditText) findViewById(R.id.input12);
        input13 = (EditText) findViewById(R.id.input13);
        input14 = (EditText) findViewById(R.id.input14);
        input15 = (EditText) findViewById(R.id.input15);
        input16 = (EditText) findViewById(R.id.input16);
        input17 = (EditText) findViewById(R.id.input17);
        input18 = (EditText) findViewById(R.id.input18);
        input19 = (EditText) findViewById(R.id.input19);
        input21 = (EditText) findViewById(R.id.input21);
        input22 = (EditText) findViewById(R.id.input22);
        input23 = (EditText) findViewById(R.id.input23);
        input24 = (EditText) findViewById(R.id.input24);
        input25 = (EditText) findViewById(R.id.input25);
        input26 = (EditText) findViewById(R.id.input26);
        input27 = (EditText) findViewById(R.id.input27);
        input28 = (EditText) findViewById(R.id.input28);
        input29 = (EditText) findViewById(R.id.input29);
        input31 = (EditText) findViewById(R.id.input31);
        input32 = (EditText) findViewById(R.id.input32);
        input33 = (EditText) findViewById(R.id.input33);
        input34 = (EditText) findViewById(R.id.input34);
        input35 = (EditText) findViewById(R.id.input35);
        input36 = (EditText) findViewById(R.id.input36);
        input37 = (EditText) findViewById(R.id.input37);
        input38 = (EditText) findViewById(R.id.input38);
        input39 = (EditText) findViewById(R.id.input39);
        input41 = (EditText) findViewById(R.id.input41);
        input42 = (EditText) findViewById(R.id.input42);
        input43 = (EditText) findViewById(R.id.input43);
        input44 = (EditText) findViewById(R.id.input44);
        input45 = (EditText) findViewById(R.id.input45);
        input46 = (EditText) findViewById(R.id.input46);
        input47 = (EditText) findViewById(R.id.input47);
        input48 = (EditText) findViewById(R.id.input48);
        input49 = (EditText) findViewById(R.id.input49);
        input51 = (EditText) findViewById(R.id.input51);
        input52 = (EditText) findViewById(R.id.input52);
        input53 = (EditText) findViewById(R.id.input53);
        input54 = (EditText) findViewById(R.id.input54);
        input55 = (EditText) findViewById(R.id.input55);
        input56 = (EditText) findViewById(R.id.input56);
        input57 = (EditText) findViewById(R.id.input57);
        input58 = (EditText) findViewById(R.id.input58);
        input59 = (EditText) findViewById(R.id.input59);
        input61 = (EditText) findViewById(R.id.input61);
        input62 = (EditText) findViewById(R.id.input62);
        input63 = (EditText) findViewById(R.id.input63);
        input64 = (EditText) findViewById(R.id.input64);
        input65 = (EditText) findViewById(R.id.input65);
        input66 = (EditText) findViewById(R.id.input66);
        input67 = (EditText) findViewById(R.id.input67);
        input68 = (EditText) findViewById(R.id.input68);
        input69 = (EditText) findViewById(R.id.input69);
        input71 = (EditText) findViewById(R.id.input71);
        input72 = (EditText) findViewById(R.id.input72);
        input73 = (EditText) findViewById(R.id.input73);
        input74 = (EditText) findViewById(R.id.input74);
        input75 = (EditText) findViewById(R.id.input75);
        input76 = (EditText) findViewById(R.id.input76);
        input77 = (EditText) findViewById(R.id.input77);
        input78 = (EditText) findViewById(R.id.input78);
        input79 = (EditText) findViewById(R.id.input79);
        input81 = (EditText) findViewById(R.id.input81);
        input82 = (EditText) findViewById(R.id.input82);
        input83 = (EditText) findViewById(R.id.input83);
        input84 = (EditText) findViewById(R.id.input84);
        input85 = (EditText) findViewById(R.id.input85);
        input86 = (EditText) findViewById(R.id.input86);
        input87 = (EditText) findViewById(R.id.input87);
        input88 = (EditText) findViewById(R.id.input88);
        input89 = (EditText) findViewById(R.id.input89);
        input91 = (EditText) findViewById(R.id.input91);
        input92 = (EditText) findViewById(R.id.input92);
        input93 = (EditText) findViewById(R.id.input93);
        input94 = (EditText) findViewById(R.id.input94);
        input95 = (EditText) findViewById(R.id.input95);
        input96 = (EditText) findViewById(R.id.input96);
        input97 = (EditText) findViewById(R.id.input97);
        input98 = (EditText) findViewById(R.id.input98);
        input99 = (EditText) findViewById(R.id.input99);

    }
}