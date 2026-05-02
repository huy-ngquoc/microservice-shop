package vn.uit.edu.msshop.recommendation.adapter.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.uit.edu.msshop.recommendation.adapter.in.web.response.AIServerResponse;

@FeignClient(name="ML-Service", url="http://localhost:8082")
public interface MLServerCaller {
@PostMapping(
        value = "/", 
        consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<AIServerResponse> getPrediction(@RequestBody byte[] imageBytes);
}
