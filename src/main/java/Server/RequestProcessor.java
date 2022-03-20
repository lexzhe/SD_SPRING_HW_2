package Server;

import DataBase.Currency;
import DataBase.DataBase;
import DataBase.User;
import DataBase.Item;

import java.text.ParseException;

import java.util.List;
import java.util.Map;

public class RequestProcessor {
    public static final String ADD_USER = "add_user";
    public static final String ADD_ITEM = "add_item";
    public static final String GET_ITEMS = "get_items";
    private final DataBase dataBase;

    public RequestProcessor(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public Response process(String path, Map<String, List<String>> body) {
        return switch (path){
            case ADD_USER -> addUser(body);
            case ADD_ITEM -> addItem(body);
            case GET_ITEMS -> getItems(body);
            default -> Response.badRequestResponse("Unexpected endpoint");
        };
    }

    private Response addUser(Map<String, List<String>> body){
        try{
            return Response.okResponseInsert(dataBase.addUser(
                    new User(
                            extractId(body),
                            extractParam("name", body),
                            extractCurrency(body)
                    )
            ));
        } catch (Exception e){
            return Response.badRequestResponse(e.getMessage());
        }
    }

    private Response addItem(Map<String, List<String>> body){
        try{
            return Response.okResponseInsert(dataBase.addItem(
                    new Item(
                            extractId(body),
                            extractParam("name", body),
                            extractPrice(body),
                            extractCurrency(body)
                    )
            ));
        } catch (Exception e){
            return Response.badRequestResponse(e.getMessage());
        }
    }

    private Response getItems(Map<String, List<String>> body){
        try{
            return Response.okResponseQuery(
                    dataBase.getUser(extractId(body)).flatMap(user ->
                                    dataBase.getItems().map(item -> item.toString(user.currency))
                    ));
        } catch (Exception e){
            return Response.badRequestResponse(e.getMessage());
        }
    }

    private int extractId(Map<String, List<String>> params) throws ParseException, NumberFormatException {
        return Integer.parseInt(extractParam("id", params));
    }

    private double extractPrice(Map<String, List<String>> params) throws ParseException, NumberFormatException {
        return Double.parseDouble(extractParam("price", params));
    }

    private Currency extractCurrency(Map<String, List<String>> params) throws ParseException {
        return Currency.valueOf(extractParam("currency", params));
    }

    private String extractParam(String name, Map<String, List<String>> params) throws ParseException {
        if (params.get(name) == null) {
            throw new ParseException(String.format("Required parameter %s not found", name), 0);
        } else {
             return params.get(name).get(0);
        }
    }
}
