import React from 'react';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

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
       fetch("/api/rest/history/content/tracking/" + this.props.trackingID + "/summary")
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
        return (
              <div>
                 <h4>Tracking Summary</h4>
                 Tracking ID:   { summary.trackingID } <br />
                 <Link to={{  pathname:"upload", search: "?p=1" }}>Uploads</Link>  { summary.uploadCount } <br />
                 <Link to={{  pathname:"download", search: "?p=1" }}>Downloads</Link>  { summary.downloadCount }
              </div>
            );
      }

    };
}
