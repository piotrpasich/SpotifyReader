import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITrack } from 'app/shared/model/track.model';
import { getEntities as getTracks } from 'app/entities/track/track.reducer';
import { getEntity, updateEntity, createEntity, reset } from './playlist.reducer';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlaylistUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPlaylistUpdateState {
  isNew: boolean;
  idstracks: any[];
}

export class PlaylistUpdate extends React.Component<IPlaylistUpdateProps, IPlaylistUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idstracks: [],
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getTracks();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { playlistEntity } = this.props;
      const entity = {
        ...playlistEntity,
        ...values,
        tracks: mapIdList(values.tracks)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/playlist');
  };

  render() {
    const { playlistEntity, tracks, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="spotifyReaderApp.playlist.home.createOrEditLabel">Create or edit a Playlist</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : playlistEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="playlist-id">ID</Label>
                    <AvInput id="playlist-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="playlist-name">
                    Name
                  </Label>
                  <AvField id="playlist-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="playlist-url">
                    Url
                  </Label>
                  <AvField id="playlist-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="externalIdLabel" for="playlist-externalId">
                    External Id
                  </Label>
                  <AvField id="playlist-externalId" type="text" name="externalId" />
                </AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="playlist-image">
                    Image
                  </Label>
                  <AvField id="playlist-image" type="text" name="image" />
                </AvGroup>
                <AvGroup>
                  <Label id="ownerLabel" for="playlist-owner">
                    Owner
                  </Label>
                  <AvField id="playlist-owner" type="text" name="owner" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="playlist-description">
                    Description
                  </Label>
                  <AvField id="playlist-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="playlist-tracks">Tracks</Label>
                  <AvInput
                    id="playlist-tracks"
                    type="select"
                    multiple
                    className="form-control"
                    name="tracks"
                    value={playlistEntity.tracks && playlistEntity.tracks.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {tracks
                      ? tracks.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/playlist" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  tracks: storeState.track.entities,
  playlistEntity: storeState.playlist.entity,
  loading: storeState.playlist.loading,
  updating: storeState.playlist.updating,
  updateSuccess: storeState.playlist.updateSuccess
});

const mapDispatchToProps = {
  getTracks,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PlaylistUpdate);
