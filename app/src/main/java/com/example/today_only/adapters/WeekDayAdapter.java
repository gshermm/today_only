package com.example.today_only.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_only.R;
import com.example.today_only.entities.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayAdapter.DayViewHolder> {
    private final List<Day> days = new ArrayList<>();
    private int selectedPosition = -1;

    private final OnDaySelectedListener listener;

    public interface OnDaySelectedListener {
        void onDaySelected(Calendar date);
    }

    public WeekDayAdapter(OnDaySelectedListener listener) {
        this.listener = listener;
        updateDays();
    }

    public void updateDays() {
        days.clear();
        Calendar calendar = Calendar.getInstance();

        // Get to the start of the week (Sunday)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_WEEK, -dayOfWeek + Calendar.SUNDAY);

        for (int i = 0; i < 7; i++) {
            Calendar dayCalendar = (Calendar) calendar.clone();
            boolean isToday = isSameDay(dayCalendar, Calendar.getInstance());
            Day dayModel = new Day(dayCalendar, isToday);
            days.add(dayModel);

            if (isToday) {
                selectedPosition = i;
                dayModel.setSelected(true);
            }
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        if (selectedPosition == -1 && !days.isEmpty()) {
            selectedPosition = 0;
            days.get(0).setSelected(true);
        }

        notifyDataSetChanged();
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public void setSelectedDay(Calendar selectedDate) {
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            if (isSameDay(day.getDate(), selectedDate)) {
                int previousSelected = selectedPosition;
                selectedPosition = i;

                if (previousSelected != -1 && previousSelected < days.size()) {
                    days.get(previousSelected).setSelected(false);
                    notifyItemChanged(previousSelected);
                }

                day.setSelected(true);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_day, parent, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.bind(day);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            int newPosition = holder.getAdapterPosition();

            if (newPosition == RecyclerView.NO_POSITION) {
                return;
            }

            if (previousSelected != newPosition) {
                if (previousSelected != -1 && previousSelected < days.size()) {
                    days.get(previousSelected).setSelected(false);
                    notifyItemChanged(previousSelected);
                }
                day.setSelected(true);
                selectedPosition = newPosition;
                notifyItemChanged(selectedPosition);
            }

            listener.onDaySelected(day.getDate());
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayText, dateText;
        LinearLayout dayContainer;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            dateText = itemView.findViewById(R.id.dateText);
            dayContainer = itemView.findViewById(R.id.dayContainer);
        }

        public void bind(Day day) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            dayText.setText(dayFormat.format(day.getDate().getTime()));
            dateText.setText(String.valueOf(day.getDate().get(Calendar.DAY_OF_MONTH)));

            dayContainer.setSelected(day.isSelected());
            dayContainer.setBackgroundResource(day.isSelected() ?
                    R.drawable.bg_selected_day :
                    R.drawable.bg_default_day);
        }
    }
}
