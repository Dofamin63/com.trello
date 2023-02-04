package core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModel {

    private String name;
    private String id;
    private String due;
    private String desc;
    private String state;
    private String closed;
    private String _value;
    private String idList;

    private Data data;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String text;
    }
}
