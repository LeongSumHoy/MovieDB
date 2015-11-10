package ot.sh.com.moviedb;

/**
 * Created by brandon on 30/10/2015.
 */
public class Movie {
    String url;
    String title;
    String plot;
    String rating;
    String release_date;

    public Movie() {}

    public Movie(String url, String title, String plot, String rating, String release_date) {
        this.url = url;
        this.title = title;
        this.plot = plot;
        this.rating = rating;
        this.release_date = release_date;
    }
}
