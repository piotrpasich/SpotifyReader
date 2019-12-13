import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './artist.reducer';
import { IArtist } from 'app/shared/model/artist.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IArtistProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IArtistState = IPaginationBaseState;

export class Artist extends React.Component<IArtistProps, IArtistState> {
  state: IArtistState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { artistList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="artist-heading">
          Artists
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Artist
          </Link>
        </h2>
        <div className="table-responsive">
          {artistList && artistList.length > 0 ? (
            <Table responsive aria-describedby="artist-heading">
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('followers')}>
                    Followers <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('genres')}>
                    Genres <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('externalId')}>
                    External Id <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('image')}>
                    Image <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('popularity')}>
                    Popularity <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('url')}>
                    Url <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {artistList.map((artist, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${artist.id}`} color="link" size="sm">
                        {artist.id}
                      </Button>
                    </td>
                    <td>{artist.name}</td>
                    <td>{artist.followers}</td>
                    <td>{artist.genres}</td>
                    <td>{artist.externalId}</td>
                    <td>{artist.image}</td>
                    <td>{artist.popularity}</td>
                    <td>{artist.url}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${artist.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${artist.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${artist.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Artists found</div>
          )}
        </div>
        <div className={artistList && artistList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={this.state.activePage} total={totalItems} itemsPerPage={this.state.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={this.state.activePage}
              onSelect={this.handlePagination}
              maxButtons={5}
              itemsPerPage={this.state.itemsPerPage}
              totalItems={this.props.totalItems}
            />
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ artist }: IRootState) => ({
  artistList: artist.entities,
  totalItems: artist.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Artist);
