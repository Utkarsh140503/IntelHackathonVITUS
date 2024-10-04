package com.vitus.intelkisanmadad;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public List<Item> parse(InputStream inputStream) {
        List<Item> items = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
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
                                    items.add(currentItem);
                                    currentItem = null;
                                    break;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}

class Item {
    private String state;
    private String district;
    private String market;
    private String commodity;
    private String variety;
    private String grade;
    private String arrivalDate;
    private String minPrice;
    private String maxPrice;
    private String modalPrice;
    private String commodityCode;

    // Getters and setters for each field
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }

    public String getCommodity() { return commodity; }
    public void setCommodity(String commodity) { this.commodity = commodity; }

    public String getVariety() { return variety; }
    public void setVariety(String variety) { this.variety = variety; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }

    public String getMinPrice() { return minPrice; }
    public void setMinPrice(String minPrice) { this.minPrice = minPrice; }

    public String getMaxPrice() { return maxPrice; }
    public void setMaxPrice(String maxPrice) { this.maxPrice = maxPrice; }

    public String getModalPrice() { return modalPrice; }
    public void setModalPrice(String modalPrice) { this.modalPrice = modalPrice; }

    public String getCommodityCode() { return commodityCode; }
    public void setCommodityCode(String commodityCode) { this.commodityCode = commodityCode; }
}
