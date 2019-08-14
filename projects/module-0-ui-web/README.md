# Module Zero UI
This UI connects to a WebSocket server and displays the most recently received
events.

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
    datetime: Int,
    department: String,
    itemId: Int,
    qty: Int,
    price: String
}
```

### Variables

* PORT (default: 8080) - HTTP traffic port
* WSS_INCOMING_PAYLOAD_PORT (default: 8181) - Port that a WebSocket server listens on for payloads to send connected clients.
* WS_MOCK_DATA_ENABLED (default: false) - Determines if mock data should be sent to clients. 
* WS_CONNECTION_STRING (default: undefined) - Use this to have the frontend connect to a different socket server
* LOG_LEVEL (default: info) - Logging level of trace, debug, info, etc.

### Deployment as Container

Build using the following command from this directory. Replace `$TAG_NAME` with
your chosen tag name.

```
docker build . -t $TAG_NAME
```

You can run the image via Docker like so. The `WS_CONNECTION_STRING` must be
set (an example is provided):

```
SOCKET_SERVER=wss://localhost:9090
docker run -d -p 8080:8080 -e WS_CONNECTION_STRING=$SOCKET_SERVER $TAG_NAME:latest
```

Or you can push it to a registry and deploy on a container platform, e.g OpenShift.


## Icon Attribution

* <div>Icons made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>