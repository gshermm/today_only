package com.example.today_only.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.today_only.R;
import com.example.today_only.entities.Deal;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private final Context context;
    private List<Deal> deals;
    private Set<String> favoritedDeals;

    public DealAdapter(Context context, List<Deal> deals) {
        this.context = context;
        this.deals = deals;
        SharedPreferences prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        this.favoritedDeals = new HashSet<>(prefs.getStringSet("favorited_deals", new HashSet<>()));
    }

    public void updateDeals(List<Deal> newDeals) {
        this.deals = newDeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.titleTextView.setText(deal.getTitle());
        holder.restaurantTextView.setText(deal.getRestaurant());
        holder.locationTextView.setText(deal.getLocation());
        holder.timeTextView.setText(deal.getTime());
        holder.ratingTextView.setText(String.format("%.1f", deal.getRating()));

        holder.itemView.setOnClickListener(v -> showDealDetailsModal(deal));

        ImageButton favoriteButton = holder.itemView.findViewById(R.id.favoriteButton);
        updateFavoriteButtonState(favoriteButton, deal.getId());

        favoriteButton.setOnClickListener(v -> toggleFavorite(deal, favoriteButton));
    }

    private void toggleFavorite(Deal deal, ImageButton button) {
        String dealId = deal.getId();
        boolean isFavorited = favoritedDeals.contains(dealId);
        System.out.println(favoritedDeals);

        if (isFavorited) {
            favoritedDeals.remove(dealId);
            button.setImageResource(R.drawable.ic_favorite_border);
        } else {
            favoritedDeals.add(dealId);
            button.setImageResource(R.drawable.ic_favorite_filled);
        }

        // Save updated favorites
        SharedPreferences prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Clear existing favorites
        editor.putStringSet("favorited_deals", favoritedDeals);
        editor.apply();
    }

    private void updateFavoriteButtonState(ImageButton button, String dealId) {
        if (favoritedDeals.contains(dealId)) {
            button.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            button.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void showDealDetailsModal(Deal deal) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bottom_sheet_deal_details);
        dialog.setCancelable(true);

        // Set up window attributes
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            // Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Calculate dimensions with padding
            int screenWidth = displayMetrics.widthPixels;
            int horizontalPadding = (int) (screenWidth * 0.03);
            int dialogWidth = screenWidth - (2 * horizontalPadding);

            // Set layout parameters
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = dialogWidth;
            window.setAttributes(params);

            // Set background to transparent to show rounded corners
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Initialize views
        TextView dealTitle = dialog.findViewById(R.id.dealTitle);
        TextView dealRestaurant = dialog.findViewById(R.id.dealRestaurant);
        TextView dealDay = dialog.findViewById(R.id.dealDay);
        TextView dealTime = dialog.findViewById(R.id.dealTime);
        TextView dealRequirement = dialog.findViewById(R.id.dealRequirement);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        ImageView closeButton = dialog.findViewById(R.id.closeButton);
        ImageButton shareButton = dialog.findViewById(R.id.shareButton);

        // Set data
        dealTitle.setText(deal.getTitle());
        dealRestaurant.setText(deal.getRestaurant());
        dealTime.setText(deal.getTime());
        dealRequirement.setText("Must Present Student ID");
        ratingBar.setRating(deal.getRating());
        dealDay.setText(deal.getAvailableDatesAsString());

        // Close button listener
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Share button listener
        shareButton.setOnClickListener(v -> {
            View contentView = dialog.findViewById(android.R.id.content);
            shareDealScreenshot(contentView, context);
        });

        // Rate Deal Button listener
        TextView rateDealButton = dialog.findViewById(R.id.rateButton);
        rateDealButton.setOnClickListener(v -> {
            showRatingDialog(deal);
        });

        // Invalid Button listener
        TextView reportInvalidButton = dialog.findViewById(R.id.reportInvalidButton);
        reportInvalidButton.setOnClickListener(v -> {
            showReportDialog(deal);
        });

        dialog.show();
    }

    private void showRatingDialog(Deal deal) {
        Dialog ratingDialog = new Dialog(context);
        ratingDialog.setContentView(R.layout.dialog_rate_deal);

        // Set up window attributes for proper sizing
        Window window = ratingDialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            // Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Calculate dimensions with padding
            int screenWidth = displayMetrics.widthPixels;
            int horizontalPadding = (int) (screenWidth * 0.03);
            int dialogWidth = screenWidth - (2 * horizontalPadding);

            // Set layout parameters
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = dialogWidth;
            window.setAttributes(params);

            // Set background to transparent to show rounded corners
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Initialize views
        TextView restaurantName = ratingDialog.findViewById(R.id.dealRestaurant);
        TextView dealNameText = ratingDialog.findViewById(R.id.dealNameText);
        RatingBar ratingBar = ratingDialog.findViewById(R.id.ratingBar);
        Slider ratingSlider = ratingDialog.findViewById(R.id.ratingSlider);
        Button submitButton = ratingDialog.findViewById(R.id.submitRatingButton);
        ImageView closeButton = ratingDialog.findViewById(R.id.closeButton);

        dealNameText.setText(deal.getTitle());
        restaurantName.setText(deal.getRestaurant());

        ratingSlider.addOnChangeListener((slider, value, fromUser) -> {
            ratingBar.setRating(value);
        });

        // Set initial rating
        ratingSlider.setValue(deal.getRating());

        // Handle rating submission
        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            // Add code to submit rating to backend
            ratingDialog.dismiss();
            showThankYouRatingDialog();
        });

        closeButton.setOnClickListener(v -> ratingDialog.dismiss());

        ratingDialog.show();
    }

    private void showReportDialog(Deal deal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report Invalid Deal");

        final EditText input = new EditText(context);
        input.setHint("Please explain why this deal is invalid");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String reason = input.getText().toString();
            // Handle the report submission
            showThankYouDialog();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showThankYouRatingDialog() {
        Dialog thankYouDialog = new Dialog(context);
        thankYouDialog.setContentView(R.layout.dialog_thank_you_rating);

        Window window = thankYouDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        // Auto dismiss after 2 seconds
        new Handler().postDelayed(thankYouDialog::dismiss, 2000);

        thankYouDialog.show();
    }

    private void showThankYouDialog() {
        Dialog thankYouDialog = new Dialog(context);
        thankYouDialog.setContentView(R.layout.dialog_thank_you);

        Window window = thankYouDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        // Auto dismiss after 2 seconds
        new Handler().postDelayed(thankYouDialog::dismiss, 2000);

        thankYouDialog.show();
    }

    private Bitmap takeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void shareDealScreenshot(View view, Context context) {
        Bitmap bitmap = takeScreenshot(view);

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/deal_screenshot.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(context.getCacheDir(), "images");
            File newFile = new File(imagePath, "deal_screenshot.png");
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider", newFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            context.startActivity(Intent.createChooser(shareIntent, "Share Deal"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, restaurantTextView, locationTextView, timeTextView, ratingTextView;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            restaurantTextView = itemView.findViewById(R.id.restaurantTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
        }
    }
}