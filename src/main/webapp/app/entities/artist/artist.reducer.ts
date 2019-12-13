import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IArtist, defaultValue } from 'app/shared/model/artist.model';

export const ACTION_TYPES = {
  FETCH_ARTIST_LIST: 'artist/FETCH_ARTIST_LIST',
  FETCH_ARTIST: 'artist/FETCH_ARTIST',
  CREATE_ARTIST: 'artist/CREATE_ARTIST',
  UPDATE_ARTIST: 'artist/UPDATE_ARTIST',
  DELETE_ARTIST: 'artist/DELETE_ARTIST',
  RESET: 'artist/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IArtist>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ArtistState = Readonly<typeof initialState>;

// Reducer

export default (state: ArtistState = initialState, action): ArtistState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ARTIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ARTIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ARTIST):
    case REQUEST(ACTION_TYPES.UPDATE_ARTIST):
    case REQUEST(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ARTIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ARTIST):
    case FAILURE(ACTION_TYPES.CREATE_ARTIST):
    case FAILURE(ACTION_TYPES.UPDATE_ARTIST):
    case FAILURE(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARTIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARTIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ARTIST):
    case SUCCESS(ACTION_TYPES.UPDATE_ARTIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ARTIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/artists';

// Actions

export const getEntities: ICrudGetAllAction<IArtist> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ARTIST_LIST,
    payload: axios.get<IArtist>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IArtist> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ARTIST,
    payload: axios.get<IArtist>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IArtist> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ARTIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IArtist> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ARTIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IArtist> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ARTIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
