package com.example.today_only.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_only.R;
import com.example.today_only.entities.Deal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DAY_HEADER = 0;
    private static final int TYPE_DEAL = 1;

    private final Context context;
    private final List<Object> items = new ArrayList<>();

    public FavoritesAdapter(Context context, Map<Integer, List<Deal>> dealsByDay) {
        this.context = context;
        processDealsByDay(dealsByDay);
    }

    private void processDealsByDay(Map<Integer, List<Deal>> dealsByDay) {
        for (Map.Entry<Integer, List<Deal>> entry : dealsByDay.entrySet()) {
            // Add day header
            items.add(getDayName(entry.getKey()));
            // Add deals for that day
            items.addAll(entry.getValue());
        }
    }

    private String getDayName(int day) {
        switch (day) {
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
            default: return "";
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_DAY_HEADER : TYPE_DEAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DAY_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_day_header, parent, false);
            return new DayHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_deal, parent, false);
            return new DealViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DayHeaderViewHolder) {
            ((DayHeaderViewHolder) holder).dayText.setText((String) items.get(position));
        } else if (holder instanceof DealViewHolder) {
            Deal deal = (Deal) items.get(position);
            DealViewHolder dealHolder = (DealViewHolder) holder;
            dealHolder.bind(deal);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DayHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;

        DayHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
        }
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, restaurantText, locationText, timeText, ratingText;
        ImageButton favoriteButton;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextView);
            restaurantText = itemView.findViewById(R.id.restaurantTextView);
            locationText = itemView.findViewById(R.id.locationTextView);
            timeText = itemView.findViewById(R.id.timeTextView);
            ratingText = itemView.findViewById(R.id.ratingTextView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }

        void bind(Deal deal) {
            titleText.setText(deal.getTitle());
            restaurantText.setText(deal.getRestaurant());
            locationText.setText(deal.getLocation());
            timeText.setText(deal.getTime());
            ratingText.setText(String.format("%.1f", deal.getRating()));

            // Set favorite button to filled since this is in favorites
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        }
    }
}
