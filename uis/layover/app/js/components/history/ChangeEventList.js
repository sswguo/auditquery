import React from 'react';
import ReactTable from "react-table";

import "react-table/react-table.css";

export class ChangeEventList extends React.Component{
    constructor(props){
       super(props);
       this.state = {
         error: null,
         entries: [],
         total: 0,
         loading: true,
         pageSize: 20,
         page: 0
       };
    }

    componentWillMount(){
        fetchAll(this.state.page, this.state.pageSize,
          (result) => {
            this.setState({
              loading: false,
              total: result.total,
              entries: result.items,
              pageSize: result.pageSize,
              page: result.page
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
        const {entries, total, pageSize } = this.state;
        let totalPage = total/pageSize;
        if(total % pageSize > 0){
          totalPage = Math.floor(totalPage) +1;
        }
        // console.log(`total: ${total}, totalPage: ${totalPage}, pageSize:${pageSize}`)
        return (
          <React.Fragment>
              <h4>Repository Change History</h4>
              <ReactTable
                manual
                data={entries}
                columns={ [
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
                ]}
                loading={loading}
                showPagination={true}
                showPaginationTop={false}
                showPaginationBottom={true}
                defaultPageSize={pageSize}
                pageSizeOptions={[10, 20, 50, 100]}
                pages={totalPage}
                onFetchData={state => this.fetchData(state)}
              />
          </React.Fragment>
         );
      }
    };
}

var fetchAll = (page, pageSize, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/all?pageSize=${pageSize}&page=${page}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
