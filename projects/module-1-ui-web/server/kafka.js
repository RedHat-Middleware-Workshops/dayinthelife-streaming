'use strict'

const { KafkaClient, Consumer } = require('kafka-node')
const log = require('./log').getNamedLogger('kafka')
const env = require('env-var')

/**
 * This is a websocket server that will accept incoming payloads and proxy them
 * to connected clients.
 *
 * This is not exposed on the ocp router!
 */
module.exports = function getKafkaMessages () {
  const labUser = env.get('LAB_USER', 'user1').asString()
  const kafkaHost = env.get(
    'KAFKA_HOST',
    'earth-cluster-kafka-bootstrap.shared-kafka-earth.svc:9092'
  ).asString()

  log.info(`connecting to ${kafkaHost}`)

  const topic = `${labUser}.earth.dbo.Orders`
  const client = new KafkaClient({ kafkaHost })

  return new Promise((resolve, reject) => {
    client.once('error', (err) => {
      log.error('client encountered an error', err)
      reject(err)
    })

    client.once('ready', () => {
      log.info('client is ready. creating consumer')

      const consumer = new Consumer(
        client,
        [
          { topic }
        ]
      )

      consumer.on('error', (err) => {
        log.error('consumer encountered an error', err)
        reject(err)
      })

      resolve(consumer)
    })
  })
}
