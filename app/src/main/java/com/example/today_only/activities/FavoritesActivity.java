package com.example.today_only.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_only.R;
import com.example.today_only.adapters.FavoritesAdapter;
import com.example.today_only.entities.Deal;
import com.example.today_only.utilities.DealUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView favoritesRecyclerView;
    private TextView emptyStateText;
    private DealUtil dealUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        dealUtil = new DealUtil();
        initializeViews();
        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();  // Reload favorites when returning to the activity
    }

    private void initializeViews() {
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        dealUtil.loadAllDeals();

        // Setup back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void loadFavorites() {
        // Get favorited deals from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("Favorites", MODE_PRIVATE);
        Set<String> favoritedDealIds = prefs.getStringSet("favorited_deals", new HashSet<>());

        // Filter all deals to get only favorites
        List<Deal> allDeals = dealUtil.getAllDeals();
        List<Deal> favoritedDeals = allDeals.stream()
                .filter(deal -> favoritedDealIds.contains(deal.getId()))
                .collect(Collectors.toList());

        if (favoritedDeals.isEmpty()) {
            showEmptyState();
        } else {
            showFavorites(favoritedDeals);
        }
    }

    private void showEmptyState() {
        emptyStateText.setVisibility(View.VISIBLE);
        favoritesRecyclerView.setVisibility(View.GONE);
    }

    private void showFavorites(List<Deal> favorites) {
        emptyStateText.setVisibility(View.GONE);
        favoritesRecyclerView.setVisibility(View.VISIBLE);

        // Group deals by day
        Map<Integer, List<Deal>> dealsByDay = new TreeMap<>();
        for (Deal deal : favorites) {
            for (Integer day : deal.getAvailableDays()) {
                dealsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(deal);
            }
        }

        FavoritesAdapter adapter = new FavoritesAdapter(this, dealsByDay);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritesRecyclerView.setAdapter(adapter);
    }
}
