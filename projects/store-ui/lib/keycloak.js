'use strict'

const { KEYCLOAK_CONFIG } = require('./config')(process.env)
const log = require('./log')
const { store } = require('./session')
const Keycloak = require('keycloak-connect')

/**
 * Applies keycloak middleware to an express application if a KEYCLOAK_CONFIG
 * environment variable is set to a valid OIDC JSON from a "public" Keycloak
 * client type
 */
module.exports = (app) => {
  if (KEYCLOAK_CONFIG) {
    log.info('KEYCLOAK_CONFIG was found, applying keycloak middleware')
    // If a keycloak config is found we need to create a session store and
    // middleware, then mount these and keycloak.

    const kc = new Keycloak({ store }, KEYCLOAK_CONFIG)

    // Mount the keycloak middleware. Also expose a /logout endpoint
    app.use(kc.middleware({
      logout: '/logout'
    }))

    // Apply keycloak protection to all routes defined after this is called
    app.use(kc.protect())
  }
}
