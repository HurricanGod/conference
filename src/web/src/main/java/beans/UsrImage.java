package beans;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by NewObject on 2017/8/17.
 */
public class UsrImage implements Serializable{
    private Integer image_id;
    private String image_name;
    private String image_url;
    private Date upload_date;
    private UserInfo user;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("image_id:\t").append(image_id)
                .append("\nimage_name:\t").append(image_name)
                .append("\nimage_url:\t").append(image_url).
                append("\nemail:\t").append(user.getEmail())
                .append("\nusername:\t").append(user.getUsername()).append("\n");
        return builder.toString();
    }

    public UsrImage(String image_name, String image_url) {
        this.image_name = image_name;
        this.image_url = image_url;
    }

    public UsrImage() {
        this.image_id = 0;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
