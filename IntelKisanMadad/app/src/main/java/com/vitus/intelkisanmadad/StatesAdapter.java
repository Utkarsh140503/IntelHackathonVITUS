package com.vitus.intelkisanmadad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateViewHolder> {

    private final List<State> stateList;
    private final Context context;
    private final String userId;

    public StatesAdapter(Context context, List<State> stateList, String userId) {
        this.context = context;
        this.stateList = stateList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
        State state = stateList.get(position);
        holder.stateName.setText(state.getName());
        holder.stateImage.setImageResource(state.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StateSellersActivity.class);
            intent.putExtra("stateName", state.getName());
            intent.putExtra("stateDrawable", state.getImageResource());
            intent.putExtra("userId", userId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    static class StateViewHolder extends RecyclerView.ViewHolder {
        CircleImageView stateImage;
        TextView stateName;

        StateViewHolder(View itemView) {
            super(itemView);
            stateImage = itemView.findViewById(R.id.state_image);
            stateName = itemView.findViewById(R.id.state_name);
        }
    }
}

class State {
    private final String name;
    private final int imageResource;

    public State(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}