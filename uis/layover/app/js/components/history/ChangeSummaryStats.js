import React from 'react';
import ReactTable from "react-table";
import { Link } from 'react-router-dom';

import "react-table/react-table.css";

export class ChangeSummaryStats extends React.Component{
    constructor(props){
       super(props);
       this.state = {
         error: null,
         entries: [],
         total: 0,
         loading: true,
         pageSize: 25,
         page: 0
       };
    }

    componentDidMount(){
      fetchAll(this.state.page, this.state.pageSize,
        (result) => {
          this.setState({
            loading: false,
            entries: result
          });
        },
        (error) => {
          this.setState({
            loading: false,
            error
          });
        }
      );
    }

    fetchData = (state)=>{
      fetchAll(state.page, state.pageSize,
        (result) => {
          this.setState({
            total: result.total,
            entries: result.items,
            pageSize: result.pageSize,
            page: result.page
          });
        },
        (error) => {
          this.setState({
            error
          });
        }
      );
    }

    render(){
      const { loading } = this.state;
      if (loading) {
        return <div>Loading...</div>;
      } else {
        const {entries} = this.state;
        return (
          <React.Fragment>
              <h4>Repository Change History Status</h4>
              <ReactTable
                data={entries}
                columns={ [
                  {
                    "Header": "Store Key",
                    "accessor": "storeKey",
                    "Cell": s => {
                      let parts = s.value.split(":");
                      return <Link to={`/brwose/history/change/summary/by-store/${parts[0]}/${parts[1]}/${parts[2]}`}>{s.value}</Link>;
                    }
                  },
                  {
                    "Header": "Last Updated",
                    "accessor": "lastUpdate"
                  },
                  {
                    "Header": "Creates",
                    "accessor": "creates"
                  },
                  {
                    "Header": "Updates",
                    "accessor": "updates"
                  },
                  {
                    "Header": "Deletes",
                    "accessor": "deletes"
                  },
                  {
                    "Header": "Total",
                    "accessor": "total"
                  }
                ]}                               
              />
          </React.Fragment>
         );
      }
    };
}

var fetchAll = (page, pageSize, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/summary/stats?pageSize=${pageSize}&page=${page}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
