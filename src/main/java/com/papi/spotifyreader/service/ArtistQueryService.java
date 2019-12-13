package com.papi.spotifyreader.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.papi.spotifyreader.domain.Artist;
import com.papi.spotifyreader.domain.*; // for static metamodels
import com.papi.spotifyreader.repository.ArtistRepository;
import com.papi.spotifyreader.service.dto.ArtistCriteria;

/**
 * Service for executing complex queries for {@link Artist} entities in the database.
 * The main input is a {@link ArtistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Artist} or a {@link Page} of {@link Artist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArtistQueryService extends QueryService<Artist> {

    private final Logger log = LoggerFactory.getLogger(ArtistQueryService.class);

    private final ArtistRepository artistRepository;

    public ArtistQueryService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Return a {@link List} of {@link Artist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Artist> findByCriteria(ArtistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Artist> specification = createSpecification(criteria);
        return artistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Artist} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Artist> findByCriteria(ArtistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Artist> specification = createSpecification(criteria);
        return artistRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArtistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Artist> specification = createSpecification(criteria);
        return artistRepository.count(specification);
    }

    /**
     * Function to convert {@link ArtistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Artist> createSpecification(ArtistCriteria criteria) {
        Specification<Artist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Artist_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Artist_.name));
            }
            if (criteria.getFollowers() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFollowers(), Artist_.followers));
            }
            if (criteria.getGenres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenres(), Artist_.genres));
            }
            if (criteria.getExternalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalId(), Artist_.externalId));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Artist_.image));
            }
            if (criteria.getPopularity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPopularity(), Artist_.popularity));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Artist_.url));
            }
        }
        return specification;
    }
}
