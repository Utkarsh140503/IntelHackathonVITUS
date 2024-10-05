package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketplaceActivity extends AppCompatActivity {

    private Spinner spinnerState, spinnerDistrict;
    private Button btnSearch, btnSavePrice;
    private LineChart lineChart;
    private TextView tvCropName, tvCropMeanValue;
    private String selectedCommodity;
    EditText selectedC;
    private float meanPrice;

    // State and District Data
    private Map<String, List<String>> statesAndDistricts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#ECEF8F"));
        }

        // Initialize views
        spinnerState = findViewById(R.id.spinnerState);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        btnSearch = findViewById(R.id.btnSearch);
        btnSavePrice = findViewById(R.id.btnSavePrice);
        lineChart = findViewById(R.id.lineChart);
        tvCropName = findViewById(R.id.tvCropName);
        tvCropMeanValue = findViewById(R.id.tvCropMeanValue);

        // Load states and districts
        loadStatesAndDistricts();

        // Search button listener
        btnSearch.setOnClickListener(v -> {
            String state = (String) spinnerState.getSelectedItem();
            String district = (String) spinnerDistrict.getSelectedItem();
            selectedC = findViewById(R.id.etCommodity);
            selectedCommodity = selectedC.getText().toString().trim();

            new FetchDataTask().execute(state, district, selectedCommodity);
        });

        // Save button listener
        btnSavePrice.setOnClickListener(v -> savePrice());
    }

    private void loadStatesAndDistricts() {
        // Hardcoded data for states and districts
        statesAndDistricts = new HashMap<>();

        statesAndDistricts.put("Andhra Pradesh", List.of("Ananthapur", "Chittoor", "Guntur", "Visakhapatnam", "Krishna", "Kurnool", "Nellore", "Srikakulam", "West Godavari", "East Godavari"));
        statesAndDistricts.put("Arunachal Pradesh", List.of("Itanagar", "Papum Pare", "Tawang", "West Siang", "Lower Subansiri", "Upper Subansiri", "East Siang"));
        statesAndDistricts.put("Assam", List.of("Guwahati", "Dibrugarh", "Jorhat", "Tinsukia", "Nagaon", "Karimganj", "Cachar", "Sonitpur", "Kamrup", "Baksa"));
        statesAndDistricts.put("Bihar", List.of("Patna", "Gaya", "Bhagalpur", "Munger", "Nalanda", "Vaishali", "Saran", "Darbhanga", "Purnia", "Sitamarhi"));
        statesAndDistricts.put("Chhattisgarh", List.of("Raipur", "Bilaspur", "Durg", "Korba", "Rajnandgaon", "Dantewada", "Kanker", "Surguja", "Jashpur", "Narayanpur"));
        statesAndDistricts.put("Goa", List.of("Panaji", "Margao", "Mapusa", "Ponda", "Sanguem"));
        statesAndDistricts.put("Gujarat", List.of("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Gandhinagar", "Bhavnagar", "Jamnagar", "Junagadh", "Kutch", "Dahod"));
        statesAndDistricts.put("Haryana", List.of("Gurugram", "Faridabad", "Panchkula", "Ambala", "Hisar", "Karnal", "Rohtak", "Sonipat", "Yamunanagar", "Fatehabad"));
        statesAndDistricts.put("Himachal Pradesh", List.of("Shimla", "Manali", "Dharamshala", "Kullu", "Mandi", "Solan", "Bilaspur", "Hamirpur", "Una", "Chamba"));
        statesAndDistricts.put("Jharkhand", List.of("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Deoghar", "Dumka", "Giridih", "Palamu", "Hazaribagh", "Chatra"));
        statesAndDistricts.put("Karnataka", List.of("Bangalore", "Mysore", "Mangalore", "Hubli", "Dharwad", "Bellary", "Belgaum", "Tumkur", "Chitradurga", "Kodagu"));
        statesAndDistricts.put("Kerala", List.of("Thiruvananthapuram", "Kochi", "Kollam", "Kozhikode", "Malappuram", "Thrissur", "Pathanamthitta", "Idukki", "Wayanad", "Kottayam"));
        statesAndDistricts.put("Madhya Pradesh", List.of("Bhopal", "Indore", "Gwalior", "Jabalpur", "Ujjain", "Sagar", "Satna", "Dewas", "Rewa", "Chhindwara"));
        statesAndDistricts.put("Maharashtra", List.of("Mumbai", "Pune", "Nashik", "Nagpur", "Aurangabad", "Thane", "Solapur", "Kolhapur", "Ratnagiri", "Sindhudurg"));
        statesAndDistricts.put("Manipur", List.of("Imphal", "Churachandpur", "Thoubal", "Senapati", "Ukhrul", "Tamenglong", "Bishnupur", "Chandel"));
        statesAndDistricts.put("Meghalaya", List.of("Shillong", "Tura", "Jowai", "Williamnagar", "Barddhaman", "Nongstoin", "Mawkyrwat"));
        statesAndDistricts.put("Mizoram", List.of("Aizawl", "Lunglei", "Champhai", "Serchhip", "Mamit", "Lawngtlai", "Kolasib"));
        statesAndDistricts.put("Nagaland", List.of("Kohima", "Dimapur", "Mokokchung", "Mon", "Tuensang", "Zunheboto", "Wokha"));
        statesAndDistricts.put("Odisha", List.of("Bhubaneswar", "Cuttack", "Berhampur", "Rourkela", "Sambalpur", "Balasore", "Puri", "Khurda", "Jagatsinghpur", "Kandhamal"));
        statesAndDistricts.put("Punjab", List.of("Amritsar", "Ludhiana", "Jalandhar", "Patiala", "Mohali", "Bathinda", "Fatehgarh Sahib", "Mansa"));
        statesAndDistricts.put("Rajasthan", List.of("Jaipur", "Udaipur", "Jodhpur", "Ajmer", "Bikaner", "Kota", "Sikar", "Churu", "Pali", "Alwar"));
        statesAndDistricts.put("Sikkim", List.of("Gangtok", "Namchi", "Mangan", "Gyalshing"));
        statesAndDistricts.put("Tamil Nadu", List.of("Chennai", "Madurai", "Coimbatore", "Tiruchirappalli", "Salem", "Erode", "Tirunelveli", "Vellore", "Kanyakumari", "Dharmapuri"));
        statesAndDistricts.put("Telangana", List.of("Hyderabad", "Warangal", "Nizamabad", "Khammam", "Karimnagar", "Mahabubnagar", "Rangareddy", "Medchal-Malkajgiri"));
        statesAndDistricts.put("Tripura", List.of("Agartala", "Ambassa", "Udaipur", "Dharmanagar", "Kailashahar", "Khowai"));
        statesAndDistricts.put("Uttar Pradesh", List.of("Lucknow", "Agra", "Varanasi", "Kanpur", "Ghaziabad", "Noida", "Meerut", "Aligarh", "Bareilly", "Allahabad"));
        statesAndDistricts.put("Uttarakhand", List.of("Dehradun", "Haridwar", "Nainital", "Rudraprayag", "Tehri Garhwal", "Pauri Garhwal", "Champawat"));
        statesAndDistricts.put("West Bengal", List.of("Kolkata", "Darjeeling", "Howrah", "Siliguri", "Asansol", "Bardhaman", "Malda", "Jalpaiguri", "Nadia"));


        // Load states into spinner
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(statesAndDistricts.keySet()));
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(stateAdapter);

        // Set listener for state selection changes
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedState = (String) spinnerState.getSelectedItem();
                loadDistricts(selectedState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void loadDistricts(String state) {
        List<String> districts = statesAndDistricts.get(state);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);
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
                            break;

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
            // Set ListView adapter (assuming you have it)
            ItemAdapter adapter = new ItemAdapter(MarketplaceActivity.this, result);
            ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

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
            arrivalDates.add(item.getArrivalDate());

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
        meanPrice = count > 0 ? totalModalPrice / count : 0;

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
        String askingPriceStr = ((TextView) findViewById(R.id.etAskingPrice)).getText().toString();
        if (askingPriceStr.isEmpty()) {
            Toast.makeText(this, "Please enter your asking price", Toast.LENGTH_SHORT).show();
            return;
        }

        float askingPrice = Float.parseFloat(askingPriceStr);
        String userId = getIntent().getStringExtra("userid");

        String state = (String) spinnerState.getSelectedItem();

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
