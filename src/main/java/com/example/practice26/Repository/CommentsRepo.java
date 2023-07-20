package com.example.practice26.Repository;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import com.example.practice26.Model.Comments;

@Repository
public class CommentsRepo {
    @Autowired
    private MongoTemplate template;

    public Boolean insertComment (Comments c){
        Document newComment = Document.parse(c.toJSON().toString());
        System.out.println(c.toJSON().toString() + " adding success");
        Document inserting = template.insert(newComment, "comments");
        return inserting == null ? true : false;
    }

    public Optional<Comments> update (Comments c) {
        Query query = Query.query(Criteria.where("c_id").is(c.getCId()));
        Update updateOps = new Update().set("rating", c.getRating())
                                        .set("c_text", c.getCText());
        
        if (template.updateMulti(query, updateOps, Document.class, "comments").getModifiedCount() > 0){
            return Optional.of(c);
        }

        return Optional.empty();
    }

    public Optional<Document> updateWithDoc(Document d, String id) {
        Query query = Query.query(Criteria.where("c_id").is(id));
        Update updateOps = new Update().set("rating", d.getInteger("rating"))
                                        .set("c_text", d.getString("comment"))
                                        .push("edited").each(List.of(d).toArray());
        
        if (template.updateMulti(query, updateOps, Document.class, "comments").getModifiedCount() > 0){
            return Optional.of(d);
        }

        return Optional.empty();
    }

    public List<Document> getComment (String id) {
        Query q = Query.query(Criteria.where("c_id").is(id));
        return template.find(q, Document.class, "comments");
    }
}
