package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * In-memory implementation of UserRepository
 *
 * @see UserRepository
 */
@Repository
public class InMemoryUserRepository implements UserRepository {

    private static final Map<Long, User> data = new HashMap<>();
    private static final Set<String> emails = new HashSet<>();
    private static Long count = 0L;

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> list() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(++count);
        data.put(count, user);
        emails.add(user.getEmail());
        return data.get(count);
    }

    @Override
    public User updateUser(Long id, User user) {
        User currentUser = data.get(id);
        if (user.getName() != null)
            currentUser.setName(user.getName());
        if (user.getEmail() != null) {
            emails.remove(currentUser.getEmail());
            currentUser.setEmail(user.getEmail());
            emails.add(currentUser.getEmail());
        }

        return currentUser;
    }

    @Override
    public boolean deleteUser(Long userId) {
        emails.remove(data.get(userId).getEmail());
        return data.remove(userId, data.get(userId));
    }

    @Override
    public boolean isEmailExists(String email) {
        return emails.contains(email);
    }
}
