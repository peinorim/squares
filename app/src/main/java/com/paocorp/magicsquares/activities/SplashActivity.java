package com.paocorp.magicsquares.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.paocorp.magicsquares.R;
import com.paocorp.magicsquares.models.MagicSquare;
import com.paocorp.magicsquares.models.MagicSquareSearch;

public class SplashActivity extends AppCompatActivity {

    private MagicSquare square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                new PrefetchData().execute();
            }
        }, SPLASH_TIME_OUT);

    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            MagicSquareSearch magicSquareSearch = new MagicSquareSearch();
            square = magicSquareSearch.getMagicSquare();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and launch main activity
            Intent intent = new Intent(SplashActivity.this, SquareActivity.class);
            intent.putExtra("square", square);

            startActivity(intent);
            // close this activity
            finish();
        }

    }
}
