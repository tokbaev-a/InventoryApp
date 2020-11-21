package knigh4ttk.application.inventoryapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter presenter;
    Toolbar toolbar;
    RecyclerView rView;
    ItemAdapter iAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete all items?");
            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                if (presenter.getAll() != null) {
                    presenter.deleteAll();
                    setView();
                } else {
                    Toast.makeText(MainActivity.this, "Database is empty", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "All items deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setView() {
        rView = findViewById(R.id.list);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true);
        iAdapter = new ItemAdapter(this);
        iAdapter.setItems(presenter.getAll());
        rView.setAdapter(iAdapter);
        rView.invalidateItemDecorations();
        iAdapter.setOnItemClickListener((position, item) -> {
            Intent intentEditMode = new Intent(MainActivity.this, EditorActivity.class);
            intentEditMode.putExtra(EditorActivity.EXTRA_ID, item.getId());
            intentEditMode.putExtra("LIST", item);
            startActivity(intentEditMode);
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new MainPresenter(getApplication());
        setView();
        FloatingActionButton buttonAddNote = findViewById(R.id.add_product_button);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }
}