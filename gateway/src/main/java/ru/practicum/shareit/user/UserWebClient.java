package ru.practicum.shareit.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.FeignConfiguration;

import javax.validation.Valid;

@FeignClient(value = "user", url = "${shareit-server.url}/users", decode404 = true, configuration = FeignConfiguration.class)
@Component
public interface UserWebClient {
    @GetMapping
    ResponseEntity<Object> listUsers();

    @GetMapping("/{id}")
    ResponseEntity<Object> getUser(@PathVariable Long id);

    @PostMapping
    ResponseEntity<Object> addUser(@RequestBody @Valid User user);

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user);

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable Long id);
}
