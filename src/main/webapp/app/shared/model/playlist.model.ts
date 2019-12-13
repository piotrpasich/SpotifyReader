import { ITrack } from 'app/shared/model/track.model';

export interface IPlaylist {
  id?: number;
  name?: string;
  url?: string;
  externalId?: string;
  image?: string;
  owner?: string;
  description?: string;
  tracks?: ITrack[];
}

export const defaultValue: Readonly<IPlaylist> = {};
