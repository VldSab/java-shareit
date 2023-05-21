package ru.practicum.shareit.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import javax.validation.groups.Default;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    Map<String, String> curMap = new HashMap<>();

    @Override
    public Exception decode(String methodKey, Response response) {
        Map<String, String> message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, Map.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        switch (response.status()) {
            case 400:
                return new ValidationException(message != null && message.get("error") != null ? message.get("error") : "Bad Request");
            case 404:
                return new NotFoundException(message != null && message.get("error") != null ? message.get("error") : "Not found");
            case 409:
                return new AlreadyExistsException("Conflict");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}