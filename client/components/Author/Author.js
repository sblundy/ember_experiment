import React, { Component } from 'react';

export default class extends Component {

  render() {
    return (
      <div>
        <span>name: {this.props.fullName}</span>
      </div>
    );
  }

}
