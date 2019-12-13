export interface IArtist {
  id?: number;
  name?: string;
  followers?: number;
  genres?: string;
  externalId?: string;
  image?: string;
  popularity?: number;
  url?: string;
}

export const defaultValue: Readonly<IArtist> = {};
