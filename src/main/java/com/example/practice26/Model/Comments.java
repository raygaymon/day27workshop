package com.example.practice26.Model;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    private String cId;
    private String user;
    private Integer rating;
    private String cText;
    private Integer gid;
    private List<Document> edited;

    public JsonObject toJSON(){
        return Json.createObjectBuilder()
                    .add("c_id", this.getCId())
                    .add("user", this.getUser())
                    .add("rating", this.getRating())
                    .add("c_text", this.getCText())
                    .add("gid", this.getGid())
                    .build();
    }
}
