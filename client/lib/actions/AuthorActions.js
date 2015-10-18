import AuthorsDispatcher from '../AuthorsDispatcher';

class AuthorActions {
  static add(name) {
    AuthorsDispatcher.dispatch({
      eventName: 'new-author',
      newAuthor: {fullName: name }
    });
    return true;
  }
}

export default AuthorActions;

