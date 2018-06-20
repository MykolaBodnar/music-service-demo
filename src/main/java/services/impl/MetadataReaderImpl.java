package services.impl;

import com.mpatric.mp3agic.Mp3File;
import model.SearchRequest;
import services.MetadataReader;

public class MetadataReaderImpl implements MetadataReader {
    private final static String SPLITTER = " - ";

    @Override
    public SearchRequest getSearchRequest(String filePath) {
        try {
            String songName;
            String artist;
            String[] pathInfo = filePath.split("\\\\");
            String fileName = pathInfo[pathInfo.length - 1];
            fileName = fileName.replace('_',' ');
            System.out.println(fileName);
            int splitterPosition = fileName.indexOf(SPLITTER);
            if (splitterPosition > 1) {
                artist = fileName.substring(0, splitterPosition);
                songName = fileName.substring(splitterPosition + SPLITTER.length(), fileName.length() - 4);
            } else {
                Mp3File mp3File = new Mp3File(filePath);
                songName = mp3File.getId3v2Tag().getTitle();
                artist = mp3File.getId3v2Tag().getArtist();
            }
            if (notNullOrEmpty(songName) && notNullOrEmpty(artist)) {
                System.out.println("songName" + songName);
                System.out.println("artist" + artist);
                return new SearchRequest(songName, artist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean notNullOrEmpty(String string) {
        return string != null && string.length() > 0;
    }
}
