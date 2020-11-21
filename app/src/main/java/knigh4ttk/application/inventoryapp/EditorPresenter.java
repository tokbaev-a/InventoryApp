package knigh4ttk.application.inventoryapp;

import android.app.Application;

public class EditorPresenter implements EditorContract.Presenter {

    AppDatabase InventoryDatabase;
    EditorContract.View view;

    public EditorPresenter(EditorContract.View view, Application app) {
        this.view = view;
        InventoryDatabase = AppDatabase.getInstance(app);
    }

    @Override
    public void delete(int id) {
        InventoryDatabase.itemDao().deleteItem(id);
    }

    @Override
    public void insert(Item item) {
        InventoryDatabase.itemDao().insert(item);
    }

    @Override
    public void update(int id, String name, Integer price, Integer quantity, String supplier, String picture) {
        InventoryDatabase.itemDao().update(id, name, price, quantity, supplier, picture);
    }

}