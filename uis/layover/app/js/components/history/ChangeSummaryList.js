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
import React from 'react';
import ReactTable from "react-table";
import { Link } from 'react-router-dom';

import "react-table/react-table.css";

export class ChangeSummaryList extends React.Component{
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

    componentWillMount(){
      let {packageType, type, name} = this.props;
      fetchAll(packageType, type, name, this.state.page, this.state.pageSize,
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
      const {packageType, type, name} = this.props;
      fetchAll(packageType, type, name, state.page, state.pageSize,
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
        const {packageType, type, name} = this.props;
        return (
          <React.Fragment>
              <h4>Repository Change History Summary for {`${packageType}:${type}:${name}`}</h4>
              <ReactTable
                manual
                data={entries}
                columns={ [
                  {
                    "Header": "Event Id",
                    "accessor": "eventId",
                    "Cell": e => <Link to={`/browse/history/change/event/${e.value}`}>{e.value}</Link>
                  },                  
                  {
                    "Header": "Change Time",
                    "accessor": "changeTime"
                  },
                  {
                    "Header": "User",
                    "accessor": "user"
                  },
                  {
                    "Header": "Summary",
                    "accessor": "summary"
                  }
                ]}
                loading={loading}
                showPagination={true}
                showPaginationTop={false}
                showPaginationBottom={true}
                defaultPageSize={pageSize}
                pageSizeOptions={[10, 25, 50, 100]}
                pages={totalPage}
                onFetchData={state => this.fetchData(state)}
              />
          </React.Fragment>
         );
      }
    };
}

var fetchAll = (packageType, type, name, page, pageSize, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/summary/by-store/${packageType}/${type}/${name}?pageSize=${pageSize}&page=${page}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
