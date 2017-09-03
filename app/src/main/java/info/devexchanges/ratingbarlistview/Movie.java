package info.devexchanges.ratingbarlistview;

public class Movie {

    private float ratingStar;
    private String name; //Stores the movie name
    private String movieId;  //stores the movie id
    private String geners; // stores the movie gener
    private String url ;  // stores the movie url
    private Integer globalArrayIndex; // Sets the global array index for the item
    private Boolean inGlobalArray = false;  //Flag to check if the particular item is in the Global array declared in application class
    public Movie() {
        this.ratingStar = ratingStar;
        this.name = name;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setGeners(String geners) {
        this.geners = geners;
    }

    public String getGeners() {
        return geners;
    }

    public Integer getGlobalArrayIndex() {
        return globalArrayIndex;
    }

    public void setGlobalArrayIndex(Integer globalArrayIndex) {
        this.globalArrayIndex = globalArrayIndex;
    }

    public Boolean getInGlobalArray() {
        return inGlobalArray;
    }

    public void setInGlobalArray(Boolean inGlobalArray) {
        this.inGlobalArray = inGlobalArray;
    }
}
