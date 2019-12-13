import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './album.reducer';
import { IAlbum } from 'app/shared/model/album.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAlbumDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AlbumDetail extends React.Component<IAlbumDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { albumEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Album [<b>{albumEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{albumEntity.name}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>{albumEntity.image}</dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{albumEntity.url}</dd>
            <dt>
              <span id="externalId">External Id</span>
            </dt>
            <dd>{albumEntity.externalId}</dd>
            <dt>
              <span id="popularity">Popularity</span>
            </dt>
            <dd>{albumEntity.popularity}</dd>
            <dt>
              <span id="genres">Genres</span>
            </dt>
            <dd>{albumEntity.genres}</dd>
          </dl>
          <Button tag={Link} to="/album" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/album/${albumEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ album }: IRootState) => ({
  albumEntity: album.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AlbumDetail);
