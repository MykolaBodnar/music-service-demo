package services;

import model.SearchRequest;
import model.TrackInfo;
import org.junit.Test;
import services.impl.SpotifyServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class SpotifyServiceTest {

    private SpotifyService spotifyService = new SpotifyServiceImpl();

    @Test
    public void findTrackInfoTest_WhenTrackExist(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setArtistName("Three Days Grace");
        searchRequest.setTrackName("Never Too Late");
        TrackInfo trackInfo = spotifyService.findTrackInfo(searchRequest);
        assertNotNull(trackInfo);
        assertThat(trackInfo.getArtist(), is("Three Days Grace"));
        assertThat(trackInfo.getAlbum(), is("One-X"));
        assertThat(trackInfo.getName(), is("Never Too Late"));
        assertThat(trackInfo.getGenre(), is("alternative metal"));
        assertThat(trackInfo.getYear(), is("2006"));
        assertThat(trackInfo.getNumber(), is("4/12"));
        assertThat(trackInfo.getDiscNumber(), is("1"));
        assertNotNull(trackInfo.getImage());
    }

    @Test
    public void findTrackInfoTest_WhenTrackNotExist(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setArtistName("Three Days Grace");
        searchRequest.setTrackName("TrackNotExist");
        TrackInfo trackInfo = spotifyService.findTrackInfo(searchRequest);
        assertNull(trackInfo);
    }
}
