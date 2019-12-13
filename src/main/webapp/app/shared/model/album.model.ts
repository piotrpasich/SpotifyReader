import { IArtist } from 'app/shared/model/artist.model';

export interface IAlbum {
  id?: number;
  name?: string;
  image?: string;
  url?: string;
  externalId?: string;
  popularity?: number;
  genres?: string;
  artists?: IArtist[];
}

export const defaultValue: Readonly<IAlbum> = {};
