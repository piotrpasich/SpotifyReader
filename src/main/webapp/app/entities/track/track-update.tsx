import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IArtist } from 'app/shared/model/artist.model';
import { getEntities as getArtists } from 'app/entities/artist/artist.reducer';
import { IAlbum } from 'app/shared/model/album.model';
import { getEntities as getAlbums } from 'app/entities/album/album.reducer';
import { getEntity, updateEntity, createEntity, reset } from './track.reducer';
import { ITrack } from 'app/shared/model/track.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITrackUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITrackUpdateState {
  isNew: boolean;
  artistsId: string;
  albumId: string;
}

export class TrackUpdate extends React.Component<ITrackUpdateProps, ITrackUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      artistsId: '0',
      albumId: '0',
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

    this.props.getArtists();
    this.props.getAlbums();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { trackEntity } = this.props;
      const entity = {
        ...trackEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/track');
  };

  render() {
    const { trackEntity, artists, albums, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="spotifyReaderApp.track.home.createOrEditLabel">Create or edit a Track</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : trackEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="track-id">ID</Label>
                    <AvInput id="track-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="track-name">
                    Name
                  </Label>
                  <AvField
                    id="track-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="track-url">
                    Url
                  </Label>
                  <AvField id="track-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="externalIdLabel" for="track-externalId">
                    External Id
                  </Label>
                  <AvField id="track-externalId" type="text" name="externalId" />
                </AvGroup>
                <AvGroup>
                  <Label for="track-album">Album</Label>
                  <AvInput id="track-album" type="select" className="form-control" name="album.id">
                    <option value="" key="0" />
                    {albums
                      ? albums.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/track" replace color="info">
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
  artists: storeState.artist.entities,
  albums: storeState.album.entities,
  trackEntity: storeState.track.entity,
  loading: storeState.track.loading,
  updating: storeState.track.updating,
  updateSuccess: storeState.track.updateSuccess
});

const mapDispatchToProps = {
  getArtists,
  getAlbums,
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
)(TrackUpdate);
