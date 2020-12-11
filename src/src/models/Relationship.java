package src.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "relationships")

@NamedQueries({
    @NamedQuery(
            name = "checkRelationship",
            query = "SELECT r FROM Relationship AS r WHERE r.following = :following AND r.followed = :followed AND r.delete_flag = 0"
    ),
    @NamedQuery(
            name = "getAllFollowing",
            query = "SELECT r FROM Relationship AS r WHERE r.following = :following AND r.delete_flag = 0"
    ),
    @NamedQuery(
            name = "getAllFollowed",
            query = "SELECT r FROM Relationship AS r WHERE r.followed = :followed AND r.delete_flag = 0"
    ),
    @NamedQuery(
            name = "getFollowingCount",
            query = "SELECT COUNT(r) FROM Relationship AS r WHERE r.following = :following AND r.delete_flag = 0"
    ),
    @NamedQuery(
            name = "getFollowedCount",
            query = "SELECT COUNT(r) FROM Relationship AS r WHERE r.followed = :followed AND r.delete_flag = 0"
    ),
})

@Entity
public class Relationship {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private Employee following;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private Employee followed;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name = "delete_flag", nullable = false)
    private Integer delete_flag;

    // ここからgetterとsetter

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollowing() {
        return following;
    }
    public void setFollowing(Employee following) {
        this.following = following;
    }

    public Employee getFollowed() {
        return followed;
    }
    public void setFollowed(Employee followed) {
        this.followed = followed;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getDelete_flag() {
        return delete_flag;
    }
    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }
}
