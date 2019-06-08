package com.juntao.blogsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.io.Serializable;
import java.sql.Timestamp;

@Document(indexName = "blog",type = "blog")
public class EsBlog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id; //Es主键id要用String
    @Field(type = FieldType.Long)
    private  Long blogId;
    @Field(type =FieldType.Text )
    private String title;
    @Field(type = FieldType.Text)
    private String content;
    @Field(type = FieldType.Text)
    private String summary;
    @Field(type = FieldType.Keyword,index = false) //index=false不做全文搜索
    private String username;
    @Field(type=FieldType.Text,index = false)
    private String avatar;
    @Field(type = FieldType.Date,index = false)
    private Timestamp createTime;
    @Field(type = FieldType.Integer,index = false)
    private Integer readSize;
    @Field(type = FieldType.Integer,index = false)
    private Integer commentSize;
    @Field(type = FieldType.Integer,index =false)
    private  Integer voteSize;
    @Field(type = FieldType.Text,fielddata = true,searchAnalyzer = "ik_smart",analyzer = "ik_smart")  //对分词Field进行聚合操作必须使fielddata=true
    private String tags;  //标签

    protected EsBlog() { //Jap规范要求无参构造函数
    }
    public EsBlog(Long blogId,String title,String summary,String content,String username,String avatar,
                  Timestamp createTime,Integer readSize,Integer commentSize,Integer voteSize,String tags) {
        this.blogId=blogId;
        this.title =title;
        this.summary=summary;
        this.content=content;
        this.username=username;
        this.avatar=avatar;
        this.createTime=createTime;
        this.readSize=readSize;
        this.commentSize=commentSize;
        this.voteSize=voteSize;
        this.tags=tags;
    }

    public EsBlog(Blog blog) {
        this.blogId =blog.getId();
        this.title=blog.getTitle();
        this.summary=blog.getSummary();
        this.content=blog.getContent();
        this.username=blog.getUser().getUsername();
        this.avatar=blog.getUser().getAvatar();
        this.createTime=blog.getCreateTime();
        this.readSize=blog.getReadSize();
        this.commentSize=blog.getCommentSize();
        this.voteSize=blog.getVoteSize();
        this.tags=blog.getTags();
    }

    public void update(Blog blog) {
        this.blogId =blog.getId();
        this.title=blog.getTitle();
        this.summary=blog.getSummary();
        this.content=blog.getContent();
        this.username=blog.getUser().getUsername();
        this.avatar=blog.getUser().getAvatar();
        this.createTime=blog.getCreateTime();
        this.readSize=blog.getReadSize();
        this.commentSize=blog.getCommentSize();
        this.voteSize=blog.getVoteSize();
        this.tags=blog.getTags();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format(
                "Esblog[blogId=%d,title='%s',summary='%s']",
                blogId,title,summary);
    }
}
