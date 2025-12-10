package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<Task> tasks = new ArrayList<>();
    private FloatingActionButton fabAdd;

    private TaskDAO taskDAO;
    private static final int ADD_TASK_REQUEST = 1;
    private static final int EDIT_TASK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerTasks);
        fabAdd = findViewById(R.id.fabAddTask);

        TaskDatabase db = TaskDatabase.getInstance(this);
        taskDAO = db.taskDAO();

        adapter = new TaskAdapter(tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        taskDAO.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> taskList) {
                tasks.clear();
                tasks.addAll(taskList);
                adapter.notifyDataSetChanged();
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });

        adapter.setOnTaskClickListener(task -> {
            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("task_text", task.getTitle());
            startActivityForResult(intent, EDIT_TASK_REQUEST);
        });


        adapter.setOnTaskLongClickListener(task -> {
            new Thread(() -> taskDAO.delete(task)).start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK && data != null) {
            String taskText = data.getStringExtra("task_text");
            if (taskText != null && !taskText.isEmpty()) {
                Task newTask = new Task(taskText);
                new Thread(() -> taskDAO.insert(newTask)).start();
            }
        }

        if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra("task_id", -1);
            String updatedText = data.getStringExtra("task_text");

            if (id != -1 && updatedText != null) {
                Task task = new Task(updatedText);
                task.setId(id);
                new Thread(() -> taskDAO.update(task)).start();
            }
        }
    }
}
