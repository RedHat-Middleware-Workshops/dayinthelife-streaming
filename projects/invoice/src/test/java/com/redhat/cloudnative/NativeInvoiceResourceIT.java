package com.redhat.cloudnative;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeInvoiceResourceIT extends InvoiceResourceTest {

    // Execute the same tests but in native mode.
}