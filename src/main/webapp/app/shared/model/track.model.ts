import { IArtist } from 'app/shared/model/artist.model';
import { IAlbum } from 'app/shared/model/album.model';

export interface ITrack {
  id?: number;
  name?: string;
  url?: string;
  externalId?: string;
  artists?: IArtist[];
  album?: IAlbum;
}

export const defaultValue: Readonly<ITrack> = {};
