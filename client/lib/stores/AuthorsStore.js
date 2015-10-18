/**
 *
 */
import React from 'react';
import {EventEmitter} from 'fbemitter';
import AuthorsDispatcher from '../AuthorsDispatcher';

const emitter = new EventEmitter();
const store = {
  authors: [],
  _addAuthor: function(author) {
    this.authors.push(author);
    emitter.emit('changed');
  },
  addUpdateListener: function(listener) {
    var token = emitter.addListener('changed', listener);
    return {
      removeListener: function() {
        token.remove();
      }
    };
  },
  getAllAuthors: function() {
    return this.authors;
  }
};

AuthorsDispatcher.register(function(payload) {
  switch (payload.eventName) {
    case 'new-author':
      console.log('in new-author');
      store._addAuthor(payload.newAuthor);
      break;
  }
  return true;
});

export default store;
