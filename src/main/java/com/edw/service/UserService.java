package com.edw.service;

import com.edw.model.GenMdAccountEntity;
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
import java.util.Random;
import java.util.UUID;

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

    public void generate() {
        final RemoteCache cache = remoteCacheManager.getCache("GEN_MD_ACCOUNT");
        for (int i = 0; i < 10000; i ++) {
            cache.put(UUID.randomUUID().toString(), new GenMdAccountEntity(new Random().nextLong(), ""+new Random().nextLong(), 0l, "111",
                    "A", "00", "aaa", "2222",
                    "A", "00", "aaa", "2222",
                    null, "00", null, "2222",
                    null, "00", null, "2222"
            ));
        }
    }

    public void get() {
        final RemoteCache cache = remoteCacheManager.getCache("GEN_MD_ACCOUNT");
        for (int i = 0; i < 10000; i ++) {
            QueryFactory queryFactory = Search.getQueryFactory(cache);
            Query<GenMdAccountEntity> query = queryFactory.create("from default.GenMdAccountEntity where ACCOUNT_CODE = \"7592791042774889696\"");

            Long timestamp = System.currentTimeMillis();
            System.out.println("hasil --> "+ query.execute().list()+" ~ " + (System.currentTimeMillis() - timestamp));
        }
    }
}
