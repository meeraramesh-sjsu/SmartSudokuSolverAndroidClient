package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Tokens extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tokens);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(NewUser.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.tokens);

        textView.setText(message);
    }

    public  void useAToken(View V) {
        Intent intent1 = new Intent(this,LoginUser.class);
        startActivity(intent1);
    }
}
