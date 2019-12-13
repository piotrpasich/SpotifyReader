package com.papi.spotifyreader.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Album.
 */
@Entity
@Table(name = "album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "url")
    private String url;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "popularity")
    private Integer popularity;

    @Column(name = "genres")
    private String genres;

    @ManyToMany(mappedBy = "albums")
    @JsonIgnore
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album")
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

    public Album name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public Album image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public Album url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExternalId() {
        return externalId;
    }

    public Album externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Album popularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getGenres() {
        return genres;
    }

    public Album genres(String genres) {
        this.genres = genres;
        return this;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public Album artists(Set<Artist> artists) {
        this.artists = artists;
        return this;
    }

    public Album addArtists(Artist artist) {
        this.artists.add(artist);
        artist.getAlbums().add(this);
        return this;
    }

    public Album removeArtists(Artist artist) {
        this.artists.remove(artist);
        artist.getAlbums().remove(this);
        return this;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    // tracks
    public Set<Track> getTracks() {
        return tracks;
    }

    public Album tracks(Set<Track> tracks) {
        this.tracks = tracks;
        return this;
    }

    public Album addTrack(Track track) {
        this.tracks.add(track);
        track.setAlbum(this);
        return this;
    }

    public Album removeTrack(Track track) {
        this.tracks.remove(track);
        track.setAlbum(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        return id != null && id.equals(((Album) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", url='" + getUrl() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", popularity=" + getPopularity() +
            ", genres='" + getGenres() + "'" +
            "}";
    }
}
