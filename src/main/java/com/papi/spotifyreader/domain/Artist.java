package com.papi.spotifyreader.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Artist.
 */
@Entity
@Table(name = "artist")
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "followers", nullable = false)
    private Integer followers;

    @NotNull
    @Column(name = "genres", nullable = false)
    private String genres;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "image")
    private String image;

    @Column(name = "popularity")
    private Integer popularity;

    @Column(name = "url")
    private String url;

    @ManyToMany
    @JoinTable(
        name = "artists_to_albums",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id"))
    @JsonIgnore
    private Set<Album> albums = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "artists_to_tracks",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "track_id"))
    @JsonIgnore
    private Set<Track> tracks = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Artist name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFollowers() {
        return followers;
    }

    public Artist followers(Integer followers) {
        this.followers = followers;
        return this;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getGenres() {
        return genres;
    }

    public Artist genres(String genres) {
        this.genres = genres;
        return this;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getExternalId() {
        return externalId;
    }

    public Artist externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getImage() {
        return image;
    }

    public Artist image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Artist popularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getUrl() {
        return url;
    }

    public Artist url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Set<Album> getAlbums() {
        return albums;
    }

    public Artist albums(Set<Album> albums) {
        this.albums = albums;
        return this;
    }

    public Artist addAlbum(Album album) {
        this.albums.add(album);
        album.getArtists().add(this);
        return this;
    }

    public Artist removeAlbum(Album album) {
        this.albums.remove(album);
        album.getArtists().remove(this);
        return this;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    // tracks
    public Set<Track> getTracks() {
        return tracks;
    }

    public Artist tracks(Set<Track> tracks) {
        this.tracks = tracks;
        return this;
    }

    public Artist addTrack(Track track) {
        this.tracks.add(track);
        track.getArtists().add(this);
        return this;
    }

    public Artist removeTrack(Track track) {
        this.albums.remove(track);
        track.getArtists().remove(this);
        return this;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artist)) {
            return false;
        }
        return id != null && id.equals(((Artist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Artist{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", followers=" + getFollowers() +
            ", genres='" + getGenres() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", image='" + getImage() + "'" +
            ", popularity=" + getPopularity() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
