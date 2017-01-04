package com.paocorp.magicsquares.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.paocorp.magicsquares.R;
import com.paocorp.magicsquares.models.Global;
import com.paocorp.magicsquares.util.IabHelper;
import com.paocorp.magicsquares.util.IabResult;
import com.paocorp.magicsquares.util.Inventory;
import com.paocorp.magicsquares.util.Purchase;

import java.util.ArrayList;
import java.util.List;


public class BillingActivity extends AppCompatActivity {

    IabHelper mHelper;
    boolean mIsRemoveAds = false;
    String SKU_NOAD = Global.SKU_NOAD;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        parentLayout = findViewById(android.R.id.content);

        String base64EncodedPublicKey = this.getResources().getString(R.string.billingKey);
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh no, there was a problem.
                    Snackbar.make(parentLayout, R.string.purchaseError, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (result.isSuccess()) {
                    try {
                        List additionalSkuList = new ArrayList<>();
                        additionalSkuList.add(SKU_NOAD);
                        mHelper.queryInventoryAsync(true, additionalSkuList, additionalSkuList, mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void purchaseNoAds(View v) {
        try {
            mHelper.launchPurchaseFlow(this, SKU_NOAD, 10001,
                    mPurchasedFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent setIntent = new Intent(this, SquareActivity.class);

        if (id == R.id.home) {
            setIntent = new Intent(this, SquareActivity.class);
        }

        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, SquareActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                // handle error here
                Snackbar.make(parentLayout, R.string.purchaseNotDone, Snackbar.LENGTH_LONG).show();
            } else {
                mIsRemoveAds = inventory.hasPurchase(SKU_NOAD);
                if (!mIsRemoveAds) {
                    if (inventory.getSkuDetails(SKU_NOAD) != null) {
                        String noAdsPrice = inventory.getSkuDetails(SKU_NOAD).getPrice();
                        Button btnNoAds = (Button) BillingActivity.this.findViewById(R.id.noAdsBtn);
                        if (btnNoAds != null) {
                            btnNoAds.setText(getApplicationContext().getResources().getString(R.string.purchaseNoAds, noAdsPrice));
                        }
                    }
                } else {
                    RelativeLayout noAds = (RelativeLayout) BillingActivity.this.findViewById(R.id.noAds);
                    noAds.setVisibility(View.GONE);

                    RelativeLayout thx = (RelativeLayout) BillingActivity.this.findViewById(R.id.thx);
                    thx.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    public IabHelper.OnIabPurchaseFinishedListener mPurchasedFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Snackbar.make(parentLayout, R.string.purchaseNotDone, Snackbar.LENGTH_LONG).show();
            } else if (purchase.getSku().equals(SKU_NOAD)) {
                Intent intent = new Intent(getApplicationContext(), SquareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void queryPurchasedItems() {
        //check if user has bought "remove adds"
        if (mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
            try {
                mHelper.queryInventoryAsync(mGotInventoryListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryPurchasedItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryPurchasedItems();
    }

}
