# demo-chat

A simple chat application.

[Deployed version](https://chat-by-cheatex.herokuapp.com/)

## Development

### Configuration

The application only expects the `redis-url` parameter set.

The simplest way to set it is by creating `.lein-env` file and write there

```
{:redis-url "<your redis server>"}
```

### VSCode

I've used VS Code and Calva plugin for development. Just instal them, open project folder and use "Calva Jack in" command.

### Console

```
$ lein figwheel dev
```

### Run tests:

```
$ lein test
```

### Infrastructure

[Circle CI](https://circleci.com/gh/CheatEx/demo-chat/). I've only managed to fail build on test failures. Compatible result reporters didn't work under my version of lein.

Eastwood is also running there.

## Production Build

```
$ lein clean
$ lein with-profile prod uberjar
```

That should compile the clojurescript code first, and then create the standalone jar.

The jar could be run by
```
$ java -jar target/demo-chat.jar
```

It takes the following settings from the environment:

* `PORT` web server port. Defaults to 3000
* `REDIS_URL` redis server URL. Defaults to "redis://localhost:6379/"

For example
```
$ PORT=4200 REDIS_URL='redis://172.17.0.2:6379/' java -jar target/demo-chat.jar
```

### Heroku

To run the app on Heroku you'll need to enable Redis addon. The simplest way is by Heroku CLI

```
heroku addons:create heroku-redis:hobby-dev -a <application name> --wait
```

Consult [documentation](https://devcenter.heroku.com/articles/managing-add-ons) for more options.

## Area for improvements

* Proper test results in Circle
* Connectivity status on client
* Server logging
* Strategy for DB failure on server
