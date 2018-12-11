import React from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import ReactTable from "react-table";

import "react-table/react-table.css";

export class TrackingContentEntry extends React.Component{
    constructor(props){
       super(props);
       this.state = {
       error: null,
         entries: [],
         isLoaded: false
       };
       this.fetchData = this.fetchData.bind(this);
    }

    fetchData(){
       fetch("/api/rest/history/content/tracking/" +
            this.props.trackingID +
               "/entries?type=" +
                    this.props.type)
           .then(function(response) {
              return response.json();
           })
           .then(
              (result) => {
                 this.setState({
                    isLoaded: true,
                    entries: result
                 });
              },
              (error) => {
                 this.setState({
                   isLoaded: true,
                   error
                 });
              }
           )
    }

    componentDidMount(){
        this.fetchData();
    }

    componentDidUpdate(prevProps) {
      if (this.props.type !== prevProps.type) {
        this.fetchData();
      }
    }

    render(){
      const { isLoaded, entries } = this.state;

      const columns = [{
          Header: 'Store Key',
          accessor: 'storeKey'
      }, {
          Header: 'Access Channel',
          accessor: 'accessChannel'
      }, {
         Header: 'Path',
         accessor: 'path'
      }];


      if (!isLoaded) {
         return <div>Loading...</div>;
      } else {
         return (
            <div>
               <h4>Tracking Content Entry</h4>
               <ReactTable
                   data={entries}
                   columns={columns}
               />
            </div>
         );
      }
    };
}
