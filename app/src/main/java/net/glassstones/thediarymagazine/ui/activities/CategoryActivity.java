package net.glassstones.thediarymagazine.ui.activities;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import net.glassstones.thediarymagazine.R;

public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        int category = getIntent().getIntExtra("cat",-1);
        Log.e("TAG", String.valueOf(category));
    }

}
