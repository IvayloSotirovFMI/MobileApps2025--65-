package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditTaskActivity extends AppCompatActivity {

    EditText edtTaskName;
    Button btnSave;
    int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTaskName = findViewById(R.id.edtTaskName);
        btnSave = findViewById(R.id.btnSaveTask);

        taskId = getIntent().getIntExtra("task_id", -1);
        String text = getIntent().getStringExtra("task_text");

        edtTaskName.setText(text);

        btnSave.setText("Update");

        btnSave.setOnClickListener(v -> {
            String updated = edtTaskName.getText().toString().trim();

            if (!updated.isEmpty()) {
                Intent result = new Intent();
                result.putExtra("task_id", taskId);
                result.putExtra("task_text", updated);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
