package com.rofik.miniproject.domain.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Transient;

import java.util.Base64;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {
    private Long id;

    private String name;

    private String description;

    private Integer volume;

    private Integer weight;

    private Integer price;

    @Getter(AccessLevel.NONE)
    private Resource picture;

    @Transient
    @SneakyThrows
    public String getPicture() {
        if (this.picture != null && this.picture.exists()) {
            byte[] fileContent = FileUtils.readFileToByteArray(this.picture.getFile());
            return Base64.getEncoder().encodeToString(fileContent);
        }

        return null;
    }
}
