package com.juntao.blogsystem.model;

import com.github.rjeschke.txtmark.Processor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "blog_blog")
public class Blog implements Serializable {
    private static final long serialVersionUID  =1L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    private  Long id; //用户的唯一标识

    @NotEmpty(message = "标题不能为空")
    @Size(min =2,max = 50)
    @Column(nullable = false,length = 50) //映射为字段，值不能为空
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2,max=300)
    @Column(nullable = false) //映射为字段，值不能为空
    private String summary;

    @Lob //大对象，映射MySQL的Long Text类型
    @Basic(fetch =FetchType.LAZY) //“懒”加载
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String content;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String htmlContent; //将md转为html

    @OneToOne(cascade = CascadeType.DETACH,fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp  //由数据库自动创建时间
    private Timestamp createTime;

    @Column(name = "readSize")
    private Integer readSize =0;  //访问量，阅读量

    @Column(name="commentSize") //评论量
    private Integer commentSize =0;

    @Column(name = "voteSize")
    private Integer voteSize =0; //点赞量

    @Column(name = "tags",length = 100)
    private String tags; //标签

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)  //FetchType.EAGER 立即加载
    @JoinTable(name = "comment_blog",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"),
            inverseJoinColumns=@JoinColumn(name = "comment_id",referencedColumnName ="id" ))
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "vote_blog",joinColumns =@JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns=@JoinColumn(name = "vote_id",referencedColumnName = "id"))
    private List<Vote> votes;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    protected Blog() {

    }

    public Blog(String title,String summary,String content) {
        this.title = title;
        this.summary =summary;
        this.content =content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public  String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content =content;
        this.htmlContent = Processor.process(content);  //将Markdown内转为HTML
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize =this.comments.size();
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize =this.votes.size();
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * 添加评论
     * @param comment
     */
    public void addComment(Comment comment){
        this.comments.add(comment);
        this.commentSize =comments.size();
    }

    /**
     * 删除评论
     * @param commentId
     */
    public void removeComment(Long commentId) {
        for (int index =0;index<this.comments.size(); index++) {
            if(comments.get(index).getId() == commentId) {
                this.comments.remove(comments.get(index));
                break;
            }
        }
        this.commentSize =comments.size();
    }

    /**
     * 点赞
     * @param vote
     * @return
     */
    public boolean addVote(Vote vote) {
        boolean isExist = false;
        //判断重复
        for (int index=0;index<this.votes.size();index++) {
            if (this.votes.get(index).getUser().getId() ==vote.getUser().getId()) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            this.votes.add(vote);
            this.voteSize =this.votes.size();
        }
        return isExist;
    }

    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId) {
        for (int index =0;index <this.votes.size();index++) {
            if (this.votes.get(index).getId() == voteId) {
                this.votes.remove(index);
                break;
            }
        }
        this.voteSize =votes.size();
    }
}
