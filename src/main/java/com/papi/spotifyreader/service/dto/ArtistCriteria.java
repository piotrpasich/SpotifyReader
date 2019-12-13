package com.papi.spotifyreader.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.papi.spotifyreader.domain.Artist} entity. This class is used
 * in {@link com.papi.spotifyreader.web.rest.ArtistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /artists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArtistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter followers;

    private StringFilter genres;

    private StringFilter externalId;

    private StringFilter image;

    private IntegerFilter popularity;

    private StringFilter url;

    public ArtistCriteria(){
    }

    public ArtistCriteria(ArtistCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.followers = other.followers == null ? null : other.followers.copy();
        this.genres = other.genres == null ? null : other.genres.copy();
        this.externalId = other.externalId == null ? null : other.externalId.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.popularity = other.popularity == null ? null : other.popularity.copy();
        this.url = other.url == null ? null : other.url.copy();
    }

    @Override
    public ArtistCriteria copy() {
        return new ArtistCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getFollowers() {
        return followers;
    }

    public void setFollowers(IntegerFilter followers) {
        this.followers = followers;
    }

    public StringFilter getGenres() {
        return genres;
    }

    public void setGenres(StringFilter genres) {
        this.genres = genres;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public StringFilter getImage() {
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public IntegerFilter getPopularity() {
        return popularity;
    }

    public void setPopularity(IntegerFilter popularity) {
        this.popularity = popularity;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArtistCriteria that = (ArtistCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(followers, that.followers) &&
            Objects.equals(genres, that.genres) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(image, that.image) &&
            Objects.equals(popularity, that.popularity) &&
            Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        followers,
        genres,
        externalId,
        image,
        popularity,
        url
        );
    }

    @Override
    public String toString() {
        return "ArtistCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (followers != null ? "followers=" + followers + ", " : "") +
                (genres != null ? "genres=" + genres + ", " : "") +
                (externalId != null ? "externalId=" + externalId + ", " : "") +
                (image != null ? "image=" + image + ", " : "") +
                (popularity != null ? "popularity=" + popularity + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
            "}";
    }

}
