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

## Production Build

```
$ lein clean
$ lein with-profile prod uberjar
```

That should compile the clojurescript code first, and then create the standalone jar.

The jar coul be run by 
```
$ java -jar target/demo-chat.jar
```

It takes the following settings from the environment:

* `PORT` web server port. Defaults to 3000
* `REDIS_URL` redis server URL. Defaults to "redis://localhost:6379/"