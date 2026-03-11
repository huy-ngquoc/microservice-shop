package vn.uit.edu.msshop.image.adapter.in.web.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveImageRequest {
    private String imagePublicId;
    private String destination;
    private UUID objectId;
}
