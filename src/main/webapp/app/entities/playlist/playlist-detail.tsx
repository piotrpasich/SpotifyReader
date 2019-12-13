import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './playlist.reducer';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlaylistDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PlaylistDetail extends React.Component<IPlaylistDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { playlistEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Playlist [<b>{playlistEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{playlistEntity.name}</dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{playlistEntity.url}</dd>
            <dt>
              <span id="externalId">External Id</span>
            </dt>
            <dd>{playlistEntity.externalId}</dd>
            <dt>
              <span id="image">Image</span>
            </dt>
            <dd>{playlistEntity.image}</dd>
            <dt>
              <span id="owner">Owner</span>
            </dt>
            <dd>{playlistEntity.owner}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{playlistEntity.description}</dd>
            <dt>Tracks</dt>
            <dd>
              {playlistEntity.tracks
                ? playlistEntity.tracks.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.name}</a>
                      {i === playlistEntity.tracks.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/playlist" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/playlist/${playlistEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ playlist }: IRootState) => ({
  playlistEntity: playlist.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PlaylistDetail);
