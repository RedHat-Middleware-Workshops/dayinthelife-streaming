'use strict'

const WebSocket = require('ws')
const { getKafkaConsumer } = require('./kafka')
const { resolve } = require('path')
const env = require('env-var')
const csv = require('csvtojson')
const log = require('./log')

// Parsing is async. It returns a promise that will resolve eventually
const sampleOrders = csv().fromFile(resolve(__dirname, '../fixtures/orders.csv')).then((data) => Promise.resolve(data))

/**
 * Configures a WebSocket server that sends data to connected clients.
 * Attaches to our existing http server instead of using a different port.
 */
exports.configureClientWebSocketServer = function (server) {
  const wss = new WebSocket.Server({ server })
  const WS_MOCK_DATA_ENABLED = env.get('WS_MOCK_DATA_ENABLED', 'false').asBool()

  wss.on('connection', async (sock) => {
    log.info('client connected to client socket server')

    // Placeholder for kafka.Consumer instance
    let consumer

    sock.on('close', () => {
      if (consumer) {
        // Cleanup kafka connection if necessary
        consumer.close()
        consumer.client.close()
      }
    })

    if (WS_MOCK_DATA_ENABLED) {
      log.info('mock data enabled for connected client')

      const send = () => {
        if (sock.readyState === WebSocket.OPEN) {
          log.info('queuing send of mock data')
          // Send at least one payload every 5 seconds
          setTimeout(async () => {
            const orders = await sampleOrders
            const data = orders[Math.floor(Math.random() * orders.length - 1)]

            log.info('sending mock data to client: %j', data)

            sock.send(JSON.stringify({
              data,
              ts: Date.now()
            }))

            // Queue another send
            send()
          }, Math.random() * 2000)
        } else {
          log.warn(`not sending mock data. socket.readyState was ${sock.readyState}`)
        }
      }

      send()
    } else {
      consumer = await getKafkaConsumer()

      log.info('consumer created')

      consumer.on('error', (err) => {
        log.error('kafka consumer encountered an error', err)
        // Close the websocket. This forces client to reconnect. Clients
        // reconnecting will reestablish the kafka connection
        sock.close()
      })

      consumer.on('message', (msg) => {
        log.debug('received payload from kafka, forwarding to ws:', msg)
        sock.send(
          JSON.stringify({
            data: JSON.parse(msg.value.payload.after),
            ts: msg.value.payload.ts_ms
          })
        )
      })
    }
  })

  log.info('client facing websocket server listening on application http port')
}
