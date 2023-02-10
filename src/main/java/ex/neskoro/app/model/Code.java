package ex.neskoro.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "codes")
public class Code {

    @Column(name = "code", columnDefinition = "VARCHAR(5000)")
    private String code;

    @JsonIgnore
    @Column(name = "date")
    private LocalDateTime date;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @JsonIgnore
    @Column(name = "uuid")
    private final UUID uuid;

    @Column(name = "time")
    private long time;

    @JsonIgnore
    @Column(name = "timeLeft")
    private long timeLeft;

    @Column(name = "views")
    private int views;

    @JsonIgnore
    @Column(name = "viewsRestriction")
    private boolean viewsRestriction;

    public Code() {
        uuid = UUID.randomUUID();
        date = dateTimeInit();
    }

    public Code(String code) {
        this();
        this.code = code;
    }

    public Code(String code, long time, int views) {
//        uuid = UUID.randomUUID();
//        date = dateTimeInit();
//        this.code = code;
        this(code);
        this.time = time;
        this.views = views;
        timeLeft = time;
        viewsRestriction = views > 0;
    }

    private LocalDateTime dateTimeInit() {
        return LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonProperty("date")
    public String getFormattedDate() {
        return getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTime() {
        return time;
    }

    public long getTimeLeft() {return timeLeft;}

    public int getViews() {
        return views;
    }

    public boolean isViewsRestriction() {return viewsRestriction;}

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(long id) {
        this.id=id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTimeLeft(long timeLeft) {this.timeLeft = timeLeft;}

    public void setViews(int views) {
        this.views = views;
    }

    public void setViewsRestriction(boolean bol) {this.viewsRestriction = bol;}

}
