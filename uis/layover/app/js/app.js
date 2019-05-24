import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import TrackingIDForm from "./components/TrackingIDForm.js";
import { TrackingSummary } from "./components/TrackingSummary.js";
import { TrackingContentEntry } from "./components/TrackingContentEntry.js";
import {ChangelogList} from "./components/changelog/ChangelogList.js";
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => (
   <Router>
      <div>
        <Header />

        <Route path="/browse/history/content/tracking/" component={TrackingIDForm} />
        <Route path="/browse/history/content/tracking/:trackingID/" component={TrackingSummaryDTO} />
        <Route path="/browse/history/content/tracking/:trackingID/:type(upload|download)" component={TrackingContentEntryDTO}/>

        <Route path="/brwose/repository/changelog/" component={ChlogList} />

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
          <Link to="/brwose/repository/changelog/">Repository Change History</Link>
        </li>
      </div>
    </div>
  </nav>
);

const TrackingSummaryDTO = ({ match }) => (
   <TrackingSummary trackingID={match.params.trackingID} />
);

const TrackingContentEntryDTO = ({ match }) => (
    <TrackingContentEntry trackingID={match.params.trackingID} type={match.params.type} location={location}/>
);

const ChlogList = ({ match }) => (
  <ChangelogList />
)

ReactDOM.render(
		 <App />,
document.getElementById('router'));
