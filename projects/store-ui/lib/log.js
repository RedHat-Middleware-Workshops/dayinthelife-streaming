'use strict'

const { LOG_LEVEL: level } = require('./config')(process.env)

module.exports = require('pino')({
  name: 'openshift-nodejs-template',
  level
})
