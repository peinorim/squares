package com.paocorp.magicsquares.Activities;

import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paocorp.magicsquares.R;
import com.paocorp.magicsquares.models.MagicSquare;
import com.paocorp.magicsquares.models.MagicSquareSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SquareActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MagicSquare magicSquareBase;
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

        Bundle b = getIntent().getExtras();
        int order = b.getInt("order");
        if (order == 0) {
            order = 3;
        }
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

        MagicSquareSearch magicSquareSearch = new MagicSquareSearch(order, 1);
        magicSquareBase = magicSquareSearch.getMagicSquare();

        fillGrid(magicSquareBase.getSquare());
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

        MagicSquare squareToCheck = new MagicSquare(arrayInput);
        Toast toast;
        if (magicSquareBase.compareSquares(squareToCheck)) {
            //square solved !
            toast = Toast.makeText(getBaseContext(), "solved !", Toast.LENGTH_SHORT);
        } else {
            //remains errors
            toast = Toast.makeText(getBaseContext(), "errors remain...", Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        h1.setText(String.valueOf(squareToCheck.getRowSum()[0]));
        h2.setText(String.valueOf(squareToCheck.getRowSum()[1]));
        h3.setText(String.valueOf(squareToCheck.getRowSum()[2]));
        v1.setText(String.valueOf(squareToCheck.getColSum()[0]));
        v2.setText(String.valueOf(squareToCheck.getColSum()[1]));
        v3.setText(String.valueOf(squareToCheck.getColSum()[2]));
        d1.setText(String.valueOf(squareToCheck.getDiagonalFirst()));
        d2.setText(String.valueOf(squareToCheck.getDiagonalSecond()));
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

    public void fillGrid(int[][] square) {
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
                    } else {
                        input.setText("");
                        input.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.teal_darken3));
                    }
                }
            }
        }
        edt11.addTextChangedListener(new GenericTextWatcher(edt11));
        edt12.addTextChangedListener(new GenericTextWatcher(edt12));
        edt13.addTextChangedListener(new GenericTextWatcher(edt13));
        edt21.addTextChangedListener(new GenericTextWatcher(edt21));
        edt22.addTextChangedListener(new GenericTextWatcher(edt22));
        edt23.addTextChangedListener(new GenericTextWatcher(edt23));
        edt31.addTextChangedListener(new GenericTextWatcher(edt31));
        edt32.addTextChangedListener(new GenericTextWatcher(edt32));
        edt33.addTextChangedListener(new GenericTextWatcher(edt33));
    }

    public static int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
