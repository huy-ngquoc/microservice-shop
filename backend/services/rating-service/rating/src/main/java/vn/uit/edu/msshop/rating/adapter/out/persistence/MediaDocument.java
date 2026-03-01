package vn.uit.edu.msshop.rating.adapter.out.persistence;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDocument {
    private String type;
    private String url;
    private int size;
    
    @Field("public_id") // Đảm bảo trùng tên với key trong MongoDB
    private String publicId;
}