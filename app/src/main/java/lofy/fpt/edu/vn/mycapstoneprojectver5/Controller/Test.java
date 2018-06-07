package lofy.fpt.edu.vn.mycapstoneprojectver5.Controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lofy.fpt.edu.vn.mycapstoneprojectver5.R;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        returnIntent();
    }
    private void returnIntent() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","String");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }
}
