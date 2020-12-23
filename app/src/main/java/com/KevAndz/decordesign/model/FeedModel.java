package com.KevAndz.decordesign.model;

public class FeedModel {
    private String author_image_url;
    private String author_name;
    private String author_id;
    private String post_caption;
    private String post_like;
    private String post_date;
    private String post_imgurl;
    private String post_id;

    public String getAuthorImageUrl() {
        return this.author_image_url;
    }
    public String getAuthorName() {
        return this.author_name;
    }
    public String getCaption() {
        return this.post_caption;
    }
    public String getLike() {
        return this.post_like;
    }
    public String getPostDate() {
        return this.post_date;
    }
    public String getPostImageUrl() {
        return this.post_imgurl;
    }
    public String getAuthorId() {
        return this.author_id;
    }
    public String getPostId() {
        return this.post_id;
    }
    public void setAuthorImgUrl(String author_image_url) {
        this.author_image_url = author_image_url;
    }
    public void setAuthorId(String user_id) {
        this.author_id = user_id;
    }
    public void setPostId(String post_id) {
        this.post_id = post_id;
    }
    public void setAuthorName(String user_name) {
        this.author_name = user_name;
    }
    public void setCaption(String caption) {
        this.post_caption = caption;
    }
    public void setPostImageUrl(String img_url) {
        this.post_imgurl = img_url;
    }
    public void setLike(String likes) {
        this.post_like = likes;
    }
    public void setPostDate(String postDate) {
        this.post_date = postDate;
    }
}
