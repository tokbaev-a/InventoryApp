package knigh4ttk.application.inventoryapp;

public interface EditorContract {
    interface View {
    }

    interface Presenter {
        void insert(Item item);

        void update(int id, String name, Integer price, Integer quantity, String supplier, String picture);

        void delete(int id);
    }
}


