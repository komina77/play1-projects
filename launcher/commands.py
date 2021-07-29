import os, os.path
import sys
import subprocess
# Here you can create play commands that are specific to the module, and extend existing commands

MODULE = 'launcher'

# Commands that are specific to your module

COMMANDS = ['launcher:run']

HELP = {
    'launcher:run': 'Launch any class that is specifyed  by "--class=" option. (no start WebServer)'
}

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "launcher:run":
        print "~ Launch "
        print "~"
        java_args = []
        for arg in args:
            if arg.startswith('-D'):
                java_args.append(arg)
        java_cmd = app.java_cmd(java_args, None, "play.modules.launcher.Launcher", args)
        try:
            subprocess.call(java_cmd, env=os.environ)
        except OSError:
            print "Could not execute the java executable, please make sure the JAVA_HOME environment variable is set properly (the java executable should reside at JAVA_HOME/bin/java). "
            sys.exit(-1)
        print


# This will be executed before any command (new, run...)
def before(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")


# This will be executed after any command (new, run...)
def after(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "new":
        pass
