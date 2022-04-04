println 'Hello world.'

for (int i=0; i<args.length; i++) {
    System.out.printf("args[%d]:\t%s", i, args[i]);
    System.out.println();
}

println 'Exeption sample.'
throw new IllegalArgumentException('groovyの例外スロー')
