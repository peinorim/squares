package com.paocorp.magicsquares.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.paocorp.magicsquares.R;
import com.paocorp.magicsquares.models.MagicSquare;

import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ShareDialog shareDialog;
    CallbackManager callbackManager;
    MagicSquare magicSquareBase;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        magicSquareBase = (MagicSquare) getIntent().getSerializableExtra("square");
        long duration = getIntent().getLongExtra("time", 0);
        boolean solved = getIntent().getBooleanExtra("solved", false);

        if (solved && duration > 0) {
            String time = getDurationBreakdown(duration);
            TextView endTitle2 = (TextView) findViewById(R.id.endTitle2);
            endTitle2.setText(getResources().getString(R.string.end_title2, time));
        } else {
            TextView endTitle = (TextView) findViewById(R.id.endTitle);
            endTitle.setText(getResources().getString(R.string.end_title_failed));

            TextView endTitle2 = (TextView) findViewById(R.id.endTitle2);
            endTitle2.setText(getResources().getString(R.string.end_title2_failed));

            Button fbShare = (Button) findViewById(R.id.fbshare);
            fbShare.setVisibility(View.GONE);
        }

        this.fillSquareGrid();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    private void fillSquareGrid() {
        if (magicSquareBase != null) {
            int[][] squareInput = magicSquareBase.getSquare();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    String editTextId = "tv" + (i + 1) + (j + 1);
                    int resID = getResources().getIdentifier(editTextId, "id", getPackageName());
                    TextView input = (TextView) findViewById(resID);
                    if (input != null) {
                        if (squareInput[i][j] != 0) {
                            input.setText(String.valueOf(squareInput[i][j]));
                        }
                    }
                }
            }
        }
    }

    private String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(getResources().getString(R.string.days, days));
        }
        if (hours > 0) {
            sb.append(getResources().getString(R.string.hours, hours));
        }
        if (minutes > 0) {
            sb.append(getResources().getString(R.string.minutes, minutes));
        }
        if (seconds > 0) {
            sb.append(getResources().getString(R.string.seconds, seconds));
        }

        return (sb.toString());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, SquareActivity.class);

        if (id == R.id.new_game) {
            Bundle b = new Bundle();
            b.putBoolean("showAd", true);
            intent.putExtras(b);

        } else if (id == R.id.nav_share) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                String fbText = getResources().getString(R.string.fb_ContentDesc);
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(getResources().getString(R.string.store_url)))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentDescription(fbText)
                        .setImageUrl(Uri.parse(getResources().getString(R.string.app_icon_url)))
                        .build();

                shareDialog.show(linkContent);
            }
        } else if (id == R.id.nav_rate) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.store_url)));
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        finish();
        return true;
    }

    public void shareFB(View v) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String fbText = getResources().getString(R.string.fb_solved);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(getResources().getString(R.string.store_url)))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentDescription(fbText)
                    .setImageUrl(Uri.parse(getResources().getString(R.string.app_icon_url)))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    public void restartGame(View v) {
        Intent intent = new Intent(this, SquareActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("showAd", true);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, SquareActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("showAd", true);
        intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }
}
