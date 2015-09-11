

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Ai {


    static class YesNoMap extends AIMap {

        Response yes;
        Response no;

        public YesNoMap(Ai.Response yes, Ai.Response no) {
            this.yes = yes;
            this.no = no;
        }

        public Ai.Response get(String choice) {
            if ("yes".equalsIgnoreCase(choice)) return yes;
            if ("no".equalsIgnoreCase(choice)) return no;
            return null;
        }

        @Override
        public void add(String movie, Response moviesList) {
            //not implemented
        }
    }

    static class MultipleMap extends AIMap {

        public Map<String, Response> getMap() {
            return map;
        }

        public MultipleMap() {
            map = new HashMap<String, Response>();
        }

        public Ai.Response get(String choice) {
            return map.get(choice);
        }

        public void add(String name, Response value) {
            map.put(name, value);
        }

    }

    static class NextResponseHolder extends AIMap {
        private int counter = 0;
        private Response[] values;

        NextResponseHolder(Response[] values) {
            this.values = values;
        }

        public Response get(String notUsed) {
            return getNext();
        }

        public Response getNext() {
            return values[counter++ % values.length];
        }

        public void add(String movie, Response moviesList) {
            //not implemented
        }
    }

    static abstract class AIMap {
        public Map<String, Response> map;

        abstract Response get(String val);

        abstract void add(String movie, Response moviesList);

        public boolean contains(String s) {
            return map.containsKey(s);
        }
    }

    public enum ResponseType {
        YES_NO_QUESTION,
        MULTIPLE_CHOICE_QUESTION,
        NOT_EXPECTING_RESPONSE;
    }

    static Response hbTommorow, beginner, thanxBye, moviesList,
            theatersList, prettyPlaces, eat, great, iFeelYaBro,
            tellMeMore, what, iDontUnderstand ;

    static AIMap moviesTheatersClubsEtcEtc;

    static NextResponseHolder abstractAnswers;
    static {
         abstractAnswers = new NextResponseHolder(new Response[]{hbTommorow, tellMeMore, great, iFeelYaBro, what, iDontUnderstand});

        moviesList = new Response("There you go! Here is the list of movies: ........ ",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);
        theatersList = new Response("Well, let me give you the list of theaters: ........ ",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);
        prettyPlaces = new Response("You can take a walk down there pretty places in Kyiv: ........ ",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);
        eat = new Response("Here is the list of the best restaurants sorted by ratings: ........ ",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        moviesTheatersClubsEtcEtc = new MultipleMap();
        moviesTheatersClubsEtcEtc.add("movie", moviesList);
        moviesTheatersClubsEtcEtc.add("theater", theatersList);
        moviesTheatersClubsEtcEtc.add("tour", prettyPlaces);
        moviesTheatersClubsEtcEtc.add("eat", eat);

        hbTommorow = new Response(
                "Would you like to pick something for tomorrow?",
                ResponseType.YES_NO_QUESTION, new YesNoMap(beginner, thanxBye));
        beginner = new Response("What would you like to do?", ResponseType.MULTIPLE_CHOICE_QUESTION, moviesTheatersClubsEtcEtc);

        iFeelYaBro = new Response("I feel ya', bro", ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        what = new Response("Sorry, didn't get that, could you be more clear?",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        iDontUnderstand = new Response("Man, I still don't understand what you want!",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        great = new Response("that's great!", ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        tellMeMore = new Response("Tell me more!", ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);

        thanxBye = new Response("Well, enjoy your time out! I'm glad I was of any help :)",
                ResponseType.NOT_EXPECTING_RESPONSE, abstractAnswers);
    }
    
    static class Response {
        String text;
        ResponseType type;
        AIMap nextMoves;

        public Response(String text, ResponseType type, AIMap nextMoves) {
            this.text = text;
            this.type = type;
            this.nextMoves = nextMoves;
        }

        public String toString() {
            return text;
        }
    }

    public static void main(String[] args) throws IOException {
        Response computersThoughts = beginner;
        byte[] usersAnswer;
        while (true) {
            try {
                usersAnswer = new byte[9000];
                if (computersThoughts != null && computersThoughts.toString() != null) System.out.println(computersThoughts.toString());
                System.in.read(usersAnswer);
                computersThoughts = decode(new String(usersAnswer), computersThoughts.type, computersThoughts.nextMoves);
            } catch (Exception e) {
                computersThoughts = abstractAnswers.getNext();
            }
        }
    }

    private static Response decode(String userAnswer, ResponseType type, AIMap nextMoves) {
        String[] tokens = userAnswer.split(" ");
        Response response = null;
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].toLowerCase();
        }
        switch (type) { 
            case NOT_EXPECTING_RESPONSE:
                response = nextMoves.get("");
            case YES_NO_QUESTION: {
                for (String s : tokens) {
                    if (nextMoves.get(s) != null) {
                        response = nextMoves.get(s);
                        break;
                    }
                    else break;
                }
            }
            case MULTIPLE_CHOICE_QUESTION: {
                for (String s : tokens) {
                    if (nextMoves.contains(s)) {
                        response = nextMoves.get(s);
                        break;
                    }
                }
            }
        }

        if (response == null) {
            response = what;
            response.type = type;
            response.nextMoves = nextMoves;
        }
        return response;
    }
}
