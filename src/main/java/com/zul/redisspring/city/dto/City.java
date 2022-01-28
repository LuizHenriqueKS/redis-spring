package com.zul.redisspring.city.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {

    private String zip;
    // private String lat;
    // private String lng;
    private String city;
    // private String stateId;
    private String stateName;
    private int temperature;

}
