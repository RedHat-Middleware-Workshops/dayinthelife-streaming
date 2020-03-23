'use strict'

const session = require('express-session')
const { NODE_ENV, SESSION_SECRET } = require('./config')(process.env)

// NOTE: MemoryStore is not designed for use in production
// NOTE: Change to another store for production use cases
const store = new session.MemoryStore()

module.exports = {
  store,
  session: session({
    secret: SESSION_SECRET,
    resave: false,
    saveUninitialized: false,
    cookie: {
      // Use secure cookies in production
      secure: NODE_ENV === 'production'
    },
    store
  })
}
