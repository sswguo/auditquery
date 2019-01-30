import React from 'react';
import ReactTable from "react-table";

import "react-table/react-table.css";

export class TrackingContentEntry extends React.Component{
    constructor(props){
       super(props);
       console.log(this.props.location.search);
               let queryParams = new URLSearchParams(this.props.location.search);
       this.state = {
         error: null,
         entries: [],
         isLoaded: false,
         pageSkip: queryParams.get("p"),
         pageCount: queryParams.get("c")
       };
    }

    fetchData(){
       this.parseQuery();
       fetch(`/api/rest/history/content/tracking/
            ${this.props.trackingID}/entries?type=${this.props.type}&skip=${this.state.pageSkip}&count=${this.state.pageCount}`)
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

    parseQuery(){
        let queryParams = new URLSearchParams(this.props.location.search);
        this.setState({
            pageSkip: queryParams.get("p"),
            pageCount: queryParams.get("c")
        });
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
         Header: 'Checksum',
         accessor: 'sha256'
      },{
          Header: 'Access Channel',
          accessor: 'accessChannel'
      }, {
         Header: 'Path',
         accessor: 'path'
      }, {
         Header: 'Origin URL',
         accessor: 'originUrl'
      }, {
         Header: 'Local URL',
         accessor: 'localUrl'
      }];


      if (!isLoaded) {
         return <div>Loading...</div>;
      } else {
         return (
            <React.Fragment>
               <h4>Tracking Content Entry</h4>
               <ReactTable
                   data={entries}
                   columns={columns}
                   onPageSizeChange={(pageSize, pageIndex) => {
                        console.log(pageIndex + "|" + pageSize)
                        // TODO update location or just refresh the table
                   }}
               />
            </React.Fragment>
         );
      }
    };
}
