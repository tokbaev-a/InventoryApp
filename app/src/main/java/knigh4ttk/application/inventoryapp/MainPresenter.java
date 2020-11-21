package knigh4ttk.application.inventoryapp;

import android.app.Application;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private AppDatabase appDatabase;

    public MainPresenter(Application application) {
        appDatabase = AppDatabase.getInstance(application);
    }

    @Override
    public void deleteAll() {
        appDatabase.itemDao().deleteAllNotes();
    }

    @Override
    public List<Item> getAll() {
        return appDatabase.itemDao().getAllNotes();
    }

    @Override
    public Item getItem(int position) {
        return getItem(position);
    }

}