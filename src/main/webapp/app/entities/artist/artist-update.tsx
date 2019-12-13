import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './artist.reducer';
import { IArtist } from 'app/shared/model/artist.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IArtistUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IArtistUpdateState {
  isNew: boolean;
}

export class ArtistUpdate extends React.Component<IArtistUpdateProps, IArtistUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { artistEntity } = this.props;
      const entity = {
        ...artistEntity,
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
    this.props.history.push('/artist');
  };

  render() {
    const { artistEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="spotifyReaderApp.artist.home.createOrEditLabel">Create or edit a Artist</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : artistEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="artist-id">ID</Label>
                    <AvInput id="artist-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="artist-name">
                    Name
                  </Label>
                  <AvField
                    id="artist-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="followersLabel" for="artist-followers">
                    Followers
                  </Label>
                  <AvField
                    id="artist-followers"
                    type="string"
                    className="form-control"
                    name="followers"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="genresLabel" for="artist-genres">
                    Genres
                  </Label>
                  <AvField
                    id="artist-genres"
                    type="text"
                    name="genres"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="externalIdLabel" for="artist-externalId">
                    External Id
                  </Label>
                  <AvField id="artist-externalId" type="text" name="externalId" />
                </AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="artist-image">
                    Image
                  </Label>
                  <AvField id="artist-image" type="text" name="image" />
                </AvGroup>
                <AvGroup>
                  <Label id="popularityLabel" for="artist-popularity">
                    Popularity
                  </Label>
                  <AvField id="artist-popularity" type="string" className="form-control" name="popularity" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="artist-url">
                    Url
                  </Label>
                  <AvField id="artist-url" type="text" name="url" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/artist" replace color="info">
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
  artistEntity: storeState.artist.entity,
  loading: storeState.artist.loading,
  updating: storeState.artist.updating,
  updateSuccess: storeState.artist.updateSuccess
});

const mapDispatchToProps = {
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
)(ArtistUpdate);
