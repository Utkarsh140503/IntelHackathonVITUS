package com.vitus.intelkisanmadad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        TextView state;
        TextView district;
        TextView market;
        TextView commodity;
        TextView arrivalDate;
        TextView minPrice;
        TextView maxPrice;
        TextView modalPrice;
    }

    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            // Initialize the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.state = convertView.findViewById(R.id.tvState);
            viewHolder.district = convertView.findViewById(R.id.tvDistrict);
            viewHolder.market = convertView.findViewById(R.id.tvMarket);
            viewHolder.commodity = convertView.findViewById(R.id.tvCommodity);
            viewHolder.arrivalDate = convertView.findViewById(R.id.tvArrivalDate);
            viewHolder.minPrice = convertView.findViewById(R.id.tvMinPrice);
            viewHolder.maxPrice = convertView.findViewById(R.id.tvMaxPrice);
            viewHolder.modalPrice = convertView.findViewById(R.id.tvModalPrice);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Item item = getItem(position);

        // Clear previous data
        viewHolder.state.setVisibility(View.GONE);
        viewHolder.district.setVisibility(View.GONE);
        viewHolder.market.setVisibility(View.GONE);
        viewHolder.commodity.setVisibility(View.GONE);
        viewHolder.arrivalDate.setVisibility(View.GONE);
        viewHolder.minPrice.setVisibility(View.GONE);
        viewHolder.maxPrice.setVisibility(View.GONE);
        viewHolder.modalPrice.setVisibility(View.GONE);

        // Populate the data into the template view using the data object, only if not null
        if (item != null) {
            if (item.getState() != null) {
                viewHolder.state.setText("State: " + item.getState());
                viewHolder.state.setVisibility(View.VISIBLE);
            }
            if (item.getDistrict() != null) {
                viewHolder.district.setText("District: " + item.getDistrict());
                viewHolder.district.setVisibility(View.VISIBLE);
            }
            if (item.getMarket() != null) {
                viewHolder.market.setText("Market: " + item.getMarket());
                viewHolder.market.setVisibility(View.VISIBLE);
            }
            if (item.getCommodity() != null) {
                viewHolder.commodity.setText("Commodity: " + item.getCommodity());
                viewHolder.commodity.setVisibility(View.VISIBLE);
            }
            if (item.getArrivalDate() != null) {
                viewHolder.arrivalDate.setText("Arrival Date: " + item.getArrivalDate());
                viewHolder.arrivalDate.setVisibility(View.VISIBLE);
            }
            if (item.getMinPrice() != null) {
                viewHolder.minPrice.setText("Min Price: " + item.getMinPrice());
                viewHolder.minPrice.setVisibility(View.VISIBLE);
            }
            if (item.getMaxPrice() != null) {
                viewHolder.maxPrice.setText("Max Price: " + item.getMaxPrice());
                viewHolder.maxPrice.setVisibility(View.VISIBLE);
            }
            if (item.getModalPrice() != null) {
                viewHolder.modalPrice.setText("Modal Price: " + item.getModalPrice());
                viewHolder.modalPrice.setVisibility(View.VISIBLE);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
