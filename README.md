# Welcome to Chatfoundry!

Chatfoundry is a chatroom implementation using Kotlin/Redis, deployed to Cloud Foundry.
The purpose of this app is to play with [Cloud Foundry](https://www.cloudfoundry.org), and learn how to use it.

## Prerequisites

Tests are made using [Pivotal Web Services (PWS)](https://run.pivotal.io),
but you can also deploy this web on your own instance.
You may use [PCF Dev](https://pivotal.io/fr/pcf-dev) to deploy this app on your workstation.

You need a Java 8 SDK to build this app.

## Build instructions

This project is using [Maven 3.5+](http://maven.apache.org):

    $ ./mvnw clean package

## How to run this app

To deploy this app to PWS, use these commands:

    $ cf login -a http://api.run.pivotal.io
    $ cf create-service redis 30mb rediscloud
    $ cf push
    $ cf bind-service chatfoundry redis

If you are using a Cloud Foundry instance other than PWS, make sure you bind
a Redis service to this app.

## Enjoy!

The app should be available at https://chatfoundry-RANDOM.cfapps.io.
Check command output or [Apps Manager](https://console.run.pivotal.io) to get the real endpoint.
