package com.example.practice26.Model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestOutput {
    private List<Games> games;
    private int offset;
    private int limit;
    private int total = offset + limit;
    private Date timestamp;
}
