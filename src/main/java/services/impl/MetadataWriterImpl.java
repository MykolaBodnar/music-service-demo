package services.impl;

import com.mpatric.mp3agic.*;
import model.TrackInfo;
import services.MetadataWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MetadataWriterImpl implements MetadataWriter {

    @Override
    public void writeMetadata(TrackInfo trackInfo, String filePath) {
        try {
            Mp3File mp3File = new Mp3File(filePath);
            mp3File.getId3v2Tag().setTitle(trackInfo.getName());
            mp3File.getId3v2Tag().setAlbum(trackInfo.getAlbum());
            mp3File.getId3v2Tag().setArtist(trackInfo.getArtist());
            mp3File.getId3v2Tag().setAlbumArtist(trackInfo.getArtist());
            mp3File.getId3v2Tag().setTrack(trackInfo.getNumber());
            mp3File.getId3v2Tag().setYear(trackInfo.getYear());
            mp3File.getId3v2Tag().setGenre(trackInfo.getGenre());
            mp3File.getId3v2Tag().setAlbumImage(getImageBytes(trackInfo.getImage()), "image/jpeg");
            mp3File.save("output/" + trackInfo.getArtist() + " - " + trackInfo.getName() + ".mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getImageBytes(String imageURL) throws Exception {
        BufferedImage image = ImageIO.read(new URL(imageURL));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos );
        return baos.toByteArray();
    }

    public byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }
}
