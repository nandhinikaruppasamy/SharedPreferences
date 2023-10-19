package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText etName;
    EditText etAge;
    Button btSave;
    Button btDelete;
    TextView tvSize;
    ArrayList<ModelClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.age);
        btSave = findViewById(R.id.button);
        btDelete = findViewById(R.id.btDelete);
        tvSize = findViewById(R.id.list_item);
        loadData();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(etName.getText().toString(), etAge.getText().toString());
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private void saveData(String name, String age) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        arrayList.add(new ModelClass(name, Integer.parseInt(age)));
        String json = gson.toJson(arrayList);
        editor.putString("student_data", json);
        editor.apply();
        tvSize.setText("List Data\n");
        loadData();
    }

    private void deleteData() {
        // Get the index of the item you want to delete (for example, the last item in the list)
        int indexToDelete = arrayList.size() - 1;

        if (indexToDelete >= 0 && indexToDelete < arrayList.size()) {
            arrayList.remove(indexToDelete);

            // Save the updated list back to SharedPreferences
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(arrayList);
            editor.putString("student_data", json);
            editor.apply();

            // Update the UI to reflect the changes
            loadData();
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("student_data", null);
        Type type = new TypeToken<ArrayList<ModelClass>>() {}.getType();
        arrayList = gson.fromJson(json, type);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            tvSize.setText("List is empty");
        } else {
            tvSize.setText("List Data\n");
            for (ModelClass item : arrayList) {
                tvSize.append(item.name + "\n");
            }
        }
    }
}
