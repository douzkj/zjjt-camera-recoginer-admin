package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CleanupRequest implements Serializable {

    @NotBlank
    private String folder;
}
