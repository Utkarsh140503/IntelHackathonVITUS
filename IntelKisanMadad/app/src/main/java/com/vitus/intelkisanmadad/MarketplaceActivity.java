package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceActivity extends AppCompatActivity {

    private EditText etState, etDistrict, etCommodity, etAskingPrice;
    private Button btnSearch, btnSavePrice;
    private ListView listView;
    private LineChart lineChart;
    private TextView tvCropName, tvCropMeanValue;
    private String selectedCommodity;
    private float meanPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }


        etState = findViewById(R.id.etState);
        etDistrict = findViewById(R.id.etDistrict);
        etCommodity = findViewById(R.id.etCommodity);
        etAskingPrice = findViewById(R.id.etAskingPrice);
        btnSearch = findViewById(R.id.btnSearch);
        btnSavePrice = findViewById(R.id.btnSavePrice);
        listView = findViewById(R.id.listView);
        lineChart = findViewById(R.id.lineChart);
        tvCropName = findViewById(R.id.tvCropName);
        tvCropMeanValue = findViewById(R.id.tvCropMeanValue);

        btnSearch.setOnClickListener(v -> {
            String state = etState.getText().toString().trim();
            String district = etDistrict.getText().toString().trim();
            selectedCommodity = etCommodity.getText().toString().trim();

            new FetchDataTask().execute(state, district, selectedCommodity);
        });

        btnSavePrice.setOnClickListener(v -> savePrice());
    }

    private class FetchDataTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... params) {
            String state = params[0];
            String district = params[1];
            String commodity = params[2];
            List<Item> result = new ArrayList<>();

            try {
                String urlString = "https://api.data.gov.in/resource/35985678-0d79-46b4-9ed6-6f13308a1d24?api-key=579b464db66ec23bdd000001530e5613d4274c796bde3aeb17266f89" +
                        "&format=xml&filters[State.keyword]=" + state +
                        "&filters[District.keyword]=" + district +
                        "&filters[Commodity.keyword]=" + commodity;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                Item currentItem = null;
                String text = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                currentItem = new Item();
                            }

                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if (currentItem != null) {
                                switch (parser.getName()) {
                                    case "State":
                                        currentItem.setState(text);
                                        break;
                                    case "District":
                                        currentItem.setDistrict(text);
                                        break;
                                    case "Market":
                                        currentItem.setMarket(text);
                                        break;
                                    case "Commodity":
                                        currentItem.setCommodity(text);
                                        break;
                                    case "Variety":
                                        currentItem.setVariety(text);
                                        break;
                                    case "Grade":
                                        currentItem.setGrade(text);
                                        break;
                                    case "Arrival_Date":
                                        currentItem.setArrivalDate(text);
                                        break;
                                    case "Min_Price":
                                        currentItem.setMinPrice(text);
                                        break;
                                    case "Max_Price":
                                        currentItem.setMaxPrice(text);
                                        break;
                                    case "Modal_Price":
                                        currentItem.setModalPrice(text);
                                        break;
                                    case "Commodity_Code":
                                        currentItem.setCommodityCode(text);
                                        break;
                                    case "item":
                                        if (hasNonNullValue(currentItem)) {
                                            result.add(currentItem);
                                        }
                                        currentItem = null;
                                        break;
                                }
                            }
                            break;
                    }
                    eventType = parser.next();
                }

                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        private boolean hasNonNullValue(Item item) {
            return item.getState() != null ||
                    item.getDistrict() != null ||
                    item.getMarket() != null ||
                    item.getCommodity() != null ||
                    item.getArrivalDate() != null ||
                    item.getMinPrice() != null ||
                    item.getMaxPrice() != null ||
                    item.getModalPrice() != null;
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            // Set ListView adapter
            ItemAdapter adapter = new ItemAdapter(MarketplaceActivity.this, result);
            listView.setAdapter(adapter);

            // Plot data on LineChart
            updateLineChart(result);
        }
    }

    private void updateLineChart(List<Item> itemList) {
        List<Entry> minPriceEntries = new ArrayList<>();
        List<Entry> maxPriceEntries = new ArrayList<>();
        List<Entry> modalPriceEntries = new ArrayList<>();
        List<String> arrivalDates = new ArrayList<>();

        float totalModalPrice = 0;
        int count = 0;

        // Loop through items and prepare chart data
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            // Assuming arrival date is in a format that can be plotted as a string label
            arrivalDates.add(item.getArrivalDate());

            // Add Min, Max, and Modal price to corresponding lists
            if (item.getMinPrice() != null) {
                minPriceEntries.add(new Entry(i, Float.parseFloat(item.getMinPrice())));
            }
            if (item.getMaxPrice() != null) {
                maxPriceEntries.add(new Entry(i, Float.parseFloat(item.getMaxPrice())));
            }
            if (item.getModalPrice() != null) {
                float modalPrice = Float.parseFloat(item.getModalPrice());
                modalPriceEntries.add(new Entry(i, modalPrice));
                totalModalPrice += modalPrice;
                count++;
            }
        }

        // Calculate the mean price
        if (count > 0) {
            meanPrice = totalModalPrice / count;
        } else {
            meanPrice = 0;
        }

        // Set Crop Name and Crop Mean Value TextViews
        tvCropName.setText("Crop Name: " + selectedCommodity);
        tvCropMeanValue.setText("Crop Mean Value: " + String.format("%.2f", meanPrice));

        // Create DataSets for Min, Max, and Modal Prices
        LineDataSet minPriceDataSet = new LineDataSet(minPriceEntries, "Min Price");
        minPriceDataSet.setColor(getResources().getColor(R.color.colorMinPrice));
        minPriceDataSet.setCircleColor(getResources().getColor(R.color.colorMinPrice));

        LineDataSet maxPriceDataSet = new LineDataSet(maxPriceEntries, "Max Price");
        maxPriceDataSet.setColor(getResources().getColor(R.color.colorMaxPrice));
        maxPriceDataSet.setCircleColor(getResources().getColor(R.color.colorMaxPrice));

        LineDataSet modalPriceDataSet = new LineDataSet(modalPriceEntries, "Modal Price");
        modalPriceDataSet.setColor(getResources().getColor(R.color.colorModalPrice));
        modalPriceDataSet.setCircleColor(getResources().getColor(R.color.colorModalPrice));

        // Add datasets to the chart
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(minPriceDataSet);
        dataSets.add(maxPriceDataSet);
        dataSets.add(modalPriceDataSet);

        LineData lineData = new LineData(dataSets);

        // Configure X-Axis with arrival dates
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(arrivalDates));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Set the data and refresh chart
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart with new data
    }

    private void savePrice() {
        String askingPriceStr = etAskingPrice.getText().toString();
        if (askingPriceStr.isEmpty()) {
            Toast.makeText(this, "Please enter your asking price", Toast.LENGTH_SHORT).show();
            return;
        }

        float askingPrice = Float.parseFloat(askingPriceStr);
        String userId = getIntent().getStringExtra("userid");

        String state = etState.getText().toString().trim();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference marketplaceRef = database.child("MarketPlace")
                .child("Commodity")
                .child(state)
                .child(userId)
                .child(selectedCommodity); // Use the commodity as a key

        // Create a new data entry
        DataEntry dataEntry = new DataEntry(userId, selectedCommodity, meanPrice, askingPrice);

        // Save data to Firebase
        marketplaceRef.setValue(dataEntry).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MarketplaceActivity.this, "Price saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MarketplaceActivity.this, "Failed to save price", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // DataEntry class to represent the data structure in Firebase
    private static class DataEntry {
        public String userId;
        public String commodity;
        public float meanPrice;
        public float askingPrice;

        public DataEntry() {
            // Default constructor required for calls to DataSnapshot.getValue(DataEntry.class)
        }

        public DataEntry(String userId, String commodity, float meanPrice, float askingPrice) {
            this.userId = userId;
            this.commodity = commodity;
            this.meanPrice = meanPrice;
            this.askingPrice = askingPrice;
        }
    }
}
