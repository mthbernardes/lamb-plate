# {{name}}

AWS Lambda handler function implemented in Clojure designed to...you tell me!

Things in this README that you might wish to change:

1. Document the components your project uses and their options
2. Document any helpful developer functions you put into `dev/user.clj`
3. Replace the [Environment Variables and Configuration](#environment-variables-and-configuration) 
section with how your project manages this
4. Update the license

### Quick Usage

Run `lein repl`. Then, to get a fresh system:

```
user=> (reset)
```

To run the Lambda function:

```
user=> (execute)
```

### Overview

You'll find the entry point of the project in the _{{name}}/core.clj_ namespace. Here,
we implement a `-handle` function that is the main entry point for AWS Lambda. Within
that function, we log out a helpful message, parse the event map, and bootstrap the
system. The initialized system is then passed into the `Handler` component's `execute`
function to perform..._something!_ That's up to you. We cover the `Handler` component shortly.

You can find detailed information on the anatomy of this `-handle` function by visiting
[Amazon's Lambda Function Handler (Java)](http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html)
documentation. In fact, you may find many of the Lambda articles for Java to be helpful to you
when trying to understand various interop scenarios.

### Reloaded Workflow and the Component Architecture

This project is set up to support Stuart Sierra's [component architecture](https://github.com/stuartsierra/component) and
[reloaded workflow](http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded).
This workflow is incredibly powerful in the context of [the container model](http://docs.aws.amazon.com/lambda/latest/dg/lambda-introduction.html)
that AWS uses to run Lambda functions. If you're unfamiliar with these ideas, consider them required reading.

When you run `lein repl`, you will be loaded into the `user` namespace by default. This namespace is augmented
by the `dev/user.clj` file, which you can edit to create more development functions for yourself _(note this file is only
loaded in development)_.

The `reset` function is the bread and butter of the reloaded workflow, and is included
in the `dev/user.clj` file. When invoked at the repl, it will:

1. Tear down all the system's components
2. Refresh all namespaces in dependency order
3. Restart all the system's components

Your repl-driven workflow will entail: exercising functions at the repl, making some code changes,
running `(reset)`, repeat.

#### Included Components and Beyond

This project contains the following components:

- **Handler**: contains the `{{name}}.handler/exectute` function, and depends on the `AWS` component
- **AWS**: encapsulates the event map, and the [Context Object](http://docs.aws.amazon.com/lambda/latest/dg/java-context-object.html)
both of which are passed in by the Lambda runtime environment<sup>1</sup>

The `{{name}}.handler/execute` function is where your custom Lambda logic will go. At this stage in the component architecture,
all other components have been started and injected; all that remains is to tie them together however you see fit.

Naturally, you'll eventually want to create new components. For example, a `Database` component, a `MemCache` component,
etc. You'll find both of the included components to be great examples for creating your own, and you're encouraged to copy them
as a starting point. Also see the [Creating Components](https://github.com/stuartsierra/component#creating-components) section
of the component project for more examples.

Once your new component is authored, you can add it to the `system` map and specify its dependencies 
within the `{{name}}.core` namespace. See the [Systems](https://github.com/stuartsierra/component#systems) section
of the component project for examples.

Keep in mind also that the entire `system` var is accessible to you from the `user` namespace at the repl. For example,
you can access the `Handler` component like so:

```
user=> (:handler system)
```

<sup>1</sup>_During development, this Context object is mocked with an "empty" implementation using [reify](http://clojuredocs.org/clojure.core/reify)._

### Logging

Logging is handled by `Log4j` augmented with the [AWS Lambda custom appender](http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html),
and is completely handled for you. You're free to make calls to the `log/*` family of functions documented in 
[tools.logging](http://clojure.github.io/tools.logging/) and they will show up in your CloudWatch logs.

You can change the log level within the `resources/log4j.properties` file:

```
log = .
log4j.rootLogger = INFO, LAMBDA
                   ^
                   |------ Change this to DEBUG, for example

...
```

### JSON Event Map

Lambda functions are invoked with an event map, usually formatted as a JSON file. This event map contains
any information relevant to the event that triggered the Lambda (CloudWatch scheduled event, S3 object changed, etc.)
When you create a Lambda function in the Java programming language, this event map is passed as an
instance of `java.util.LinkedHashMap`. Clojure, being hosted in the JVM, is no different.
To make working with this map more idiomatic, it gets immediately converted into a basic 
Clojure map with keyword keys.

So this:

```
{"name": "Robert Baratheon"}
```

Will be converted to this:

```
{:name "Robert Baratheon"}
```

And can be accessed via the `AWS` component like so:

```
user=> (:event (:aws system))
```

The conversion is implemented in the `{{name}}.core/linkedhashmap->map` function.

#### Event Fixture

You'll undoubtedly need to provide a stub event map while you're testing locally. You can
do this by overriding the `user/event-fixture` function, found in the `dev/user.clj` file.
This function returns a `java.util.LinkedHashMap` instance in order to mimic the Lambda
production environment as closely as possible.

### Environment Variables and Configuration

This project includes the [environ](https://github.com/weavejester/environ) library for managing
environment variables. A humble suggestion: **pass the `env` map as an argument to each component
factory function that needs it**. In other words, do **not** require `environ` in any namespace
other than `{{name}}.core`, the place where components are actually created. Example:

```Clojure
(defn example-component [config-options]
  (map->ExampleComponent {:option1 (get config-options :option1 :default)
                          :option2 (get config-options :option2 :other-default})
```

Now when you invoke `example-component`, you can pass in the `env` map provided by `environ`
and cherry-pick exactly what you need to configure and start that component.

### Temporary Files

This is more of a "bonus" tidbit, but keep in mind that the `/tmp` directory is fair game for caching valuable resources
between Lambda invocations. As noted in the [container model](http://docs.aws.amazon.com/lambda/latest/dg/lambda-introduction.html)
docs, a Lambda's encompassing container can be "frozen" as an optimization measure, and the `/tmp/` directory stays in-tact.
You can use this to your advantage to, for example, cache auth tokens or other useful data.

## License

Copyright © {{year}} FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

---

_Created using the [lamb-boiler](https://github.com/cjsauer/lamb-boiler) template_
