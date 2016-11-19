package null256code.githubsearcher.activities;

import android.graphics.Bitmap;

/**
 * Created by kanto on 2016/11/20.
 */

public class RepositoryInfo {

    private Integer id;
    private String ownerLogin;
    private Bitmap ownerImg;
    private String htmlURL;
    private String description;
    private String language;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public Bitmap getOwnerImg() {
        return ownerImg;
    }

    public void setOwnerImg(Bitmap ownerImg) {
        this.ownerImg = ownerImg;
    }

    public String getHtmlURL() {
        return htmlURL;
    }

    public void setHtmlURL(String htmlURL) {
        this.htmlURL = htmlURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
