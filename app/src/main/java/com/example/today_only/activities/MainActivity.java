package com.example.today_only.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_only.R;
import com.example.today_only.adapters.DealAdapter;
import com.example.today_only.adapters.WeekDayAdapter;
import com.example.today_only.entities.Deal;
import com.example.today_only.utilities.DealUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // RecyclerViews
    private RecyclerView dayDealsRecyclerView;
    private RecyclerView weekDealsRecyclerView;
    private RecyclerView monthDealsRecyclerView;
    private RecyclerView weekDaysRecyclerView;

    // Adapters
    private DealAdapter dayDealAdapter;
    private DealAdapter weekDealAdapter;
    private DealAdapter monthDealAdapter;

    // Classes
    private final DealUtil dealUtil = new DealUtil();

    // Views
    private Button buttonAddADeal;
    private Spinner spinnerSort;
    private CalendarView monthCalendar;

    // Misc
    private ViewGroup dayViewContainer, weekViewContainer, monthViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and RecyclerViews
        initializeViews();
        setupRecyclerView();

        // Load Deals data
        dealUtil.loadAllDeals();

        // Setup view switchers and listeners
        setupViewSwitchers();

        // Set initial view
        showDayView();
    }

    private void initializeViews() {
        // Main containers
        dayViewContainer = findViewById(R.id.dayViewContainer);
        weekViewContainer = findViewById(R.id.weekViewContainer);
        monthViewContainer = findViewById(R.id.monthViewContainer);

        // RecyclerViews
        dayDealsRecyclerView = findViewById(R.id.dayDealsRecyclerView);
        weekDealsRecyclerView = findViewById(R.id.weekDealsRecyclerView);
        monthDealsRecyclerView = findViewById(R.id.monthDealsRecyclerView);
        weekDaysRecyclerView = findViewById(R.id.weekDaysRecyclerView);

        // Buttons
        buttonAddADeal = findViewById(R.id.buttonAddADeal);
        buttonAddADeal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubmitDealActivity.class);
            startActivity(intent);
        });

        // Other views
        monthCalendar = findViewById(R.id.monthCalendar);

        ImageView filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(this::showFilterMenu);

        ImageView favoritesActivityButton = findViewById(R.id.favoritesActivityButton);
        favoritesActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            // Handle filter selection
            if (itemId == R.id.menu_favorites) {
                // Handle favorites filter
                return true;
            } else if (itemId == R.id.menu_best_rated) {
                // Handle best rated filter
                return true;
            } else if (itemId == R.id.menu_active) {
                // Handle active filter
                return true;
            } else if (itemId == R.id.menu_upcoming) {
                // Handle upcoming filter
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void setupRecyclerView() {
        // Day view
        dayDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayDealAdapter = new DealAdapter(this, new ArrayList<>());
        dayDealsRecyclerView.setAdapter(dayDealAdapter);

        // Week view
        weekDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weekDealAdapter = new DealAdapter(this, new ArrayList<>());
        weekDealsRecyclerView.setAdapter(weekDealAdapter);

        // Month view
        monthDealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        monthDealAdapter = new DealAdapter(this, new ArrayList<>());
        monthDealsRecyclerView.setAdapter(monthDealAdapter);

        // Week days
        weekDaysRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        WeekDayAdapter weekDayAdapter = new WeekDayAdapter(this::onDaySelected);
        weekDaysRecyclerView.setAdapter(weekDayAdapter);
    }

    private void setupViewSwitchers() {
        TabLayout tabLayout = findViewById(R.id.viewSwitcherTabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showDayView();
                        break;
                    case 1:
                        showWeekView();
                        break;
                    case 2:
                        showMonthView();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed
            }
        });

        monthCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            onMonthDateSelected(selectedDate);
        });
    }

    private void showDayView() {
        dayViewContainer.setVisibility(View.VISIBLE);
        weekViewContainer.setVisibility(View.GONE);
        monthViewContainer.setVisibility(View.GONE);

//        Date today = new Date();
//        List<Deal> dealsForDay = dealUtil.showDealsForDate(today);
        Calendar today = Calendar.getInstance();
        List<Deal> dealsForDay = dealUtil.showDealsForDay(today.get(Calendar.DAY_OF_WEEK));
        dayDealAdapter.updateDeals(dealsForDay);
    }

    private void showWeekView() {
        dayViewContainer.setVisibility(View.GONE);
        weekViewContainer.setVisibility(View.VISIBLE);
        monthViewContainer.setVisibility(View.GONE);

        if (weekDaysRecyclerView.getAdapter() != null) {
            WeekDayAdapter weekDayAdapter = (WeekDayAdapter) weekDaysRecyclerView.getAdapter();
            weekDayAdapter.updateDays();

            // Select today's date
            Calendar todayCalendar = Calendar.getInstance();
            weekDayAdapter.setSelectedDay(todayCalendar);

            // Update deals for today
            onDaySelected(todayCalendar);
        }
    }

    private void showMonthView() {
        dayViewContainer.setVisibility(View.GONE);
        weekViewContainer.setVisibility(View.GONE);
        monthViewContainer.setVisibility(View.VISIBLE);

        // Set the calendar to today's date
        Calendar todayCalendar = Calendar.getInstance();
        monthCalendar.setDate(todayCalendar.getTimeInMillis(), false, true);

        // Show deals for today's date
        onMonthDateSelected(todayCalendar);
    }

    private void onDaySelected(Calendar selectedDate) {
//        Date date = selectedDate.getTime();
//        List<Deal> dealsForSelectedDay = dealUtil.showDealsForDate(date);
        List<Deal> dealsForSelectedDay = dealUtil.showDealsForDay(
                selectedDate.get(Calendar.DAY_OF_WEEK)
        );

        // Update week view's deals
        weekDealAdapter.updateDeals(dealsForSelectedDay);
    }

    private void onMonthDateSelected(Calendar selectedDate) {
//        Date date = selectedDate.getTime();
//        List<Deal> dealsForSelectedDay = dealUtil.showDealsForDate(date);
        List<Deal> dealsForSelectedDay = dealUtil.showDealsForDay(
                selectedDate.get(Calendar.DAY_OF_WEEK)
        );

        // Update month view's deals
        monthDealAdapter.updateDeals(dealsForSelectedDay);
    }
}
