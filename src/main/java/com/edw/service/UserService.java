package com.edw.service;

import com.edw.model.User;
import com.edw.repository.UserRepository;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 *  com.edw.service.UserService
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 15 Oct 2024 19:43
 */
@Service
@Transactional
public class UserService {

    private RemoteCacheManager remoteCacheManager;

    private UserRepository userRepository;

    @Autowired
    public UserService(RemoteCacheManager remoteCacheManager, UserRepository userRepository) {
        this.remoteCacheManager = remoteCacheManager;
        this.userRepository = userRepository;
    }

    /**
     * this is to get users from cache
     *
     * @return List<User> users
     */
    public List<User> getUsers() {
        final RemoteCache cache = remoteCacheManager.getCache("user");
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query<User> query = queryFactory.create("FROM proto.User order by name ASC");

        // execute the query
        return query.execute().list();
    }

    /**
     * this is to save user model to database (h2)
     * @param user
     */
    public void save(User user) {
        userRepository.save(user);
    }


    /**
     * this is to sync user models from database (h2) to cache
     */
    public void synchronize() {
        final RemoteCache cache = remoteCacheManager.getCache("user");
        userRepository.findAll()
                .forEach(user -> cache.put(user.getName(), user));
    }
}
