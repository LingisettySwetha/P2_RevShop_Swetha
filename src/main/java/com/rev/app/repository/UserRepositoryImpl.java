package com.rev.app.repository;

import com.rev.app.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements IUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

   
    @Override
    public User saveUser(User user) {
        entityManager.persist(user);
        return user;
    }

    
    @Override
    public Optional<User> findUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    
    @Override
    public Optional<User> findUserByEmail(String email) {

        List<User> users = entityManager
                .createQuery(
                        "SELECT u FROM User u WHERE u.email = :email",
                        User.class)
                .setParameter("email", email)
                .getResultList();

        return users.stream().findFirst();
    }

    
    @Override
    public List<User> findAllUsers() {
        return entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

   
    @Override
    public void deleteUser(Long id) {

        User user = entityManager.find(User.class, id);

        if (user != null) {
            entityManager.remove(user);
        }
    }
}