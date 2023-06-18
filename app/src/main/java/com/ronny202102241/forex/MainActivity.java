package com.ronny202102241.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView audTextView, bndTextView, eurTextView, gbpTextView, hkdTextView, inrTextView, jpyTextView, myrTextView, usdTextView, btcTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        audTextView = (TextView) findViewById(R.id.audTextView);
        eurTextView = (TextView) findViewById(R.id.eurTextView);
        gbpTextView = (TextView) findViewById(R.id.gbpTextView);
        hkdTextView = (TextView) findViewById(R.id.hkdTextView);
        inrTextView = (TextView) findViewById(R.id.inrTextView);
        jpyTextView = (TextView) findViewById(R.id.jpyTextView);
        myrTextView = (TextView) findViewById(R.id.myrTextView);
        usdTextView = (TextView) findViewById(R.id.usdTextView);
        btcTextView = (TextView) findViewById(R.id.btcTextView);
        bndTextView = (TextView) findViewById(R.id.bndTextView);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initForex();
                swipeRefreshLayout1.setRefreshing(false);
            }
        });
    }
    public String formatNumber(double number, String format){
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    private void initForex(){
        loadingProgressBar.setVisibility(TextView.VISIBLE);
        String url = "https://openexchangerates.org/api/latest.json?app_id=478ac68dbac94abe8b16a225df47e21f";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double idr = ratesModel.getIDR();
                double aud = idr / ratesModel.getAUD();
                double bnd = idr / ratesModel.getBND();
                double btc = idr / ratesModel.getBTC();
                double eur = idr / ratesModel.getEUR();
                double gbp = idr / ratesModel.getGBP();
                double hkd = idr / ratesModel.getHKD();
                double inr = idr / ratesModel.getINR();
                double jpy = idr / ratesModel.getJPY();
                double myr = idr / ratesModel.getMYR();

                audTextView.setText(formatNumber(aud, "###,##0.##"));
                bndTextView.setText(formatNumber(bnd, "###,##0.##"));
                btcTextView.setText(formatNumber(btc, "###,##0.##"));
                eurTextView.setText(formatNumber(eur, "###,##0.##"));
                gbpTextView.setText(formatNumber(gbp, "###,##0.##"));
                hkdTextView.setText(formatNumber(hkd, "###,##0.##"));
                inrTextView.setText(formatNumber(inr, "###,##0.##"));
                jpyTextView.setText(formatNumber(jpy, "###,##0.##"));
                myrTextView.setText(formatNumber(myr, "###,##0.##"));
                usdTextView.setText(formatNumber(idr, "###,##0.##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }
        });
    }
}