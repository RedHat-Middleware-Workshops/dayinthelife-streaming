'use strict'

const env = require('env-var')
const pkg = require('../package')
const pino = require('pino')

const LOG_LEVEL = env.get('LOG_LEVEL', 'info').asEnum(
  // ["trace, "debug"] etc.
  Object.values(pino.levels.labels)
)

module.exports = pino({
  level: LOG_LEVEL,
  name: pkg.name
})
