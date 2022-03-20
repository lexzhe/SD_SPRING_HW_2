
import DataBase.MongoDBReactive;
import Server.RequestProcessor;
import Server.Response;
import io.reactivex.netty.protocol.http.server.HttpServer;


public class Main {

    public static void main(String[] args) {
        RequestProcessor requestProcessor = new RequestProcessor(new MongoDBReactive());
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    String name = req.getDecodedPath().substring(1);
                    System.err.println("input name is " + name);
                    Response response = requestProcessor.process(name, req.getQueryParameters());
                    System.out.println("response status is: " + response.status);
                    resp.setStatus(response.status);
                    return resp.writeString(response.message);
                })
                .awaitShutdown();
    }
}
