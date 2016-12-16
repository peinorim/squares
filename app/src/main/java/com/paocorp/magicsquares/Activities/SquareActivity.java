package com.paocorp.magicsquares.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.paocorp.magicsquares.R;
import com.paocorp.magicsquares.models.MagicSquare;
import com.paocorp.magicsquares.models.MagicSquareSearch;
import com.paocorp.magicsquares.models.ShowAdsApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SquareActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MagicSquare magicSquareBase;
    MagicSquare squareToCheck;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    PackageInfo pInfo;
    protected InterstitialAd mInterstitialAd = new InterstitialAd(this);
    AdView adView;
    Chronometer chrono;
    EditText edt11;
    EditText edt12;
    EditText edt13;
    EditText edt21;
    EditText edt22;
    EditText edt23;
    EditText edt31;
    EditText edt32;
    EditText edt33;

    TextView h1;
    TextView h2;
    TextView h3;
    TextView v1;
    TextView v2;
    TextView v3;
    TextView d1;
    TextView d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView);

        edt11 = (EditText) findViewById(R.id.et11);
        edt12 = (EditText) findViewById(R.id.et12);
        edt13 = (EditText) findViewById(R.id.et13);
        edt21 = (EditText) findViewById(R.id.et21);
        edt22 = (EditText) findViewById(R.id.et22);
        edt23 = (EditText) findViewById(R.id.et23);
        edt31 = (EditText) findViewById(R.id.et31);
        edt32 = (EditText) findViewById(R.id.et32);
        edt33 = (EditText) findViewById(R.id.et33);

        h1 = (TextView) findViewById(R.id.SumXResult1);
        h2 = (TextView) findViewById(R.id.SumXResult2);
        h3 = (TextView) findViewById(R.id.SumXResult3);
        v1 = (TextView) findViewById(R.id.SumYResult1);
        v2 = (TextView) findViewById(R.id.SumYResult2);
        v3 = (TextView) findViewById(R.id.SumYResult3);

        d1 = (TextView) findViewById(R.id.SumDResult1);
        d2 = (TextView) findViewById(R.id.SumDResult2);

        chrono = (Chronometer) findViewById(R.id.chrono);

        magicSquareBase = (MagicSquare) getIntent().getSerializableExtra("square");
        if (magicSquareBase == null || !magicSquareBase.valid()) {
            MagicSquareSearch magicSquareSearch = new MagicSquareSearch();
            magicSquareBase = magicSquareSearch.getMagicSquare();
        }

        fillGrid();
        checkSquare(d2);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        final ShowAdsApplication hideAdObj = ((ShowAdsApplication) getApplicationContext());
        boolean hideAd = hideAdObj.getHideAd();

        if (isNetworkAvailable() && !hideAd) {
            mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.interstitial));
            requestNewInterstitial();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    showInterstitial();
                    hideAdObj.setHideAd(true);
                }
            });
        }

        adView = (AdView) findViewById(R.id.banner_bottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView txv = (TextView) findViewById(R.id.appVersion);
            String APPINFO = txv.getText() + " v" + pInfo.versionName;
            txv.setText(APPINFO);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkSquare(View v) {

        //first row
        int et11 = Integer.parseInt(nulltoIntegerDefault(edt11.getText().toString()));
        int et12 = Integer.parseInt(nulltoIntegerDefault(edt12.getText().toString()));
        int et13 = Integer.parseInt(nulltoIntegerDefault(edt13.getText().toString()));
        //second row
        int et21 = Integer.parseInt(nulltoIntegerDefault(edt21.getText().toString()));
        int et22 = Integer.parseInt(nulltoIntegerDefault(edt22.getText().toString()));
        int et23 = Integer.parseInt(nulltoIntegerDefault(edt23.getText().toString()));
        //third row
        int et31 = Integer.parseInt(nulltoIntegerDefault(edt31.getText().toString()));
        int et32 = Integer.parseInt(nulltoIntegerDefault(edt32.getText().toString()));
        int et33 = Integer.parseInt(nulltoIntegerDefault(edt33.getText().toString()));

        int[][] arrayInput = new int[3][3];
        arrayInput[0][0] = et11;
        arrayInput[0][1] = et12;
        arrayInput[0][2] = et13;
        arrayInput[1][0] = et21;
        arrayInput[1][1] = et22;
        arrayInput[1][2] = et23;
        arrayInput[2][0] = et31;
        arrayInput[2][1] = et32;
        arrayInput[2][2] = et33;

        squareToCheck = new MagicSquare(arrayInput);

        if (magicSquareBase.compareSquares(squareToCheck)) {
            chrono.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
            Intent intent = new Intent(this, EndActivity.class);
            intent.putExtra("square", magicSquareBase);
            intent.putExtra("time", elapsedMillis);
            intent.putExtra("solved", true);
            startActivity(intent);
            finish();
        }

        h1.setText(String.valueOf(squareToCheck.getRowSum()[0]));
        checkColor(h1, squareToCheck.getRowSum()[0]);

        h2.setText(String.valueOf(squareToCheck.getRowSum()[1]));
        checkColor(h2, squareToCheck.getRowSum()[1]);

        h3.setText(String.valueOf(squareToCheck.getRowSum()[2]));
        checkColor(h3, squareToCheck.getRowSum()[2]);

        v1.setText(String.valueOf(squareToCheck.getColSum()[0]));
        checkColor(v1, squareToCheck.getColSum()[0]);

        v2.setText(String.valueOf(squareToCheck.getColSum()[1]));
        checkColor(v2, squareToCheck.getColSum()[1]);

        v3.setText(String.valueOf(squareToCheck.getColSum()[2]));
        checkColor(v3, squareToCheck.getColSum()[2]);

        d1.setText(String.valueOf(squareToCheck.getDiagonalFirst()));
        checkColor(d1, squareToCheck.getDiagonalFirst());

        d2.setText(String.valueOf(squareToCheck.getDiagonalSecond()));
        checkColor(d2, squareToCheck.getDiagonalSecond());
    }

    public void resolveSquare() {
        chrono.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("square", magicSquareBase);
        intent.putExtra("time", elapsedMillis);
        intent.putExtra("solved", false);
        startActivity(intent);
        finish();
    }

    String nulltoIntegerDefault(String value) {
        if (!isIntValue(value)) {
            value = "0";
        }
        return value;
    }

    boolean isIntValue(String val) {
        try {
            val = val.replace(" ", "");
            Integer.parseInt(val);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void checkColor(TextView tv, int check) {
        if (check == magicSquareBase.getMagicConstant()) {
            tv.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green_darken3));
        } else {
            tv.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.red_darken3));
        }
    }

    public void fillGrid() {
        MagicSquare magicSquareInput = magicSquareBase.copy();
        int[][] squareInput = magicSquareInput.getSquare();
        List<Integer> randList = new ArrayList<Integer>();
        while (randList.size() < 6) {
            int randRow = randInt(0, 2);
            int randCol = randInt(0, 2);
            if (squareInput[randRow][randCol] != 0) {
                squareInput[randRow][randCol] = 0;
                randList.add(0);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String editTextId = "et" + (i + 1) + (j + 1);
                int resID = getResources().getIdentifier(editTextId, "id", getPackageName());
                EditText input = (EditText) findViewById(resID);
                if (input != null) {
                    if (squareInput[i][j] != 0) {
                        input.setText(String.valueOf(squareInput[i][j]));
                        input.setEnabled(false);
                    } else {
                        input.setText("");
                        input.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.teal_darken3));
                    }
                }
            }
        }
        TextView cst = (TextView) findViewById(R.id.magicCst);
        cst.setText(getResources().getString(R.string.magic_cst, magicSquareBase.getMagicConstant()));
        edt11.addTextChangedListener(new GenericTextWatcher(edt11));
        edt12.addTextChangedListener(new GenericTextWatcher(edt12));
        edt13.addTextChangedListener(new GenericTextWatcher(edt13));
        edt21.addTextChangedListener(new GenericTextWatcher(edt21));
        edt22.addTextChangedListener(new GenericTextWatcher(edt22));
        edt23.addTextChangedListener(new GenericTextWatcher(edt23));
        edt31.addTextChangedListener(new GenericTextWatcher(edt31));
        edt32.addTextChangedListener(new GenericTextWatcher(edt32));
        edt33.addTextChangedListener(new GenericTextWatcher(edt33));

        chrono.setVisibility(View.VISIBLE);
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    public static int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    public void createHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.help_dialog, null))
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void createResolveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setCancelable(true);

        builder.setView(inflater.inflate(R.layout.resolve_dialog, null))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resolveSquare();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            createHelpDialog();
            return true;
        } else if (id == R.id.action_resolve) {
            createResolveDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, SquareActivity.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.new_game) {

        } else if (id == R.id.action_help2) {
            drawer.closeDrawer(GravityCompat.START);
            createHelpDialog();
            return true;
        } else if (id == R.id.action_resolve2) {
            drawer.closeDrawer(GravityCompat.START);
            createResolveDialog();
            return true;
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

        drawer.closeDrawer(GravityCompat.START);
        finish();
        startActivity(intent);
        return true;
    }

    public void createResolveDialog(View view) {
        createResolveDialog();
    }

    public void createHelpDialog(View view) {
        createHelpDialog();
    }

    //Declaration
    public class GenericTextWatcher implements TextWatcher {

        private View view;

        public GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            int input = Integer.parseInt(nulltoIntegerDefault(editable.toString()));
            if (input != 0) {
                checkSquare(view);
            }
        }
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    protected void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
