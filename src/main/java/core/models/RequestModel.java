package core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestModel {

    private String name;
    private String idBoard;
    private String idList;
    private String idCards;
    private String due;
    private String desc;
    private String state;
    private String idListTwo;
    private String closed;
    private String text;


    public Map<String, String> createParam(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
