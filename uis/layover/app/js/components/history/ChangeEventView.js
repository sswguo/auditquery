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
            <DisplayBlock id="summary" label="Summary" content={event.summary} />
            <DisplayBlock id="user" label="User" content={event.user} />
            <DisplayBlock id="version" label="Version" content={event.version} />
            <DisplayBlock id="diff" label="Diff" content={event.diffContent} code="true" />
          </React.Fragment>
         );
      }
    };
}

const DisplayBlock = (props)=>{
  const [rowClass, labelClass, valueClass, spanClass] = ["form-group row", "col-sm-1 col-form-label", "col-sm-10", "form-control-plaintext"];
  let codeBlock = props.code?<code>{props.content}</code>:props.content;
  return <div className={rowClass}>
    <label htmlFor={props.id} className={labelClass}>{props.label}:</label>
    <div className={valueClass}>
    <span id={props.id} className={spanClass}>
    { codeBlock }
    </span>
    </div>
  </div>;
}

var fetchEvent = (eventId, resultHander, errorHandler) => {
  fetch(`/api/rest/history/stores/change/${eventId}`)
      .then((response)=>response.json())
      .then(resultHander,errorHandler);
};
