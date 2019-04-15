import React from 'react';
import ReactTable from "react-table";

import "react-table/react-table.css";

export class ChangelogList extends React.Component{
    constructor(props){
       super(props);
       this.state = {
         error: null,
         entries: [],
         isLoaded: false,
         pageMax: 20,
         pageStart: 0
       };
    }

    fetchData(){
       fetch(`/api/rest/repository/changelog/all?pageMax=${this.state.pageMax}&pageStart=${this.state.pageStart}`)
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

      const columns = [
         {
           "Header": "Store Key",
           "accessor": "storeKey"
         },
         {
           "Header": "Change Time",
           "accessor": "changeTime"
         },
         {
           "Header": "Version",
           "accessor": "version"
         },
         {
           "Header": "Summary",
           "accessor": "summary"
         },
         {
           "Header": "Change Type",
           "accessor": "changeType"
         },
         {
           "Header": "User",
           "accessor": "user"
         },
         {
           "Header": "Diff",
           "accessor": "diffContent"
         }
       ];


      if (!isLoaded) {
         return <div>Loading...</div>;
      } else {
         return (
            <React.Fragment>
               <h4>Repository Change Log History</h4>
               <ReactTable
                   data={entries}
                   columns={columns}
                   onPageSizeChange={(pageSize, pageIndex) => {
                        // console.log(pageIndex + "|" + pageSize)
                        // TODO update location or just refresh the table
                   }}
               />
            </React.Fragment>
         );
      }
    };
}
