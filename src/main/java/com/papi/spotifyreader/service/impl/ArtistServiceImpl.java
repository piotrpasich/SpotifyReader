package com.papi.spotifyreader.service.impl;

import com.papi.spotifyreader.service.ArtistService;
import com.papi.spotifyreader.domain.Artist;
import com.papi.spotifyreader.repository.ArtistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Artist}.
 */
@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final Logger log = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Save a artist.
     *
     * @param artist the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Artist save(Artist artist) {
        log.debug("Request to save Artist : {}", artist);
        return artistRepository.save(artist);
    }

    /**
     * Get all the artists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Artist> findAll(Pageable pageable) {
        log.debug("Request to get all Artists");
        return artistRepository.findAll(pageable);
    }


    /**
     * Get one artist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Artist> findOne(Long id) {
        log.debug("Request to get Artist : {}", id);
        return artistRepository.findById(id);
    }

    /**
     * Delete the artist by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Artist : {}", id);
        artistRepository.deleteById(id);
    }
}
