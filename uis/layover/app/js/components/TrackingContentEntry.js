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

import "react-table/react-table.css";

export class TrackingContentEntry extends React.Component{
    constructor(props){
        super(props);
        let queryParams = new URLSearchParams(this.props.location.search);
        this.state = {
            error: null,
            entries: [],
            isLoaded: false,
            page: parseInt(queryParams.get("p")),
            pageCount: parseInt(queryParams.get("c"))
        };
    }

    fetchData(){

        const { location, trackingID, type } = this.props;

        let queryParams = new URLSearchParams(location.search);
        let p = parseInt(queryParams.get("p"));
        let c = parseInt(queryParams.get("c"));

        fetch(`/api/rest/history/content/tracking/
            ${trackingID}/entries?type=${type}&skip=${p}&count=${c}`)
            .then(function(response) {
                return response.json();
            })
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        entries: result.items,
                        pages: Math.ceil(result.total/c)
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

    handlePageChange(pageIndex, pageCount) {

        const { trackingID, type, history } = this.props;

        this.setState({
            page: pageIndex,
            pageCount: pageCount,
            isLoaded: false
        });

        history.replace(`/browse/history/content/tracking/${trackingID}/${type}?p=${pageIndex}&c=${pageCount}`);
        this.fetchData();
    }

    render(){
        const { isLoaded, entries, page, pages, pageCount } = this.state;

        const columns = [{
            Header: 'Store Key',
            accessor: 'storeKey'
        }, {
            Header: 'Checksum',
            accessor: 'sha256'
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
                        manual
                        minRows={5}
                        data={entries}
                        columns={columns}
                        pages={pages}
                        page={page}
                        onPageSizeChange={(pageSize, pageIndex) => {
                            this.handlePageChange(pageIndex, pageSize)
                        }}
                        onPageChange={(pageIndex) => {
                            this.handlePageChange(pageIndex, pageCount)
                        }}
                    />
                </React.Fragment>
            );
        }
    };
}
