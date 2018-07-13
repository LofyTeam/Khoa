package lofy.fpt.edu.vn.lofy_version106.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import lofy.fpt.edu.vn.lofy_version106.R;
import lofy.fpt.edu.vn.lofy_version106.adapter.PlacesAutoCompleteAdapter;

public class SearchAutocompleteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView autocompleteView;
    public static final String INTENT_RETURN_MAIN= "RESULT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_autocomplete);
        initView();
    }


    private void initView() {
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.tv_search_autocompletePlace);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getApplication(), R.layout.item_autocomplete_list));
        autocompleteView.setOnItemClickListener(this);

    }
    // return intent back to main
    private void returnIntent() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_RETURN_MAIN,autocompleteView.getText().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        returnIntent();
    }
}
