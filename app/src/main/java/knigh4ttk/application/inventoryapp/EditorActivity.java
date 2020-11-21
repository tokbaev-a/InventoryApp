package knigh4ttk.application.inventoryapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.Objects;

public class EditorActivity extends AppCompatActivity implements EditorContract.View, Serializable {

    Uri imageURI;
    String imageString;
    String imageGallery;
    EditorContract.Presenter pres;
    private EditText TextName;
    private EditText TextPrice;
    private EditText TextSupplier;
    private EditText TextQuantity;
    private ImageView imageView;
    private Button imageButton;
    public static final String EXTRA_ID = "knigh4ttk.application.inventoryapp.EditorActivity_Package.EXTRA_ID";
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        TextName = findViewById(R.id.name_editText);
        TextPrice = findViewById(R.id.price_editText);
        TextSupplier = findViewById(R.id.supplier_editText);
        imageView = findViewById(R.id.product_picture);
        imageButton = findViewById(R.id.picture_button);
        TextQuantity = findViewById(R.id.number_picker);

        pres = new EditorPresenter(this, getApplication());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_delete);
        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit");
            getItem((Item) Objects.requireNonNull(getIntent().getSerializableExtra("LIST")));
        } else {
            setTitle("Add");
        }

        imageButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Pick"), REQUEST_CODE);
        });
    }
    private void getItem(Item item) {
        TextName.setText(item.getName());
        TextPrice.setText(String.valueOf(item.getPrice()));
        TextSupplier.setText(item.getSupplier());
        TextQuantity.setText(String.valueOf(item.getQuantity()));
        imageGallery = item.getImageUri();
        Glide
                .with(getApplicationContext())
                .load(imageGallery)
                .into(imageView);
        if (imageGallery == null) {
            imageButton.setText("Set image");
        } else {
            imageButton.setText("Change image");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            if (imageURI == null) {
                throw new AssertionError();
            }
            imageString = imageURI.toString();
            Glide
                    .with(getApplicationContext())
                    .load(imageString)
                    .into(imageView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.save_item) {
            saveData();
            return true;
        } else if (itemID == R.id.delete_item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete this item?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                pres.delete(id);
                finish();
                Toast.makeText(EditorActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        String name = TextName.getText().toString();
        String priceString = TextPrice.getText().toString();
        String quantityString = TextQuantity.getText().toString();
        String supplier = TextSupplier.getText().toString();
        int quantityValue = Integer.parseInt(quantityString);
        int price = Integer.parseInt(priceString);
        Item item1 = new Item(name, price, quantityValue, supplier, imageString);
        Item item2 = (Item) getIntent().getSerializableExtra("LIST");

        if (!name.trim().isEmpty() && !supplier.trim().isEmpty() && !priceString.trim().isEmpty()) {
            if (item2 != null) {
                int id = item2.getId();
                pres.update(id, item1.getName(), item1.getPrice(), item1.getQuantity(), item1.getSupplier(), item1.getImageUri());
                Toast.makeText(EditorActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            } else {
                pres.insert(item1);
                Toast.makeText(EditorActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "Make sure that all fields are filled in", Toast.LENGTH_SHORT).show();
        }

    }
}

