package DataBase;

import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;

import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;
import rx.Observable;


import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;


public class MongoDBReactive implements DataBase {

    private final MongoClient client;
    private final MongoDatabase DB;


    public MongoDBReactive() {
        ClusterSettings clusterSettings = ClusterSettings.builder().hosts(List.of(new ServerAddress("localhost"))).build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .clusterSettings(clusterSettings)
//                .credentialList(List.of(MongoCredential.createMongoCRCredential("rootuser", "webCatalog", "rootpass".toCharArray())))
                .build();
        client = MongoClients.create(settings);
        DB = client.getDatabase("webCatalog");
    }

    private MongoCollection<Document> getUserCollection() {
        return DB.getCollection("user");
    }

    private MongoCollection<Document> getItemCollection() {
        return DB.getCollection("item");
    }

    private Observable<Boolean> addDocument(Document document, MongoCollection<Document> collection) {
        return collection
                .find(Filters.eq("id", document.getInteger("id")))
                .toObservable()
                .singleOrDefault(null)
                .flatMap(foundDoc -> {
                    if (foundDoc == null) {
                        return collection.insertOne(document).map(success -> true);
                    } else {
                        return Observable.just(false);
                    }
                });
    }

    @Override
    public Observable<Boolean> addUser(User user) {
        return addDocument(user.toDocument(), getUserCollection());
    }

    @Override
    public Observable<Boolean> addItem(Item item) {
        return addDocument(item.toDocument(), getItemCollection());
    }

    @Override
    public Observable<User> getUser(int id) {
        return getUserCollection().find(Filters.eq("id", id)).toObservable().map(User::new);
    }

    @Override
    public Observable<Item> getItems() {
        return getItemCollection().find().toObservable().map(Item::new);
    }
}
