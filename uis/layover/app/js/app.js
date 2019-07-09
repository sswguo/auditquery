/*
 * Copyright (C) 2018 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import TrackingIDForm from "./components/TrackingIDForm.js";
import { TrackingSummary } from "./components/TrackingSummary.js";
import { TrackingContentEntry } from "./components/TrackingContentEntry.js";
import {ChangeSummaryStats} from "./components/history/ChangeSummaryStats.js";
import {ChangeSummaryList} from "./components/history/ChangeSummaryList.js";
import {ChangeEventView} from "./components/history/ChangeEventView.js";
import 'bootstrap/dist/css/bootstrap.min.css';
import { withRouter } from 'react-router-dom'

const App = () => (
   <Router>
      <div>
        <Header />

        <Route path="/browse/history/content/tracking/" component={TrackingIDForm} />
        <Route path="/browse/history/content/tracking/:trackingID/" component={TrackingSummaryDTO} />
        <Route path="/browse/history/content/tracking/:trackingID/:type(upload|download)" component={TrackingContentEntryDTO}/>

        <Route path="/browse/history/change/summary/stats" component={ChgSumStats} />
        <Route path="/browse/history/change/summary/by-store/:packageType/:type/:name" component={ChgSumList} />
        <Route path="/browse/history/change/event/:eventId" component={ChgEvt} />

      </div>
   </Router>
);

const Header = () => (
  <nav className="navbar navbar-expand-lg navbar-light bg-light">
    <a className="navbar-brand" href="#">AuditQuery</a>
    <div className="collapse navbar-collapse">
      <div className="navbar-nav">
        <li className="nav-item nav-link">
          <Link to="/browse/history/content/tracking/">Tracking Records</Link>
          { " | " }
          <Link to="/browse/history/change/summary/stats">Repository Change History States</Link>
        </li>
      </div>
    </div>
  </nav>
);

const TrackingSummaryDTO = ({ match }) => (
   <TrackingSummary trackingID={match.params.trackingID} />
);

const TrackingContentEntryDTO = withRouter(({ match, history }) => (
    <TrackingContentEntry trackingID={match.params.trackingID} type={match.params.type} location={location} history={history}/>
));

const ChgSumStats = ({ match }) => (
  <ChangeSummaryStats />
)

const ChgSumList = ({ match }) => (
  <ChangeSummaryList packageType={match.params.packageType} type={match.params.type} name={match.params.name}/>
)

const ChgEvt = ({ match }) => (
  <ChangeEventView eventId={match.params.eventId}/>
)

ReactDOM.render(
		 <App />,
document.getElementById('router'));
