'use strict'

const pino = require('pino')
const assert = require('assert')
const { from } = require('env-var')
const { randomBytes } = require('crypto')

const randomSessionSecret = randomBytes(16).toString('hex')

module.exports = (env) => {
  assert(typeof env === 'object', 'config.js requires a process.env argument')

  const { get } = from(env)

  return {
    HTTP_PORT: get('HTTP_PORT', 8080).asPortNumber(),

    NODE_ENV: get('NODE_ENV').asString(),

    LOG_LEVEL: get('LOG_LEVEL', 'info').asEnum(Object.values(pino.levels.labels)),

    // Can be set to valid keycloak config for a "public" client
    KEYCLOAK_CONFIG: get('KEYCLOAK_CONFIG').asJsonObject(),

    // If keycloak is configured we also need a session secret.
    // This will default to a random hex string if not set
    SESSION_SECRET: get('SESSION_SECRET', randomSessionSecret).asString(),

    // Ordering HTTP API base url, e.g http://orders.svc.local:8080/
    ORDERS_REST_BASE_URL: get('ORDERS_REST_BASE_URL').asUrlString()
  }
}
