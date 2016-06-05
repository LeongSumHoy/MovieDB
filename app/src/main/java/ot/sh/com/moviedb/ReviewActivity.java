package ot.sh.com.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ReviewFragment()).commit();
        }
    }
}
