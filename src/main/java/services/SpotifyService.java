package services;

import model.SearchRequest;
import model.TrackInfo;

public interface SpotifyService {
    TrackInfo findTrackInfo(SearchRequest searchRequest);
}
