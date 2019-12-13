package com.papi.spotifyreader.web.rest;

import com.papi.spotifyreader.SpotifyReaderApp;
import com.papi.spotifyreader.domain.Artist;
import com.papi.spotifyreader.repository.ArtistRepository;
import com.papi.spotifyreader.service.ArtistService;
import com.papi.spotifyreader.web.rest.errors.ExceptionTranslator;
import com.papi.spotifyreader.service.dto.ArtistCriteria;
import com.papi.spotifyreader.service.ArtistQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.papi.spotifyreader.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ArtistResource} REST controller.
 */
@SpringBootTest(classes = SpotifyReaderApp.class)
public class ArtistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_FOLLOWERS = 1;
    private static final Integer UPDATED_FOLLOWERS = 2;
    private static final Integer SMALLER_FOLLOWERS = 1 - 1;

    private static final String DEFAULT_GENRES = "AAAAAAAAAA";
    private static final String UPDATED_GENRES = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POPULARITY = 1;
    private static final Integer UPDATED_POPULARITY = 2;
    private static final Integer SMALLER_POPULARITY = 1 - 1;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistQueryService artistQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restArtistMockMvc;

    private Artist artist;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtistResource artistResource = new ArtistResource(artistService, artistQueryService);
        this.restArtistMockMvc = MockMvcBuilders.standaloneSetup(artistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createEntity(EntityManager em) {
        Artist artist = new Artist()
            .name(DEFAULT_NAME)
            .followers(DEFAULT_FOLLOWERS)
            .genres(DEFAULT_GENRES)
            .externalId(DEFAULT_EXTERNAL_ID)
            .image(DEFAULT_IMAGE)
            .popularity(DEFAULT_POPULARITY)
            .url(DEFAULT_URL);
        return artist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createUpdatedEntity(EntityManager em) {
        Artist artist = new Artist()
            .name(UPDATED_NAME)
            .followers(UPDATED_FOLLOWERS)
            .genres(UPDATED_GENRES)
            .externalId(UPDATED_EXTERNAL_ID)
            .image(UPDATED_IMAGE)
            .popularity(UPDATED_POPULARITY)
            .url(UPDATED_URL);
        return artist;
    }

    @BeforeEach
    public void initTest() {
        artist = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist
        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArtist.getFollowers()).isEqualTo(DEFAULT_FOLLOWERS);
        assertThat(testArtist.getGenres()).isEqualTo(DEFAULT_GENRES);
        assertThat(testArtist.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testArtist.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testArtist.getPopularity()).isEqualTo(DEFAULT_POPULARITY);
        assertThat(testArtist.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createArtistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist with an existing ID
        artist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setName(null);

        // Create the Artist, which fails.

        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isBadRequest());

        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFollowersIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setFollowers(null);

        // Create the Artist, which fails.

        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isBadRequest());

        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenresIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setGenres(null);

        // Create the Artist, which fails.

        restArtistMockMvc.perform(post("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isBadRequest());

        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].followers").value(hasItem(DEFAULT_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }
    
    @Test
    @Transactional
    public void getArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.followers").value(DEFAULT_FOLLOWERS))
            .andExpect(jsonPath("$.genres").value(DEFAULT_GENRES))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.popularity").value(DEFAULT_POPULARITY))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getArtistsByIdFiltering() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        Long id = artist.getId();

        defaultArtistShouldBeFound("id.equals=" + id);
        defaultArtistShouldNotBeFound("id.notEquals=" + id);

        defaultArtistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArtistShouldNotBeFound("id.greaterThan=" + id);

        defaultArtistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArtistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArtistsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name equals to DEFAULT_NAME
        defaultArtistShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the artistList where name equals to UPDATED_NAME
        defaultArtistShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllArtistsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name not equals to DEFAULT_NAME
        defaultArtistShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the artistList where name not equals to UPDATED_NAME
        defaultArtistShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllArtistsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name in DEFAULT_NAME or UPDATED_NAME
        defaultArtistShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the artistList where name equals to UPDATED_NAME
        defaultArtistShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllArtistsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name is not null
        defaultArtistShouldBeFound("name.specified=true");

        // Get all the artistList where name is null
        defaultArtistShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllArtistsByNameContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name contains DEFAULT_NAME
        defaultArtistShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the artistList where name contains UPDATED_NAME
        defaultArtistShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllArtistsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where name does not contain DEFAULT_NAME
        defaultArtistShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the artistList where name does not contain UPDATED_NAME
        defaultArtistShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllArtistsByFollowersIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers equals to DEFAULT_FOLLOWERS
        defaultArtistShouldBeFound("followers.equals=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers equals to UPDATED_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.equals=" + UPDATED_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers not equals to DEFAULT_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.notEquals=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers not equals to UPDATED_FOLLOWERS
        defaultArtistShouldBeFound("followers.notEquals=" + UPDATED_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers in DEFAULT_FOLLOWERS or UPDATED_FOLLOWERS
        defaultArtistShouldBeFound("followers.in=" + DEFAULT_FOLLOWERS + "," + UPDATED_FOLLOWERS);

        // Get all the artistList where followers equals to UPDATED_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.in=" + UPDATED_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers is not null
        defaultArtistShouldBeFound("followers.specified=true");

        // Get all the artistList where followers is null
        defaultArtistShouldNotBeFound("followers.specified=false");
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers is greater than or equal to DEFAULT_FOLLOWERS
        defaultArtistShouldBeFound("followers.greaterThanOrEqual=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers is greater than or equal to UPDATED_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.greaterThanOrEqual=" + UPDATED_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers is less than or equal to DEFAULT_FOLLOWERS
        defaultArtistShouldBeFound("followers.lessThanOrEqual=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers is less than or equal to SMALLER_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.lessThanOrEqual=" + SMALLER_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsLessThanSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers is less than DEFAULT_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.lessThan=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers is less than UPDATED_FOLLOWERS
        defaultArtistShouldBeFound("followers.lessThan=" + UPDATED_FOLLOWERS);
    }

    @Test
    @Transactional
    public void getAllArtistsByFollowersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where followers is greater than DEFAULT_FOLLOWERS
        defaultArtistShouldNotBeFound("followers.greaterThan=" + DEFAULT_FOLLOWERS);

        // Get all the artistList where followers is greater than SMALLER_FOLLOWERS
        defaultArtistShouldBeFound("followers.greaterThan=" + SMALLER_FOLLOWERS);
    }


    @Test
    @Transactional
    public void getAllArtistsByGenresIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres equals to DEFAULT_GENRES
        defaultArtistShouldBeFound("genres.equals=" + DEFAULT_GENRES);

        // Get all the artistList where genres equals to UPDATED_GENRES
        defaultArtistShouldNotBeFound("genres.equals=" + UPDATED_GENRES);
    }

    @Test
    @Transactional
    public void getAllArtistsByGenresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres not equals to DEFAULT_GENRES
        defaultArtistShouldNotBeFound("genres.notEquals=" + DEFAULT_GENRES);

        // Get all the artistList where genres not equals to UPDATED_GENRES
        defaultArtistShouldBeFound("genres.notEquals=" + UPDATED_GENRES);
    }

    @Test
    @Transactional
    public void getAllArtistsByGenresIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres in DEFAULT_GENRES or UPDATED_GENRES
        defaultArtistShouldBeFound("genres.in=" + DEFAULT_GENRES + "," + UPDATED_GENRES);

        // Get all the artistList where genres equals to UPDATED_GENRES
        defaultArtistShouldNotBeFound("genres.in=" + UPDATED_GENRES);
    }

    @Test
    @Transactional
    public void getAllArtistsByGenresIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres is not null
        defaultArtistShouldBeFound("genres.specified=true");

        // Get all the artistList where genres is null
        defaultArtistShouldNotBeFound("genres.specified=false");
    }
                @Test
    @Transactional
    public void getAllArtistsByGenresContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres contains DEFAULT_GENRES
        defaultArtistShouldBeFound("genres.contains=" + DEFAULT_GENRES);

        // Get all the artistList where genres contains UPDATED_GENRES
        defaultArtistShouldNotBeFound("genres.contains=" + UPDATED_GENRES);
    }

    @Test
    @Transactional
    public void getAllArtistsByGenresNotContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where genres does not contain DEFAULT_GENRES
        defaultArtistShouldNotBeFound("genres.doesNotContain=" + DEFAULT_GENRES);

        // Get all the artistList where genres does not contain UPDATED_GENRES
        defaultArtistShouldBeFound("genres.doesNotContain=" + UPDATED_GENRES);
    }


    @Test
    @Transactional
    public void getAllArtistsByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId equals to DEFAULT_EXTERNAL_ID
        defaultArtistShouldBeFound("externalId.equals=" + DEFAULT_EXTERNAL_ID);

        // Get all the artistList where externalId equals to UPDATED_EXTERNAL_ID
        defaultArtistShouldNotBeFound("externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllArtistsByExternalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId not equals to DEFAULT_EXTERNAL_ID
        defaultArtistShouldNotBeFound("externalId.notEquals=" + DEFAULT_EXTERNAL_ID);

        // Get all the artistList where externalId not equals to UPDATED_EXTERNAL_ID
        defaultArtistShouldBeFound("externalId.notEquals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllArtistsByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId in DEFAULT_EXTERNAL_ID or UPDATED_EXTERNAL_ID
        defaultArtistShouldBeFound("externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID);

        // Get all the artistList where externalId equals to UPDATED_EXTERNAL_ID
        defaultArtistShouldNotBeFound("externalId.in=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllArtistsByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId is not null
        defaultArtistShouldBeFound("externalId.specified=true");

        // Get all the artistList where externalId is null
        defaultArtistShouldNotBeFound("externalId.specified=false");
    }
                @Test
    @Transactional
    public void getAllArtistsByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId contains DEFAULT_EXTERNAL_ID
        defaultArtistShouldBeFound("externalId.contains=" + DEFAULT_EXTERNAL_ID);

        // Get all the artistList where externalId contains UPDATED_EXTERNAL_ID
        defaultArtistShouldNotBeFound("externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    public void getAllArtistsByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where externalId does not contain DEFAULT_EXTERNAL_ID
        defaultArtistShouldNotBeFound("externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);

        // Get all the artistList where externalId does not contain UPDATED_EXTERNAL_ID
        defaultArtistShouldBeFound("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID);
    }


    @Test
    @Transactional
    public void getAllArtistsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image equals to DEFAULT_IMAGE
        defaultArtistShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the artistList where image equals to UPDATED_IMAGE
        defaultArtistShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArtistsByImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image not equals to DEFAULT_IMAGE
        defaultArtistShouldNotBeFound("image.notEquals=" + DEFAULT_IMAGE);

        // Get all the artistList where image not equals to UPDATED_IMAGE
        defaultArtistShouldBeFound("image.notEquals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArtistsByImageIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultArtistShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the artistList where image equals to UPDATED_IMAGE
        defaultArtistShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArtistsByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image is not null
        defaultArtistShouldBeFound("image.specified=true");

        // Get all the artistList where image is null
        defaultArtistShouldNotBeFound("image.specified=false");
    }
                @Test
    @Transactional
    public void getAllArtistsByImageContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image contains DEFAULT_IMAGE
        defaultArtistShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the artistList where image contains UPDATED_IMAGE
        defaultArtistShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArtistsByImageNotContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where image does not contain DEFAULT_IMAGE
        defaultArtistShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the artistList where image does not contain UPDATED_IMAGE
        defaultArtistShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }


    @Test
    @Transactional
    public void getAllArtistsByPopularityIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity equals to DEFAULT_POPULARITY
        defaultArtistShouldBeFound("popularity.equals=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity equals to UPDATED_POPULARITY
        defaultArtistShouldNotBeFound("popularity.equals=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity not equals to DEFAULT_POPULARITY
        defaultArtistShouldNotBeFound("popularity.notEquals=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity not equals to UPDATED_POPULARITY
        defaultArtistShouldBeFound("popularity.notEquals=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity in DEFAULT_POPULARITY or UPDATED_POPULARITY
        defaultArtistShouldBeFound("popularity.in=" + DEFAULT_POPULARITY + "," + UPDATED_POPULARITY);

        // Get all the artistList where popularity equals to UPDATED_POPULARITY
        defaultArtistShouldNotBeFound("popularity.in=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity is not null
        defaultArtistShouldBeFound("popularity.specified=true");

        // Get all the artistList where popularity is null
        defaultArtistShouldNotBeFound("popularity.specified=false");
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity is greater than or equal to DEFAULT_POPULARITY
        defaultArtistShouldBeFound("popularity.greaterThanOrEqual=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity is greater than or equal to UPDATED_POPULARITY
        defaultArtistShouldNotBeFound("popularity.greaterThanOrEqual=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity is less than or equal to DEFAULT_POPULARITY
        defaultArtistShouldBeFound("popularity.lessThanOrEqual=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity is less than or equal to SMALLER_POPULARITY
        defaultArtistShouldNotBeFound("popularity.lessThanOrEqual=" + SMALLER_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsLessThanSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity is less than DEFAULT_POPULARITY
        defaultArtistShouldNotBeFound("popularity.lessThan=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity is less than UPDATED_POPULARITY
        defaultArtistShouldBeFound("popularity.lessThan=" + UPDATED_POPULARITY);
    }

    @Test
    @Transactional
    public void getAllArtistsByPopularityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where popularity is greater than DEFAULT_POPULARITY
        defaultArtistShouldNotBeFound("popularity.greaterThan=" + DEFAULT_POPULARITY);

        // Get all the artistList where popularity is greater than SMALLER_POPULARITY
        defaultArtistShouldBeFound("popularity.greaterThan=" + SMALLER_POPULARITY);
    }


    @Test
    @Transactional
    public void getAllArtistsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url equals to DEFAULT_URL
        defaultArtistShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the artistList where url equals to UPDATED_URL
        defaultArtistShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArtistsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url not equals to DEFAULT_URL
        defaultArtistShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the artistList where url not equals to UPDATED_URL
        defaultArtistShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArtistsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url in DEFAULT_URL or UPDATED_URL
        defaultArtistShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the artistList where url equals to UPDATED_URL
        defaultArtistShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArtistsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url is not null
        defaultArtistShouldBeFound("url.specified=true");

        // Get all the artistList where url is null
        defaultArtistShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllArtistsByUrlContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url contains DEFAULT_URL
        defaultArtistShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the artistList where url contains UPDATED_URL
        defaultArtistShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArtistsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artistList where url does not contain DEFAULT_URL
        defaultArtistShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the artistList where url does not contain UPDATED_URL
        defaultArtistShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArtistShouldBeFound(String filter) throws Exception {
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].followers").value(hasItem(DEFAULT_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].popularity").value(hasItem(DEFAULT_POPULARITY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restArtistMockMvc.perform(get("/api/artists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArtistShouldNotBeFound(String filter) throws Exception {
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArtistMockMvc.perform(get("/api/artists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtist() throws Exception {
        // Initialize the database
        artistService.save(artist);

        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        Artist updatedArtist = artistRepository.findById(artist.getId()).get();
        // Disconnect from session so that the updates on updatedArtist are not directly saved in db
        em.detach(updatedArtist);
        updatedArtist
            .name(UPDATED_NAME)
            .followers(UPDATED_FOLLOWERS)
            .genres(UPDATED_GENRES)
            .externalId(UPDATED_EXTERNAL_ID)
            .image(UPDATED_IMAGE)
            .popularity(UPDATED_POPULARITY)
            .url(UPDATED_URL);

        restArtistMockMvc.perform(put("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtist)))
            .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artistList.get(artistList.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArtist.getFollowers()).isEqualTo(UPDATED_FOLLOWERS);
        assertThat(testArtist.getGenres()).isEqualTo(UPDATED_GENRES);
        assertThat(testArtist.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testArtist.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testArtist.getPopularity()).isEqualTo(UPDATED_POPULARITY);
        assertThat(testArtist.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingArtist() throws Exception {
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Create the Artist

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtistMockMvc.perform(put("/api/artists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artist)))
            .andExpect(status().isBadRequest());

        // Validate the Artist in the database
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtist() throws Exception {
        // Initialize the database
        artistService.save(artist);

        int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Delete the artist
        restArtistMockMvc.perform(delete("/api/artists/{id}", artist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artist> artistList = artistRepository.findAll();
        assertThat(artistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
