package services;

import model.TrackInfo;

public interface MetadataWriter {
    void writeMetadata(TrackInfo trackInfo, String filePath);
}
