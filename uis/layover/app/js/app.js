import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import TrackingIDForm from "./components/TrackingIDForm.js";
import { TrackingSummary } from "./components/TrackingSummary.js";
import { TrackingContentEntry } from "./components/TrackingContentEntry.js";
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => (
   <Router>
      <div>
         <Header />

         <Route path="/browse/history/content/tracking/" component={TrackingIDForm} />
         <Route path="/browse/history/content/tracking/:trackingID/" component={TrackingSummaryDTO} />
         <Route path="/browse/history/content/tracking/:trackingID/:type(upload|download)" component={TrackingContentEntryDTO}/>

      </div>
   </Router>
);

const Header = () => (
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">AuditQuery</a>
    <div class="collapse navbar-collapse">
      <div class="navbar-nav">
        <li class="nav-item nav-link">
          <Link to="/browse/history/content/tracking/">Tracking Records</Link>
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

ReactDOM.render(
		 <App />,
document.getElementById('router'));
