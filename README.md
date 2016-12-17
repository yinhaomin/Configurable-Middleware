# Configurable-Middleware
This is a configurable middleware system.

This project want to solve a problem like this.
In a subscribe system, every time we have to change the field subscription, 
we have to change the subscribe codes and re-publish the whole project.
I think that it is a time waste thing, and I want to rebuild the project and make the field subscription configurable.

Here is how did I rebuild the system.

![Configurable middleware system](https://raw.githubusercontent.com/yinhaomin/Configurable-Middleware/master/images/process-of-building-configurable-system.png "Configurable middleware system")

I used the disconf to save the class config, here is the source of [disconf](https://github.com/knightliao/disconf)
