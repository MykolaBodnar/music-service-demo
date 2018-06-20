package services.impl;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.AlbumType;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import model.SearchRequest;
import model.TrackInfo;
import services.SpotifyService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyServiceImpl implements SpotifyService {
    private static final String clientId = "234a9150f1f8499f86e9098e66f97fb3";
    private static final String clientSecret = "90dd1c2f05374af4a1a3f94a24ff2fab";
    private static SpotifyApi spotifyApi = getSpotifyApi();

    public TrackInfo findTrackInfo(SearchRequest searchRequest) {
        try {
            System.out.println(searchRequest);
            Paging<Track> trackPaging = findTracks(searchRequest);
            Track track = getActualTrack(trackPaging);
            Album album = findAlbumById(track.getAlbum().getId());
            Artist artist = findArtistById(track.getArtists()[0].getId());
            TrackInfo trackInfo = new TrackInfo();
            trackInfo.setName(track.getName());
            trackInfo.setAlbum(getAlbum(album));
            trackInfo.setArtist(artist.getName());
            trackInfo.setGenre(getGenre(album, artist));
            trackInfo.setYear(getYear(album.getReleaseDate()));
            trackInfo.setNumber(getTrackNumber(track, album));
            trackInfo.setDiscNumber("" + track.getDiscNumber());
            trackInfo.setImage(getImage(trackPaging));
            System.out.println(trackInfo);
            return trackInfo;
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            return null;
        }
    }

    private static SpotifyApi getSpotifyApi() {
        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .build();
            ClientCredentials clientCredentials = spotifyApi
                    .clientCredentials()
                    .build()
                    .execute();
            System.out.println(clientCredentials.getAccessToken());
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi;
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            return null;
        }
    }

    private static String getImage(Paging<Track> trackPaging) {
        if (trackPaging.getTotal() == 1) {
            return trackPaging.getItems()[0].getAlbum().getImages()[0].getUrl();
        }
        for (int i = 0; i < trackPaging.getTotal(); i++) {
            if (AlbumType.SINGLE.equals(trackPaging.getItems()[i].getAlbum().getAlbumType())) {
                return trackPaging.getItems()[i].getAlbum().getImages()[0].getUrl();
            }
        }
        for (int i = 0; i < trackPaging.getTotal(); i++) {
            if (AlbumType.ALBUM.equals(trackPaging.getItems()[i].getAlbum().getAlbumType())) {
                return trackPaging.getItems()[i].getAlbum().getImages()[0].getUrl();
            }
        }
        return null;
    }

    private static Track getActualTrack(Paging<Track> trackPaging) {
        if (trackPaging.getTotal() == 1) {
            return trackPaging.getItems()[0];
        }
        for (int i = 0; i < trackPaging.getTotal(); i++) {
            if (AlbumType.ALBUM.equals(trackPaging.getItems()[i].getAlbum().getAlbumType())) {
                return trackPaging.getItems()[i];
            }
        }
        for (int i = 0; i < trackPaging.getTotal(); i++) {
            if (AlbumType.SINGLE.equals(trackPaging.getItems()[i].getAlbum().getAlbumType())) {
                return trackPaging.getItems()[i];
            }
        }
        return null;
    }

    private static Paging<Track> findTracks(SearchRequest searchRequest) throws Exception {
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(getQuery(searchRequest)).build();
        Paging<Track> trackPaging = searchTracksRequest.execute();
        if (trackPaging.getTotal() < 1) {
            throw new Exception();
        }
        return trackPaging;
    }

    private static String getQuery(SearchRequest searchRequest) {
        return "track:\"" + searchRequest.getTrackName() + "\"%20artist:\"" + searchRequest.getArtistName() + "\"";
    }


    private static String getAlbum(Album album) {
        String albumName = album.getName();
        if (AlbumType.SINGLE.equals(album.getAlbumType())) {
            albumName += "(Single)";
        }
        return albumName;
    }

    private static String getGenre(Album album, Artist artist) {
        if (album.getGenres().length > 0) {
            return album.getGenres()[0];
        }
        if (artist.getGenres().length > 0) {
            return artist.getGenres()[0];
        }
        return null;
    }

    private static String getYear(String releaseDate) {
        if (releaseDate.length() == 4) {
            return releaseDate;
        }
        if (releaseDate.matches("^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$")) {
            return releaseDate.substring(0, 4);
        }
        if (releaseDate.matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-(\\d{4})$")) {
            System.out.println("12-12-2017");
            return releaseDate.substring(releaseDate.length() - 4);
        }
        return null;
    }

    private static String getTrackNumber(Track track, Album album) {
        return track.getTrackNumber() + "/" + album.getTracks().getTotal();
    }

    public static boolean matches(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static Album findAlbumById(String albumId) throws Exception {
        return spotifyApi.getAlbum(albumId)
                .market(CountryCode.US)
                .build()
                .execute();
    }

    public static Artist findArtistById(String artistId) throws Exception {
        return spotifyApi.getArtist(artistId).build().execute();
    }

}
