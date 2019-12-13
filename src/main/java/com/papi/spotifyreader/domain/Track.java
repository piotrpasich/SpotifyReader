package com.papi.spotifyreader.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Track.
 */
@Entity
@Table(name = "track")
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "external_id")
    private String externalId;

    @ManyToMany(mappedBy = "tracks")
    @JsonIgnore
    private Set<Artist> artists = new HashSet<>();

    @ManyToOne
    private Album album;

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

    public Track name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Track url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExternalId() {
        return externalId;
    }

    public Track externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public Track artists(Set<Artist> artists) {
        this.artists = artists;
        return this;
    }

    public Track addArtists(Artist artist) {
        this.artists.add(artist);
        artist.getTracks().add(this);
        return this;
    }

    public Track removeArtists(Artist artist) {
        this.artists.remove(artist);
        artist.getTracks().remove(this);
        return this;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public Track album(Album album) {
        this.album = album;
        return this;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Track)) {
            return false;
        }
        return id != null && id.equals(((Track) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Track{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
