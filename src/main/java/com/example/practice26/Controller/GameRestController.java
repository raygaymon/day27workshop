package com.example.practice26.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.practice26.Model.Comments;
import com.example.practice26.Model.Games;
import com.example.practice26.Model.RestOutput;
import com.example.practice26.Service.CommentsService;
import com.example.practice26.Service.GameService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api")
public class GameRestController {

    @Autowired
    private GameService GService;

    @Autowired
    private CommentsService CService;

    @GetMapping("/{limit}/{offset}")
    public ResponseEntity<RestOutput> getAllGames (@PathVariable("limit") int limit, @PathVariable("offset") int offset) {
        List<Document> gameList = GService.getAllGames(limit, offset);
        List<Games> games = new ArrayList<>();

        for (Document d : gameList) {
            Games g = new Games();
            g.setName(d.getString("name"));
            g.setId(d.getInteger("gid"));
            games.add(g);
        }

        RestOutput ro = new RestOutput(games, limit, offset, (limit + offset), new Date());

        if (games.isEmpty()) {
            System.out.println("emtpy list");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(ro);
        }
    }

    @GetMapping("/games/rank/ASC")
    public ResponseEntity<RestOutput> getAllGamesRankASC () {
        List<Document> gamesByRank = GService.getAllGamesByRankASC(25, 0);
        List<Games> games = new ArrayList<>();

        for (Document d : gamesByRank) {
            Games g = new Games();
            g.setName(d.getString("name"));
            g.setId(d.getInteger("gid"));
            games.add(g);
        }

        RestOutput ro = new RestOutput(games, 0, 25, 25, new Date());

        if (games.isEmpty()) {
            return new ResponseEntity<RestOutput>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<RestOutput>(ro, HttpStatus.OK);
        }
    }

    @GetMapping("/games/rank/DESC")
    public ResponseEntity<RestOutput> getAllGamesRankDESC () {
        List<Document> gamesByRank = GService.getAllGamesByRankDESC(25, 0);
        List<Games> games = new ArrayList<>();

        for (Document d : gamesByRank) {
            Games g = new Games();
            g.setName(d.getString("name"));
            g.setId(d.getInteger("gid"));
            games.add(g);
        }

        RestOutput ro = new RestOutput(games, 0, 25, 25, new Date());

        if (games.isEmpty()) {
            return new ResponseEntity<RestOutput>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<RestOutput>(ro, HttpStatus.OK);
        }
    }

    @GetMapping("/games/game/{id}")
    public ResponseEntity<Games> getGameById (@PathVariable("id") int id) {
        Optional<Games> foundGame = GService.getGameById(id);

        if (foundGame == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(foundGame.get());
        }
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<String> update (@RequestBody MultiValueMap<String, String> update, @PathVariable String id) {
        String rating = update.getFirst("rating");
        String comment = update.getFirst("cText");

        Comments c = new Comments();
        c.setCText(comment);
        c.setRating(Integer.parseInt(rating));
        c.setCId(id);
        
        Document fuckedUp = new Document();
        fuckedUp.put("error", "you fucked up");

        Document output = new Document();
        output.put("comment", comment);
        output.put("rating", Integer.parseInt(rating));
        output.put("posted", new Date().toString());
        Optional<Document> d = CService.updateWithDoc(output, id);

        

        Comments check = CService.getComment(id);
        System.out.println(check.getGid());
        Optional<Games> findGame = GService.getGameById(check.getGid());
        String gameName = findGame.get().getName();
        


        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Document doc : check.getEdited()){
            jab.add(
                Json.createObjectBuilder()
            .add("comment", doc.getString("comment"))
            .add("rating", doc.getInteger("rating"))
            .add("posted", doc.getString("posted"))
            );
            
        }

        JsonObject result = Json.createObjectBuilder()
        .add("user", check.getUser())
        .add("rating", check.getRating())
        .add("comment", check.getCText())
        .add("ID", check.getGid())
        .add("posted", new Date().toString())
        .add("name", gameName)
        .add("edited", jab.build())
        .build();

        // Optional<Comments> toUpdate = CService.update(c);
        // System.out.println(toUpdate);
        if (d.isPresent()) {
            return ResponseEntity.ok(result.toString());
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fuckedUp.toString());
    }

    @PostMapping(path="/review", consumes= "application/x-www-form-urlencoded")
    public ResponseEntity<String> insertComment (@ModelAttribute Comments c) {
        
        c.setCId(UUID.randomUUID().toString().substring(0, 8));
        Boolean insertSuccess = CService.insertComment(c);
        System.out.printf("Comment extraction success for %S", c.getUser());
        System.out.printf("Comment extraction success for %S", c.getGid());
        // List<Document> foundGame = GService.getGameById(c.getGid());
        // List<Games> games = new ArrayList<>();
        // for (Document d : foundGame) {
        //     Games g = new Games();
        //     g.setName(d.getString("name"));
        //     games.add(g);
        // }

        JsonObject output = Json.createObjectBuilder()
        .add("c_id", c.getCId())
        .add("user", c.getUser())
        .add("rating", c.getRating())
        .add("comment", c.getCText())
        .add("ID", c.getGid())
        .add("posted", new Date().toString())
        .build();

        if(insertSuccess) {
            System.out.println("failed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            System.out.printf("Comment insertion success for %S", c.getUser());
            return ResponseEntity.ok().body(output.toString());
        }
    }
    
}
