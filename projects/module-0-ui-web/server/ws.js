'use strict'

const log = require('./log')

/**
 * Confiures a WebSocket server that sends mock data.
 * This is only enabled locally when devDependencies are installed.
 */
module.exports = function configureWebSocketServer (server) {
  try {
    const WebSocket = require('ws')
    const wss = new WebSocket.Server({ server })

    wss.on('connection', (sock) => {
      log.info('client connected to mock socket server')

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
    })
  } catch (e) {
    log.warn('dummy websocket server not configured')
  }
}
