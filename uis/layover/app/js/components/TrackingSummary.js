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
import { Link } from "react-router-dom";

export class TrackingSummary extends React.Component{
    constructor(props){
       super(props);
       this.state = {
       error: null,
         summary: {},
         isLoaded: false
       }
    }


    fetchData(){
       fetch(`/api/rest/history/content/tracking/${this.props.trackingID}/summary`)
          .then(function(response) {
               return response.json()
          })
          .then(
             (result) => {
                this.setState({
                   isLoaded: true,
                   summary: result
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
       if (this.props.trackingID !== prevProps.trackingID) {
          this.fetchData();
       }
    }

    render(){
      const { isLoaded, summary } = this.state;

      if (!isLoaded) {
        return <div>Loading...</div>;
      } else {
        if ( summary.error ) {
          return (
             <React.Fragment>
                    { summary.error }
             </React.Fragment>
          );
        } else {
          return (
            <React.Fragment>
               <h4>Tracking Summary</h4>
               Tracking ID:   { summary.trackingID } <br />
               <Link to={{  pathname:"upload", search: "?p=0&c=20" }}>Uploads</Link>  { summary.uploadCount } <br />
               <Link to={{  pathname:"download", search: "?p=0&c=20" }}>Downloads</Link>  { summary.downloadCount }
            </React.Fragment>
          );
        }
     }

    };
}
