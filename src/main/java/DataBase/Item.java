package DataBase;

import org.bson.Document;

import java.util.Map;

public class Item implements DocumentEntity{
    public final int id;
    public final String name;
    public final double price;
    public final Currency currency;


    public Item(Document doc) {
        this(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getDouble("price"),
                Currency.valueOf(doc.getString("currency")));
    }

    public Item(int id, String name, double price, Currency currency) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency = currency;
    }

    public String toString(Currency to) {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ",price ='" + currency.convert(price, to) + " " + to + '\'' +
                '}';
    }

    @Override
    public Document toDocument() {
        return new Document(Map.of(
                "id", id,
                "name", name,
                "price", price,
                "currency", currency.toString()
        ));
    }
}
