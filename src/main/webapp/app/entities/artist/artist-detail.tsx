import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './artist.reducer';
import { IArtist } from 'app/shared/model/artist.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IArtistDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ArtistDetail extends React.Component<IArtistDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { artistEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Artist [<b>{artistEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{artistEntity.name}</dd>
            <dt>
              <span id="followers">Followers</span>
            </dt>
            <dd>{artistEntity.followers}</dd>
            <dt>
              <span id="genres">Genres</span>
            </dt>
            <dd>{artistEntity.genres}</dd>
            <dt>
              <span id="externalId">External Id</span>
            </dt>
            <dd>{artistEntity.externalId}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>{artistEntity.image}</dd>
            <dt>
              <span id="popularity">Popularity</span>
            </dt>
            <dd>{artistEntity.popularity}</dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{artistEntity.url}</dd>
          </dl>
          <Button tag={Link} to="/artist" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/artist/${artistEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ artist }: IRootState) => ({
  artistEntity: artist.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArtistDetail);
