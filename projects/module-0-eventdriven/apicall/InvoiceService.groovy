/*
kamel run InvoiceService.groovy --dev
*/
rest {
    configuration { 
        component 'undertow'
        apiContextPath '/api-docs'
        port '8080'
    }

    path('/my/path') { 
        get {
            to 'direct:notify'
        }
    }
}

from('direct:notify')
    .setBody()
        .constant('Hello Camel K!')
    .to('log:info')