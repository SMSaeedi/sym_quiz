# APX market data parser

---
**NB!** Please check your spam folder if you did not receive an email "**Test assignment from Sympower**" with instructions 
from [dev-test-assignment@sympower.net](mailto:dev-test-assignment@sympower.net)
---
Example of JSON data that we need to parse is available at
`src/test/resources/net/sympower/cityzen/apx/apx-data.json`

We need the following data for each hour of the day:

* **date** ("date_applied" in JSON) - Raw data is provided as milliseconds since the start of epoch (start of day). We need to figure out the date in the Netherlands time zone.
* **hour** - based on either "Order" or "Hour" field, we need to figure out the hour-of-day for which particular data row is applicable for.
* **net volume** - use the value from "Net Volume" field
* **price** - use the value from "Price" field

For JSON parsing, we use [Jackson](https://github.com/FasterXML/jackson).

### Basic steps

* Define object model in Java.
* Implement code to parse JSON into the object model.
* Implement tests for the code (e.g. load sample from class path, verify it was parsed correctly).

### Building

You can use either Gradle, Gradle wrapper or Maven (we use Gradle 3.1+).

### FAQ

#### Troubleshooting repository cloning issues

If you can access the repository in the browser, you already have the necessary permissions.

Here is a guide on [how to clone the repository](https://support.atlassian.com/bitbucket-cloud/docs/clone-a-git-repository/) via HTTPS or SSH.

If you decide to use SSH, here is a guide on [setting up your SSH key](https://support.atlassian.com/bitbucket-cloud/docs/set-up-an-ssh-key/).

If you decide to use HTTPS, keep in mind that you need to [create an app password](https://bitbucket.org/blog/deprecating-atlassian-account-password-for-bitbucket-api-and-git-activity).
