package sudoku.edu.sjsu.sudokusover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void signUp(View v) {
        Intent intent = new Intent(getApplicationContext(), NewUser.class);
        startActivity(intent);
    }

    public void login(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginUser.class);
        startActivity(intent);
    }
}
