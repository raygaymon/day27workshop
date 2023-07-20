package com.example.practice26.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practice26.Model.Comments;
import com.example.practice26.Repository.CommentsRepo;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepo repo;

    public Boolean insertComment(Comments c) {
        return repo.insertComment(c);
    }

    public Optional<Comments> update(Comments c) {
        return repo.update(c);
    }

    public Optional<Document> updateWithDoc(Document d, String id){
        return repo.updateWithDoc(d, id);
    }

    public Comments getComment(String id){

        List<Document> retrieved = repo.getComment(id);
        List<Document> editHistory = new ArrayList<>();
        Comments c = new Comments();
        for (Document d : retrieved) {
            c.setUser(d.getString("user"));
            c.setRating(d.getInteger("rating"));
            c.setCText(d.getString("c_text"));
            c.setGid(d.getInteger("gid"));
            c.setCId(id);
            c.setEdited((List<Document>) d.get("edited"));
        }

        return c;
    }
}
