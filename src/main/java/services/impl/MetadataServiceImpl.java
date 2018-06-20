package services.impl;

import model.SearchRequest;
import model.TrackInfo;
import services.MetadataReader;
import services.MetadataService;
import services.MetadataWriter;
import services.SpotifyService;

import java.io.File;

public class MetadataServiceImpl implements MetadataService{

    private  MetadataReader metadataReader = new MetadataReaderImpl();
    private  MetadataWriter metadataWriter = new MetadataWriterImpl();
    private  SpotifyService spotifyService = new SpotifyServiceImpl();

    @Override
    public void updateMetadata(String inputDirectoryPath, String outputDirectoryPath) {
        File doneDir = new File(outputDirectoryPath);
        for (int i = 0; i < doneDir.listFiles().length; i++) {
            doneDir.listFiles()[i].delete();
        }
        File musicDir = new File(inputDirectoryPath);

        for (int i = 0; i < musicDir.listFiles().length; i++) {
            String filePath = musicDir.listFiles()[i].getAbsolutePath();
            SearchRequest searchRequest = metadataReader.getSearchRequest(filePath);
            if(searchRequest != null){
                TrackInfo trackInfo = spotifyService.findTrackInfo(searchRequest);
                metadataWriter.writeMetadata(trackInfo, filePath);
            }
        }
    }
}
