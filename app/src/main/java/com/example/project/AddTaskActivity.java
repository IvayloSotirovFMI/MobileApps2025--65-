package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {

    EditText edtTaskName;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTaskName = findViewById(R.id.edtTaskName);
        btnSave = findViewById(R.id.btnSaveTask);

        btnSave.setOnClickListener(v -> {
            String text = edtTaskName.getText().toString().trim();
            if (!text.isEmpty()) {
                Intent result = new Intent();
                result.putExtra("task_text", text);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
