import React from 'react';

export class ChangeEvent extends React.Component{
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
              <div>
                <label htmlFor="eventId">Evnet id:</label>{ } 
                <span id="eventId">{event.eventId}</span> <br />
                <label htmlFor="storeKey">Store key:</label>{ }
                <span id="storeKey">{event.storeKey}</span> <br />
                <label htmlFor="changeTime">Change time:</label>{ }
                <span id="changeTime">{event.changeTime}</span> <br />
                <label htmlFor="changeType">Change type:</label>{ }
                <span id="changeType">{event.changeType}</span> <br />
                <label htmlFor="user">User:</label>{ }
                <span id="user">{event.user}</span> <br />
                <label htmlFor="version">Version:</label>{ }
                <span id="version">{event.version}</span> <br />
                <label htmlFor="diff">Diff:</label>{ }
                <span id="diff">{event.diffContent}</span> <br />
              </div>
          </React.Fragment>
         );
      }
    };
}

var fetchEvent = (eventId, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/change/${eventId}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
