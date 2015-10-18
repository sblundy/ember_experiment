import React, { Component } from 'react';

import AuthorsStore from '../../lib/stores/AuthorsStore';
import AuthorActions from '../../lib/actions/AuthorActions';

class AuthorsList extends Component {
  constructor(props) {
    super(props);
    this.state = {authors:AuthorsStore.getAllAuthors()};
    console.log('in constructor');
    var listener = this.handleChange.bind(this);
    this.updateListernHandler = AuthorsStore.addUpdateListener(listener);
  }

  willComponentUnmount() {
    this.updateListernHandler.removeListener();
  }

  handleChange () {
    console.log('in handleChange');
    this.setState({authors: AuthorsStore.getAllAuthors()});
  }

  addNewAuthor(evt) {
    console.log('in addNewAuthor');
    var name = 'Author';
    AuthorActions.add( name );
  }

  render() {
    return (
      <table>
        <thead>
          <tr><th>Author</th></tr>
        </thead>
        <tbody>
        {
          this.state.authors.map(function (author) {
            console.log('in map');
            return <tr>
              <td>{author.fullName}</td>
            </tr>
          })
        }
        </tbody>
        <tfoot>
          <tr>
            <td>
              <button onClick={this.addNewAuthor}>Add</button>
            </td>
          </tr>
        </tfoot>
      </table>
    );
  }
}

export default AuthorsList
