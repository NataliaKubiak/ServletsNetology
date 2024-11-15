package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    private static final int DB_CAPACITY = 1000;
    private final ConcurrentMap<Long, Post> fakeDB;

    private final AtomicLong currentMaxId = new AtomicLong(0);

    public PostRepository() {
        fakeDB = new ConcurrentHashMap<>(DB_CAPACITY);
        initDB(); // Инициализация базы данных при создании репозитория
    }

    // Метод для инициализации базы данных
    private void initDB() {
        addDefaultPosts();
    }

    private void addDefaultPosts() {
        fakeDB.put(1L, new Post(1, "This is the first post! Yay!"));
        fakeDB.put(2L, new Post(2, "This is the second one!"));
        fakeDB.put(3L, new Post(3, "I am writing a third POST!"));

        currentMaxId.set(3); // Начинаем с 3, так как у нас уже есть посты с id 1, 2, 3.
    }

    public List<Post> all() {
        return new ArrayList<>(fakeDB.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(fakeDB.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            // Генерируем новый потокобезопасный id
            long newId = currentMaxId.incrementAndGet();
            post.setId(newId);
            fakeDB.put(newId, post);

        } else {
            if (fakeDB.containsKey(post.getId())) {
                fakeDB.put(post.getId(), post);

            } else {
                throw new IllegalArgumentException("Post with id " + post.getId() + " does not exist.");
                //или вернуть null
            }
        }
        return post;
    }

    public boolean removeById(long id) {
        if (fakeDB.containsKey(id)) {
            fakeDB.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
