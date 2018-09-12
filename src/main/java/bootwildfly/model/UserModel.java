package bootwildfly.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */

public class UserModel {

    @Min(0)
    private Long uid;
    @NotBlank @Email
    private String email;
    @NotBlank @Length(min = 50, max = 2000)
    private String headers;
    private Long msgTime;
    private String msg;

    public UserModel() {
    }

    public UserModel(Long uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public UserModel(Long uid, String email, String headers) {
        this.uid = uid;
        this.email = email;
        this.headers = headers;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Long msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "uid=" + uid +
                ", email='" + email + '\'' +
                ", headers='" + headers + '\'' +
                '}';
    }
}
