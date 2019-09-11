# Lab 2 UI
This UI connects to a WebSocket server and displays the most recently received
Orders by streaming them from a Kafka topic.

## Setup

* Node.js 10+
* npm 5+


### Local Development

```
npm install
npm run start-dev
```

The development setup includes a WebSocket Server that sends mock data to the
frontend. The data format is as a JSON Object with the following structure:

```
{
    data: {
        "OrderId": "6817D382-CDBA-E369-FE20-FF7C79DE5A26",
        "OrderType": "E",
        "OrderItemName": "Lemon Bar",
        "Quantity": 17,
        "Price": "0.09",
        "ShipmentAddress": "Ap #249-5876 Magna. Rd.",
        "ZipCode": "I9E 0JN"
    },
    ts: 1568062055995
}
```

This matches what the server builds from a Kafka payload in production mode.

### Variables

* PORT (default: 8080) - HTTP traffic port
* WS_MOCK_DATA_ENABLED (default: false) - Determines if mock data should be sent to clients. 
* WS_CONNECTION_STRING (default: same as frontend host) - Use this to have the frontend connect to a different socket server. By default it uses the host for the frontend
* LOG_LEVEL (default: info) - Logging level of trace, debug, info, etc.

### Deployment as Container

Build using the following command from this directory. Replace `$TAG_NAME` with
your chosen tag name.

```
docker build . -t $TAG_NAME
```

You can run the image via Docker with this command:

```
docker run -d -p 8080:8080 -p 8181:8181 $TAG_NAME:latest
```

Push it to a registry and deploy on a container platform, e.g OpenShift.


## Icon Attribution

* <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
* <div>Icons made by <a href="https://www.flaticon.com/authors/turkkub" title="turkkub">turkkub</a> from <a href="https://www.flaticon.com/"             title="Flaticon">www.flaticon.com</a></div>
