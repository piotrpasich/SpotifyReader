package com.papi.spotifyreader.service;

import com.papi.spotifyreader.domain.Artist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Artist}.
 */
public interface ArtistService {

    /**
     * Save a artist.
     *
     * @param artist the entity to save.
     * @return the persisted entity.
     */
    Artist save(Artist artist);

    /**
     * Get all the artists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Artist> findAll(Pageable pageable);


    /**
     * Get the "id" artist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Artist> findOne(Long id);

    /**
     * Delete the "id" artist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
