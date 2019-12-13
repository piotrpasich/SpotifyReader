import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Artist from './artist';
import Album from './album';
import Track from './track';
import Playlist from './playlist';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}artist`} component={Artist} />
      <ErrorBoundaryRoute path={`${match.url}album`} component={Album} />
      <ErrorBoundaryRoute path={`${match.url}track`} component={Track} />
      <ErrorBoundaryRoute path={`${match.url}playlist`} component={Playlist} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
