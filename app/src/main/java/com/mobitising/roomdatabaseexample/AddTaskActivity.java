package com.mobitising.roomdatabaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobitising.roomdatabaseexample.dao.DatabaseClient;
import com.mobitising.roomdatabaseexample.dao.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddTaskActivity extends AppCompatActivity {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    private EditText editTextTask, editTextDesc, editTextFinishBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }
    private void saveTask() {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();
        if (sTask.isEmpty()) {
            editTextTask.setError("Task required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        if (sFinishBy.isEmpty()) {
            editTextFinishBy.setError("Finish by required");
            editTextFinishBy.requestFocus();
            return;
        }
        Runnable worker= () -> {
            Task task = new Task();
            task.setTask(sTask);
            task.setDesc(sDesc);
            task.setFinishedBy(sFinishBy);
            task.setFinished(false);

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .taskDao()
                    .insert(task);
            handler.post(() -> {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            });
        };
        executor.execute(worker);
//        Future<?> f = executor.submit(worker);
//        try {
//            f.get();
//            while (f.isDone()){
//                executor.shutdown();
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//            executor.shutdownNow();
//        }

    }

    //List<Future<?>> futures = new ArrayList<Future<?>>();
    //ExecutorService exec = Executors.newFixedThreadPool(5);
    //
    //// Instead of using exec.execute() use exec.submit()
    //// because it returns a monitorable future
    //while((item = stack.pollFirst()) != null){
    //    Runnable worker = new Solider(this, item);
    //    Future<?> f = exec.submit(worker);
    //    futures.add(f);
    //}
    //
    //// A) Await all runnables to be done (blocking)
    //for(Future<?> future : futures)
    //    future.get(); // get will block until the future is done
    //
    //// B) Check if all runnables are done (non-blocking)
    //boolean allDone = true;
    //for(Future<?> future : futures){
    //    allDone &= future.isDone(); // check if future is done
    //}



//        class SaveTask extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                //creating a task
//                Task task = new Task();
//                task.setTask(sTask);
//                task.setDesc(sDesc);
//                task.setFinishedBy(sFinishBy);
//                task.setFinished(false);
//
//                //adding to database
//                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .taskDao()
//                        .insert(task);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                finish();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        SaveTask st = new SaveTask();
//        st.execute();

}