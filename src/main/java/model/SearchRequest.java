package model;

public class SearchRequest {
    private String trackName;
    private String artistName;

    public SearchRequest() {
    }

    public SearchRequest(String trackName, String artistName) {
        this.trackName = trackName;
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "trackName='" + trackName + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
