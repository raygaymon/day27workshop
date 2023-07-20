package com.example.practice26.Model;

import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Games {
    private int id;
    private String name;
    private int year;
    private int ranking;
    private int avgRank;
    private int usersRated;
    private String url;
    private String imageURL;
    private Date timestamp;

    public Games(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public JsonObject toJSON (){
        return Json.createObjectBuilder().add("name", this.getName()).add("id", this.getId()).build();
    }

    
}
