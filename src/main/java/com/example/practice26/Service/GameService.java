package com.example.practice26.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practice26.Model.Games;
import com.example.practice26.Repository.GameRepo;

@Service
public class GameService {
    @Autowired
    private GameRepo repo;

    public List<Document> getAllGames(int limit, int offset){
        System.out.println("running service");
        return repo.getAllGames(limit, offset);
    }

    public List<Document> getAllGamesByRankASC (int limit, int offset) {
        System.out.println("running service asc");
        return repo.getAllGamesByRankASC(limit, offset);
    }

    public List<Document> getAllGamesByRankDESC (int limit, int offset) {
        return repo.getAllGamesByRankDESC(limit, offset);
    }

    public Optional<Games> getGameById (int id) {

        Document d = repo.getGameById(id);

        if (d == null) {
            return Optional.empty();
        }

        Games g = new Games();
        g.setName(d.getString("name"));
        g.setId(d.getInteger("gid"));
        g.setYear(d.getInteger("year"));
        g.setRanking(d.getInteger("ranking"));
        g.setUsersRated(d.getInteger("users_rated"));
        g.setUrl(d.getString("url"));
        g.setImageURL(d.getString("image"));
        g.setTimestamp(new Date());
    

        return Optional.of(g);
    }
}
