package com.douzkj.zjjt.web.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class AlgoRecoVO implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("labelMetrics")
    private List<LabelMetricsDTO> labelMetrics;
    @JsonProperty("labelPoints")
    private List<LabelPointsDTO> labelPoints;

    private String error;

    @NoArgsConstructor
    @Data
    public static class LabelMetricsDTO implements Serializable{
        @JsonProperty("label")
        private String label;
        @JsonProperty("count")
        private Integer count;
    }

    @NoArgsConstructor
    @Data
    public static class LabelPointsDTO implements Serializable {
        @JsonProperty("label")
        private String label;
        @JsonProperty("points")
        private List<List<Integer>> points;
        @JsonProperty("group_id")
        private Object groupId;
        @JsonProperty("shape_type")
        private String shapeType;
        @JsonProperty("flags")
        private Object flags;
        @JsonProperty("probability")
        private Double probability;
    }


    public static AlgoRecoVO error(String id, String error) {
        AlgoRecoVO algoRecoVO = new AlgoRecoVO();
        algoRecoVO.setId(id);
        algoRecoVO.setError(error);
        return algoRecoVO;
    }
}
