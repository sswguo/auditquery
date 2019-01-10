import React from 'react';
import ReactDOM from "react-dom";
import { withRouter } from 'react-router-dom';
import { TrackingSummary } from './TrackingSummary.js';

class TrackingIDForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {value: ''};
  }

  handleChange = (event) => {
    this.setState({value: event.target.value});
  }

  handleSubmit = (event) => {
    event.preventDefault();
    this.props.history.replace(`/browse/history/content/tracking/${this.state.value}/`);
  }

  render() {
    return (
    <React.Fragment>
      <form onSubmit={this.handleSubmit}>
        <label>
          Tracking ID:
          <input type="text" value={this.state.value} onChange={this.handleChange} />
        </label>
        <input type="submit" value="Submit" />
      </form>
    </React.Fragment>
    );
  }
}

export default withRouter(TrackingIDForm);
