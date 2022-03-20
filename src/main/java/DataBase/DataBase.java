package DataBase;

import rx.Observable;

public interface DataBase {
    Observable<Boolean> addUser(User user);
    Observable<Boolean> addItem(Item item);
    Observable<User> getUser(int id);
    Observable<Item> getItems();
}
