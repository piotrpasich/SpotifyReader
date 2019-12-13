import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './track.reducer';
import { ITrack } from 'app/shared/model/track.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITrackDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TrackDetail extends React.Component<ITrackDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { trackEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Track [<b>{trackEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{trackEntity.name}</dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{trackEntity.url}</dd>
            <dt>
              <span id="externalId">External Id</span>
            </dt>
            <dd>{trackEntity.externalId}</dd>
            <dt>Album</dt>
            <dd>{trackEntity.album ? trackEntity.album.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/track" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/track/${trackEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ track }: IRootState) => ({
  trackEntity: track.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TrackDetail);
