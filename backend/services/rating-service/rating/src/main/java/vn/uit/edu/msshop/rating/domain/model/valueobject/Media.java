package vn.uit.edu.msshop.rating.domain.model.valueobject;

import java.util.Set;

public record Media(String type, String url,int size,String publicId) {
    private static final Set<String> validType = Set.of("VIDEO","IMAGE","AUDIO");

    public Media {
        if(!validType.contains(type)) {
            throw new IllegalArgumentException("Invalid media type");
        }
        if(url==null||url.isBlank()) {
            throw new IllegalArgumentException("Invalid url");
        }
        if(size<=0||size>50) {
            throw new IllegalArgumentException("Invalid size");
        }
        if(publicId==null||publicId.isBlank()) {
            throw new IllegalArgumentException("Invalid public Id");
        }
    }
    
}
