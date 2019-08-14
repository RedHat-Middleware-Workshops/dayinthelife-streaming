'use strict'

const log = require('./log')
const WebSocket = require('ws')
const env = require('env-var')
const { sendPayloadToClients } = require('./ws-outgoing')

/**
 * This is a websocket server that will accept incoming payloads and proxy them
 * to connected clients.
 *
 * This is not exposed on the ocp router!
 */
exports.configureIncomingPayloadWebSocketServer = function () {
  const port = env.get('WSS_INCOMING_PAYLOAD_PORT', '8181').asPortNumber()
  const wss = new WebSocket.Server({
    port
  })

  wss.on('listening', () => {
    log.info(`incoming websocket server listening on port ${port}`)
  })

  wss.on('connection', (sock) => {
    log.info('client connected to incoming payload websocket server')
    sock.on('message', (message) => {
      log.debug('received incoming message of type', typeof message)
      log.debug('incoming wecsocket message received %j', message)
      sendPayloadToClients(message)
    })
  })
}
