package Server;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;


public class Response {
    public final HttpResponseStatus status;
    public final Observable<String> message;

    private Response(HttpResponseStatus status, Observable<String> message) {
        this.status = status;
        this.message = message;
    }

    public static Response badRequestResponse(String message) {
       return new Response(HttpResponseStatus.BAD_REQUEST, Observable.just(message));
   }

   public static Response okResponseQuery(Observable<String> message) {
       return new Response(HttpResponseStatus.OK, message);
   }

   public static Response okResponseInsert(Observable<Boolean> resultOfInsertion) {
       return new Response(HttpResponseStatus.OK,
               resultOfInsertion.map(bool -> bool ? "Successfully inserted" : "Insertion failed"));
   }

//   public void setUpResponse(HttpServerResponse response){
//        response.setStatus(status);
//        response.writeString(message);
//   }
}
