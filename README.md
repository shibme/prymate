## PryMate
A proxy tool that logs all requests for the configured domain to enable traffic analysis

### Requirements
* Latest Docker CLI have to be installed in the environment
* Prepare a config file defining how it should work ([Sample](https://shibme.github.io/prymate/prymate.json))
* The config should be given as URL through environment variable `PRYMATE_CONFIG_URL` or should exist as `prymate.json` file in the current directory

### How to make it work?
Run the following command
```
docker run -v $(pwd):/workspace/ -p 7796:7796 shibme/prymate
```

### Configuring Proxy
* Download and install [this certificate](https://shibme.github.io/prymate/prymate.cer) to your trusted keystore
* Set the proxy pointing to the destination address where the service is run with the respective port

When run locally, use the following proxy configuration
```
Host: localhost
Port: 7796
```