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
import { getEntity, updateEntity, createEntity, reset } from './album.reducer';
import { IAlbum } from 'app/shared/model/album.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAlbumUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAlbumUpdateState {
  isNew: boolean;
  artistsId: string;
}

export class AlbumUpdate extends React.Component<IAlbumUpdateProps, IAlbumUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      artistsId: '0',
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { albumEntity } = this.props;
      const entity = {
        ...albumEntity,
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
    this.props.history.push('/album');
  };

  render() {
    const { albumEntity, artists, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="spotifyReaderApp.album.home.createOrEditLabel">Create or edit a Album</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : albumEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="album-id">ID</Label>
                    <AvInput id="album-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="album-name">
                    Name
                  </Label>
                  <AvField
                    id="album-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="album-image">
                    Image
                  </Label>
                  <AvField id="album-image" type="text" name="image" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="album-url">
                    Url
                  </Label>
                  <AvField id="album-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="externalIdLabel" for="album-externalId">
                    External Id
                  </Label>
                  <AvField id="album-externalId" type="text" name="externalId" />
                </AvGroup>
                <AvGroup>
                  <Label id="popularityLabel" for="album-popularity">
                    Popularity
                  </Label>
                  <AvField id="album-popularity" type="string" className="form-control" name="popularity" />
                </AvGroup>
                <AvGroup>
                  <Label id="genresLabel" for="album-genres">
                    Genres
                  </Label>
                  <AvField id="album-genres" type="text" name="genres" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/album" replace color="info">
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
  albumEntity: storeState.album.entity,
  loading: storeState.album.loading,
  updating: storeState.album.updating,
  updateSuccess: storeState.album.updateSuccess
});

const mapDispatchToProps = {
  getArtists,
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
)(AlbumUpdate);
