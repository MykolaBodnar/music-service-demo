package services;

import org.junit.Before;
import org.junit.Test;
import services.impl.MetadataServiceImpl;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MetadataServiceTest {
    private MetadataService metadataService = new MetadataServiceImpl();

    private String inputDirPath = "input";
    private String outputDirPath = "output";

    @Before
    public void setup(){
        File outputDir = new File(outputDirPath);
        for (int i = 0; i < outputDir.listFiles().length; i++) {
            outputDir.listFiles()[i].delete();
        }
    }
    @Test
    public void updateMetadataTest(){
        File inputDir = new File(inputDirPath);
        int filesCount = inputDir.listFiles().length;
        metadataService.updateMetadata(inputDirPath,outputDirPath);
        File outputDir = new File(outputDirPath);
        assertThat(outputDir.listFiles().length, is(filesCount));
    }
}
