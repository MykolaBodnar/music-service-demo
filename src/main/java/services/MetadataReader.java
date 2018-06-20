package services;

import model.SearchRequest;

public interface MetadataReader {
    SearchRequest getSearchRequest(String filePath);
}