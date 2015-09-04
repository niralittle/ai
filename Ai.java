import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Ai {

    public enum ResponseType {
        YES_NO_QUESTION,
        MULTIPLE_CHOICE_QUESTION,
        NOT_EXPECTING_RESPONSE;
    }

    static Response abstract1, beginner, thanxBye;

    static {
        abstract1 = new Response(
                "Would you like to pick something for tomorrow?",
                ResponseType.YES_NO_QUESTION, new YesNoMap(beginner, thanxBye));
        beginner = new Response("What would you like to do?", ResponseType.MULTIPLE_CHOICE_QUESTION);
    }
    static class Response {
        String test;
        ResponseType type;
        AIMap nextMoves;

        public Response(String test, ResponseType type, AIMap nextMoves) {
            this.test = test;
            this.type = type;
            this.nextMoves = nextMoves;
        }
    }

    private static List<Response> abstractWhatevers = new LinkedList<>();
    static {
        abstractWhatevers.add(abstract1);
    }
    private static List<Response> sorryIdkWhatYouWant;



    public static void main(String[] args) {
        System.out.println("Hello World!");
    }


    interface AIMap {
        Response get(String val);
    }

    static class YesNoMap implements AIMap {

        Response yes;
        Response no;

        public YesNoMap(Ai.Response yes, Ai.Response no) {
            this.yes = yes;
            this.no = no;
        }

        public Ai.Response get(String choice) {
            switch (choice) {
                case "yes" : return yes;
                default: return no;
            }
        }
    }

    static class MultipleMap implements AIMap {

        public Map<String, Response> getMap() {
            return map;
        }

        Map<String, Response> map;

        public MultipleMap() {
        }

        public Ai.Response get(String choice) {
            return map.get(choice);
        }
    }
}


