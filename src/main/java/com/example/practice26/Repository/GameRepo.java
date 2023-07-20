package com.example.practice26.Repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo {
    @Autowired
    private MongoTemplate template;

    public List<Document> getAllGames (int limit, int offset) { 
        System.out.println("running repo");
        Criteria c = Criteria.where("name").exists(true);
        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by(Sort.Direction.ASC, "name"));
        return template.find(q, Document.class, "games");
    }

    public List<Document> getAllGamesByRankASC (int limit, int offset) {
        System.out.println("running repo asc");
        Criteria c = Criteria.where("name").exists(true);
        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by(Sort.Direction.ASC, "ranking"));
        return template.find(q, Document.class, "games");
    }

    public List<Document> getAllGamesByRankDESC (int limit, int offset) {
        Criteria c = Criteria.where("name").exists(true);
        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by(Sort.Direction.DESC, "rank"));
        return template.find(q, Document.class, "games");
    }

    public Document getGameById (int id) {
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);
        return template.findOne(q, Document.class, "games");
    }
}
