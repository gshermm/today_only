package com.example.today_only.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.today_only.R;

public class SubmitDealActivity extends AppCompatActivity {

    private Spinner recurringDropdown;
    private LinearLayout linearLayoutThankYou;
    private Button buttonSubmit;
    private Button buttonBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_deal);

        initViews();
    }

    private void initViews() {
        // Recurring dropdown
        recurringDropdown = findViewById(R.id.recurringDropdown);
        String[] recurringOptions = new String[] {"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recurringOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurringDropdown.setAdapter(adapter);

        linearLayoutThankYou = findViewById(R.id.linearLayoutThankYou);

        buttonBackToHome = findViewById(R.id.buttonBackToHomeInThanks);
        buttonBackToHome.setOnClickListener(v -> finish());

        // Submit button
        buttonSubmit = findViewById(R.id.buttonSubmitDeal);
        buttonSubmit.setOnClickListener(v -> {
            if (validateSubmission()) {
                linearLayoutThankYou.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.GONE);
            }
        });

    }

    private Boolean validateSubmission() {
        // Check if identity is selected
        RadioGroup radioGroupIdentity = findViewById(R.id.radioGroupBtns);
        int selectedIdentityId = radioGroupIdentity.getCheckedRadioButtonId();
        if (selectedIdentityId == -1) {
            showError("Please select your identity.");
            return false;
        }

        // Validate personal information
        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);

        if (editTextFirstName.getText().toString().trim().isEmpty()) {
            showError("First name is required.");
            return false;
        }

        if (editTextLastName.getText().toString().trim().isEmpty()) {
            showError("Last name is required.");
            return false;
        }

        if (editTextEmail.getText().toString().trim().isEmpty()) {
            showError("Email is required.");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()) {
            showError("Please enter a valid email address.");
            return false;
        }

        // Validate deal information
        EditText editTextRestaurantName = findViewById(R.id.editTextRestaurantName);
        EditText editTextDealName = findViewById(R.id.editTextDealName);
        EditText editTextDealDesc = findViewById(R.id.editTextDealDesc);

        if (editTextRestaurantName.getText().toString().trim().isEmpty()) {
            showError("Restaurant name is required.");
            return false;
        }

        if (editTextDealName.getText().toString().trim().isEmpty()) {
            showError("Deal name is required.");
            return false;
        }

        if (editTextDealDesc.getText().toString().trim().isEmpty()) {
            showError("Deal description is required.");
            return false;
        }

        // Validate date
        EditText editTextDate = findViewById(R.id.editTextStartDate);
        if (editTextDate.getText().toString().trim().isEmpty()) {
            showError("Date is required.");
            return false;
        }

        if (!editTextDate.getText().toString().trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
            showError("Please enter a valid date in MM/DD/YYYY format.");
            return false;
        }

        // Validate time window
        EditText editTextTimeWindow = findViewById(R.id.editTextTimeWindow);
        if (editTextTimeWindow.getText().toString().trim().isEmpty()) {
            showError("Time window is required.");
            return false;
        }

        // Validation successful
        return true;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}