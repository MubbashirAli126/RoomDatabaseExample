package com.mobitising.roomdatabaseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobitising.roomdatabaseexample.adapter.TasksAdapter;
import com.mobitising.roomdatabaseexample.dao.DatabaseClient;
import com.mobitising.roomdatabaseexample.dao.Task;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTask = findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });


        getTasks();
    }

    private void getTasks(){
        Runnable worker= ()->{

            List<Task> taskList = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .taskDao()
                    .getAllTasks();

            handler.post(()->{
                TasksAdapter adapter = new TasksAdapter(MainActivity.this, taskList);
                recyclerView.setAdapter(adapter);
            });

        };

        service.execute(worker);
//        Future<?> f = service.submit(worker);
//        try {
//            f.get();
//            while (f.isDone()){
//                service.shutdown();
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//            service.shutdownNow();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (service != null) {
            service.shutdownNow();
        }
    }
}