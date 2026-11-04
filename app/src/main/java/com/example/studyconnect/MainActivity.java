package com.example.studyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studyconnect.ui.class_ui.ClassActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Belum ada data kelas, jadi tombol ini membuka layar kelas kosong sebagai
        // pratinjau UI. Ganti dengan alur gabung/buat kelas saat API tersambung.
        findViewById(R.id.joinClass).setOnClickListener(view -> openClassScreen());
        findViewById(R.id.createClass).setOnClickListener(view -> openClassScreen());
        findViewById(R.id.add).setOnClickListener(view ->
                Toast.makeText(this, R.string.text_belum_ada_data, Toast.LENGTH_SHORT).show());
    }

    private void openClassScreen() {
        startActivity(new Intent(this, ClassActivity.class));
    }
}
