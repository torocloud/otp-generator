# OTP-Generator
An API that allows you to create one-time passwords and verifying the authorisation code, or generate a Time-based One-time Password (TOTP) url which can be use when setting up Two-Factor Authentication  

### Prerequisites

  - Apache Maven 3+
  - [Martini Desktop](https://www.torocloud.com/martini/download)

### Building the Martini Package

```
$ mvn clean package
```
This will create a ZIP file named `otp-generator.zip` containing all the files (services, configurations, etc.) needed under the `target` folder. 
This ZIP file is what we call a [Martini Package](https://docs.torocloud.com/martini/latest/developing/package/) 
which then you can import into Martini Desktop to get started. You can learn more about how to import a Martini Package 
by visiting our [documentation](https://docs.torocloud.com/martini/latest/developing/package/importing/).

### Usage
This package exposes REST APIs for creating one-time passwords, verifying authorisation codes, 
and generating Time-based One-time Password (TOTP) url. 
You can find the [Gloop REST API](https://docs.torocloud.com/martini/latest/developing/gloop/api/rest/) file 
at `/code/api/OtpGeneratorAPI.api` after importing the package to your Martini Desktop application.

### Setup
To change the values of the default configurations, update the [package properties](https://docs.torocloud.com/martini/latest/developing/package/properties/) 
located at [conf](https://docs.torocloud.com/martini/latest/developing/package/directory/) folder.
Below is what the contents of the properties file look like.

```
totp.timeStepSizeInSeconds=30

totp.numberOfScratchCodes=5

totp.windowSize=3

totp.codeDigitsLength=6

totp.enableScratchCodes=false
```
* `totp.timeStepSizeInSeconds` - The number to tell the generator how long the one-time password is valid, defaults to 30 seconds.
* `totp.numberOfScratchCodes` - The number of scratch codes to generate. Defaults to 5, and the maximum allowed is 1,000 codes.
* `totp.windowSize` - An integer value representing the number of windows of size timeStepSizeInMillis that are checked during the validation process, 
to account for differences between the server and the client clocks. The bigger the window, the more tolerant the library code is about clock skews.
* `totp.codeDigitsLength` - The number of digits in the generated code. Minimum is 6 digits, and maximum is 8.
* `totp.enableScratchCodes` - Scratch codes are meant to be a safety net in case a user loses access to their token device. 
Scratch codes are disabled by default, if you wish to use scratch codes on your application then set this to *true*

### Operations

The base url is `<host>/api/otp-generator` where `host` is the location where the Martini instance is deployed. By default, it's `localhost:8080`.

`POST /generate-otp`

Generates a one-time password. Returns a `JSON` body which includes the secret key to which the generated password is authorised.

**Request Body**

This is optional, and if not set it will use the default values from the package.properties.
```json
{
    "codeDigitsLength": "6",
    "numberOfScratchCodes": "5",
    "windowSize": "3",
    "timeStepSizeInSeconds": "30",
    "enableScratchCodes": "false"
}
```

**Sample Request**

**cURL**
```
curl --location --request POST 'http://localhost:8080/api/otp-generator/generate-otp' \
--header 'Content-Type: application/json' \
--data-raw '{
    "codeDigitsLength": "6",
    "numberOfScratchCodes": "5",
    "windowSize": "3",
    "timeStepSizeInSeconds": "30",
    "enableScratchCodes": "false"
}'
```

**Sample Response**

```
{
    "result": "OK",
    "message": "Successfully generated OTP Auth TOTP URL",
    "warnings": [],
    "otpAuthenticatorKey": {
        "secretKey": "R4FE55SIKWMI2VZT",
        "scratchCodes": [],
        "oneTimePassword": "13773"
    }
}
```

`POST /generate-totp-url`

Returns a secret key which you can save for later use and 
TOTP URL which you can use in your application when setting up Two-Factor Authentication.

**Request Body**
```json
{
    "config": {
        "codeDigitsLength": "6",
        "numberOfScratchCodes": "5",
        "windowSize": "3",
        "timeStepSizeInSeconds": "30",
        "enableScratchCodes": "false"
    },
    "issuer": "ACME Corp",
    "accountName": "john.doe@example.com"
}
```
* `config` - optional, and if not set it will use the default values from the package.properties.
* `issuer` - indicates the provider or service the account is associated with, e.g your application or company's name
* `accountName` - usually the user's email address

**Sample Request**

**curl**
```
curl --location --request POST 'http://localhost:8080/api/OtpGeneratorAPI/generate-totp-url' \
--header 'Content-Type: application/json' \
--data-raw '{
    "config": {
        "codeDigitsLength": "6",
        "numberOfScratchCodes": "5",
        "windowSize": "3",
        "timeStepSizeInSeconds": "30",
        "enableScratchCodes": "false"
    },
    "issuer": "ACME Corp",
    "accountName": "john.doe@example.com"
}'
```

**Response**

```json
{
    "result": "OK",
    "message": "Successfully generated OTP Auth TOTP URL",
    "warnings": [],
    "otpAuthenticatorKey": {
        "secretKey": "OLCG6RZ5NMM3DNEN",
        "scratchCodes": [],
        "otpAuthTotpUrl": "otpauth://totp/ACME%20Corp:john.doe@example.com?secret=OLCG6RZ5NMM3DNEN&issuer=ACME+Corp&algorithm=SHA1&digits=6&period=30"
    }
}
```
Generate a QRCode using the `otpAuthTotpUrl` for your client to scan and set up the Two-Factor Authentication.

`POST /authorise-code`

Endpoint to authorise the verification code against the provided secret key. 

**Request Body**
```json
{
    "config": {
        "codeDigitsLength": "6",
        "numberOfScratchCodes": "5",
        "windowSize": "3",
        "timeStepSizeInSeconds": "300",
        "enableScratchCodes": "false"
    },
    "secretKey": "SECRETKEY123",
    "verificationCode": "123456"
}
```
* `config` - optional, and if not set it will use the default values from the package.properties. 
NOTE: ensure that the configuration generated for the `secretKey` is the same as one sent on the request.
* `secretKey` - the generated secret key of the user returned by the `/generate-otp` or `/generate-totp-url`.
* `verificationCode` - the code combination to verify against the `secretKey`.
