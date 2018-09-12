package bootwildfly.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
public class HeaderModel {

    private Long id;
    @NotBlank @Length(min = 1, max = 50)
    private String key;
    @NotNull @Min(0)
    private Long uid;
    @NotBlank @Length(max = 500)
    private String value;

    public HeaderModel() {
    }

    public HeaderModel(String key, String value, Long uid) {
        this.key = key;
        this.value = value;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
