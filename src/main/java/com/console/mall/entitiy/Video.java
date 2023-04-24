package com.console.mall.entitiy;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "videos")
@Getter
@Setter
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SerializedName("video_id")
    @Column(name = "video_id")
    private String videoId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}