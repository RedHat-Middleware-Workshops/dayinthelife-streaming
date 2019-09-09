'use strict'

const WebSocket = require('ws')
const env = require('env-var')
const log = require('./log')
const kafka = require('./kafka')
const sockets = []

/**
 * Configures a WebSocket server that sends data to connected clients.
 * Attaches to our existing http server instead of using a different port.
 */
exports.configureClientWebSocketServer = function (server) {
  const wss = new WebSocket.Server({ server })
  const WS_MOCK_DATA_ENABLED = env.get('WS_MOCK_DATA_ENABLED', 'false').asBool()

  wss.on('connection', async (sock) => {
    log.info('client connected to client socket server')

    sockets.push(sock)

    sock.on('close', () => {
      // Remove this socket from the connected sockets list
      sockets.splice(sockets.indexOf(sock, 1))
    })

    const kafkaConsumer = await kafka()

    kafkaConsumer.on('message', (msg) => {
      log.debug('kafka message received', msg)
      sock.send(JSON.stringify(msg))
    })

    if (WS_MOCK_DATA_ENABLED) {
      const send = () => {
        if (sock.readyState === WebSocket.OPEN) {
          // Send at least one payload every 5 seconds
          setTimeout(() => {
            const data = {
              datetime: Date.now(),
              department: 'Sales',
              itemId: Math.round(Math.random() * 20000),
              qty: Math.round(Math.random() * 1000),
              price: (Math.random() * 50).toFixed(2)
            }

            log.info('sending mock data to client: %j', data)

            sock.send(JSON.stringify(data))

            // Queue another send
            send()
          }, Math.random() * 5000)
        }
      }

      send()
    }
  })

  log.info('client facing websocket server listening on application http port')
}

/**
 * Send a given payload to connected clients.
 * @param {Object} payload
 */
exports.sendPayloadToClients = function (payload) {
  if (typeof payload !== 'string') {
    log.trace('serialising payload to send:', payload)
    payload = JSON.stringify(payload)
  }

  sockets.forEach((sock) => sock.send(payload))
}
