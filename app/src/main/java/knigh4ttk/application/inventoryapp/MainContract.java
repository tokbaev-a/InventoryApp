package knigh4ttk.application.inventoryapp;

import java.util.List;

public interface MainContract {
    interface View {
    }

    interface Presenter {
        void deleteAll();

        List<Item> getAll();

        Item getItem(int position);
    }
}
