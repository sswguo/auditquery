import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

export class ChangeEventView extends React.Component{
    constructor(props){
      super(props);
      this.state = {
        error: null,
        event: null,
        loading: true
      };
    }

    componentDidMount(){
      let {eventId} = this.props;
      console.log(eventId);
      fetchEvent(`${eventId}`,
        (result) => {
          this.setState({
            loading: false,
            event: result
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

    render(){
      const { loading } = this.state;
      if (loading) {
        return <div>Loading...</div>;
      } else {
        let {event} = this.state;
        return (
          <React.Fragment>
            <h4>Repository Change Event {event.eventId}</h4>
            <DisplayBlock id="eventId" label="Event id" content={event.eventId} />
            <DisplayBlock id="storeKey" label="Store key" content={event.storeKey} />
            <DisplayBlock id="changeTime" label="Change time" content={event.changeTime} />
            <DisplayBlock id="changeType" label="Change type" content={event.changeType} />
            <DisplayBlock id="user" label="User" content={event.user} />
            <DisplayBlock id="version" label="Version" content={event.version} />
            <DisplayBlock id="diff" label="Diff" content={event.diffContent} />
          </React.Fragment>
         );
      }
    };
}

const DisplayBlock = (props)=>{
  const [rowClass, labelClass, valueClass, spanClass] = ["form-group row", "col-sm-1 col-form-label", "col-sm-10", "form-control-plaintext"];
  return <div className={rowClass}>
    <label htmlFor={props.id} className={labelClass}>{props.label}:</label>
    <div className={valueClass}>
    <span id="diff" className={spanClass}>{props.content}</span>
    </div>
  </div>;
}

var fetchEvent = (eventId, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/change/${eventId}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
